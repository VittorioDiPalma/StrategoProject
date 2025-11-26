package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.exceptions.SchieramentoException;
import it.unimol.stratego.app.pedine.Pedina;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * La classe Tabellone rappresenta il tabellone di gioco.
 * Contiene una griglia di caselle e metodi per gestire le caselle, le pedine e
 * l'aggiornamento dello stato del tabellone.
 *
 * @author Vittorio
 * @version 1.0
 */
public class Tabellone implements Serializable {
        private final Casella[][] griglia;
        public static final int DIMENSIONE = 10;

        /**
         * Costruttore della classe Tabellone.
         * Inizializza la griglia di caselle e crea i laghi.
         */
        public Tabellone(){
             this.griglia = new Casella[DIMENSIONE][DIMENSIONE];
             for(int i = 0; i< DIMENSIONE; i++){
                  for(int j = 0; j< DIMENSIONE; j++){
                       this.griglia[i][j] = new Casella(i,j);
                  }
             }
             creaLaghi();
        }


     public void creaLaghi() {
          int[][] coordinateLaghi = {
                  {2, 4}, {2, 5}, {3, 4}, {3, 5},
                  {6, 4}, {6, 5}, {7, 4}, {7, 5}
          };
          for (int[] coord : coordinateLaghi) {
               this.griglia[coord[0]][coord[1]].setAcqua();
          }
     }

     /**
      * Restituisce una lista di caselle adiacenti a una data casella.
      * Le caselle adiacenti sono quelle sopra, sotto, a sinistra e a destra di essa,
      * purché non siano caselle d'acqua.
      *
      * @param casella La casella di riferimento.
      * @return Una lista di caselle adiacenti non d'acqua.
      */
     public ArrayList<Casella> getCaselleAdiacenti(Casella casella) {
          ArrayList<Casella> adiacenti = new ArrayList<>();
          int row = casella.getPosizioneX();
          int col = casella.getPosizioneY();
          int[][] direzioni = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // sopra, sotto, sinistra, destra

          for (int[] dir : direzioni) {
               int r = row + dir[0];
               int c = col + dir[1];
               if (r >= 0 && r < DIMENSIONE && c >= 0 && c < DIMENSIONE && !griglia[r][c].isAcqua()) {
                    adiacenti.add(griglia[r][c]);
               }
          }
          return adiacenti;
     }

      /**
       * Restituisce una lista di caselle visitabili da un esploratore a partire da una data casella.
       * L'esploratore può muoversi in linea retta in orizzontale o verticale fino a incontrare
       * una casella d'acqua o una casella occupata.
       *
       * @param casella La casella di partenza dell'esploratore.
       * @return Una lista di caselle visitabili dall'esploratore.
       */
     public ArrayList<Casella> getVisitabiliDaEsploratore(Casella casella) {
          ArrayList<Casella> visitabili = new ArrayList<>();
          int row = casella.getPosizioneX();
          int col = casella.getPosizioneY();

          int[][] direzioni = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}}; // sopra, sotto, sinistra, destra

          for (int[] dir : direzioni) {
               int r = row + dir[0];
               int c = col + dir[1];
               while (r >= 0 && r < DIMENSIONE && c >= 0 && c < DIMENSIONE) {
                    Casella corrente = griglia[r][c];
                    if (corrente.isAcqua()) break;
                    visitabili.add(corrente);
                    if (corrente.isOccupata()) break;
                    r += dir[0];
                    c += dir[1];
               }
          }
          return visitabili;
     }

            /**
             * Assegna una pedina a una casella specifica del tabellone.
             * Utilizzata durante la fase di schieramento delle pedine.
             *
             * @param pedina La pedina da assegnare.
             * @param casella La casella in cui assegnare la pedina.
             * @throws IndexOutOfBoundsException Se la casella è fuori dai limiti del tabellone.
             * @throws SchieramentoException Se la casella è già occupata.
             */
        public void assegnaPedinaACasella(Pedina pedina, Casella casella) throws IndexOutOfBoundsException, SchieramentoException {

             if (casella.getPosizioneX() < 0 ||
                     casella.getPosizioneX() >= DIMENSIONE ||
                     casella.getPosizioneY() < 0 ||
                     casella.getPosizioneY() >= DIMENSIONE)
             {
              throw new IndexOutOfBoundsException("Posizione fuori dai limiti del tabellone");
             }

             if (casella.isOccupata()) {
              throw new SchieramentoException("Casella già occupata");
             }

             casella.setPedinaOccupante(pedina);
        }

        /**
         * Restituisce la casella in cui si trova una determinata pedina.
         *
         * @param pedina La pedina di cui cercare la casella.
         * @return La casella in cui si trova la pedina, o null se non trovata.
         */
        public Casella getCasella(Pedina pedina) {
             for (int i = 0; i < DIMENSIONE; i++) {
                  for (int j = 0; j < DIMENSIONE; j++) {
                       Casella casella = griglia[i][j];
                       if (casella.isOccupata() && casella.getPedinaOccupante().equals(pedina)) {
                            return casella;
                       }
                  }
             }
             return null;
        }

            /**
             * Restituisce la casella in una posizione specifica del tabellone.
             * Metodo overload per ottenere una casella tramite coordinate (row, col).
             *
             * @param row La riga della casella.
             * @param col La colonna della casella.
             * @return La casella alla posizione specificata.
             * @throws IndexOutOfBoundsException Se la posizione è fuori dai limiti del tabellone.
             */
        public Casella getCasella(int row, int col) throws IndexOutOfBoundsException {
             if(row >= 0 && row < DIMENSIONE && col >= 0 && col < DIMENSIONE) {
                  return griglia[row][col];
             } else {
                  throw new IndexOutOfBoundsException("Posizione fuori dai limiti del tabellone");
             }
        }

            /**
             * Stampa il tabellone di gioco sulla console.
             * Le caselle occupate mostrano la forza della pedina,
             * le caselle d'acqua sono rappresentate da '~',
             * e le caselle vuote sono rappresentate da '.'.
             * Utilizzato per il debug e la visualizzazione dello stato del tabellone.
             */
        public void stampaTabellone(){
             System.out.println("\n\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\t");
             for(int y = 0; y < DIMENSIONE; y++){
                  System.out.print(y + " ");
                  for(int x = 0; x < DIMENSIONE; x++){
                       Casella casella = getCasella(x, y);
                       if(casella.isOccupata()){
                            Pedina pedina = casella.getPedinaOccupante();
                            System.out.print("\t " + pedina.getForza());
                       } else if(casella.isAcqua()){
                            System.out.print("\t~");
                       } else {
                            System.out.print("\t.");
                       }
                  }
                  System.out.println();
             }
        }
}
