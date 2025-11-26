package it.unimol.stratego.ui;

import it.unimol.stratego.app.*;
import it.unimol.stratego.app.LogicaGioco.Giocatore;
import it.unimol.stratego.app.LogicaGioco.Mossa;
import it.unimol.stratego.app.exceptions.SchieramentoException;
import it.unimol.stratego.app.pedine.*;

import java.util.List;
import java.util.Scanner;

public class InterfacciaPartita {
     private Controller controller;
     private final Scanner scanner = new Scanner(System.in);

     public void gioca() throws SchieramentoException {
          System.out.println("Inserisci nome Giocatore 1: ");
          String g1 = scanner.nextLine();
          System.out.println("Inserisci nome Giocatore 2: ");
          String g2 = scanner.nextLine();

          controller = new Controller(g1, g2);

          faseSchieramento();

          controller.mostraTabellone();

          loopDiGioco();
     }

     private void faseSchieramento() throws SchieramentoException {
          System.out.println("Vuoi lo schieramento automatico? (s/n)");
          String scelta = scanner.nextLine();

          if (scelta.equalsIgnoreCase("s")) {
               controller.schieramentoAutomatico();
          } else {
               schieraPedineGiocatore(controller.turnoCorrente().getGiocatoreCorrente());
               controller.cambiaTurno();
               schieraPedineGiocatore(controller.turnoCorrente().getGiocatoreCorrente());
          }
     }

     private void loopDiGioco() {
          do {
               mostraMenuTurno();
               int scelta = scanner.nextInt();

               switch (scelta) {
                    case 1:
                         eseguiTurno();
                         break;
                    case 2:
                         salvaPartita();
                         break;
                    case 3:
                         mostraCronologia();
                         break;
                    case 4:
                         System.out.println("Tornando al menu principale...");
                         return;
                    default:
                         System.out.println("Scelta non valida!");
               }

          } while (!controller.isPartitaTerminata());

          Giocatore vincitore = controller.turnoCorrente().getGiocatoreCorrente();
          System.out.println("Partita terminata! Ha vinto " + vincitore.getNome());
     }

     private void mostraMenuTurno() {
          System.out.println("\n=== TURNO DI " +
                  controller.turnoCorrente().getGiocatoreCorrente().getNome() + " ===");
          System.out.println("1. Muovi pedina");
          System.out.println("2. Salva partita");
          System.out.println("3. Mostra cronologia");
          System.out.println("4. Menu principale");
          System.out.print("Scelta (1-4): ");
     }

     private void eseguiTurno() {
          Pedina pedina = selezionePedinaDaMuovere();

          try {
               System.out.println("Coordinate di destinazione:");
               System.out.print("X (0-9): ");
               int xDest = scanner.nextInt();
               System.out.print("Y (0-9): ");
               int yDest = scanner.nextInt();

               controller.eseguiMossa(pedina, xDest, yDest);
               controller.mostraTabellone();
          } catch (Exception e) {
               System.out.println("Errore: " + e.getMessage());
          }
     }

     public void giocaConController(Controller controller){
          this.controller = controller;

          System.out.println("Partita ripresa!");
          controller.mostraTabellone();

          loopDiGioco();
     }


     private void schieraPedineGiocatore(Giocatore giocatore) {
          System.out.println("\nSchieramento giocatore " + giocatore.getColore() + " (" + giocatore.getNome() + ")");


          List<Pedina> pedine = giocatore.getPedine();
          for (Pedina p : pedine) {
               boolean piazzata = false;
               while (!piazzata) {
                    try {
                         System.out.println("Posiziona: " + p.getNome());
                         System.out.print("X (0-9): ");
                         int x = scanner.nextInt();
                         System.out.print("Y (0-9): ");
                         int y = scanner.nextInt();

                         controller.schieraPedina(p, x, y);
                         piazzata = true;
                         controller.mostraTabellone();

                    } catch (Exception e) {
                         System.out.println("Errore: " + e.getMessage());
                    }
               }
          }
          scanner.nextLine();
     }

     public Pedina selezionePedinaDaMuovere() {
          Pedina pedina = null;
          while(pedina == null){
               try {
                    System.out.println("Seleziona le coordinate della pedina da muovere: ");
                    System.out.println("X (0-9): ");
                    int x = scanner.nextInt();
                    System.out.println("Y (0-9): ");
                    int y = scanner.nextInt();


                    pedina = controller.selezionaPedina(x, y);
               } catch (Exception e) {
                    System.out.println("Errore: " + e.getMessage());
               }
          }
          return pedina;
     }

     private void mostraCronologia() {
          List<Mossa> mosse = controller.getCronologiaMosse();

          if (mosse.isEmpty()) {
               System.out.println("\nNessuna mossa eseguita ancora.");
               return;
          }

          System.out.println("\n=== CRONOLOGIA MOSSE ===");
          for (int i = 0; i < mosse.size(); i++) {
               Mossa mossa = mosse.get(i);
               String vincente = mossa.isMossaVincente() ? " [MOSSA VINCENTE]" : "";
               System.out.println((i + 1) + ". " + mossa + vincente);
          }

          Mossa ultima = controller.getUltimaMossa();
          if (ultima != null) {
               System.out.println("\nUltima mossa: " + ultima);
          }
     }

     private void salvaPartita() {
          try {
               System.out.print("Nome del file (senza estensione): ");
               Scanner scanner = new Scanner(System.in);
               String nomeFile = scanner.nextLine();
               controller.salvaPartita(nomeFile);
               System.out.println("Partita salvata!");
          } catch (Exception e) {
               System.out.println("Errore nel salvataggio: " + e.getMessage());
          }
     }
}