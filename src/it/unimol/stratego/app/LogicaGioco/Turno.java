package it.unimol.stratego.app.LogicaGioco;

import java.io.Serializable;

/**
 * La classe Turno rappresenta il turno di un giocatore.
 * Contiene informazioni sul giocatore corrente.
 *
 * @author Vittorio
 * @version 1.0
 */
public class Turno implements Serializable {
     private final Giocatore giocatoreCorrente;

     public Turno(Giocatore giocatoreIniziale) {
          this.giocatoreCorrente = giocatoreIniziale;
     }

     public Giocatore getGiocatoreCorrente() {
          return giocatoreCorrente;
     }
}