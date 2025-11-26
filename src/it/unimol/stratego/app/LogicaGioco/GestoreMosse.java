package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.exceptions.MossaException;
import it.unimol.stratego.app.exceptions.MovimentoPedinaException;
import it.unimol.stratego.app.pedine.*;
import java.io.Serializable;

/**
 * La classe GestoreMosse si occupa di gestire l'esecuzione delle mosse nel gioco.
 * Verifica la validità delle mosse e aggiorna lo stato del tabellone di conseguenza.
 *
 * @author Vittorio
 * @version 1.0
 */
public class GestoreMosse implements Serializable {
     private final Tabellone tabellone;

     public GestoreMosse(Tabellone tabellone){
          this.tabellone = tabellone;
     }

     /**
      * Esegue una mossa, aggiornando lo stato del tabellone in base alla mossa effettuata.
      * Gestisce gli scontri tra pedine e aggiorna le caselle coinvolte.
      *
      * @param mossa La mossa da eseguire.
      */
     public void eseguiMossa(Mossa mossa) {
          Casella partenza = mossa.getPartenza();
          Casella destinazione = mossa.getDestinazione();
          Pedina pedinaAttaccante = mossa.getPedina();

          if (!destinazione.isOccupata()) {
               destinazione.setPedinaOccupante(pedinaAttaccante);
               partenza.rimuoviPedina();
               return;
          }

          Pedina pedinaAttaccata = destinazione.getPedinaOccupante();
          pedinaAttaccante.scopri();
          pedinaAttaccata.scopri();

          switch (pedinaAttaccante.attacca(pedinaAttaccata)) {
               case VITTORIA -> {
                    destinazione.rimuoviPedina();
                    destinazione.setPedinaOccupante(pedinaAttaccante);
                    partenza.rimuoviPedina();
               }
               case SCONFITTA -> partenza.rimuoviPedina();
               case PAREGGIO -> {
                    destinazione.rimuoviPedina();
                    partenza.rimuoviPedina();
               }
               case BANDIERA_CATTURATA -> {
                    mossa.setMossaVincente();
                    destinazione.rimuoviPedina();
                    destinazione.setPedinaOccupante(pedinaAttaccante);
                    partenza.rimuoviPedina();
               }
          }
     }


     /**
      * Verifica se una mossa è valida in base alle regole del gioco.
      *
      * @param pedina  La pedina che sta per effettuare la mossa.
      * @param partenza  La casella di partenza della pedina.
      * @param destinazione La casella di destinazione della pedina.
      *
      * @return true se la mossa è valida, altrimenti lancia un'eccezione.
      *
      * @throws MossaException  Se la mossa non è valida per motivi legati alla pedina selezionata.
      * @throws MovimentoPedinaException  Se la casella destinazione non è raggiungibile dalla pedina.
      */
     public boolean isMossaValida(Pedina pedina, Casella partenza, Casella destinazione)
             throws MossaException, MovimentoPedinaException {

          if (pedina == null) throw new IllegalArgumentException("Pedina nulla");
          if (!pedina.puoMuoversi()) throw new MossaException("Questa pedina non può muoversi");

          if (destinazione.isOccupata() &&
                  destinazione.getPedinaOccupante().getColore() == pedina.getColore()) {
               throw new MossaException("Casella occupata da una tua pedina");
          }

          if (pedina instanceof Esploratore) {
               if (!tabellone.getVisitabiliDaEsploratore(partenza).contains(destinazione)) {
                    throw new MovimentoPedinaException("Casella non raggiungibile da esploratore");
               }
          } else {
               if (!tabellone.getCaselleAdiacenti(partenza).contains(destinazione)) {
                    throw new MovimentoPedinaException("Casella non raggiungibile");
               }
          }

          return true;
     }
}
