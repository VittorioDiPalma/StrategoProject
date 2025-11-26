package it.unimol.stratego.app.LogicaGioco;

import java.io.Serializable;

import it.unimol.stratego.app.*;
import it.unimol.stratego.app.exceptions.MossaException;
import it.unimol.stratego.app.exceptions.MovimentoPedinaException;
import it.unimol.stratego.app.exceptions.SchieramentoException;
import it.unimol.stratego.app.exceptions.TurnoException;
import it.unimol.stratego.app.pedine.Esploratore;
import it.unimol.stratego.app.pedine.Pedina;

import java.util.ArrayList;

/**
 * La classe Partita rappresenta una partita di Stratego tra due giocatori.
 * Gestisce lo stato del gioco, i turni dei giocatori, le mosse e verifica le condizioni di vittoria.
 * Si occupa dell'intera logica di gioco.
 *
 * @author Vittorio
 * @version 1.0
 */
public class Partita implements Serializable {
     private final Giocatore giocatore1;
     private final Giocatore giocatore2;
     private final Tabellone tabellone;
     private final GestoreMosse gestoreMosse;
     private final GestoreTurni gestoreTurni;
     private boolean partitaVinta = false;


     private Partita (Giocatore giocatore1, Giocatore giocatore2) {
          this.giocatore1 = giocatore1;
          this.giocatore2 = giocatore2;
          this.tabellone = new Tabellone();
          this.gestoreMosse = new GestoreMosse(tabellone);
          this.gestoreTurni = new GestoreTurni(giocatore1, giocatore2);

     }

     /**
      * Inizializza una nuova partita con due giocatori.
      *
      * @param nomeGiocatore1 Nome del primo giocatore.
      * @param nomeGiocatore2 Nome del secondo giocatore.
      * @return Una nuova istanza di Partita.
      */
     public static Partita inizializzaPartita(String nomeGiocatore1, String nomeGiocatore2) {
          Giocatore giocatore1 = new Giocatore(nomeGiocatore1, Colore.BLU);
          Giocatore giocatore2 = new Giocatore(nomeGiocatore2, Colore.ROSSO);
          return new Partita(giocatore1, giocatore2);
     }

     public Turno getTurnoCorrente() {
          return gestoreTurni.getTurnoCorrente();
     }

     public void cambioTurno() {
          gestoreTurni.passaAlProssimoTurno();
     }

     public Tabellone getTabellone() {
          return tabellone;
     }

     public boolean isTurnoDelGiocatore(Giocatore giocatore) {
          return getTurnoCorrente().getGiocatoreCorrente().equals(giocatore);
     }

     /**
      * Schiera una pedina in una posizione specifica sul tabellone.
      * Verifica che la pedina sia schierata nella zona corretta in base al colore del giocatore.
      *
      * @param pedina La pedina da schierare.
      * @param x Coordinata X della casella di destinazione.
      * @param y Coordinata Y della casella di destinazione.
      * @throws SchieramentoException Se la pedina viene schierata in una zona non valida.
      */
     public void schieramentoIniziale(Pedina pedina, int x, int y)
             throws SchieramentoException {
          Casella casella = tabellone.getCasella(x, y);
          Turno turnoCorrente = getTurnoCorrente();
          Giocatore giocatoreCorrente = turnoCorrente.getGiocatoreCorrente();

          if (giocatoreCorrente.getColore() == Colore.BLU && y < 6) {
               throw new SchieramentoException
                       ("Le pedine blu possono essere schierate solo nelle righe 6-9.");
          }
          if (giocatoreCorrente.getColore() == Colore.ROSSO && y > 3) {
               throw new SchieramentoException
                       ("Le pedine rosse possono essere schierate solo nelle righe 0-3.");
          }
          tabellone.assegnaPedinaACasella(pedina, casella);
     }

     /**
      * Esegue una mossa per una pedina selezionata verso una destinazione specifica.
      * Verifica che sia il turno del giocatore e che la mossa sia valida.
      * Aggiorna lo stato del gioco e verifica se la mossa ha portato alla vittoria.
      *
      * @param pedina La pedina da muovere.
      * @param xDest Coordinata X della casella di destinazione.
      * @param yDest Coordinata Y della casella di destinazione.
      * @return La mossa eseguita.
      * @throws MossaException Se la mossa non è valida.
      * @throws MovimentoPedinaException Se la pedina non può muoversi.
      * @throws TurnoException Se non è il turno del giocatore.
      */
     public Mossa eseguiMossa(Pedina pedina, int xDest, int yDest)
             throws MossaException, MovimentoPedinaException, TurnoException {

            if (!isTurnoDelGiocatore(pedina.getColore() == Colore.BLU ? giocatore1 : giocatore2)) {
                 throw new TurnoException("Non è il turno del giocatore.");
            }

            Casella partenza = tabellone.getCasella(pedina);
            Casella destinazione = tabellone.getCasella(xDest, yDest);

            if (!gestoreMosse.isMossaValida(pedina, partenza, destinazione)) {
                 throw new MossaException("Mossa non valida.");
            }

          Mossa mossa = new Mossa(pedina, partenza, destinazione);
          gestoreMosse.eseguiMossa(mossa);

            if (mossa.isMossaVincente()) {
                 partitaVinta = true;
            }else{
                 gestoreTurni.passaAlProssimoTurno();
            }
            return mossa;
     }

     public boolean isPartitaVinta() {
          return partitaVinta;
     }

      /**
       *Verifica che una pedina possa essere mossa e la seleziona in base
       * alle coordinate fornite.
       *
       * @param x Coordinata X della casella selezionata.
       * @param y Coordinata Y della casella selezionata.
       * @return La pedina selezionata da muovere.
       * @throws MovimentoPedinaException Se la casella è vuota, la pedina non può muoversi o non ha mosse valide.
       * @throws TurnoException Se la pedina non appartiene al giocatore corrente.
       */
     public Pedina selezionaPedinaDaMuovere(int x, int y)
             throws MovimentoPedinaException, TurnoException {

          Casella casella = tabellone.getCasella(x, y);
          if (!casella.isOccupata()) {
               throw new MovimentoPedinaException
                       ("La casella selezionata è vuota.");
          }

          Pedina pedina = casella.getPedinaOccupante();
          Giocatore giocatoreCorrente = getTurnoCorrente().getGiocatoreCorrente();

          verificaPedinaDelGiocatore(giocatoreCorrente, pedina);
          verificaPedinaMovibile(pedina);
          verificaMosseDisponibili(pedina, casella);

          return pedina;
     }

     private void verificaPedinaDelGiocatore(Giocatore giocatore, Pedina pedina)
             throws TurnoException {
          if (!giocatore.possiedePedina(pedina)) {
               throw new TurnoException
                       ("La pedina selezionata non appartiene al giocatore corrente.");
          }
     }

     private void verificaPedinaMovibile(Pedina pedina)
             throws MovimentoPedinaException {
          if (!pedina.puoMuoversi()) {
               throw new MovimentoPedinaException
                       ("La pedina selezionata non può muoversi.");
          }
     }

     private void verificaMosseDisponibili(Pedina pedina, Casella casella)
             throws MovimentoPedinaException {
          ArrayList<Casella> caselleVisitabili;
          if (pedina instanceof Esploratore) {
               caselleVisitabili = tabellone.getVisitabiliDaEsploratore(casella);
          } else {
               caselleVisitabili = tabellone.getCaselleAdiacenti(casella);
          }

          boolean haMosseValide = caselleVisitabili.stream().anyMatch(
                  c -> !c.isOccupata() ||
                          (c.isOccupata() &&
                                  c.getPedinaOccupante().getColore() != pedina.getColore())
          );

          if (!haMosseValide) {
               throw new MovimentoPedinaException
                       ("La pedina selezionata non ha mosse valide.");
          }
     }
}
