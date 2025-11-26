package it.unimol.stratego.GUI.panels;

import it.unimol.stratego.GUI.gestioneRisorse.GestoreRisorse;
import it.unimol.stratego.GUI.MainFrame;
import it.unimol.stratego.app.Controller;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
     private final MainFrame mainFrame;
     private JButton btnSalva;

     public MenuPanel(MainFrame mainFrame) {
          this.mainFrame = mainFrame;

          setLayout(new FlowLayout());
          setBorder(BorderFactory.createTitledBorder(
                  BorderFactory.createLineBorder(Color.WHITE),
                  "Menù",
                  0,0, null, Color.WHITE));
          setBackground(new Color(139, 69, 19));

          inizializzaPulsanti();
     }

     private void inizializzaPulsanti() {
          JButton btnInfoPedine = new JButton("Info Pedine");
          btnSalva = new JButton("Salva");
          JButton btnMenu = new JButton("Torna al Menù Principale");

          btnInfoPedine.addActionListener(_ -> mostraInfoPedine());
          btnSalva.addActionListener(_ -> salvaPartita());
          btnMenu.addActionListener(_ -> tornaAlMenu());

          add(btnInfoPedine);
          add(btnSalva);
          add(btnMenu);
     }

     private void mostraInfoPedine() {
          ImageIcon icon = GestoreRisorse.getImmagineInfoPedine(690, 411);
          JLabel label = (icon != null) ? new JLabel(icon) : new JLabel("Immagine non trovata");
          JOptionPane.showMessageDialog(mainFrame, label, "Info Pedine", JOptionPane.PLAIN_MESSAGE);
     }

     private void salvaPartita() {
          if (mainFrame.getController() == null) return;

          String nomeFile = JOptionPane.showInputDialog(mainFrame, "Nome del file:");
          if (nomeFile != null && !nomeFile.trim().isEmpty()) {
               try {
                    mainFrame.getController().salvaPartita(nomeFile.trim());
                    JOptionPane.showMessageDialog(mainFrame, "Partita salvata!");
               } catch (Exception e) {
                    JOptionPane.showMessageDialog(mainFrame, "Errore salvataggio: " + e.getMessage());
               }
          }
     }

     private void tornaAlMenu(){
              int scelta = JOptionPane.showConfirmDialog(mainFrame,
                    "Vuoi tornare al menù principale? I dati della partita in corso andranno persi.",
                    "Torna al Menù Principale",
                    JOptionPane.YES_NO_OPTION);

              if (scelta == JOptionPane.YES_OPTION) {
                     mainFrame.dispose();
                     new MainFrame().setVisible(true);
              }
     }

     public void aggiorna(Controller controller) {
          btnSalva.setEnabled(controller != null);
     }
}