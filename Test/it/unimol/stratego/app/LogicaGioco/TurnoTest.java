package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurnoTest {
     @Test
      void testCostruttore(){
            Giocatore giocatore = new Giocatore("Mario", Colore.ROSSO);
            Turno turno = new Turno(giocatore);
            assertEquals(giocatore, turno.getGiocatoreCorrente());
      }
}
