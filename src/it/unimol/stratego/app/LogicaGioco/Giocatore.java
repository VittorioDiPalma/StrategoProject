package it.unimol.stratego.app.LogicaGioco;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.pedine.*;
import java.io.Serializable;
import java.util.*;

/**
 * La classe Giocatore rappresenta un giocatore nel gioco Stratego.
 * Ogni giocatore ha un nome, un colore e una lista di pedine.
 *
 * @author Vittorio
 * @version 1.0
 */
public class Giocatore implements Serializable {
     private final String nome;
     private final Colore colore;
     private final List<Pedina> pedine = new ArrayList<>(40);


     public Giocatore(String nome, Colore colore) {
          this.nome = nome;
          this.colore = colore;
          creaPedineGiocatore();
     }

     /**
      * Crea le pedine per il giocatore in base al colore assegnato.
      * Aggiunge le pedine alla lista delle pedine del giocatore.
      */
     private void creaPedineGiocatore() {
          pedine.add(new Bandiera(colore));
          pedine.add(new Spia(colore));
          pedine.add(new Maresciallo(colore));
          pedine.add(new Generale(colore));
          for (int i = 0; i < 2; i++) pedine.add(new Colonnello(colore));
          for (int i = 0; i < 3; i++) pedine.add(new Comandante(colore));
          for (int i = 0; i < 4; i++) pedine.add(new Capitano(colore));
          for (int i = 0; i < 4; i++) pedine.add(new Tenente(colore));
          for (int i = 0; i < 4; i++) pedine.add(new Sergente(colore));
          for (int i = 0; i < 5; i++) pedine.add(new Artificiere(colore));
          for (int i = 0; i < 6; i++) pedine.add(new Bomba(colore));
          for (int i = 0; i < 8; i++) pedine.add(new Esploratore(colore));
     }

     /**
      * Verifica se il giocatore possiede una determinata pedina.
      *
      * @param pedina La pedina da verificare.
      * @return true se il giocatore possiede la pedina, false altrimenti.
      */
     public boolean possiedePedina(Pedina pedina) {
          return pedine.contains(pedina);
     }


     public List<Pedina> getPedine() { return Collections.unmodifiableList(pedine); }
     public String getNome() { return nome; }
     public Colore getColore() { return colore; }
}