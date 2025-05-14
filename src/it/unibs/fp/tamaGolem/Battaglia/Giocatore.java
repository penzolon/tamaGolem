package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Elementi;
import it.unibs.fp.tamaGolem.Equilibrio;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * Classe che rappresenta un giocatore nel contesto della battaglia dei TamaGolem.
 * Ogni giocatore ha un ID, una lista di TamaGolem, una lista di pietre e altre proprietà
 * utili per gestire il gioco.
 */
public class Giocatore {
    int idGiocatore;
    ArrayList<TamaGolem> listaGolem;
    int numTamaGolemEliminati;
    ArrayList<Elementi> listaElementi;

    /**
     * Costruttore della classe Giocatore.
     *
     * @param idGiocatore l'ID univoco del giocatore.
     */
    public Giocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
        this.listaGolem = new ArrayList<>();
        this.numTamaGolemEliminati = 0;
    }

    /**
     * Restituisce l'ID del giocatore.
     *
     * @return l'ID del giocatore.
     */
    public int getIdGiocatore() {
        return idGiocatore;
    }



    /**
     * Restituisce la lista dei TamaGolem posseduti dal giocatore.
     *
     * @return una lista di oggetti TamaGolem.
     */
    public ArrayList<TamaGolem> getListaGolem() {
        return listaGolem;
    }

    /**
     * Gestisce l'invocazione di un TamaGolem da parte del giocatore.
     *
     * @param numTamaGolem il numero massimo di TamaGolem che il giocatore può invocare.
     * @param numPietre il numero di pietre da assegnare al TamaGolem.
     * @param equilibrio l'oggetto Equilibrio che rappresenta le interazioni tra gli elementi.
     * @param scortaPietre la scorta comune di pietre disponibile.
     */
    public TamaGolem invocazioneTamaGolem(int numTamaGolem, int numPietre, Equilibrio equilibrio, ScortaPietre scortaPietre, int numTamaEliminati) {
        TamaGolem nuovoGolem =  null;
        numTamaGolemEliminati = numTamaEliminati;
        if (numTamaGolemEliminati < numTamaGolem) {
            nuovoGolem = new TamaGolem(CostantiPartita.VITA_TAMAGOLEM);
            Deque<PietreElementi> listaPietre = selezionaPietre(numPietre, numTamaGolem, equilibrio, scortaPietre, nuovoGolem);
            StringBuilder s = new StringBuilder();
            s.append("[ ");
            for (PietreElementi elementi : listaPietre) {
                s.append(elementi.getNome());
                s.append(", ");
            }
            if (!listaPietre.isEmpty()) {
                s.setLength(s.length() - 2); // Rimuove l'ultima virgola e lo spazio
            }
            s.append(" ]\n");
            System.out.println(s);
        } else {
            System.out.println("Hai già invocato il numero massimo di TamaGolem.");
            System.out.println("Non puoi invocare un altro TamaGolem.");
        }
        return nuovoGolem;
    }

    /**
     * Permette al giocatore di selezionare un certo numero di pietre dalla scorta comune.
     *
     * @param numPietre il numero di pietre da selezionare.
     * @param equilibrio l'oggetto Equilibrio che rappresenta gli elementi disponibili.
     * @param scortaPietre la scorta comune di pietre disponibile.
     * @param tamaGolem il TamaGolem a cui assegnare le pietre selezionate.
     * @return una deque contenente le pietre selezionate.
     */
    public Deque<PietreElementi> selezionaPietre(int numPietre, int numTamaGolem,Equilibrio equilibrio, ScortaPietre scortaPietre, TamaGolem tamaGolem) {
        List<String> listaElementi = equilibrio.getElementi();
        Deque<PietreElementi> listaPietre = tamaGolem.getListaPietre();
        if (numTamaGolemEliminati != 0) {
            System.out.printf("Giocatore %d: ", idGiocatore);
            System.out.println("Invoca un altro tamaGolem.");
            System.out.println("Tamagolem rimasti: " + (numTamaGolem - numTamaGolemEliminati));
        } else {
            System.out.printf("Turno del Giocatore %d\n", idGiocatore);
        }

        System.out.println("Scegli le pietre da dare in pasto al tuo tamaGolem: ");
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
                    listaPietre.addLast(new PietreElementi(nomeElemento));
                }
            } while (!trovato);
        }

        return listaPietre;
    }
}