package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.pedine.*;
import java.io.Serializable;

/**
 * La classe Casella rappresenta una singola casella del tabellone di gioco.
 * Ogni casella ha una posizione (coordinate X e Y), e può essere occupata da una pedina
 *
 *
 * @author Vittorio
 * @version 1.0
 */

public class Casella implements Serializable {
     private final int posizioneX;
     private final int posizioneY;
     private Pedina pedinaOccupante;
     private boolean isAcqua;

     public Casella(int posizioneX, int posizioneY) {
          this.posizioneX = posizioneX;
          this.posizioneY = posizioneY;
          this.pedinaOccupante = null;
     }

     public void setAcqua() {
          this.isAcqua = true;
     }

     public boolean isAcqua() {
          return this.isAcqua;
     }

     public int getPosizioneX() {
          return this.posizioneX;
     }

     /**
      * Rimuove la pedina che occupa la casella.
      */
     public void rimuoviPedina(){
          this.pedinaOccupante.elimina();
          this.pedinaOccupante = null;
     }

     public void setPedinaOccupante(Pedina pedina) {
          this.pedinaOccupante = pedina;
     }

     public int getPosizioneY() {
          return this.posizioneY;
     }

     public Pedina getPedinaOccupante() {
          return this.pedinaOccupante;
     }

     public boolean isOccupata() {
          return this.pedinaOccupante != null;
     }
}
