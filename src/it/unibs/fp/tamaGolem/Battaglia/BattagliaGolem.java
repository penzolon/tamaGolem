package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.myutil.inputOutput.RandomDraws;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;
import it.unibs.fp.tamaGolem.Equilibrio;
import it.unibs.fp.tamaGolem.InterfacciaUtente;
import it.unibs.fp.tamaGolem.JsonReader;

import java.io.File;
import java.util.ArrayList;
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
            int numElementi = 0;
            int numFile = 1;
            File file = null;
            boolean trovato = false;
            int interazione = 0;
            ArrayList<Giocatore> giocatori = new ArrayList<>();
            JsonReader jsonReader = new JsonReader(CostantiString.ELEMENTI_PATH);
            do {
                Giocatore giocatore1 = new Giocatore(ID_GIOCATORE_1);
                Giocatore giocatore2 = new Giocatore(ID_GIOCATORE_2);
                numElementi = InterfacciaUtente.inserisciNumeroElementi(numElementi);
                this.numeroElementi = numElementi;
                numFile = InterfacciaUtente.scegliFileElementi(file, jsonReader, numFile);
                file = jsonReader.getFile(numFile - 1);
                equilibrio = new Equilibrio(numElementi, file);
                calcolaParametriDaInput();
                ScortaPietre scorta = new ScortaPietre();
                scorta.aggiungiPietre(numPietrePerElemento, equilibrio);
                giocatori = invocaTamaGolem(giocatore1, giocatore2, scorta);
                ArrayList<PietreElementi> pietreEstratte = new ArrayList<>();

                while (giocatore1.getListaGolem().size() > 0 && giocatore2.getListaGolem().size() > 0) {
                    pietreEstratte.clear(); // Pulisce la lista delle pietre estratte per ogni iterazione
                    System.out.println("I Golem sono pronti a scagliare le pietre!");
                    InputData.readEmptyString("Premi enter per continuare...\n", false);
                    for (Giocatore giocatore : giocatori) {
                        PietreElementi pietra = giocatore.getListaGolem().get(0).getListaPietre().poll();
                        System.out.printf("Il golem del giocatore %d ha lanciato la pietra: %s\n", giocatore.getIdGiocatore(), pietra.getNome());
                        giocatore.getListaGolem().get(0).getListaPietre().addLast(pietra);
                        pietreEstratte.add(pietra); // Aggiunge la pietra estratta alla lista
                    }
                    // Calcola l'interazione tra le pietre estratte
                    interazione = equilibrio.calcolaInterazione(pietreEstratte.get(0).getNome(), pietreEstratte.get(1).getNome());
                    int numTamaGolemEliminatiPrimoG = 0;
                    int numTamaGolemEliminatiSecondoG = 0;
                    if (interazione > 0) {
                        // Il TamaGolem del secondo giocatore subisce danni
                        TamaGolem tamaGolemSecondoGiocatore = giocatori.getLast().getListaGolem().getFirst();
                        int vitaTamaGolemSecondoGiocatore = tamaGolemSecondoGiocatore.getVita() - interazione;
                        if (vitaTamaGolemSecondoGiocatore <= 0) {
                            giocatori.getLast().getListaGolem().removeFirst(); // Rimuove il TamaGolem
                            System.out.println("\nIl tamaGolem del giocatore 2 è stato eliminato!\n");
                            InputData.readEmptyString("Premi enter per continuare...\n", false);
                            numTamaGolemEliminatiSecondoG++;
                            TamaGolem nuovoTamaGolem = giocatori.getLast().invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio, scorta, numTamaGolemEliminatiSecondoG);
                            giocatori.getLast().getListaGolem().add(nuovoTamaGolem);
                        }
                        tamaGolemSecondoGiocatore.setVita(vitaTamaGolemSecondoGiocatore);
                        System.out.println("Il TamaGolem del giocatore 2 ha subito danni pari a: " + interazione);
                        InputData.readEmptyString("Premi enter per continuare...\n", false);
                    } else {
                        interazione = -interazione;
                        // Il TamaGolem del primo giocatore subisce danni
                        TamaGolem tamaGolemPrimoGiocatore = giocatori.getFirst().getListaGolem().getFirst();
                        int vitaTamaGolemPrimoGiocatore = tamaGolemPrimoGiocatore.getVita() - interazione;
                        if (vitaTamaGolemPrimoGiocatore <= 0) {
                            giocatori.getFirst().getListaGolem().removeFirst(); // Rimuove il TamaGolem
                            System.out.println("\nIl tamaGolem del giocatore 1 è stato eliminato!\n");
                            InputData.readEmptyString("Premi enter per continuare...\n", false);
                            numTamaGolemEliminatiPrimoG++;
                            TamaGolem nuovoTamaGolem = giocatori.getFirst().invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio, scorta, numTamaGolemEliminatiPrimoG);
                            giocatori.getFirst().getListaGolem().add(nuovoTamaGolem);
                        }
                        tamaGolemPrimoGiocatore.setVita(vitaTamaGolemPrimoGiocatore);
                        if (interazione == 0){
                            System.out.println("I due tamaGolem non hanno subito danni, poiché hanno lanciato due pietre dello stesso elemento.");
                        }else {
                            System.out.println("Il TamaGolem del giocatore 1 ha subito danni pari a: " + interazione);
                        }
                        InputData.readEmptyString("Premi enter per continuare...\n", false);
                    }
                    System.out.println("Prova!");
                }

            } while (InputData.readYesOrNo("Continuare"));
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
         * @param giocatore1 il primo giocatore.
         * @param giocatore2 il secondo giocatore.
         * @param scortaPietre la scorta comune di pietre disponibile per i TamaGolem.
         */
        private ArrayList<Giocatore> invocaTamaGolem(Giocatore giocatore1, Giocatore giocatore2, ScortaPietre scortaPietre) {
            boolean giocatore1Inizia = sceltaPrimoGiocatore();
            ArrayList<Giocatore> ordineGiocatori = new ArrayList<>();

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
            boolean giocatore1Inizia = false;
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

