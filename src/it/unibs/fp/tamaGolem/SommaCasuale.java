package it.unibs.fp.tamaGolem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SommaCasuale {

    private static final Random random = new Random();

    /**
     * Genera un ArrayList di 'n' numeri casuali positivi che sommati danno 'sommaTotale'.
     *
     * @param sommaTotale la somma totale desiderata
     * @param n           il numero di elementi da generare
     * @return un ArrayList di 'n' interi positivi che sommati danno 'sommaTotale'
     * @throws IllegalArgumentException se n <= 0 o sommaTotale < n
     */
    public static ArrayList<Integer> generaNumeriCasualiCheSommanoA(int sommaTotale, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Il numero di elementi deve essere maggiore di zero.");
        }
        if (sommaTotale < n) {
            throw new IllegalArgumentException("La somma totale deve essere almeno pari al numero di elementi (minimo 1 per elemento).");
        }

        ArrayList<Integer> tagli = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            tagli.add(random.nextInt(sommaTotale - 1) + 1);
        }

        tagli.add(0);
        tagli.add(sommaTotale);
        Collections.sort(tagli);

        ArrayList<Integer> risultato = new ArrayList<>();
        for (int i = 0; i < tagli.size() - 1; i++) {
            risultato.add(tagli.get(i + 1) - tagli.get(i));
        }

        return risultato;
    }
}
