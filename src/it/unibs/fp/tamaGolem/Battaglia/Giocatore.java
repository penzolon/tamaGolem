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
        for (String e : listaElementi) {
            System.out.println((listaElementi.indexOf(e) + 1) + ". " + e);
        }
        System.out.printf("Seleziona le %d pietre per il tuo TamaGolem: \n", numPietre);
        for (int i = 0; i < numPietre; i++) {
            int idPietra = InputData.readIntegerBetween("-> ", 1, equilibrio.getElementi().size());
            String nomeElemento = listaElementi.get(idPietra - 1);
            listaPietre.add(nomeElemento);
            for (PietreElementi p : scortaPietre.getScortaPietre()) {
                if (p.getNome().equals(nomeElemento)) {
                    scortaPietre.getScortaPietre().remove(p);
                    break;
                }
            }
        }
        return listaPietre;
    }
}