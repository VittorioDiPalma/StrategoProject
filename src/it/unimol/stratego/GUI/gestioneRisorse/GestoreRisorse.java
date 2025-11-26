package it.unimol.stratego.GUI.gestioneRisorse;

import it.unimol.stratego.app.pedine.Pedina;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class GestoreRisorse {
     private static final Map<String, ImageIcon> CACHE_IMMAGINI = new HashMap<>();

     public static ImageIcon getImmagineLogo(int width, int height) {
          return getImmagineRidimensionata("images/logo.png", width, height);
     }

     public static ImageIcon getImmaginePedina(Pedina pedina, boolean coperta) {
          String percorso = getPercorsoPedina(pedina, coperta);
          return getImmagineRidimensionata(percorso, MisureImmaginiGUI.PEDINA_SIZE, MisureImmaginiGUI.PEDINA_SIZE);
     }

     public static ImageIcon getImmagineTabellone() {
          return getImmagineRidimensionata("images/tabellone.png",
                  MisureImmaginiGUI.TABELLONE_WIDTH,
                  MisureImmaginiGUI.TABELLONE_HEIGHT);
     }

     public static ImageIcon getImmagineInfoPedine(int width, int height) {
          return getImmagineRidimensionata("images/info_pedine.png", width, height);
     }

     private static String getPercorsoPedina(Pedina pedina, boolean coperta) {
          if (coperta) {
               return "images/copertura/pedina_" + pedina.getColore().toString().toLowerCase() + "_coperta.png";
          } else {
               // percorso: images/pedine/colore/nome.png
               return "images/pedine/" + pedina.getColore().toString().toLowerCase() + "/" +
                       pedina.getNome().toLowerCase() + ".png";
          }
     }

     private static ImageIcon getImmagineRidimensionata(String percorso, int width, int height) {
          String chiaveCache = percorso + "_" + width + "x" + height;
          if (CACHE_IMMAGINI.containsKey(chiaveCache)) {
               return CACHE_IMMAGINI.get(chiaveCache);
          }
          URL urlImmagine = GestoreRisorse.class.getClassLoader().getResource(percorso);
          if (urlImmagine == null) {
               return null;
          }
          ImageIcon iconaOriginale = new ImageIcon(urlImmagine);
          if (iconaOriginale.getIconWidth() <= 0) {
               return null;
          }

          int originalWidth = iconaOriginale.getIconWidth();
          int originalHeight = iconaOriginale.getIconHeight();
          double aspectRatio = (double) originalWidth / originalHeight;

          int newWidth = width;
          int newHeight = height;
          if (originalWidth > originalHeight) {
               newHeight = (int) (width / aspectRatio);
          } else {
               newWidth = (int) (height * aspectRatio);
          }

          Image scaledImage = iconaOriginale.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
          ImageIcon iconaRidimensionata = new ImageIcon(scaledImage);
          CACHE_IMMAGINI.put(chiaveCache, iconaRidimensionata);
          return iconaRidimensionata;
     }
}