package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Elementi;
import it.unibs.fp.tamaGolem.Equilibrio;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Giocatore {
    int idGiocatore;
    ArrayList<TamaGolem> listaGolem;
    ArrayList<PietreElementi> listaPietre;
    int numTamaGolemEliminati;
    ArrayList<Elementi> listaElementi;

    public Giocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
        this.listaGolem = new ArrayList<>();
        this.listaPietre = new ArrayList<>();
        this.numTamaGolemEliminati = 0;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public ArrayList<PietreElementi> getListaPietre() {
        return listaPietre;
    }

    public ArrayList<TamaGolem> getListaGolem() {
        return listaGolem;
    }

    public void invocazioneTamaGolem(int numTamaGolem, int numPietre, Equilibrio equilibrio, ScortaPietre scortaPietre) {
        // Implementa la logica per l'invocazione di un TamaGolem
        if (numTamaGolemEliminati < numTamaGolem) {
            TamaGolem nuovoGolem = new TamaGolem(CostantiPartita.VITA_TAMAGOLEM);
            Deque<PietreElementi> listaPietre = selezionaPietre(numPietre, equilibrio, scortaPietre, nuovoGolem);
            System.out.println(listaPietre.toString());
        } else {
            System.out.println("Hai già invocato il numero massimo di TamaGolem.");
            // Aggiungi logica per gestire il caso in cui il numero massimo di TamaGolem è stato raggiunto
        }
    }

    public Deque<PietreElementi> selezionaPietre(int numPietre, Equilibrio equilibrio, ScortaPietre scortaPietre, TamaGolem tamaGolem) {
        List<String> listaElementi = equilibrio.getElementi();
        Deque<PietreElementi> listaPietre = tamaGolem.getListaPietre();
        System.out.println("\nPietre disponibili:");
        for (int i = 0; i < listaElementi.size(); i++) {
            String elemento = listaElementi.get(i);
            long count = scortaPietre.getScortaPietre().stream()
                    .filter(p -> p.getNome().equals(elemento))
                    .count();
            System.out.println((i + 1) + ". " + elemento + " (" + count + " disponibili)");
        }

        System.out.printf("Seleziona %d pietre per il tuo TamaGolem: \n", numPietre);
        String nomeElemento;

        for (int i = 0; i < numPietre; i++) {
            boolean trovato;
            do {
                int idPietra = InputData.readIntegerBetween("-> ", 1, equilibrio.getElementi().size());
                nomeElemento = listaElementi.get(idPietra - 1);
                trovato = ScortaPietre.rimuoviPietraDaScorta(scortaPietre, nomeElemento);

                if (!trovato) {
                    System.out.println("Pietra non disponibile, seleziona un'altra pietra.");
                } else {
                    System.out.println("Pietra selezionata: " + nomeElemento);
                    listaPietre.add(new PietreElementi(nomeElemento));
                }
            } while (!trovato); // Continua finché la pietra non è disponibile
        }

        return listaPietre;
    }
}