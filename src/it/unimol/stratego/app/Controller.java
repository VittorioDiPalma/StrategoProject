package it.unimol.stratego.app;

import it.unimol.stratego.app.LogicaGioco.*;
import it.unimol.stratego.app.exceptions.MossaException;
import it.unimol.stratego.app.exceptions.MovimentoPedinaException;
import it.unimol.stratego.app.exceptions.SchieramentoException;
import it.unimol.stratego.app.exceptions.TurnoException;
import it.unimol.stratego.app.pedine.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * La classe Controller gestisce la logica di gioco di una partita di Stratego,
 * ponendosi come intermediario tra l'interfaccia utente e il modello di gioco.
 *
 * @author Vittorio
 * @version 1.0
 */
public class Controller  {
     private final Partita partita;
     private final List<Mossa> cronologiaMosse;

     /**
      * Costruttore della classe Controller.
      * Inizializza una nuova partita con i nomi dei due giocatori.
      *
      * @param nomeG1 Nome del primo giocatore.
      * @param nomeG2 Nome del secondo giocatore.
      * @throws IllegalArgumentException se i nomi dei giocatori sono null o vuoti.
      */
     public Controller(String nomeG1, String nomeG2) {
          if (nomeG1 == null || nomeG2 == null || nomeG1.isEmpty() || nomeG2.isEmpty()) {
               throw new IllegalArgumentException
                       ("I nomi dei giocatori non possono essere vuoti.");
          }
          this.partita = Partita.inizializzaPartita(nomeG1, nomeG2);
          this.cronologiaMosse = new ArrayList<>();
     }

     // costruttore privato per caricare una partita salvata
     private Controller(Partita partita, List<Mossa> cronologia) {
          this.partita = partita;
          this.cronologiaMosse = cronologia;
     }

     public void cambiaTurno(){
          partita.cambioTurno();
     }

     public Turno turnoCorrente(){
          return partita.getTurnoCorrente();
     }

     public void mostraTabellone(){
         partita.getTabellone().stampaTabellone();
     }

     /**
      * Seleziona una pedina da muovere in base alle coordinate fornite.
      *
      * @param x Coordinata X della pedina.
      * @param y Coordinata Y della pedina.
      * @return La pedina selezionata.
      * @throws MovimentoPedinaException se la pedina non può essere mossa.
      * @throws TurnoException se non è il turno del giocatore che possiede la pedina.
      */
     public Pedina selezionaPedina(int x, int y)
             throws MovimentoPedinaException, TurnoException {
          return partita.selezionaPedinaDaMuovere(x, y);
     }

     /**
      * Esegue una mossa per una pedina selezionata verso una destinazione specifica.
      *
      * @param pedina La pedina da muovere.
      * @param xDest Coordinata X della casella di destinazione.
      * @param yDest Coordinata Y della casella di destinazione.
      * @throws MossaException se la mossa non è valida.
      * @throws MovimentoPedinaException se la pedina non può essere mossa.
      * @throws TurnoException se non è il turno del giocatore che possiede la pedina.
      */
     public void eseguiMossa(Pedina pedina, int xDest, int yDest)
             throws MossaException, MovimentoPedinaException, TurnoException {
          Mossa mossaEseguita = partita.eseguiMossa(pedina, xDest, yDest);
          cronologiaMosse.add(mossaEseguita);
     }

      /**
       * Schiera una pedina in una posizione specifica del tabellone.
       *
       * @param pedina La pedina da schierare.
       * @param x Coordinata X della casella di destinazione.
       * @param y Coordinata Y della casella di destinazione.
       * @throws SchieramentoException se la casella è già occupata o non valida per lo schieramento.
       */
     public void schieraPedina(Pedina pedina, int x, int y)
             throws SchieramentoException {
          partita.schieramentoIniziale(pedina, x, y);
     }

     /**
      * Verifica se la partita è terminata.
      *
      * @return true se la partita ha un vincitore, altrimenti false.
      */
     public boolean isPartitaTerminata(){
          return partita.isPartitaVinta();
     }

      /**
       * Salva lo stato corrente della partita su un file.
       *
       * @param nomeFile Il nome del file in cui salvare la partita (senza estensione).
       * @throws IOException se si verifica un errore durante la scrittura del file.
       */
     public void salvaPartita(String nomeFile) throws IOException {
          String percorso = "salvataggi/" + nomeFile + ".stratego";
          try (ObjectOutputStream out = new ObjectOutputStream(
                  new FileOutputStream(percorso))) {
               out.writeObject(partita);
               out.writeObject(cronologiaMosse);
          }
     }

      /**
       * Carica una partita salvata da un file.
       *
       * @param nomeFile Il nome del file da cui caricare la partita (senza estensione).
       * @return Un'istanza di Controller con lo stato della partita caricata.
       * @throws IOException se si verifica un errore durante la lettura del file.
       * @throws ClassNotFoundException se la classe Partita o Mossa non viene trovata durante la deserializzazione.
       */
     public static Controller caricaPartitaDaFile(String nomeFile)
             throws IOException, ClassNotFoundException {
          String percorso = "salvataggi/" + nomeFile + ".stratego";
          try (ObjectInputStream in = new ObjectInputStream(
                  new FileInputStream(percorso))) {
               Partita partita = (Partita) in.readObject();
               @SuppressWarnings("unchecked")
               List<Mossa> cronologiaMosse = (List<Mossa>) in.readObject();
               return new Controller(partita, cronologiaMosse);
          }
     }


     public List<Mossa> getCronologiaMosse() {
          return Collections.unmodifiableList(cronologiaMosse);
     }

     public Mossa getUltimaMossa() {
          return cronologiaMosse.isEmpty() ? null :
                  cronologiaMosse.getLast();
     }

     /**
      * Esegue uno schieramento automatico per entrambi i giocatori,
      * posizionando le pedine in modo casuale nelle rispettive aree di schieramento.
      *
      * @throws SchieramentoException se si verifica un errore durante lo schieramento.
      */
     public void schieramentoAutomatico() throws SchieramentoException {
          Giocatore g1 = partita.getTurnoCorrente().getGiocatoreCorrente();

          schieraGiocatoreCasuale(g1);

          cambiaTurno();
          Giocatore g2 = partita.getTurnoCorrente().getGiocatoreCorrente();

          schieraGiocatoreCasuale(g2);
     }

     /**
      * Scambia le pedine tra le due caselle specificate dalle coordinate, durante
      * la fase di schieramento.
      *
      * @param x1 Coordinata X della prima casella.
      * @param y1 Coordinata Y della prima casella.
      * @param x2 Coordinata X della seconda casella.
      * @param y2 Coordinata Y della seconda casella.
      * @throws Exception se una delle caselle non è occupata o se le pedine non appartengono al giocatore corrente.
      */
     public void scambiaPedine(int x1, int y1, int x2, int y2) throws Exception {
          Casella c1 = partita.getTabellone().getCasella(x1, y1);
          Casella c2 = partita.getTabellone().getCasella(x2, y2);

          if (!c1.isOccupata() || !c2.isOccupata())
               throw new Exception
                       ("Entrambe le caselle devono essere occupate da una pedina.");

          Pedina p1 = c1.getPedinaOccupante();
          Pedina p2 = c2.getPedinaOccupante();

          if (p1.getColore() != partita.getTurnoCorrente().getGiocatoreCorrente().getColore() ||
                  p2.getColore() != partita.getTurnoCorrente().getGiocatoreCorrente().getColore())
               throw new Exception
                       ("Puoi scambiare solo le tue pedine.");

          c1.setPedinaOccupante(p2);
          c2.setPedinaOccupante(p1);
     }

     private void schieraGiocatoreCasuale(Giocatore giocatore) throws SchieramentoException {
          int yInizio, yFine;
          if (giocatore.getColore() == Colore.BLU) {
               yInizio = 6;
               yFine = 9;
          } else {
               yInizio = 0;
               yFine = 3;
          }

          List<int[]> posizioniDisponibili = new ArrayList<>();
          for (int y = yInizio; y <= yFine; y++) {
               for (int x = 0; x < 10; x++) {
                    posizioniDisponibili.add(new int[]{x, y});
               }
          }

          Collections.shuffle(posizioniDisponibili);

          List<Pedina> pedine = giocatore.getPedine();
          for (int i = 0; i < pedine.size() && i < posizioniDisponibili.size(); i++) {
               int[] pos = posizioniDisponibili.get(i);
               schieraPedina(pedine.get(i), pos[0], pos[1]);
          }
     }

     public Tabellone getTabellone() {
          return partita.getTabellone();
     }
}





