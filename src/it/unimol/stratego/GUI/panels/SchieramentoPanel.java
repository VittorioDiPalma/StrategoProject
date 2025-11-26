package it.unimol.stratego.GUI.panels;

import it.unimol.stratego.GUI.gestioneRisorse.GestoreRisorse;
import it.unimol.stratego.GUI.MainFrame;
import it.unimol.stratego.app.*;
import it.unimol.stratego.app.LogicaGioco.Casella;
import it.unimol.stratego.app.LogicaGioco.Giocatore;
import it.unimol.stratego.app.pedine.*;
import it.unimol.stratego.app.exceptions.SchieramentoException;

import javax.swing.*;
import java.awt.*;

public class SchieramentoPanel extends JDialog {
     private static final int DIMENSIONE = 10;
     private JButton[][] bottoni;
     private final Controller controller;
     private JList<Pedina> listaPedine;
     private DefaultListModel<Pedina> modelPedine;
     private Pedina pedinaSelezionata;
     private JLabel labelGiocatore;
     private int giocatoriSchierati = 0;
     private Point primaSelezione = null;
     private boolean schieramentoCompletato = false;


     public SchieramentoPanel(Frame parent, Controller controller) {
          super(parent, "Schieramento Pedine", true);
          this.controller = controller;

          setupUI();
          aggiornaPerGiocatore();

          setSize(800, 600);
          setLocationRelativeTo(parent);
          setBackground(new Color(139, 69, 19));

          addWindowListener(new java.awt.event.WindowAdapter() {
               @Override
               public void windowClosing(java.awt.event.WindowEvent e) {
                    if (modelPedine == null || !modelPedine.isEmpty()) {
                         if (parent instanceof MainFrame mainFrame) {
                              mainFrame.mostraMenuPrincipale();
                         }
                    }
                    dispose();
               }
          });
     }

     private void setupUI() {
          setLayout(new BorderLayout());
          add(creaLabelGiocatore(), BorderLayout.NORTH);
          add(creaGrigliaBottoni(), BorderLayout.CENTER);
          add(creaListaPedine(), BorderLayout.EAST);
          add(creaPannelloControlli(), BorderLayout.SOUTH);
     }

     private JPanel creaGrigliaBottoni() {
          JPanel gridPanel = new JPanel
                  (new GridLayout(DIMENSIONE, DIMENSIONE, 1, 1));
          bottoni = new JButton[DIMENSIONE][DIMENSIONE];

          for (int y = 0; y < DIMENSIONE; y++) {
               for (int x = 0; x < DIMENSIONE; x++) {
                    bottoni[x][y] = creaBottoneGriglia(x, y);
                    gridPanel.add(bottoni[x][y]);
               }
          }
          return gridPanel;
     }

     private JButton creaBottoneGriglia(int x, int y) {
          JButton button = new JButton();
          button.setPreferredSize(new Dimension(50, 50));
          button.addActionListener(_ -> gestisciClickCasella(x, y));
          return button;
     }

     private JScrollPane creaListaPedine() {
          modelPedine = new DefaultListModel<>();
          listaPedine = new JList<>(modelPedine);
          listaPedine.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          listaPedine.addListSelectionListener(e -> {
               if (!e.getValueIsAdjusting()) {
                    pedinaSelezionata = listaPedine.getSelectedValue();
               }
          });
          return new JScrollPane(listaPedine);
     }

     private JLabel creaLabelGiocatore() {
          labelGiocatore = new JLabel("", SwingConstants.CENTER);
          labelGiocatore.setFont(new Font("Arial", Font.BOLD, 16));
          return labelGiocatore;
     }

     private JPanel creaPannelloControlli() {
          JButton btnConferma = new JButton("Conferma Schieramento");
          btnConferma.addActionListener(_ -> confermaSchieramento());

          JPanel controlPanel = new JPanel(new FlowLayout());
          controlPanel.add(btnConferma);
          return controlPanel;
     }

     private void gestisciClickCasella(int x, int y) {
          if (modelPedine.isEmpty()) { // tutte schierate, abilita scambio
               Casella casella = controller.getTabellone().getCasella(x, y);
               if (primaSelezione == null) {
                    if (casella.isOccupata() &&
                            casella.getPedinaOccupante().getColore() ==
                                    controller.turnoCorrente().getGiocatoreCorrente().getColore()) {
                         primaSelezione = new Point(x, y);
                         bottoni[x][y].setBorder(BorderFactory.createLineBorder
                                 (Color.YELLOW, 3));
                    }
               } else {
                    // Evita lo scambio sulla stessa casella
                    if (primaSelezione.x == x && primaSelezione.y == y) {
                         bottoni[primaSelezione.x][primaSelezione.y].setBorder(null);
                         primaSelezione = null;
                         return;
                    }
                    try {
                         controller.scambiaPedine(primaSelezione.x, primaSelezione.y, x, y);
                         aggiornaTabellone();
                    } catch (Exception e) {
                         mostraMessaggio("Errore scambio: " + e.getMessage());
                    }
                    bottoni[primaSelezione.x][primaSelezione.y].setBorder(null);
                    primaSelezione = null;
               }
               return;
          }

          if (pedinaSelezionata == null) {
               mostraMessaggio("Seleziona prima una pedina dalla lista!");
               return;
          }

          try {
               controller.schieraPedina(pedinaSelezionata, x, y);
               modelPedine.removeElement(pedinaSelezionata);
               pedinaSelezionata = null;
               aggiornaTabellone();

               if (modelPedine.isEmpty()) {
                    mostraMessaggio("Schieramento completato per " +
                            controller.turnoCorrente().getGiocatoreCorrente().getNome() + "!");
               }
          } catch (SchieramentoException e) {
               mostraMessaggio("Errore: " + e.getMessage());
          }
     }

     private void aggiornaPerGiocatore() {
          Giocatore giocatoreCorrente = controller.turnoCorrente().getGiocatoreCorrente();
          labelGiocatore.setText("Schieramento di: " + giocatoreCorrente.getNome() +
                  " (" + giocatoreCorrente.getColore() + ")");
          caricaPedineGiocatore(giocatoreCorrente);
          aggiornaTabellone();
     }

     private void caricaPedineGiocatore(Giocatore giocatore) {
          modelPedine.clear();
          giocatore.getPedine().forEach(modelPedine::addElement);
     }

     private void aggiornaTabellone() {
          Giocatore giocatoreCorrente = controller.turnoCorrente().getGiocatoreCorrente();

          for (int y = 0; y < DIMENSIONE; y++) {
               for (int x = 0; x < DIMENSIONE; x++) {
                    JButton button = bottoni[x][y];
                    Casella casella = controller.getTabellone().getCasella(x, y);

                    if (casella.isAcqua()) {
                         configuraBottone(button, false, Color.CYAN, null);
                    } else if (casella.isOccupata()) {
                         Pedina pedina = casella.getPedinaOccupante();
                         boolean isMiaPedina = pedina.getColore() == giocatoreCorrente.getColore();

                         boolean abilitato = modelPedine.isEmpty() && isMiaPedina;
                         configuraBottone(button, abilitato, null,
                                 GestoreRisorse.getImmaginePedina(pedina, !isMiaPedina));
                    } else {
                         boolean zonaValida = isZonaValida(giocatoreCorrente.getColore(), y);
                         Color coloreSfondo = zonaValida ? getColoreZona(giocatoreCorrente.getColore()) : new Color(139, 69, 19);
                         boolean abilitato = !modelPedine.isEmpty() && zonaValida;
                         configuraBottone(button, abilitato, coloreSfondo, null);
                    }
               }
          }
     }

     private void configuraBottone(JButton button,
                                   boolean abilitato,
                                   Color sfondo,
                                   ImageIcon icona) {
          button.setText("");
          button.setEnabled(abilitato);
          if (sfondo != null) button.setBackground(sfondo);
          button.setIcon(icona);
     }

     private boolean isZonaValida(Colore colore, int y) {
          return (colore == Colore.BLU && y >= 6 && y <= 9) ||
                  (colore == Colore.ROSSO && y >= 0 && y <= 3);
     }

     private Color getColoreZona(Colore colore) {
          return colore == Colore.BLU ? new Color(173, 216, 230) : new Color(255, 182, 193);
     }

     private void confermaSchieramento() {
          if (!modelPedine.isEmpty()) {
               mostraMessaggio("Devi schierare tutte le pedine prima di confermare!");
               return;
          }
          passaAlProssimoGiocatore();
     }

     private void passaAlProssimoGiocatore() {
          giocatoriSchierati++;
          if (giocatoriSchierati >= 2) {
               terminaSchieramento();
          } else {
               cambiaGiocatore();
          }
     }

     private void terminaSchieramento() {
          schieramentoCompletato = true;
          mostraMessaggio("Schieramento completato!\n\nLa partita può iniziare!\n\nInizierà a giocare: " +
                  controller.turnoCorrente().getGiocatoreCorrente().getNome());
          dispose();
     }


     public boolean isSchieramentoCompletato() {
          return schieramentoCompletato;
     }

     private void cambiaGiocatore() {
          String giocatorePrecedente = controller.turnoCorrente().getGiocatoreCorrente().getNome();
          controller.cambiaTurno();
          String nuovoGiocatore = controller.turnoCorrente().getGiocatoreCorrente().getNome();

          mostraMessaggio("Schieramento di " + giocatorePrecedente + " completato!\n\n" +
                  "___CAMBIO GIOCATORE___\n\n" +
                  "Ora tocca a: " + nuovoGiocatore + ".\n" +
                  nuovoGiocatore + ", clicca ok quando sei pronto per schierare.");
          aggiornaPerGiocatore();
     }

     private void mostraMessaggio(String messaggio) {
          JOptionPane.showMessageDialog(this, messaggio);
     }
}