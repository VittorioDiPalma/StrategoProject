package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestoreTurniTest {
     Giocatore giocatore1 = new Giocatore("Vittorio", Colore.ROSSO);
      Giocatore giocatore2 = new Giocatore("Antonio", Colore.BLU);

     @Test
     void testCostruttore(){
          GestoreTurni gestoreTurni = new GestoreTurni(giocatore1, giocatore2);
          Giocatore expected = gestoreTurni.getTurnoCorrente().getGiocatoreCorrente();
          assert (expected.equals(giocatore1) || expected.equals(giocatore2));
     }

     @Test
      void testPassaAlProssimoTurno(){
          GestoreTurni gestore = new GestoreTurni(giocatore1, giocatore2);
          Giocatore primoGiocatore = gestore.getTurnoCorrente().getGiocatoreCorrente();
          gestore.passaAlProssimoTurno();
          assertNotEquals(primoGiocatore, gestore.getTurnoCorrente().getGiocatoreCorrente());
     }

     @Test
      void testCicloTurni(){
            GestoreTurni gestore = new GestoreTurni(giocatore1, giocatore2);
            Giocatore primoGiocatore = gestore.getTurnoCorrente().getGiocatoreCorrente();
            gestore.passaAlProssimoTurno();
            Giocatore secondoGiocatore = gestore.getTurnoCorrente().getGiocatoreCorrente();
            assertNotEquals(primoGiocatore, secondoGiocatore);
            gestore.passaAlProssimoTurno();
            assertEquals(primoGiocatore, gestore.getTurnoCorrente().getGiocatoreCorrente());
      }
}
