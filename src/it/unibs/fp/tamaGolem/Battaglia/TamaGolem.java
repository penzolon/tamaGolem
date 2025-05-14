package it.unibs.fp.tamaGolem.Battaglia;

import java.util.*;
import java.util.function.Consumer;

/**
 * Classe che rappresenta un TamaGolem nel contesto della battaglia.
 * Ogni TamaGolem ha una quantità di vita e una lista di pietre associate.
 */
public class TamaGolem {
    private int vita;
    private Deque<PietreElementi> listaPietre = new ArrayDeque<>();

    /**
     * Costruttore della classe TamaGolem.
     * Inizializza un TamaGolem con una quantità di vita specificata e una lista di pietre vuota.
     *
     * @param vita la quantità di vita iniziale del TamaGolem.
     */
    public TamaGolem(int vita) {
        this.listaPietre = new ArrayDeque<>();
        this.vita = vita;
    }

    /**
     * Restituisce la quantità di vita del TamaGolem.
     *
     * @return la quantità di vita del TamaGolem.
     */
    public int getVita() {
        return vita;
    }


    public void setVita(int vita) {
        this.vita = vita;
    }

    /**
     * Restituisce la lista di pietre associate al TamaGolem.
     *
     * @return una deque contenente le pietre associate al TamaGolem.
     */
    public Deque<PietreElementi> getListaPietre() {
        return listaPietre;
    }

    public void lanciaPietre(){
        if (!(listaPietre.isEmpty())){
            PietreElementi pietra = listaPietre.poll();
            System.out.println("Hai lanciato la pietra: " + pietra.getNome());
        }
    }

}