package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.pedine.*;
import it.unimol.stratego.app.pedine.Pedina;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MossaTest {
     Pedina pedina = new Artificiere(Colore.ROSSO);
     Casella casella1 = new Casella(1,2);
     Casella casella2 = new Casella(1, 3);

     @Test
     void testCostruttore(){
          Mossa mossa = new Mossa (pedina, casella1, casella2);
          assertEquals(pedina, mossa.getPedina());
          assertEquals(casella1, mossa.getPartenza());
          assertEquals(casella2, mossa.getDestinazione());
          assertFalse(mossa.isMossaVincente());
     }

      @Test
     void testSetMossaVincente(){
          Mossa mossa = new Mossa (pedina, casella1, casella2);
          assertFalse(mossa.isMossaVincente());
          mossa.setMossaVincente();
          assertTrue(mossa.isMossaVincente());
     }

     @Test
     void testToString(){
          Mossa mossa = new Mossa(pedina, casella1, casella2);
          String expected = pedina + " da 1,2 a 1,3";
          assertEquals(expected, mossa.toString());
     }

     @Test
     void testToStringSicuro(){
          Mossa mossa = new Mossa(pedina, casella1, casella2);
          String expected = "Pedina ROSSO da 2,3 a 2,4";
          assertEquals(expected, mossa.toStringSicuro());
     }
}
