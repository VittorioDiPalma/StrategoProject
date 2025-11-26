package it.unimol.stratego.GUI.panels;

import it.unimol.stratego.app.Controller;
import it.unimol.stratego.app.LogicaGioco.Mossa;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InfoPanel extends JPanel {
     private static final Dimension PANEL_SIZE = new Dimension(250, 0);
     private static final Font FONT_TURNO = new Font("Arial", Font.BOLD, 14);
     private static final Font FONT_CRONOLOGIA = new Font("Monospaced", Font.PLAIN, 11);

     private JLabel labelTurno;
     private JTextArea areaCronologia;

     public InfoPanel() {
          setupPanel();
          creaComponenti();
          layoutComponenti();
          setBackground(new Color(139, 69, 19));
     }

     private void setupPanel() {
          setLayout(new BorderLayout());
          setPreferredSize(PANEL_SIZE);
          setBorder(BorderFactory.createTitledBorder(
                  BorderFactory.createLineBorder(Color.WHITE),
                  "Informazioni Partita",
                  0,0, null, Color.WHITE));
     }

     private void creaComponenti() {
          labelTurno = creaLabelTurno();
          areaCronologia = creaCronologiaArea();
     }

     private JLabel creaLabelTurno() {
          JLabel label = new JLabel("Turno: ");
          label.setFont(FONT_TURNO);
          label.setHorizontalAlignment(SwingConstants.CENTER);
          label.setForeground(Color.WHITE);
          return label;
     }

     private JTextArea creaCronologiaArea() {
          JTextArea area = new JTextArea();
          area.setEditable(false);
          area.setFont(FONT_CRONOLOGIA);
          return area;
     }

     private void layoutComponenti() {
          add(labelTurno, BorderLayout.NORTH);
          add(new JScrollPane(areaCronologia), BorderLayout.CENTER);
     }

     public void aggiorna(Controller controller) {
          if (controller == null) return;

          // Aggiorna turno
          String nomeGiocatore = controller.turnoCorrente().getGiocatoreCorrente().getNome();
          String colore = controller.turnoCorrente().getGiocatoreCorrente().getColore().toString();
          labelTurno.setText("<html><center>Turno di:<br><b>" + nomeGiocatore +
                  "</b><br>(" + colore + ")<br><small>Le tue pedine sono visibili</small></center></html>");

          aggiornaCronologia(controller.getCronologiaMosse());
     }

     private void aggiornaCronologia(List<Mossa> mosse) {
          StringBuilder sb = new StringBuilder();

          if (mosse.isEmpty()) {
               sb.append("Nessuna mossa eseguita");
          } else {
               for (int i = 0; i < mosse.size(); i++) {
                    Mossa mossa = mosse.get(i);

                    sb.append((i + 1)).append(". ").append(mossa.toStringSicuro());

                    if (mossa.isMossaVincente()) {
                         sb.append(" [VINCENTE!]");
                    }
                    sb.append("\n");
               }
          }

          areaCronologia.setText(sb.toString());
          SwingUtilities.invokeLater(() ->
                  areaCronologia.setCaretPosition(areaCronologia.getDocument().getLength()));
     }
}