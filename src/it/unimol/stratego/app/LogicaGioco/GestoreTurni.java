package it.unimol.stratego.app.LogicaGioco;

import java.io.Serializable;
import java.util.Random;

/**
 * La classe GestoreTurni gestisce i turni di gioco tra due giocatori.
 * Si occupa di tenere traccia del turno corrente e di passare al turno successivo.
 *
 * @author Vittorio
 * @version 1.0
 */
public class GestoreTurni implements Serializable {
     private final Turno[] turni;
     private int turnoCorrente;

     /**
      * Costruttore della classe GestoreTurni.
      * Seleziona casualmente quale giocatore inizia per primo.
      *
      * @param giocatore1 Primo giocatore.
      * @param giocatore2 Secondo giocatore.
      */
     public GestoreTurni(Giocatore giocatore1, Giocatore giocatore2) {
          this.turni = new Turno[2];
          this.turni[0] = new Turno(giocatore1);
          this.turni[1] = new Turno(giocatore2);
          this.turnoCorrente = new Random().nextInt(2);
     }

     public Turno getTurnoCorrente() {
          return this.turni[turnoCorrente];
     }

     public void passaAlProssimoTurno() {
          this.turnoCorrente = (this.turnoCorrente + 1) % turni.length;
     }
}
