package it.unimol.stratego.app.pedine;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.EsitoScontro;

public class Spia extends Pedina {

     public Spia(Colore colore) {
          super("Spia", colore, 1, true);
     }

     @Override
     public EsitoScontro attacca(Pedina avversario) {
          if (avversario instanceof Maresciallo) {
               return EsitoScontro.VITTORIA;
          } else {
               return super.attacca(avversario);
          }
     }
}