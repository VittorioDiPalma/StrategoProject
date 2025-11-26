package it.unimol.stratego.GUI;

import javax.swing.*;

public class InfoGiocatoriGUI {

     public static class InformazioniGiocatori {
          public final String giocatore1;
          public final String giocatore2;
          public final boolean schieramentoAutomatico;

          public InformazioniGiocatori(String g1, String g2, boolean auto) {
               this.giocatore1 = g1;
               this.giocatore2 = g2;
               this.schieramentoAutomatico = auto;
          }
     }

     public static InformazioniGiocatori raccogliInformazioni(JFrame parent) {
          String giocatore1 = null, giocatore2 = null;
          while (giocatore1 == null || giocatore1.trim().isEmpty()) {
               giocatore1 = JOptionPane.showInputDialog(parent, "Nome Giocatore 1:");
               if (giocatore1 == null) return null;
               if (giocatore1.trim().isEmpty())
                    JOptionPane.showMessageDialog(parent, "Inserisci un nome valido per il Giocatore 1.");
          }
          while (giocatore2 == null || giocatore2.trim().isEmpty()) {
               giocatore2 = JOptionPane.showInputDialog(parent, "Nome Giocatore 2:");
               if (giocatore2 == null) return null;
               if (giocatore2.trim().isEmpty())
                    JOptionPane.showMessageDialog(parent, "Inserisci un nome valido per il Giocatore 2.");
          }
          boolean automatico = chiediTipoSchieramento(parent);
          return new InformazioniGiocatori(giocatore1, giocatore2, automatico);
     }
     private static boolean chiediTipoSchieramento(JFrame parent) {
          String[] opzioni = {"Schieramento Manuale", "Schieramento Automatico"};
          int scelta = JOptionPane.showOptionDialog(parent,
                  "Come vuoi schierare le pedine?",
                  "Tipo di Schieramento",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.QUESTION_MESSAGE,
                  null, opzioni, opzioni[0]);
          return scelta == 1; // true se automatico, false se manuale
     }
}