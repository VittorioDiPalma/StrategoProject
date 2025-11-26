package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.pedine.*;
import it.unimol.stratego.app.pedine.Pedina;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CasellaTest {
     @Test
     void testCostruttore() {
          Casella casella = new Casella(1, 2);
          assertEquals(1, casella.getPosizioneX());
          assertEquals(2, casella.getPosizioneY());
          assertNull(casella.getPedinaOccupante());
     }

     @Test
     void testSetAcqua() {
          Casella casella = new Casella(1, 2);
          casella.setAcqua();
          assertTrue(casella.isAcqua());
     }

     @Test
     void testSetPedinaOccupante() {
          Casella casella = new Casella(1, 2);
          Pedina pedina = new Artificiere(Colore.ROSSO);
          casella.setPedinaOccupante(pedina);
          assertTrue(casella.isOccupata());
          assertEquals(pedina, casella.getPedinaOccupante());
     }

     @Test
     void testRimuoviPedina() {
          Casella casella = new Casella(1, 2);
          Pedina pedina = new Artificiere(Colore.ROSSO);
          casella.setPedinaOccupante(pedina);
          assertTrue(casella.isOccupata());
          casella.rimuoviPedina();
          assertFalse(casella.isOccupata());
          assertNull(casella.getPedinaOccupante());
     }
}
