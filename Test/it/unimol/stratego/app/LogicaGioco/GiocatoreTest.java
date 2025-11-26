package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.pedine.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GiocatoreTest {
     @Test
     void testCostruttore(){
          Giocatore giocatore = new Giocatore("Vittorio", Colore.BLU);
          assertEquals("Vittorio", giocatore.getNome());
          assertEquals(Colore.BLU, giocatore.getColore());
          assertNotNull(giocatore.getPedine());
     }

     @Test
     void testCreaPedineGiocatoreRosse(){
          Giocatore giocatore = new Giocatore("Vittorio", Colore.ROSSO);
          assertEquals (40, giocatore.getPedine().size());
          int pedineRosse = giocatore.getPedine().stream()
                  .filter(p -> p.getColore().equals(Colore.ROSSO))
                  .toList()
                  .size();
          assertEquals(40, pedineRosse);
     }

      @Test
      void testCreaPedineGiocatoreBlu() {
           Giocatore giocatore = new Giocatore("Vittorio", Colore.BLU);
           assertEquals(40, giocatore.getPedine().size());
           int pedineBlu = giocatore.getPedine().stream()
                   .filter(p -> p.getColore().equals(Colore.BLU))
                   .toList()
                   .size();
           assertEquals(40, pedineBlu);
      }

      @Test
      void testNumeroPedineCreate(){
           Giocatore giocatore = new Giocatore("Vittorio", Colore.BLU);
           long bandiere = giocatore.getPedine().stream().filter(p -> p instanceof Bandiera).count();
           long spie = giocatore.getPedine().stream().filter(p -> p instanceof Spia).count();
           long marescialli = giocatore.getPedine().stream().filter(p -> p instanceof Maresciallo).count();
           long generali = giocatore.getPedine().stream().filter(p -> p instanceof Generale).count();
           long colonnelli = giocatore.getPedine().stream().filter(p -> p instanceof Colonnello).count();
           long comandanti = giocatore.getPedine().stream().filter(p -> p instanceof Comandante).count();
           long capitani = giocatore.getPedine().stream().filter(p -> p instanceof Capitano).count();
           long tenenti = giocatore.getPedine().stream().filter(p -> p instanceof Tenente).count();
           long sergenti = giocatore.getPedine().stream().filter(p -> p instanceof Sergente).count();
           long artificieri = giocatore.getPedine().stream().filter(p -> p instanceof Artificiere).count();
           long bombe = giocatore.getPedine().stream().filter(p -> p instanceof Bomba).count();
           long esploratori = giocatore.getPedine().stream().filter(p -> p instanceof Esploratore).count();

           assertEquals(1, bandiere);
           assertEquals(1, spie);
           assertEquals(1, marescialli);
           assertEquals(1, generali);
           assertEquals(2, colonnelli);
           assertEquals(3, comandanti);
           assertEquals(4, capitani);
           assertEquals(4, tenenti);
           assertEquals(4, sergenti);
           assertEquals(5, artificieri);
           assertEquals(6, bombe);
           assertEquals(8, esploratori);
      }

      @Test
      void testGetPedine(){
          Giocatore giocatore = new Giocatore("Vittorio", Colore.ROSSO);
          assertNotNull(giocatore.getPedine());
          assertEquals(40, giocatore.getPedine().size());
      }

      @Test
      void testPossiedePedina(){
          Giocatore giocatore = new Giocatore("Vittorio", Colore.ROSSO);
          Pedina pedina = giocatore.getPedine().getFirst();
          assertTrue(giocatore.possiedePedina(pedina));
      }
}
