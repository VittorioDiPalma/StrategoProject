package it.unimol.stratego.ui;

import it.unimol.stratego.app.Controller;
import it.unimol.stratego.app.exceptions.SchieramentoException;

import java.io.IOException;
import java.util.Scanner;

public class MenuPrincipale {

    public void menu() throws SchieramentoException {
         System.out.println("Benvenuto in Stratego!\n");
         System.out.println("1. Nuova partita");
         System.out.println("2. Carica partita");
         System.out.println("3. Esci");

         sceltaOpzione();
    }

    public void sceltaOpzione() throws SchieramentoException {
         Scanner scanner = new Scanner(System.in);
         int scelta;

         do{
              System.out.println("Seleziona un'opzione : ");
              scelta = scanner.nextInt();

              switch(scelta){
                   case 1:
                        InterfacciaPartita interfacciaPartita = new InterfacciaPartita();
                        interfacciaPartita.gioca();
                        break;
                   case 2:
                        caricaPartita(scanner);
                        break;
                   case 3:
                        System.out.println("Uscita dal gioco. Arrivederci!");
                        break;
                   default:
                        System.out.println("Opzione non valida. Riprova.");
              }
         }while(scelta != 3);
         scanner.close();
    }

     private void caricaPartita(Scanner scanner) throws SchieramentoException {
          try {
               System.out.print("Nome del file (senza estensione): ");
               scanner.nextLine(); // consuma newline
               String nomeFile = scanner.nextLine();

               Controller controller = Controller.caricaPartitaDaFile(nomeFile);
               System.out.println("Partita caricata con successo!");

               InterfacciaPartita interfaccia = new InterfacciaPartita();
               interfaccia.giocaConController(controller);

          } catch (IOException e) {
               System.out.println("Errore: File non trovato.");
          } catch (ClassNotFoundException e) {
               System.out.println("Errore: File corrotto.");
          }
     }
}
