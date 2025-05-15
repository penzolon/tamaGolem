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
            this.numeroElementi = 0;
            this.numTamaGolem = 0;
            this.numPietre = 0;
            this.qtScortaComunePietre = 0;
            this.numPietrePerElemento = 0;
            BattagliaGolem.equilibrio = null;
        }

        /**
         * Esegue una partita di TamaGolem.
         * Gestisce l'inizializzazione dei giocatori, la scelta degli elementi e il flusso della partita.
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
                System.out.println();
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
            int numElementi = InterfacciaUtente.inserisciNumeroElementi(0);
            this.numeroElementi = numElementi;
            int numFile = InterfacciaUtente.scegliFileElementi(null, new JsonReader(CostantiString.ELEMENTI_PATH), 1);
            File file = new JsonReader(CostantiString.ELEMENTI_PATH).getFile(numFile - 1);
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
         * Esegue un turno di gioco, gestendo il lancio delle pietre e i danni inflitti.
         *
         * @param giocatori la lista dei giocatori.
         * @param pietreEstratte la lista delle pietre estratte durante il turno.
         * @param scorta la scorta comune di pietre.
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
                System.out.printf(CostantiString.MESSAGGIO_LISTE_IDENTICHE + "\n" + CostantiString.MESSAGGIO_REINSERISCI_PIETRE, giocatoreDaReimmettere.getIdGiocatore());
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
                    System.out.println("\n" + CostantiString.MESSAGGIO_GOLEM_PRONTI);
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

            int interazione = equilibrio.calcolaInterazione(pietreEstratte.get(0).getNome(), pietreEstratte.get(1).getNome());
            if (interazione > 0) {
                gestisciDanni(giocatori.getLast(), interazione, numTamaGolem, scorta, equilibrio);
            } else {
                interazione = -interazione;
                if (interazione == 0) {
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
         *
         * @param giocatore il giocatore che subisce i danni.
         * @param interazione il valore dell'interazione tra le pietre.
         * @param numTamaGolem il numero totale di TamaGolem.
         * @param scorta la scorta comune di pietre.
         * @param equilibrio l'oggetto Equilibrio che rappresenta gli elementi disponibili.
         */
        private void gestisciDanni(Giocatore giocatore, int interazione, int numTamaGolem, ScortaPietre scorta, Equilibrio equilibrio) {
            TamaGolem tamaGolem = giocatore.getListaGolem().getFirst();
            int vitaTamaGolem = tamaGolem.getVita() - interazione;

            if (vitaTamaGolem <= 0) {
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
         * @param scortaPietre la scorta comune di pietre disponibile per i TamaGolem.
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
                int numTamaGolemEliminati = 0;
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

