package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;
import it.unibs.fp.tamaGolem.Setup.Equilibrio;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Classe che rappresenta un giocatore nel contesto della battaglia dei TamaGolem.
 * Ogni giocatore ha un ID, una lista di TamaGolem, una lista di pietre e altre proprietà
 * utili per gestire il gioco.
 */
public class Giocatore {
    int idGiocatore;
    ArrayList<TamaGolem> listaGolem;
    int numTamaGolemEliminati;

    public Giocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
        this.listaGolem = new ArrayList<>();
        this.numTamaGolemEliminati = CostantiPartita.ZERO;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public ArrayList<TamaGolem> getListaGolem() {
        return listaGolem;
    }

    /**
	 * Gestisce l'invocazione di un TamaGolem da parte del giocatore.
	 * Se il numero massimo di TamaGolem non è stato ancora raggiunto, crea un nuovo TamaGolem,
	 * assegna le pietre selezionate dalla scorta comune e lo aggiunge alla lista dei TamaGolem del giocatore.
	 * Altrimenti, notifica che non è possibile invocare ulteriori TamaGolem.
	 *
	 * <p>Il metodo esegue i seguenti passaggi:</p>
	 * <ol>
	 *   <li><b>Verifica del limite di TamaGolem:</b> Controlla se il numero di TamaGolem eliminati
	 *       è inferiore al numero massimo di TamaGolem invocabili.</li>
	 *   <li><b>Creazione del TamaGolem:</b> Se il limite non è stato raggiunto:
	 *       <ul>
	 *         <li>Viene creato un nuovo TamaGolem con la vita iniziale predefinita.</li>
	 *         <li>Viene chiamato il metodo `selezionaPietre` per assegnare le pietre al TamaGolem.</li>
	 *         <li>Le pietre selezionate vengono mostrate al giocatore.</li>
	 *       </ul>
	 *   </li>
	 *   <li><b>Notifica del limite raggiunto:</b> Se il limite è stato raggiunto, viene mostrato un messaggio
	 *       che informa il giocatore che non è possibile invocare ulteriori TamaGolem.</li>
	 * </ol>
	 *
	 * @param numTamaGolem il numero massimo di TamaGolem che il giocatore può invocare.
	 * @param numPietre il numero di pietre da assegnare al TamaGolem.
	 * @param equilibrio l'oggetto Equilibrio che rappresenta le interazioni tra gli elementi.
	 * @param scortaPietre la scorta comune di pietre disponibile.
	 * @param numTamaEliminati il numero di TamaGolem già eliminati dal giocatore.
	 * @return il TamaGolem invocato, o `null` se non è stato possibile invocarlo.
	 */

    public TamaGolem invocazioneTamaGolem(int numTamaGolem, int numPietre, Equilibrio equilibrio, ScortaPietre scortaPietre, int numTamaEliminati) {
        TamaGolem nuovoGolem = null;
        numTamaGolemEliminati = numTamaEliminati;

        if (numTamaGolemEliminati < numTamaGolem) {
            nuovoGolem = new TamaGolem(CostantiPartita.VITA_TAMAGOLEM);
            Deque<PietreElementi> listaPietre = selezionaPietre(numPietre, numTamaGolem, equilibrio, scortaPietre, nuovoGolem);

            String listaPietreString = listaPietre.stream()
                    .map(PietreElementi::getNome)
                    .reduce((p1, p2) -> p1 + CostantiString.VIRGOLA + p2)
                    .orElse("");
            System.out.printf(CostantiString.MESSAGGIO_LISTA_PIETRE, listaPietreString);
        } else {
            System.out.println(CostantiString.MESSAGGIO_MAX_TAMAGOLEM);
            System.out.println(CostantiString.MESSAGGIO_NON_PUOI_INVOCARE);
        }

        return nuovoGolem;
    }

    /**
	 * Permette al giocatore di selezionare un certo numero di pietre dalla scorta comune.
	 * Le pietre selezionate vengono assegnate al TamaGolem specificato.
	 *
	 * <p>Il metodo esegue i seguenti passaggi:</p>
	 * <ol>
	 *   <li><b>Visualizzazione delle pietre disponibili:</b> Mostra al giocatore la lista degli elementi
	 *       disponibili e la quantità di ciascuna pietra nella scorta comune.</li>
	 *   <li><b>Selezione delle pietre:</b> Richiede al giocatore di selezionare il numero richiesto di pietre.
	 *       Per ogni selezione:
	 *       <ul>
	 *         <li>Verifica se la pietra selezionata è disponibile nella scorta.</li>
	 *         <li>Se disponibile, rimuove la pietra dalla scorta e la aggiunge alla lista delle pietre del TamaGolem.</li>
	 *         <li>Se non disponibile, notifica al giocatore e richiede un'altra selezione.</li>
	 *       </ul>
	 *   </li>
	 *   <li><b>Assegnazione delle pietre:</b> Aggiunge le pietre selezionate alla lista delle pietre del TamaGolem.</li>
	 * </ol>
	 *
	 * @param numPietre il numero di pietre da selezionare.
	 * @param numTamaGolem il numero totale di TamaGolem disponibili per il giocatore.
	 * @param equilibrio l'oggetto `Equilibrio` che rappresenta gli elementi disponibili.
	 * @param scortaPietre la scorta comune di pietre disponibile.
	 * @param tamaGolem il TamaGolem a cui assegnare le pietre selezionate.
	 * @return una deque contenente le pietre selezionate.
	 */

    public Deque<PietreElementi> selezionaPietre(int numPietre, int numTamaGolem, Equilibrio equilibrio, ScortaPietre scortaPietre, TamaGolem tamaGolem) {
        List<String> listaElementi = equilibrio.getElementi();
        Deque<PietreElementi> listaPietre = tamaGolem.getListaPietre();

        if (numTamaGolemEliminati != CostantiPartita.ZERO) {
            System.out.printf(CostantiString.MESSAGGIO_INVOCA_ALTRO_GOLEM, idGiocatore);
            System.out.printf((CostantiString.MESSAGGIO_GOLEM_RIMASTI) + CostantiString.A_CAPO, (numTamaGolem - numTamaGolemEliminati));
        } else {
            System.out.printf(CostantiString.MESSAGGIO_TURNO_GIOCATORE, idGiocatore);
        }

        System.out.println(CostantiString.MESSAGGIO_SCEGLI_PIETRE);
        System.out.println(CostantiString.MESSAGGIO_PIETRE_DISPONIBILI);
        for (int i = 0; i < listaElementi.size(); i++) {
            String elemento = listaElementi.get(i);
            long count = scortaPietre.getScortaPietre().stream()
                    .filter(p -> p.getNome().equals(elemento))
                    .count();
            System.out.println((i + 1) + CostantiString.PUNTO + elemento + CostantiString.PARENTESI_APERTA + count + CostantiString.DISPONIBILI);
        }

        System.out.printf(CostantiString.MESSAGGIO_SELEZIONA_PIETRE, numPietre);
        for (int i = 0; i < numPietre; i++) {
            boolean trovato;
            do {
                int idPietra = InputData.readIntegerBetween(CostantiString.PROMPT_SELEZIONE_PIETRA, 1, equilibrio.getElementi().size());
                String nomeElemento = listaElementi.get(idPietra - 1);
                trovato = ScortaPietre.rimuoviPietraDaScorta(scortaPietre, nomeElemento);

                if (!trovato) {
                    System.out.println(CostantiString.MESSAGGIO_PIETRA_NON_DISPONIBILE);
                } else {
                    System.out.printf((CostantiString.MESSAGGIO_PIETRA_SELEZIONATA) + CostantiString.PERCENTO_N, nomeElemento);
                    listaPietre.addLast(new PietreElementi(nomeElemento));
                }
            } while (!trovato);
        }

        return listaPietre;
    }
}