package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.myutil.inputOutput.RandomDraws;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;
import it.unibs.fp.tamaGolem.Setup.Equilibrio;
import it.unibs.fp.tamaGolem.InterfacciaUtente;
import it.unibs.fp.tamaGolem.Setup.JsonReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

/**
     * Classe che gestisce la battaglia tra i TamaGolem.
     * Contiene i parametri della partita, come il numero di elementi, TamaGolem e pietre,
     * e gestisce il flusso della partita.
     */
    public class BattagliaGolem {
		
		int numeroElementi;
        int numTamaGolem;
        int numPietre;
        int qtScortaComunePietre;
        int numPietrePerElemento;
        static Equilibrio equilibrio;

        public BattagliaGolem() {
            this.numeroElementi = CostantiPartita.NUMERO_ELEMENTI_PREDEFINITO;
            this.numTamaGolem = CostantiPartita.NUMERO_TAMAGOLEM_PREDEFINITO;
            this.numPietre = CostantiPartita.NUMERO_PIETRE_PREDEFINITO;
            this.qtScortaComunePietre = CostantiPartita.QUANTITA_SCORTA_PREDEFINITO;
            this.numPietrePerElemento = CostantiPartita.NUMERO_PIETRE_ELEMENTO_PREDEFINITO;
            BattagliaGolem.equilibrio = null;
        }

        /**
		 * Inizializza i parametri della partita e gestisce il flusso del gioco.
		 * Esegue i turni di gioco fino a quando uno dei giocatori non perde tutti i TamaGolem.
		 */
        public void eseguiPartita() {
            do {
                inizializzaPartita();
                ScortaPietre scorta = preparaScortaPietre();
                ArrayList<Giocatore> giocatori = invocaTamaGolem(scorta);
                Giocatore giocatore1 = trovaGiocatore(giocatori, CostantiPartita.ID_GIOCATORE_1);
                Giocatore giocatore2 = trovaGiocatore(giocatori, CostantiPartita.ID_GIOCATORE_2);
                ArrayList<PietreElementi> pietreEstratte = new ArrayList<>();

                while (partitaInCorso(giocatore1, giocatore2)) {
                    eseguiTurno(giocatori, pietreEstratte, scorta);
                }
                boolean mostraEquilibrio = InputData.readYesOrNo(CostantiString.DOMANDA_VISUALIZZA_EQUILIBRIO);
                if (mostraEquilibrio) {
                    System.out.println(CostantiString.MESSAGGIO_EQUILIBRIO + equilibrio.toString());
                }

            } while (InputData.readYesOrNo(CostantiString.DOMANDA_GIOCARE_DI_NUOVO));
        }

        /**
         * Inizializza i parametri della partita, come il numero di elementi,
         * il file degli elementi e l'equilibrio tra gli elementi.
         */
        private void inizializzaPartita() {
            int numElementi = InterfacciaUtente.inserisciNumeroElementi(CostantiPartita.ZERO);
            this.numeroElementi = numElementi;
            int numFile = InterfacciaUtente.scegliFileElementi(null, new JsonReader(CostantiString.ELEMENTI_PATH), CostantiPartita.UNO);
            File file = new JsonReader(CostantiString.ELEMENTI_PATH).getFile(numFile - CostantiPartita.UNO);
            equilibrio = new Equilibrio(numElementi, file);
            calcolaParametriDaInput();
        }

        /**
         * Prepara la scorta comune di pietre per la partita,
         * aggiungendo le pietre necessarie in base agli elementi.
         *
         * @return la scorta di pietre preparata.
         */
        private ScortaPietre preparaScortaPietre() {
            ScortaPietre scorta = new ScortaPietre();
            scorta.aggiungiPietre(numPietrePerElemento, equilibrio);
            return scorta;
        }

        /**
         * Trova un giocatore nella lista di giocatori in base al suo ID.
         *
         * @param giocatori la lista di giocatori.
         * @param idGiocatore l'ID del giocatore da trovare.
         * @return il giocatore trovato, o null se non esiste.
         */
        private Giocatore trovaGiocatore(ArrayList<Giocatore> giocatori, int idGiocatore) {
            for (Giocatore g : giocatori) {
                if (g.getIdGiocatore() == idGiocatore) {
                    return g;
                }
            }
            return null;
        }

        /**
         * Controlla se la partita è ancora in corso.
         *
         * @param giocatore1 il primo giocatore.
         * @param giocatore2 il secondo giocatore.
         * @return true se la partita è in corso, false altrimenti.
         */
        private boolean partitaInCorso(Giocatore giocatore1, Giocatore giocatore2) {
            return giocatore1 != null && giocatore2 != null &&
                   !giocatore1.getListaGolem().isEmpty() && !giocatore2.getListaGolem().isEmpty();
        }

        /**
		 * Esegue un singolo turno del gioco tra i due giocatori, gestendo il lancio delle pietre
		 * e il calcolo dei danni.
		 *
		 * <p>Il metodo esegue i seguenti passaggi:</p>
		 * <ol>
		 *   <li><b>Preparazione:</b> Svuota la lista `pietreEstratte` per memorizzare le pietre lanciate durante questo turno.</li>
		 *   <li><b>Validazione delle code:</b> Recupera le code di pietre (`queue1` e `queue2`) dei TamaGolem attivi
		 *       per ciascun giocatore e verifica se sono identiche utilizzando il metodo `sonoQueueIdentichePerNome`.
		 *       Se le code sono identiche, il giocatore con la coda più lunga deve restituire le pietre alla
		 *       scorta comune e selezionare nuove pietre. Questo processo si ripete finché le code non differiscono.</li>
		 *   <li><b>Lancio delle pietre:</b> Ogni giocatore lancia la prima pietra dalla propria coda. La pietra viene rimossa
		 *       dalla testa della coda e reinserita in fondo, simulando una coda circolare. Le pietre lanciate
		 *       vengono aggiunte alla lista `pietreEstratte`.</li>
		 *   <li><b>Calcolo dell'interazione:</b> L'interazione tra le due pietre lanciate viene calcolata utilizzando
		 *       il metodo `calcolaInterazione` dell'oggetto `Equilibrio`. Il risultato determina i danni da infliggere.</li>
		 *   <li><b>Gestione dei danni:</b> In base al valore dell'interazione:
		 *       <ul>
		 *         <li>Se positivo, i danni vengono inflitti al secondo giocatore.</li>
		 *         <li>Se negativo, il valore assoluto dei danni viene inflitto al primo giocatore.</li>
		 *         <li>Se zero, non vengono inflitti danni e il turno termina.</li>
		 *       </ul>
		 *   </li>
		 *   <li><b>Aggiornamento dello stato del gioco:</b> Se la vita di un TamaGolem scende a zero o meno, viene rimosso
		 *       dalla lista del giocatore e sostituito con un nuovo TamaGolem, se disponibile. Se un giocatore esaurisce
		 *       i suoi TamaGolem, la partita termina per quel giocatore.</li>
		 * </ol>
		 *
		 * @param giocatori la lista dei giocatori che partecipano al gioco.
		 * @param pietreEstratte la lista delle pietre lanciate durante il turno.
		 * @param scorta la scorta comune di pietre disponibile per il gioco.
		 */
        private void eseguiTurno(ArrayList<Giocatore> giocatori, ArrayList<PietreElementi> pietreEstratte, ScortaPietre scorta) {
            pietreEstratte.clear();

            Deque<PietreElementi> queue1 = giocatori.getFirst().getListaGolem().getFirst().getListaPietre();
            Deque<PietreElementi> queue2 = giocatori.getLast().getListaGolem().getFirst().getListaPietre();

            boolean identiche = sonoQueueIdentichePerNome(queue1, queue2);
            if (!identiche) {
                System.out.println(CostantiString.MESSAGGIO_GOLEM_PRONTI);
                InputData.readEmptyString(CostantiString.MESSAGGIO_PREMI_ENTER, false);
            }
            while (identiche) {
                Giocatore giocatoreDaReimmettere = queue1.size() > queue2.size() ? giocatori.getFirst() : giocatori.getLast();
                System.out.printf(CostantiString.MESSAGGIO_LISTE_IDENTICHE + CostantiString.A_CAPO + CostantiString.MESSAGGIO_REINSERISCI_PIETRE, giocatoreDaReimmettere.getIdGiocatore());
                InputData.readEmptyString(CostantiString.MESSAGGIO_PREMI_ENTER, false);
                Deque<PietreElementi> listaPietre = giocatoreDaReimmettere.getListaGolem().getFirst().getListaPietre();
                for (PietreElementi pietra : listaPietre) {
                    scorta.aggiungiPietraAllaScorta(pietra.getNome());
                }
                listaPietre.clear();
                giocatoreDaReimmettere.selezionaPietre(numPietre, numTamaGolem, equilibrio, scorta, giocatoreDaReimmettere.getListaGolem().getFirst());
                queue1 = giocatori.getFirst().getListaGolem().getFirst().getListaPietre();
                queue2 = giocatori.getLast().getListaGolem().getFirst().getListaPietre();
                identiche = sonoQueueIdentichePerNome(queue1, queue2);
                if (!identiche) {
                    System.out.println(CostantiString.A_CAPO + CostantiString.MESSAGGIO_GOLEM_PRONTI);
                    InputData.readEmptyString(CostantiString.MESSAGGIO_PREMI_ENTER, false);
                }
            }

            for (Giocatore giocatore : giocatori) {
                PietreElementi pietra = giocatore.getListaGolem().getFirst().getListaPietre().poll();
                assert pietra != null;
                System.out.printf(CostantiString.MESSAGGIO_PIETRA_LANCIATA, giocatore.getIdGiocatore(), pietra.getNome());
                giocatore.getListaGolem().getFirst().getListaPietre().addLast(pietra);
                pietreEstratte.add(pietra);
            }

            int interazione = equilibrio.calcolaInterazione(pietreEstratte.get(CostantiPartita.ZERO).getNome(), pietreEstratte.get(CostantiPartita.UNO).getNome());
            if (interazione > CostantiPartita.ZERO) {
                gestisciDanni(giocatori.getLast(), interazione, numTamaGolem, scorta, equilibrio);
            } else {
                interazione = -interazione;
                if (interazione == CostantiPartita.ZERO) {
                    System.out.println(CostantiString.MESSAGGIO_NESSUN_DANNO);
                    InputData.readEmptyString(CostantiString.MESSAGGIO_PREMI_ENTER, false);
                } else {
                    gestisciDanni(giocatori.getFirst(), interazione, numTamaGolem, scorta, equilibrio);
                }
            }
        }

        /**
         * Verifica se due code di pietre sono identiche confrontando i nomi degli elementi
         * in esse contenuti, ignorando eventuali spazi iniziali o finali.
         *
         * @param q1 la prima coda di pietre.
         * @param q2 la seconda coda di pietre.
         * @return true se le due code contengono lo stesso numero di elementi con nomi identici
         *         nello stesso ordine, false altrimenti.
         */
        private boolean sonoQueueIdentichePerNome(Deque<PietreElementi> q1, Deque<PietreElementi> q2) {
            if (q1.size() != q2.size()) return false;

            Iterator<PietreElementi> it1 = q1.iterator();
            Iterator<PietreElementi> it2 = q2.iterator();

            while (it1.hasNext() && it2.hasNext()) {
                String nome1 = it1.next().getNome().trim();
                String nome2 = it2.next().getNome().trim();
                if (!nome1.equals(nome2)) return false;
            }

            return true;
        }

        /**
		 * Gestisce i danni inflitti a un giocatore e al suo TamaGolem.
		 * Se il TamaGolem subisce danni fatali, viene rimosso e sostituito con un nuovo TamaGolem,
		 * se disponibile. Se il giocatore esaurisce i TamaGolem, la partita termina per quel giocatore.
		 *
		 * <p>Il metodo esegue i seguenti passaggi:</p>
		 * <ol>
		 *   <li><b>Calcolo della vita residua:</b> Riduce la vita del TamaGolem attivo del giocatore
		 *       in base al valore dell'interazione.</li>
		 *   <li><b>Gestione dei danni fatali:</b> Se la vita del TamaGolem scende a zero o meno:
		 *       <ul>
		 *         <li>Il TamaGolem viene rimosso dalla lista del giocatore.</li>
		 *         <li>Viene incrementato il contatore dei TamaGolem eliminati per il giocatore.</li>
		 *         <li>Se il giocatore ha esaurito i TamaGolem, la partita termina per quel giocatore.</li>
		 *         <li>Altrimenti, viene invocato un nuovo TamaGolem e aggiunto alla lista del giocatore.</li>
		 *       </ul>
		 *   </li>
		 *   <li><b>Gestione dei danni non fatali:</b> Se il TamaGolem sopravvive, la sua vita viene aggiornata
		 *       e viene notificato il danno subito.</li>
		 * </ol>
		 *
		 * @param giocatore il giocatore che subisce i danni.
		 * @param interazione il valore dell'interazione tra le pietre.
		 * @param numTamaGolem il numero totale di TamaGolem disponibili per il giocatore.
		 * @param scorta la scorta comune di pietre.
		 * @param equilibrio l'oggetto Equilibrio che rappresenta gli elementi disponibili.
		 */

        private void gestisciDanni(Giocatore giocatore, int interazione, int numTamaGolem, ScortaPietre scorta, Equilibrio equilibrio) {
            TamaGolem tamaGolem = giocatore.getListaGolem().getFirst();
            int vitaTamaGolem = tamaGolem.getVita() - interazione;

            if (vitaTamaGolem <= CostantiPartita.ZERO) {
                giocatore.getListaGolem().removeFirst();
                System.out.printf(CostantiString.MESSAGGIO_DANNI_FATALI, giocatore.getIdGiocatore(), interazione);
                System.out.printf(CostantiString.MESSAGGIO_ELIMINATO, giocatore.getIdGiocatore());
                InputData.readEmptyString(CostantiString.MESSAGGIO_PREMI_ENTER, false);
                giocatore.numTamaGolemEliminati++;

                if (giocatore.numTamaGolemEliminati >= numTamaGolem) {
                    System.out.printf(CostantiString.MESSAGGIO_PARTITA_PERSA, giocatore.getIdGiocatore());
                    return;
                }

                TamaGolem nuovoTamaGolem = giocatore.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio, scorta, giocatore.numTamaGolemEliminati);
                giocatore.getListaGolem().add(nuovoTamaGolem);
            } else {
                tamaGolem.setVita(vitaTamaGolem);
                System.out.printf(CostantiString.MESSAGGIO_DANNI_SUBITI, giocatore.getIdGiocatore(), interazione);
                InputData.readEmptyString(CostantiString.MESSAGGIO_PREMI_ENTER, false);
            }
        }

        /**
		 * Gestisce l'invocazione dei TamaGolem per entrambi i giocatori.
		 * Determina quale giocatore inizia e invoca i TamaGolem in ordine.
		 *
		 * <p>Il metodo esegue i seguenti passaggi:</p>
		 * <ol>
		 *   <li><b>Determinazione del primo giocatore:</b> Effettua un'estrazione casuale per decidere quale giocatore
		 *       inizia per primo. Il risultato viene comunicato tramite un messaggio.</li>
		 *   <li><b>Creazione dell'ordine dei giocatori:</b> In base al risultato dell'estrazione, i giocatori vengono
		 *       ordinati in una lista che rappresenta l'ordine di gioco.</li>
		 *   <li><b>Invocazione dei TamaGolem:</b> Per ciascun giocatore nell'ordine stabilito:
		 *       <ul>
		 *         <li>Viene creato un nuovo TamaGolem utilizzando i parametri della partita e la scorta comune di pietre.</li>
		 *         <li>Il TamaGolem viene aggiunto alla lista dei Golem del giocatore.</li>
		 *       </ul>
		 *   </li>
		 * </ol>
		 *
		 * @param scortaPietre la scorta comune di pietre disponibile per i TamaGolem.
		 * @return una lista ordinata dei giocatori, con i loro TamaGolem invocati.
		 */

        private ArrayList<Giocatore> invocaTamaGolem(ScortaPietre scortaPietre) {
            boolean giocatore1Inizia = sceltaPrimoGiocatore();
            ArrayList<Giocatore> ordineGiocatori = new ArrayList<>();
            Giocatore giocatore1 = new Giocatore(CostantiPartita.ID_GIOCATORE_1);
            Giocatore giocatore2 = new Giocatore(CostantiPartita.ID_GIOCATORE_2);
            if (giocatore1Inizia) {
                ordineGiocatori.add(giocatore1);
                ordineGiocatori.add(giocatore2);
            } else {
                ordineGiocatori.add(giocatore2);
                ordineGiocatori.add(giocatore1);
            }

            for (Giocatore giocatore : ordineGiocatori) {
                int numTamaGolemEliminati = CostantiPartita.ZERO;
                TamaGolem tamaGolem = giocatore.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio, scortaPietre, numTamaGolemEliminati);
                giocatore.getListaGolem().add(tamaGolem);
            }
            return ordineGiocatori;
        }


        /**
         * Determina quale giocatore inizia la partita.
         * Effettua un'estrazione casuale per scegliere il primo giocatore.
         *
         * @return true se il giocatore 1 inizia, false altrimenti.
         */
        private boolean sceltaPrimoGiocatore() {
            boolean giocatore1Inizia = estrazionePrimoGiocatore();
            if (giocatore1Inizia) {
                System.out.println(CostantiString.MESSAGGIO_GIOCATORE_1_SCELTO);
            } else {
                System.out.println(CostantiString.MESSAGGIO_GIOCATORE_2_SCELTO);
            }
            return giocatore1Inizia;
        }

        /**
         * Calcola i parametri della partita in base al numero di elementi.
         * Determina il numero di TamaGolem, pietre e la quantità di pietre nella scorta comune.
         */
        public void calcolaParametriDaInput() {
            numPietre = (int) Math.ceil((numeroElementi + 1) / 3.0) + 1;
            numTamaGolem = (int) Math.ceil((numeroElementi - 1) * (numeroElementi - 2) / (double) (2 * numPietre));
            qtScortaComunePietre = (int) Math.ceil((2 * numTamaGolem * numPietre) / (double) numeroElementi) * numeroElementi;
            numPietrePerElemento = qtScortaComunePietre / numeroElementi;
            //System.out.println("N = " + numeroElementi + ", G = " + numTamaGolem + ", P = " + numPietre + ", S = " + qtScortaComunePietre + ", S/G = " + numPietrePerElemento);
        }

        /**
         * Effettua un'estrazione casuale per determinare quale giocatore inizia la partita.
         *
         * @return true se il giocatore 1 inizia, false altrimenti.
         */
        public boolean estrazionePrimoGiocatore() {
            return RandomDraws.drawBoolean();
        }
    }

