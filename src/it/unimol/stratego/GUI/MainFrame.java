package it.unimol.stratego.GUI;

import it.unimol.stratego.GUI.panels.GiocoPanel;
import it.unimol.stratego.GUI.panels.MenuPrincipalePanel;
import it.unimol.stratego.GUI.panels.SchieramentoPanel;
import it.unimol.stratego.app.Controller;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
     private JPanel currentPanel;
     private Controller controller;

     public MainFrame() {
          setTitle("Stratego");
          setMinimumSize(new Dimension(900, 700));
          setDefaultCloseOperation(EXIT_ON_CLOSE);
          setLocationRelativeTo(null);
          setVisible(true);
          try {
               AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                       new java.io.File("resources/musica.wav")
               );
               Clip clip = AudioSystem.getClip();
               clip.open(audioStream);
               clip.loop(Clip.LOOP_CONTINUOUSLY);
          } catch (Exception e) {
               e.printStackTrace();
          }
          mostraMenuPrincipale();
     }

     public void mostraMenuPrincipale() {
          setPanel(new MenuPrincipalePanel(this));
     }

     public void avviaNuovaPartita() {
          InfoGiocatoriGUI.InformazioniGiocatori info = InfoGiocatoriGUI.raccogliInformazioni(this);
          if (info != null) {
               controller = new Controller(info.giocatore1, info.giocatore2);

               if (info.schieramentoAutomatico) {
                    try {
                         controller.schieramentoAutomatico();
                         JOptionPane.showMessageDialog(this,
                                 "Schieramento automatico completato.\n" +
                                         "La partita può iniziare!\nInizia a giocare : " +
                                         controller.turnoCorrente().getGiocatoreCorrente().getNome());
                    } catch (Exception ex) {
                         JOptionPane.showMessageDialog(this,
                                 "Errore schieramento automatico: " + ex.getMessage());
                         return;
                    }
                    setPanel(new GiocoPanel(this, controller));
               } else {
                    SchieramentoPanel schieramento = new SchieramentoPanel(this, controller);
                    schieramento.setVisible(true);
                    if (schieramento.isSchieramentoCompletato()) {
                         setPanel(new GiocoPanel(this, controller));
                    } else {
                         mostraMenuPrincipale();
                    }
               }
          }
     }

     // Sostituisci nel metodo caricaPartita di MainFrame.java
     public void caricaPartita() {
          JFileChooser fileChooser = new JFileChooser("salvataggi/");
          fileChooser.setDialogTitle("Seleziona la partita da caricare");
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setAcceptAllFileFilterUsed(false);
          fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter
                  ("File di salvataggio", "stratego"));

          int result = fileChooser.showOpenDialog(this);
          if (result == JFileChooser.APPROVE_OPTION) {
               String nomeFile = fileChooser.getSelectedFile().getName()
                       .replaceFirst("[.][^.]+$", ""); //elimina tutto ciò che segue l'ultimo punto compresa l'estensione

               try {
                    controller = Controller.caricaPartitaDaFile(nomeFile);
                    JOptionPane.showMessageDialog(this,
                            "Partita caricata con successo!",
                            "Caricamento", JOptionPane.INFORMATION_MESSAGE);
                    setPanel(new GiocoPanel(this, controller));
               } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Errore caricamento: " + e.getMessage());
               }
          }
     }

     private void setPanel(JPanel panel) {
          if (currentPanel != null) remove(currentPanel);
          currentPanel = panel;
          add(currentPanel);
          revalidate();
          repaint();
     }

     public void aggiornaGUI() {
          if (currentPanel instanceof GiocoPanel && controller != null) {
               ((GiocoPanel) currentPanel).aggiorna(controller);
          }
     }

     public Controller getController() {
          return controller;
     }
}