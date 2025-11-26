package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.exceptions.MossaException;
import it.unimol.stratego.app.exceptions.MovimentoPedinaException;
import it.unimol.stratego.app.pedine.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestoreMosseTest {
     Tabellone tabellone = new Tabellone();

     @Test
     void testCostruttore(){
          GestoreMosse gestore = new GestoreMosse(tabellone);
          assertNotNull(gestore);
     }

     @Test
     void testMossaNonValidaBandiera() {
           GestoreMosse gestore = new GestoreMosse(tabellone);
           Casella partenza = tabellone.getCasella(0, 0);
           Casella destinazione = tabellone.getCasella(0, 1);
           Pedina bandiera = new Bandiera(Colore.ROSSO);
           partenza.setPedinaOccupante(bandiera);

           assertThrows(MossaException.class,
                   () -> gestore.isMossaValida(bandiera, partenza, destinazione));


      }

      @Test
     void testMossaVersoCasellaOccupataDaPropriaPedina(){
              GestoreMosse gestore = new GestoreMosse(tabellone);
              Casella partenza = tabellone.getCasella(0, 0);
              Casella destinazione = tabellone.getCasella(0, 1);
              Pedina pedina1 = new Artificiere(Colore.ROSSO);
              Pedina pedina2 = new Artificiere(Colore.ROSSO);
              partenza.setPedinaOccupante(pedina1);
              destinazione.setPedinaOccupante(pedina2);

              assertThrows(MossaException.class,
                    () -> gestore.isMossaValida(pedina1, partenza, destinazione));
      }

      @Test
     void testMovimentoPiuCaselleDiEsploratore(){
          GestoreMosse gestore = new GestoreMosse(tabellone);
          Casella partenza = tabellone.getCasella(0, 0);
          Casella destinazione = tabellone.getCasella(0, 8);
          Pedina esploratore = new Esploratore(Colore.ROSSO);
          partenza.setPedinaOccupante(esploratore);
          assertDoesNotThrow(
                  () -> gestore.isMossaValida(esploratore, partenza, destinazione));
      }

     @Test
     void testMovimentoPiuCaselleDiNonEsploratore(){
          GestoreMosse gestore = new GestoreMosse(tabellone);
          Casella partenza = tabellone.getCasella(0, 0);
          Casella destinazione = tabellone.getCasella(0, 8);
          Pedina artificiere = new Artificiere(Colore.ROSSO);
          partenza.setPedinaOccupante(artificiere);
          assertThrows(MovimentoPedinaException.class,
                  () -> gestore.isMossaValida(artificiere, partenza, destinazione));
     }

     @Test
     void testCasellaNonRaggiungibile() {
          GestoreMosse gestore = new GestoreMosse(tabellone);
          Casella partenza = tabellone.getCasella(1, 1);
          Casella destinazione = tabellone.getCasella(2, 2);
          Pedina artificiere = new Artificiere(Colore.ROSSO);
          partenza.setPedinaOccupante(artificiere);
          assertThrows(MovimentoPedinaException.class,
                  () -> gestore.isMossaValida(artificiere, partenza, destinazione));
     }

     @Test
     void testEseguiMossaSemplice(){
          GestoreMosse gestore = new GestoreMosse(tabellone);
          Casella partenza = tabellone.getCasella(0, 0);
          Casella destinazione = tabellone.getCasella(0, 1);
          Pedina artificiere = new Artificiere(Colore.ROSSO);
          partenza.setPedinaOccupante(artificiere);
          Mossa mossa = new Mossa(artificiere, partenza, destinazione);

          assertDoesNotThrow(() -> gestore.eseguiMossa(mossa));
          assertFalse(partenza.isOccupata());
          assertTrue(destinazione.isOccupata());
          assertEquals(artificiere, destinazione.getPedinaOccupante());
     }

     @Test
     void testEseguiMossaConScontro(){
          GestoreMosse gestore = new GestoreMosse(tabellone);
          Casella partenza = tabellone.getCasella(0, 0);
          Casella destinazione = tabellone.getCasella(0, 1);
          Pedina artificiere = new Artificiere(Colore.ROSSO);
          Pedina esploratore = new Esploratore(Colore.BLU);
          partenza.setPedinaOccupante(artificiere);
          destinazione.setPedinaOccupante(esploratore);
          Mossa mossa = new Mossa(artificiere, partenza, destinazione);

          assertDoesNotThrow(() -> gestore.eseguiMossa(mossa));
          assertFalse(partenza.isOccupata());
          assertTrue(destinazione.isOccupata());
          assertEquals(artificiere, destinazione.getPedinaOccupante());
     }
}
