package it.unimol.stratego;

import it.unimol.stratego.GUI.MainFrame;

import javax.swing.*;

public class Main {
     public static void main(String[] args) {
          try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) {
               e.printStackTrace();
          }

          SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
     }
}