package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.tamaGolem.Setup.Equilibrio;

import java.util.ArrayList;

/**
 * Classe che rappresenta la scorta di pietre utilizzata nel contesto della battaglia dei TamaGolem.
 * La scorta contiene una lista di pietre disponibili per i giocatori.
 */
public class ScortaPietre {

    private ArrayList<PietreElementi> scortaPietre;

    public ScortaPietre() {
        this.scortaPietre = new ArrayList<>();
    }

    public ArrayList<PietreElementi> getScortaPietre() {
        return scortaPietre;
    }

    /**
     * Aggiunge pietre alla scorta in base al numero di pietre per elemento e agli elementi disponibili.
     *
     * @param numPietrePerElemento il numero di pietre da aggiungere per ogni elemento.
     * @param equilibrio           l'oggetto Equilibrio che contiene gli elementi disponibili.
     */
    public void aggiungiPietre(int numPietrePerElemento, Equilibrio equilibrio) {
        for (int i = 0; i < numPietrePerElemento; i++) {
            for (String pietra : equilibrio.getElementi()) {
                scortaPietre.add(new PietreElementi(pietra));
            }
        }
    }

    /**
     * Rimuove una pietra dalla scorta in base al nome dell'elemento specificato.
     *
     * @param scortaPietre l'oggetto ScortaPietre da cui rimuovere la pietra.
     * @param nomeElemento il nome dell'elemento della pietra da rimuovere.
     * @return true se la pietra è stata rimossa con successo, false altrimenti.
     */
    public static boolean rimuoviPietraDaScorta(ScortaPietre scortaPietre, String nomeElemento) {
        boolean trovato = false;
        for (PietreElementi p : scortaPietre.getScortaPietre()) {
            if (p.getNome().equals(nomeElemento)) {
                trovato = scortaPietre.getScortaPietre().remove(p);
                break;
            }
        }
        return trovato;
    }

    public void aggiungiPietraAllaScorta(String nomePietra) {
        scortaPietre.add(new PietreElementi(nomePietra));
    }
}