package it.unibs.fp.tamaGolem.Battaglia;

import it.unibs.fp.tamaGolem.Equilibrio;

import java.util.ArrayList;

public class ScortaPietre {
    private ArrayList<PietreElementi> scortaPietre;

    public ScortaPietre() {
        this.scortaPietre = new ArrayList<>();
    }
    public ArrayList<PietreElementi> getScortaPietre() {
        return scortaPietre;
    }
    public ArrayList<PietreElementi> aggiungiPietre(int numPietrePerElemento, Equilibrio equilibrio) {
        for (int i = 0; i < numPietrePerElemento; i++) {
            for (String pietra : equilibrio.getElementi()) {
                scortaPietre.add(new PietreElementi(pietra));
            }
        }
        return scortaPietre;
    }
}
