package it.unimol.stratego.app.pedine;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.EsitoScontro;

public class Artificiere extends Pedina {

     public Artificiere(Colore colore) {
          super("Artificiere", colore, 3, true);
     }

     @Override
     public EsitoScontro attacca(Pedina avversario) {
          if (avversario instanceof Bomba){
               return EsitoScontro.VITTORIA;
          } else {
               return super.attacca(avversario);
          }
     }
}

