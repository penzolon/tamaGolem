package it.unibs.fp.tamaGolem.Battaglia;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

/**
 * Classe che rappresenta un elemento di tipo Pietra, utilizzato nel contesto della battaglia dei TamaGolem.
 * Ogni pietra ha un nome che identifica l'elemento associato.
 */
public class PietreElementi {
    private String nome;

    /**
     * Costruttore della classe PietreElementi.
     *
     * @param nome il nome dell'elemento associato alla pietra.
     */
    public PietreElementi(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il nome dell'elemento associato alla pietra.
     *
     * @return il nome dell'elemento.
     */
    public String getNome() {
        return nome;
    }
}