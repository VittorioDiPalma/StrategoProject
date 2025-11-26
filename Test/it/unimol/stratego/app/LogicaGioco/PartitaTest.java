package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.exceptions.MovimentoPedinaException;
import it.unimol.stratego.app.exceptions.SchieramentoException;
import it.unimol.stratego.app.exceptions.TurnoException;
import it.unimol.stratego.app.pedine.*;
import it.unimol.stratego.app.pedine.Artificiere;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PartitaTest {

     Partita partita = Partita.inizializzaPartita("Vittorio", "Antonio");

     @Test
     void testTurnoDelGiocatore(){
          Turno turno = partita.getTurnoCorrente();
              assert turno.getGiocatoreCorrente().getNome().equals(("Vittorio")) ||
                        turno.getGiocatoreCorrente().getNome().equals(("Antonio"));
     }

     @Test
     void testSchieramentoPedina(){
          Pedina pedina = new Artificiere(Colore.BLU);
          if(partita.getTurnoCorrente().getGiocatoreCorrente().getColore().equals(Colore.ROSSO))
               partita.cambioTurno();
          assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 4, 7));
          Casella casella = partita.getTabellone().getCasella(4, 7);
          assertEquals(pedina, casella.getPedinaOccupante());
     }

     @Test
     void testSchieramentoPedinaBluInCampoRosso(){
          Pedina pedina = new Artificiere(Colore.BLU);
          if(partita.getTurnoCorrente().getGiocatoreCorrente().getColore().equals(Colore.ROSSO))
               partita.cambioTurno();
          assertThrows(SchieramentoException.class, () -> partita.schieramentoIniziale(pedina, 0, 2));
          Casella casella = partita.getTabellone().getCasella(0, 2);
          assertNull(casella.getPedinaOccupante());
     }

     @Test
     void testSchieramentoPedinaRossaInCampoBlu(){
          Pedina pedina = new Artificiere(Colore.ROSSO);
          if(partita.getTurnoCorrente().getGiocatoreCorrente().getColore().equals(Colore.BLU))
               partita.cambioTurno();
          assertThrows(SchieramentoException.class, () -> partita.schieramentoIniziale(pedina, 7, 7));
          Casella casella = partita.getTabellone().getCasella(7, 7);
          assertNull(casella.getPedinaOccupante());
     }

     @Test
     void testEsecuzioneMossaRossaDuranteTurnoBlu() {
              Pedina pedina = new Artificiere(Colore.ROSSO);
              if(partita.getTurnoCorrente().getGiocatoreCorrente().getColore().equals(Colore.ROSSO))
                     partita.cambioTurno();
              Casella casellaPartenza = partita.getTabellone().getCasella(0, 6);
              assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 0, 6));
              assertFalse(partita.isTurnoDelGiocatore(new Giocatore("Vittorio", Colore.ROSSO)));
              assertThrows(TurnoException.class, () -> partita.eseguiMossa(pedina, 0, 6));
              assertEquals(pedina, casellaPartenza.getPedinaOccupante());
     }

     @Test
     void testEsecuzioneMossaNonValida(){
              Pedina pedina = new Artificiere(Colore.BLU);
              if(partita.getTurnoCorrente().getGiocatoreCorrente().getColore().equals(Colore.ROSSO))
                     partita.cambioTurno();
              Casella casellaPartenza = partita.getTabellone().getCasella(0, 6);
              assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 0, 6));
              assertThrows(MovimentoPedinaException.class, () -> partita.eseguiMossa(pedina, 0, 8));
              assertEquals(pedina, casellaPartenza.getPedinaOccupante());
     }

     @Test
     void testEsecuzioneMossaConScontro(){
                  Pedina pedinaBlu = new Artificiere(Colore.BLU);
                  Pedina pedinaRossa = new Generale(Colore.ROSSO);
                  if(partita.getTurnoCorrente().getGiocatoreCorrente().getColore().equals(Colore.ROSSO))
                         partita.cambioTurno();
                  Casella casellaPartenzaBlu = partita.getTabellone().getCasella(0, 6);
                  Casella casellaPartenzaRossa = partita.getTabellone().getCasella(0, 3);
                  assertDoesNotThrow(() -> partita.schieramentoIniziale(pedinaBlu, 0, 6));
                  partita.cambioTurno();
                  assertDoesNotThrow(() -> partita.schieramentoIniziale(pedinaRossa, 0, 3));
                  partita.cambioTurno();
                  assertDoesNotThrow(() -> partita.eseguiMossa(pedinaBlu, 0, 5));
                  assertDoesNotThrow(() -> partita.eseguiMossa(pedinaRossa, 0, 4));
                  assertDoesNotThrow(() -> partita.eseguiMossa(pedinaBlu, 0, 4));
                  System.out.println(partita.getTabellone().getCasella(0,4).getPedinaOccupante().toString());
                  assertFalse(casellaPartenzaBlu.isOccupata());
                  assertFalse(casellaPartenzaRossa.isOccupata());
                  assertTrue(partita.getTabellone().getCasella(0,4).isOccupata());
                  assertEquals(pedinaRossa, partita.getTabellone().getCasella(0,4).getPedinaOccupante());
     }

     @Test
     void testSelezionePedinaDaMuovere() throws MovimentoPedinaException, TurnoException {
          Giocatore giocatore = partita.getTurnoCorrente().getGiocatoreCorrente();
          Pedina pedina = giocatore.getPedine().get(1);
          Pedina pedinaSelezionata = null;
          if(giocatore.getColore().equals(Colore.BLU)){
               System.out.println("Blu");
               assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 0, 6));
               pedinaSelezionata = partita.selezionaPedinaDaMuovere(0, 6);
          } else if (giocatore.getColore().equals(Colore.ROSSO)) {
               System.out.println("Rosso");
               assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 0, 3));
               pedinaSelezionata = partita.selezionaPedinaDaMuovere(0, 3);
          }
          assertEquals(pedina, pedinaSelezionata);
     }

     @Test
     void testSelezionePedinaSenzaMosseDisponibili() {
          Giocatore giocatore = partita.getTurnoCorrente().getGiocatoreCorrente();
          Pedina pedina = giocatore.getPedine().get(0);
          if(giocatore.getColore().equals(Colore.BLU)){
               System.out.println("Blu");
               assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 0, 6));
               assertThrows(MovimentoPedinaException.class ,() ->partita.selezionaPedinaDaMuovere(0, 6));
          } else if (giocatore.getColore().equals(Colore.ROSSO)) {
               System.out.println("Rosso");
               assertDoesNotThrow(() -> partita.schieramentoIniziale(pedina, 0, 3));
               assertThrows(MovimentoPedinaException.class ,() ->partita.selezionaPedinaDaMuovere(0, 3));
          }
     }
}
