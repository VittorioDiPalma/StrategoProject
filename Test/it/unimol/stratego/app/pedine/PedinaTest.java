package it.unimol.stratego.app.pedine;

import it.unimol.stratego.app.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PedinaTest {

     @Test
     void testCostruttore(){
              Pedina pedina = new Artificiere(Colore.ROSSO);
              assert pedina.getForza() == 3;
              assert pedina.getColore() == Colore.ROSSO;
              assert !pedina.isScoperta();
              assert pedina.puoMuoversi();
              assert pedina.toString().equals("Artificiere (ROSSO)");
     }

     @Test
     void testScontri(){
              Pedina artif = new Artificiere(Colore.ROSSO);
              Pedina bomba = new Bomba(Colore.BLU);
              Pedina spia = new Spia(Colore.BLU);
              Pedina maresciallo = new Maresciallo(Colore.ROSSO);
              Pedina colonnello = new Colonnello(Colore.BLU);
              Pedina bandiera = new Bandiera(Colore.ROSSO);

              assertEquals(EsitoScontro.VITTORIA, artif.attacca(bomba));
              assertEquals(EsitoScontro.SCONFITTA, artif.attacca(colonnello));
              assertEquals(EsitoScontro.SCONFITTA, artif.attacca(maresciallo));

              assertEquals(EsitoScontro.VITTORIA, spia.attacca(maresciallo));
              assertEquals(EsitoScontro.SCONFITTA, spia.attacca(colonnello));

              assertEquals(EsitoScontro.BANDIERA_CATTURATA, artif.attacca(bandiera));

              assertEquals(EsitoScontro.SCONFITTA, maresciallo.attacca(bomba));
     }
}
