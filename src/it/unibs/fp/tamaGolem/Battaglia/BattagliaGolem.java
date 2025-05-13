package it.unibs.fp.tamaGolem.Battaglia;

public class BattagliaGolem {
    int numeroElementi;

    public BattagliaGolem(int numeroElementi) {
        this.numeroElementi = numeroElementi;
    }


    public int getNumeroElementi() {
        return numeroElementi;
    }

    public static void creaBattagliaGolem(int numElementi) {
        BattagliaGolem battagliaGolem = new BattagliaGolem(numElementi);
        battagliaGolem.calcolaParametriDaInput();
    }

    public void calcolaParametriDaInput(){
        int numeroGolem;
        int numPietre;
        int qtScortaComunePietre;
        int numPietrePerElemento;
        numPietre = (int) Math.ceil((numeroElementi + 1) / 3.0) + 1;
        numeroGolem =  (int) Math.ceil((numeroElementi - 1) * (numeroElementi - 2) / (double)(2 * numPietre));
        qtScortaComunePietre = (int) Math.ceil((2 * numeroGolem * numPietre) / (double) numeroElementi) * numeroElementi;
        numPietrePerElemento = qtScortaComunePietre / numeroElementi;

        System.out.println("N = " + numeroElementi + ", G = " + numeroGolem + ", P = " + numPietre + ", S = " + qtScortaComunePietre + ", S/G = " + numPietrePerElemento);
    }

}
