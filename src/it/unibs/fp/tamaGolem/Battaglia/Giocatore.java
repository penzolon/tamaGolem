package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Elementi;
import it.unibs.fp.tamaGolem.Equilibrio;

import java.util.ArrayList;
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
            ArrayList<String> listaPietre = selezionaPietre(numPietre, equilibrio, scortaPietre);
            System.out.println(listaPietre.toString());
        } else {
        }
    }

    public ArrayList<String> selezionaPietre(int numPietre, Equilibrio equilibrio, ScortaPietre scortaPietre) {
        List<String> listaElementi = equilibrio.getElementi();
        ArrayList<String> listaPietre = new ArrayList<>();

        // Stampa gli elementi disponibili
        for (String e : listaElementi) {
            System.out.println((listaElementi.indexOf(e) + 1) + ". " + e);
        }

        System.out.printf("Seleziona le %d pietre per il tuo TamaGolem: \n", numPietre);
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
                    listaPietre.add(nomeElemento); // Aggiungi la pietra solo se disponibile
                }
            } while (!trovato); // Continua finché la pietra non è disponibile
        }

        return listaPietre;
    }
}