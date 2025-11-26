package it.unimol.stratego.GUI.panels;

import it.unimol.stratego.GUI.gestioneRisorse.GestoreRisorse;
import it.unimol.stratego.GUI.MainFrame;
import it.unimol.stratego.app.*;
import it.unimol.stratego.app.LogicaGioco.Casella;
import it.unimol.stratego.app.LogicaGioco.Giocatore;
import it.unimol.stratego.app.LogicaGioco.Mossa;
import it.unimol.stratego.app.pedine.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TabellonePanel extends JPanel {
     private static final int DIMENSIONE = 10;
     private static final Dimension BUTTON_SIZE = new Dimension(15, 15);

     private JButton[][] bottoni;
     private Controller controller;
     private Pedina pedinaSelezionata;
     private JButton bottonePedinaSelezionata;
     private boolean modalitaCambioGiocatore = false;
     private final Image tabelloneBackground;

     public TabellonePanel() {
          setPreferredSize(new Dimension(
                  BUTTON_SIZE.width * DIMENSIONE,
                  BUTTON_SIZE.height * DIMENSIONE
          ));
          setupPanel();
          inizializzaBottone();
          ImageIcon bgIcon = GestoreRisorse.getImmagineTabellone();
          setBackground(new Color(139, 69, 19));
          tabelloneBackground = (bgIcon != null) ? bgIcon.getImage() : null;
     }

     public void aggiorna(Controller controller) {
          this.controller = controller;

          if (controller != null && controller.isPartitaTerminata()) {
               bloccaTabellone();
               aggiornaVisualizzazione(controller);
               return;
          }

          if (controller == null) return;

          aggiornaVisualizzazione(controller);
          resettaSelezione();
          repaint();
     }

     private void setupPanel() {
          setLayout(new GridLayout(DIMENSIONE, DIMENSIONE, 2, 2));
          setOpaque(false);
     }

     private void inizializzaBottone() {
          bottoni = new JButton[DIMENSIONE][DIMENSIONE];

          for (int y = 0; y < DIMENSIONE; y++) {
               for (int x = 0; x < DIMENSIONE; x++) {
                    JButton button = new JButton();
                    button.setPreferredSize(BUTTON_SIZE);
                    button.setOpaque(false);
                    button.setContentAreaFilled(true);
                    button.setBorderPainted(true);
                    button.setFocusPainted(false);

                    final int finalX = x;
                    final int finalY = y;
                    button.addActionListener(_ -> gestisciClickCasella(finalX, finalY));

                    bottoni[x][y] = button;
                    add(button);
               }
          }
     }

     private void gestisciClickCasella(int x, int y) {
          if (controller == null || modalitaCambioGiocatore) return;

          if (controller.isPartitaTerminata()) {
               JOptionPane.showMessageDialog(this,
                       "La partita è già terminata! Inizia una nuova partita.");
               return;
          }

          try {
               Casella casella = controller.getTabellone().getCasella(x, y);

               if (pedinaSelezionata == null) {
                    if (casella.isOccupata()) {
                         pedinaSelezionata = controller.selezionaPedina(x, y);
                         bottonePedinaSelezionata = bottoni[x][y];
                         evidenziaPedinaSelezionata();
                    }
               } else {
                    try {
                         String giocatorePrecedente = controller.turnoCorrente().getGiocatoreCorrente().getNome();

                         controller.eseguiMossa(pedinaSelezionata, x, y);

                         if (controller.isPartitaTerminata()) {
                              String vincitore = getVincitore();

                              MainFrame parentFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                              parentFrame.aggiornaGUI();

                              bloccaTabellone();

                              JOptionPane.showMessageDialog(this,
                                      "PARTITA TERMINATA!\n\n" +
                                              "VINCITORE: " + vincitore + "\n\n" ,
                                      "VITTORIA!", JOptionPane.INFORMATION_MESSAGE);

                              resettaSelezione();
                              return;
                         }

                         modalitaCambioGiocatore = true;
                         aggiornaTabelloneNascosto();
                         mostraDialogoCambioGiocatore(giocatorePrecedente);
                         modalitaCambioGiocatore = false;

                         MainFrame parentFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
                         parentFrame.aggiornaGUI();

                    } catch (Exception ex) {
                         JOptionPane.showMessageDialog(this, "Errore nella mossa: " + ex.getMessage());
                    }

                    resettaSelezione();
               }


          } catch (Exception ex) {
               JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage());
               resettaSelezione();
          }
     }

     private void mostraDialogoCambioGiocatore(String giocatorePrecedente) {
          String nuovoGiocatore = controller.turnoCorrente().getGiocatoreCorrente().getNome();

          String messaggio = "Mossa di " + giocatorePrecedente + " completata!\n\n" +
                  "___CAMBIO GIOCATORE___\n\n" +
                  "Passa il computer a: " + nuovoGiocatore + ".\n" +
                  nuovoGiocatore + ", clicca quando sei pronto.";

          JOptionPane.showMessageDialog(this, messaggio,
                  "Cambio Giocatore", JOptionPane.INFORMATION_MESSAGE);
     }

     private void aggiornaTabelloneNascosto() {
          for (int y = 0; y < DIMENSIONE; y++) {
               for (int x = 0; x < DIMENSIONE; x++) {
                    JButton button = bottoni[x][y];
                    Casella casella = controller.getTabellone().getCasella(x, y);

                    if (casella.isAcqua()) {
                         button.setText("");
                         button.setEnabled(false);
                    } else if (casella.isOccupata()) {
                         Pedina pedina = casella.getPedinaOccupante();

                         ImageIcon pedinaIcon = GestoreRisorse.getImmaginePedina(pedina, true);
                         button.setIcon(pedinaIcon);
                         button.setText("");
                         button.setEnabled(false);
                    } else {
                         button.setIcon(null);
                         button.setText("");
                         button.setEnabled(false);
                    }
               }
          }
          repaint();
     }


     private void evidenziaPedinaSelezionata() {
          if (bottonePedinaSelezionata != null) {
               bottonePedinaSelezionata.setBorder(
                       BorderFactory.createLineBorder(Color.YELLOW, 4));
          }
     }

     @Override
     protected void paintComponent(Graphics g) {
          super.paintComponent(g);
          if (tabelloneBackground != null) {
               g.drawImage(tabelloneBackground, 0, 0, getWidth(), getHeight(), this);
          }
     }

     private void bloccaTabellone() {
          for (int y = 0; y < DIMENSIONE; y++) {
               for (int x = 0; x < DIMENSIONE; x++) {
                    bottoni[x][y].setEnabled(false);
               }
          }
     }

     private void resettaSelezione() {
          pedinaSelezionata = null;
          if (bottonePedinaSelezionata != null) {
               bottonePedinaSelezionata.setBorderPainted(true);
               bottonePedinaSelezionata.setBorder(
                       BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 0));
               bottonePedinaSelezionata = null;
          }
     }

     private void aggiornaVisualizzazione(Controller controller) {
          Giocatore giocatoreCorrente = controller.turnoCorrente().getGiocatoreCorrente();

          for (int y = 0; y < DIMENSIONE; y++) {
               for (int x = 0; x < DIMENSIONE; x++) {
                    JButton button = bottoni[x][y];
                    Casella casella = controller.getTabellone().getCasella(x, y);

                    if (casella.isOccupata()) {
                         Pedina pedina = casella.getPedinaOccupante();

                         boolean deveEssereVisibile = !modalitaCambioGiocatore &&
                                 (pedina.getColore() == giocatoreCorrente.getColore() ||
                                         pedina.isScoperta());

                         ImageIcon pedinaIcon = GestoreRisorse.getImmaginePedina(pedina, !deveEssereVisibile);
                         button.setIcon(pedinaIcon);
                         button.setHorizontalAlignment(SwingConstants.CENTER);
                         button.setVerticalAlignment(SwingConstants.CENTER);
                         button.setText("");
                         button.setContentAreaFilled(true);
                         button.setBorderPainted(true);
                         button.setFocusPainted(false);
                         button.setEnabled(!modalitaCambioGiocatore && !controller.isPartitaTerminata());

                    } else {
                         button.setIcon(null);
                         button.setText("");
                         button.setBackground(null);
                         button.setContentAreaFilled(false);
                         button.setBorderPainted(false);
                         button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 50), 1));
                         button.setEnabled(!modalitaCambioGiocatore && !controller.isPartitaTerminata());
                    }
               }
          }
     }

     private String getVincitore() {
          List<Mossa> mosse = controller.getCronologiaMosse();

          for (int i = mosse.size() - 1; i >= 0; i--) {
               Mossa mossa = mosse.get(i);
               if (mossa.isMossaVincente()) {
                    return mossa.getPedina().getColore() == Colore.BLU ?
                            "Giocatore BLU" : "Giocatore ROSSO";
               }
          }
          return controller.turnoCorrente().getGiocatoreCorrente().getNome();
     }
}