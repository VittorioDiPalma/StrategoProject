package it.unimol.stratego.GUI.panels;

import it.unimol.stratego.GUI.gestioneRisorse.GestoreRisorse;
import it.unimol.stratego.GUI.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

public class MenuPrincipalePanel extends JPanel {
     public MenuPrincipalePanel(MainFrame mainFrame) {
          setLayout(new GridBagLayout());
          setBackground(new Color(139, 69, 19));

          GridBagConstraints gbc = new GridBagConstraints();
          gbc.gridx = 0;
          gbc.fill = GridBagConstraints.HORIZONTAL;
          gbc.insets = new Insets(15, 30, 15, 30);

          ImageIcon logoIcon = GestoreRisorse.getImmagineLogo(600, 218);
          JLabel logoLabel = (logoIcon != null)
                  ? new JLabel(logoIcon)
                  : creaLogoFallback();
          gbc.gridy = 0;
          gbc.insets = new Insets(0, 0, 30, 0);
          add(logoLabel, gbc);

          Font buttonFont = new Font("Arial", Font.BOLD, 22);
          Dimension buttonSize = new Dimension(260, 60);
          Color buttonBg = new Color(92, 51, 23);
          Color buttonHover = new Color(120, 70, 30);
          Color buttonFg = Color.WHITE;

          JButton btnNuova = creaButton("Nuova Partita", buttonFont, buttonSize, buttonBg, buttonHover, buttonFg);
          JButton btnCarica = creaButton("Carica Partita", buttonFont, buttonSize, buttonBg, buttonHover, buttonFg);
          JButton btnRegolamento = creaButton("Regolamento", buttonFont, buttonSize, buttonBg, buttonHover, buttonFg);
          JButton btnEsci = creaButton("Esci", buttonFont, buttonSize, buttonBg, buttonHover, buttonFg);

          btnNuova.addActionListener(_ -> mainFrame.avviaNuovaPartita());
          btnCarica.addActionListener(_ -> mainFrame.caricaPartita());
          btnRegolamento.addActionListener(_ -> apriRegolamentoPDF());
          btnEsci.addActionListener(_ -> System.exit(0));

          gbc.insets = new Insets(15, 30, 15, 30);
          gbc.gridy = 1; add(btnNuova, gbc);
          gbc.gridy = 2; add(btnCarica, gbc);
          gbc.gridy = 3; add(btnRegolamento, gbc);
          gbc.gridy = 4; add(btnEsci, gbc);
     }

     private JLabel creaLogoFallback() {
          JLabel label = new JLabel("Logo non trovato");
          label.setForeground(Color.WHITE);
          label.setFont(new Font("Arial", Font.BOLD, 20));
          return label;
     }

     private JButton creaButton(String text, Font font, Dimension size, Color bg, Color hover, Color fg) {
          JButton button = new JButton(text);
          button.setFont(font);
          button.setPreferredSize(size);
          button.setBackground(bg);
          button.setForeground(fg);
          button.setFocusPainted(false);
          button.setOpaque(true);
          button.setBorderPainted(false);

          button.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
               @Override
               public void mouseExited(MouseEvent e) { button.setBackground(bg); }
          });

          return button;
     }

     private void apriRegolamentoPDF() {
          try {
               InputStream is = getClass().getClassLoader().getResourceAsStream
                       ("regolamento.pdf");
               if (is == null) {
                    JOptionPane.showMessageDialog
                            (this,
                            "File regolamento.pdf non trovato nelle risorse!");
                    return;
               }
               File tempFile = File.createTempFile("regolamento", ".pdf");
               tempFile.deleteOnExit();
               try (OutputStream os = new FileOutputStream(tempFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                         os.write(buffer, 0, bytesRead);
                    }
               }
               is.close();

               Desktop.getDesktop().open(tempFile);
          } catch (Exception ex) {
               JOptionPane.showMessageDialog(this, "Impossibile aprire il regolamento: " + ex.getMessage());
          }
     }
}