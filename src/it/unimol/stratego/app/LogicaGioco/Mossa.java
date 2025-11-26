package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.pedine.Pedina;
import java.io.Serializable;

/**
 * La classe Mossa rappresenta una mossa effettuata da una pedina su una scacchiera.
 * Contiene informazioni sulla pedina coinvolta, la casella di partenza e la casella di destinazione.
 *
 * @author Vittorio
 * @version 1.0
 */
public class Mossa implements Serializable {
     private final Pedina pedina;
     private final Casella partenza;
     private final Casella destinazione;
     private boolean mossaVincente;

     public Mossa(Pedina pedina, Casella partenza, Casella destinazione) {
          this.pedina = pedina;
          this.partenza = partenza;
          this.destinazione = destinazione;
          this.mossaVincente = false;
     }

     public Pedina getPedina() {
          return this.pedina;
     }

     public Casella getPartenza() {
          return this.partenza;
     }

     public Casella getDestinazione() {
          return this.destinazione;
     }

     public void setMossaVincente(){
          this.mossaVincente = true;
     }

     public boolean isMossaVincente() {
          return this.mossaVincente;
     }

     @Override
     public String toString() {
          return this.pedina + " da " + this.partenza.getPosizioneX() + "," + this.partenza.getPosizioneY() +
                  " a " + this.destinazione.getPosizioneX() + "," + this.destinazione.getPosizioneY();
     }

     /**
      * ToString sicuro che restituisce una descrizione della mossa
      * senza rivelare informazioni sensibili come il tipo di pedina.
      *
      * @return Stringa che descrive la mossa senza rivelare dettagli sensibili.
      */
     public String toStringSicuro() {
          String colorePedina = this.pedina.getColore().toString();
          return "Pedina " + colorePedina + " da " +
                  (this.partenza.getPosizioneX() + 1) + "," + (this.partenza.getPosizioneY() + 1) +
                  " a " + (this.destinazione.getPosizioneX() + 1) + "," + (this.destinazione.getPosizioneY() + 1);
     }
}
