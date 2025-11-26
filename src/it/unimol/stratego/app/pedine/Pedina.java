package it.unimol.stratego.app.pedine;

import it.unimol.stratego.app.Colore;
import it.unimol.stratego.app.EsitoScontro;

import java.io.Serializable;

public abstract class Pedina implements Serializable {
     private final String nome;
     public final Colore colore;
     private final boolean puoMuovere;
     protected final int forza;
     boolean isInGioco;
     private boolean scoperta = false;

     public Pedina(String nome, Colore colore, int forza, boolean puoMuovere) {
          this.nome = nome;
          this.colore = colore;
          this.forza = forza;
          this.puoMuovere = puoMuovere;
          this.isInGioco = true;
     }

     public String getNome() {
          return nome;
     }

     public Colore getColore() {
          return colore;
     }

     public int getForza() {
          return forza;
     }

     public boolean puoMuoversi() {
          return puoMuovere;
     }

     public void elimina(){
            this.isInGioco = false;
     }

     public boolean isScoperta() {
          return scoperta;
     }

     public void scopri() {
          this.scoperta = true;

     }

     public EsitoScontro attacca(Pedina avversario) {
          if (avversario instanceof Bandiera) return EsitoScontro.BANDIERA_CATTURATA;
          if (avversario instanceof Bomba) return EsitoScontro.SCONFITTA;
          if (this.forza > avversario.forza) return EsitoScontro.VITTORIA;
          if (this.forza < avversario.forza) return EsitoScontro.SCONFITTA;

          return EsitoScontro.PAREGGIO;
     }

     @Override
     public String toString() {
          return nome + " (" + colore + ")";
     }
}
