package it.unibs.fp.tamaGolem;

import java.util.ArrayList;
import java.util.Random;

import it.kibo.fp.lib.AnsiColors;

public class EquilibrioMondo {

    private static final int NUMERO_ELEMENTI = 5;
    private  int[][] matriceEquilibrio;
    
    public int[][] getMatriceEquilibrio() {
        return matriceEquilibrio;
    }

    public EquilibrioMondo() {
        matriceEquilibrio = new int[NUMERO_ELEMENTI][NUMERO_ELEMENTI];
    }

    public  void riempiMatriceEquilibrio() {
        riempiColonne();
    }

    private  void riempiColonne() {
        int sommaRiga;
        ArrayList<Integer> lista = new ArrayList<>();
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            sommaRiga = riempiRighe(i);
            lista =  SommaCasuale.generaNumeriCasualiCheSommanoA(sommaRiga, NUMERO_ELEMENTI);
            System.out.println(AnsiColors.RED + lista.toString() + AnsiColors.RESET);
        }
    }

    private  int riempiRighe(int i) {
        int somma = 0;
        for (int j = 0; j < NUMERO_ELEMENTI; j++) {
            if (i == j) {
                matriceEquilibrio[i][j] = 0; // Nessun vantaggio contro se stesso
            } else {
                matriceEquilibrio[i][j] = generaPotenzaCasuale(i, j);
                somma += matriceEquilibrio[i][j];
            }
        }
        return somma;

    }

    private  int generaPotenzaCasuale(int i, int j) {
        Random random = new Random();
        int valore = random.nextInt(10); // 0 o 1
        return valore;
    }

    public void stampaMatriceEquilibrio() {
        System.out.println("Matrice di equilibrio:");
        for (int i = 0; i < NUMERO_ELEMENTI; i++) {
            for (int j = 0; j < NUMERO_ELEMENTI; j++) {
                System.out.print(matriceEquilibrio[i][j] + "  ");
            }
            System.out.println();
        }
    }


    
}
