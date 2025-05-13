package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.myutil.inputOutput.RandomDraws;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;
import it.unibs.fp.tamaGolem.Equilibrio;
import it.unibs.fp.tamaGolem.InterfacciaUtente;
import it.unibs.fp.tamaGolem.JsonReader;

import java.io.File;

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

    public int getNumeroElementi() {
        return numeroElementi;
    }

    public int getNumTamaGolem() {
        return numTamaGolem;
    }

    public int getNumPietre() {
        return numPietre;
    }

    public int getQtScortaComunePietre() {
        return qtScortaComunePietre;
    }

    public int getNumPietrePerElemento() {
        return numPietrePerElemento;
    }

    public void eseguiPartita() {
        int numElementi = 0 ;
        int numFile = 1 ;
        File file = null ;
        JsonReader jsonReader = new JsonReader(CostantiString.ELEMENTI_PATH) ;
        do {
            Giocatore giocatore1 = new Giocatore(0);
            Giocatore giocatore2 = new Giocatore(1);
            numElementi = InterfacciaUtente.inserisciNumeroElementi(numElementi);
            this.numeroElementi = numElementi;
            numFile = InterfacciaUtente.scegliFileElementi(file, jsonReader, numFile);
            file = jsonReader.getFile(numFile - 1) ;
            equilibrio = new Equilibrio(numElementi, file) ;
            System.out.println(equilibrio.toString());
            calcolaParametriDaInput();
            invocaTamaGolem(giocatore1, giocatore2);
        } while(InputData.readYesOrNo("Continuare")) ;
    }

    private void invocaTamaGolem(Giocatore giocatore1, Giocatore giocatore2) {
        if (sceltaPrimoGiocatore()) {
            giocatore1.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio);
            giocatore2.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio);
        } else {
            giocatore2.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio);
            giocatore1.invocazioneTamaGolem(numTamaGolem, numPietre, equilibrio);

        }
    }
        private boolean sceltaPrimoGiocatore () {
            boolean giocatore1Inizia = false;
            giocatore1Inizia = estrazionePrimoGiocatore();
            if (estrazionePrimoGiocatore()) {
                // Giocatore 1 inizia
                System.out.println("\nGiocatore 1 inizia");
            } else {
                // Giocatore 2 inizia
                System.out.println("\nGiocatore 2 inizia");
            }
            return giocatore1Inizia;
        }

        public void calcolaParametriDaInput () {
            numPietre = (int) Math.ceil((numeroElementi + 1) / 3.0) + 1;
            numTamaGolem = (int) Math.ceil((numeroElementi - 1) * (numeroElementi - 2) / (double) (2 * numPietre));
            qtScortaComunePietre = (int) Math.ceil((2 * numTamaGolem * numPietre) / (double) numeroElementi) * numeroElementi;
            numPietrePerElemento = qtScortaComunePietre / numeroElementi;
//      System.out.println("N = " + numeroElementi + ", G = " + numeroGolem + ", P = " + numPietre + ", S = " + qtScortaComunePietre + ", S/G = " + numPietrePerElemento);
        }

        public boolean estrazionePrimoGiocatore () {
            /* */
            return RandomDraws.drawBoolean();
        }
        

    }

