package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.exceptions.SchieramentoException;
import it.unimol.stratego.app.pedine.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TabelloneTest {

     @Test
     void testCreazioneLaghi() {
          Tabellone tabellone = new Tabellone();
          tabellone.creaLaghi();
          assertTrue(tabellone.getCasella(2, 4).isAcqua());
          assertTrue(tabellone.getCasella(3, 4).isAcqua());
          assertTrue(tabellone.getCasella(2, 5).isAcqua());
          assertTrue(tabellone.getCasella(3, 5).isAcqua());
          assertTrue(tabellone.getCasella(6, 4).isAcqua());
          assertTrue(tabellone.getCasella(7, 4).isAcqua());
          assertTrue(tabellone.getCasella(6, 5).isAcqua());
          assertTrue(tabellone.getCasella(7, 5).isAcqua());

          assertFalse(tabellone.getCasella(0, 0).isAcqua());
     }

     @Test
     void testGetCaselleAdiacentiConLagoVicino(){
          Tabellone tabellone = new Tabellone();
          Casella casella = tabellone.getCasella(5,5);
          ArrayList<Casella> caselle = tabellone.getCaselleAdiacenti(casella);

          assertEquals(3, caselle.size());
          assertTrue(caselle.contains(tabellone.getCasella(4,5)));
          assertFalse(caselle.contains(tabellone.getCasella(6,5)));
          assertTrue(caselle.contains(tabellone.getCasella(5,4)));
          assertTrue(caselle.contains(tabellone.getCasella(5,6)));
     }

     @Test
     void testCaselleVisitabiliDaEsploratore(){
          Tabellone tabellone = new Tabellone();
          Casella casella = tabellone.getCasella(5,5);
          ArrayList<Casella> caselle = tabellone.getVisitabiliDaEsploratore(casella);

          assertEquals(10, caselle.size());
          assertTrue(caselle.contains(tabellone.getCasella(5,0)));
          assertTrue(caselle.contains(tabellone.getCasella(5,1)));
          assertTrue(caselle.contains(tabellone.getCasella(5,2)));
          assertTrue(caselle.contains(tabellone.getCasella(5,3)));
          assertTrue(caselle.contains(tabellone.getCasella(5,4)));
          assertTrue(caselle.contains(tabellone.getCasella(5,6)));
          assertTrue(caselle.contains(tabellone.getCasella(5,7)));
          assertTrue(caselle.contains(tabellone.getCasella(5,8)));
          assertTrue(caselle.contains(tabellone.getCasella(5,9)));


          assertTrue(caselle.contains(tabellone.getCasella(4,5)));
     }

     @Test
     void testAssegnazionePedinaACasellaVuota(){
          Tabellone tabellone = new Tabellone();
          Casella casella = tabellone.getCasella(1,1);
          Pedina pedina = new Artificiere(Colore.ROSSO);

          assertDoesNotThrow(() ->tabellone.assegnaPedinaACasella(pedina, casella));
          assertTrue(casella.isOccupata());
          assertEquals(pedina, casella.getPedinaOccupante());
     }

     @Test
      void testAssegnazionePedinaACasellaOccupata(){
            Tabellone tabellone = new Tabellone();
            Casella casella = tabellone.getCasella(1,1);
            Pedina pedina1 = new Artificiere(Colore.ROSSO);
            Pedina pedina2 = new Artificiere(Colore.BLU);

            assertDoesNotThrow(() ->tabellone.assegnaPedinaACasella(pedina1, casella));
            assertThrows(SchieramentoException.class, () -> tabellone.assegnaPedinaACasella(pedina2, casella));
      }
}
