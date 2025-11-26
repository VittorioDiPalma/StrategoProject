package it.unimol.stratego.GUI.panels;

import it.unimol.stratego.GUI.MainFrame;
import it.unimol.stratego.app.Controller;
import javax.swing.*;
import java.awt.*;

public class GiocoPanel extends JPanel {
     private final TabellonePanel tabellonePanel;
     private final InfoPanel infoPanel;
     private final MenuPanel menuPanel;

     public GiocoPanel(MainFrame mainFrame, Controller controller) {
          setLayout(new BorderLayout());

          tabellonePanel = new TabellonePanel();
          infoPanel = new InfoPanel();
          menuPanel = new MenuPanel(mainFrame);

          add(tabellonePanel, BorderLayout.CENTER);
          add(infoPanel, BorderLayout.EAST);
          add(menuPanel, BorderLayout.SOUTH);

          aggiorna(controller);
     }

     public void aggiorna(Controller controller) {
          tabellonePanel.aggiorna(controller);
          infoPanel.aggiorna(controller);
          menuPanel.aggiorna(controller);
     }
}
