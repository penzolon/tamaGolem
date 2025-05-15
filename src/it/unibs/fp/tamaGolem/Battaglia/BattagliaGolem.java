package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.myutil.inputOutput.RandomDraws;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;
import it.unibs.fp.tamaGolem.Equilibrio;
import it.unibs.fp.tamaGolem.InterfacciaUtente;
import it.unibs.fp.tamaGolem.JsonReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;

/**
     * Classe che gestisce la battaglia tra i TamaGolem.
     * Contiene i parametri della partita, come il numero di elementi, TamaGolem e pietre,
     * e gestisce il flusso della partita.
     */
    public class BattagliaGolem {
        public static final int ID_GIOCATORE_2 = 2;
        public static final int ID_GIOCATORE_1 = 1;
        int numeroElementi;
        int numTamaGolem;
        int numPietre;
        int qtScortaComunePietre;
        int numPietrePerElemento;
        static Equilibrio equilibrio;

        /**
         * Costruttore della classe BattagliaGolem.
         * Inizializza i parametri della partita con valori predefiniti.
         */
        public BattagliaGolem() {
            this.numeroElementi = 0;
            this.numTamaGolem = 0;
            this.numPietre = 0;
            this.qtScortaComunePietre = 0;
            this.numPietrePerElemento = 0;
            BattagliaGolem.equilibrio = null;
        }

        /**
         * Restituisce il numero di elementi della partita.
         *
         * @return il numero di elementi.
         */
        public int getNumeroElementi() {
            return numeroElementi;
        }

        /**
         * Restituisce il numero di TamaGolem per giocatore.
         *
         * @return il numero di TamaGolem.
         */
        public int getNumTamaGolem() {
            return numTamaGolem;
        }

        /**
         * Restituisce il numero di pietre per TamaGolem.
         *
         * @return il numero di pietre.
         */
        public int getNumPietre() {
            return numPietre;
        }

        /**
         * Restituisce la quantità totale di pietre nella scorta comune.
         *
         * @return la quantità di pietre nella scorta comune.
         */
        public int getQtScortaComunePietre() {
            return qtScortaComunePietre;
        }

        /**
         * Restituisce il numero di pietre per elemento.
         *
         * @return il numero di pietre per elemento.
         */
        public int getNumPietrePerElemento() {
            return numPietrePerElemento;
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
                Giocatore giocatore1 = trovaGiocatore(giocatori, ID_GIOCATORE_1);
                Giocatore giocatore2 = trovaGiocatore(giocatori, ID_GIOCATORE_2);
                ArrayList<PietreElementi> pietreEstratte = new ArrayList<>();

                while (partitaInCorso(giocatore1, giocatore2)) {
                    eseguiTurno(giocatori, pietreEstratte, scorta);
                }
                boolean mostraEquilibrio = InputData.readYesOrNo("Vuoi visualizzare l'equilibrio tra gli elementi");
                System.out.println();
                if (mostraEquilibrio){
                    System.out.println("Equilibrio tra gli elementi: \n" + equilibrio.toString());
                }

            } while (InputData.readYesOrNo("Vuoi giocare di nuovo"));
        }

        /**
         * Verifica se due queue di pietre sono identiche, ovvero se contengono
         * gli stessi elementi nello stesso ordine.
         *
         * @param queue1 la prima queue di pietre.
         * @param queue2 la seconda queue di pietre.
         * @return true se le due queue sono identiche, false altrimenti.
         */
        private boolean sonoQueueIdentiche(Deque<PietreElementi> queue1, Deque<PietreElementi> queue2) {
            if (queue1.size() != queue2.size()) {
                return false;
            }
            return queue1.equals(queue2);
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
            if (!identiche){
                System.out.println("I Golem sono pronti a scagliare le pietre!");
                InputData.readEmptyString("Premi enter per continuare...\n", false);
            }
            while (identiche) {
                Giocatore giocatoreDaReimmettere = queue1.size() > queue2.size() ? giocatori.getFirst() : giocatori.getLast();
                System.out.printf("La liste delle pietre dei due TamaGolem sono identiche! \nGiocatore %d: Reinserisci le pietre!\n\n", giocatoreDaReimmettere.getIdGiocatore());
                InputData.readEmptyString("Premi enter per continuare...\n", false);
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
                    System.out.println("\nI Golem sono pronti a scagliare le pietre!");
                    InputData.readEmptyString("Premi enter per continuare...\n", false);
                }
            }

            for (Giocatore giocatore : giocatori) {
                PietreElementi pietra = giocatore.getListaGolem().getFirst().getListaPietre().poll();
                assert pietra != null;
                System.out.printf("Il golem del giocatore %d ha lanciato la pietra: %s\n", giocatore.getIdGiocatore(), pietra.getNome());
                giocatore.getListaGolem().getFirst().getListaPietre().addLast(pietra);
                pietreEstratte.add(pietra);
            }

            int interazione = equilibrio.calcolaInterazione(pietreEstratte.get(0).getNome(), pietreEstratte.get(1).getNome());
            if (interazione > 0) {
                gestisciDanni(giocatori.getLast(), interazione, numTamaGolem, scorta, equilibrio);
            } else {
                interazione = -interazione;
                if (interazione == 0) {
                    System.out.println("Nessun danno inflitto, i due tamaGolem hanno scagliato due pietre dello stesso elemento!\n");
                    InputData.readEmptyString("Premi enter per continuare...\n", false);
                } else {
                    gestisciDanni(giocatori.getFirst(), interazione, numTamaGolem, scorta, equilibrio);
                }
            }
        }


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
                giocatore.getListaGolem().removeFirst(); // Rimuove il TamaGolem
                System.out.printf("Il TamaGolem del giocatore %d ha subito danni fatali!(%d)\n", giocatore.getIdGiocatore(), interazione);
                System.out.printf("\nIl tamaGolem del giocatore %d è stato eliminato!\n", giocatore.getIdGiocatore());
                InputData.readEmptyString("Premi enter per continuare...\n", false);
                giocatore.numTamaGolemEliminati++;

                if (giocatore.numTamaGolemEliminati >= numTamaGolem) {
                    System.out.printf("Il giocatore %d non può più invocare TamaGolem e ha perso la partita!\n", giocatore.getIdGiocatore());
                    return;
                }

                TamaGolem nuovoTamaGolem = giocatore.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio, scorta, giocatore.numTamaGolemEliminati);
                giocatore.getListaGolem().add(nuovoTamaGolem);
            } else {
                tamaGolem.setVita(vitaTamaGolem);
                System.out.printf("Il TamaGolem del giocatore %d ha subito danni pari a: %d\n", giocatore.getIdGiocatore(), interazione);
                InputData.readEmptyString("\nPremi enter per continuare...\n", false);
            }
        }

        /**
         * Calcola l'interazione tra due elementi specificati.
         *
         * @param elemento1 il primo elemento.
         * @param elemento2 il secondo elemento.
         * @param equilibrioMap la mappa che rappresenta le interazioni tra gli elementi.
         * @return il valore dell'interazione tra i due elementi.
         * @throws IllegalArgumentException se l'interazione non è trovata.
         */
        public int calcolaInterazione(String elemento1, String elemento2, Map<String, Map<String, Integer>> equilibrioMap) {
            if (equilibrioMap.containsKey(elemento1) && equilibrioMap.get(elemento1).containsKey(elemento2)) {
                return equilibrioMap.get(elemento1).get(elemento2);
            } else {
                throw new IllegalArgumentException("Interazione non trovata tra " + elemento1 + " e " + elemento2);
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
            Giocatore giocatore1 = new Giocatore(ID_GIOCATORE_1);
            Giocatore giocatore2 = new Giocatore(ID_GIOCATORE_2);
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
            boolean giocatore1Inizia;
            giocatore1Inizia = estrazionePrimoGiocatore();
            if (estrazionePrimoGiocatore()) {
                System.out.println("\nÉ stato scelto il giocatore 1!");
            } else {
                System.out.println("\nÉ stato scelto il giocatore 2!");
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

