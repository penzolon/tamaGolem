package it.unibs.fp.tamaGolem;

import it.unibs.fp.myutil.inputOutput.AnsiColors;
import it.unibs.fp.myutil.inputOutput.RandomDraws;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Equilibrio {
/**
 * Una mappa annidata: per ogni elemento A (String), contiene una sotto-mappa che associa
 * altri elementi B a un valore Integer, che rappresenta l’equilibrio tra A e B.
 */	
    private final Map<String, Map<String, Integer>> equilibrioMap;
//	Un array statico di tutti gli elementi disponibili. 
    private static String[] allElementi;
//	La lista di elementi selezionati che verranno usati nell’equilibrio.
    private final List<String> elementi;

    public Equilibrio(int numElementi, File file) {
        allElementi = JsonReader.parseFile(file);
        if (allElementi.length < numElementi) {
            throw new IllegalArgumentException("Numero di elementi richiesto superiore a quelli definiti nel file.");
        }
        equilibrioMap = new HashMap<>();
        elementi = estraiElementi(numElementi);
        generaEquilibrio();
    }

    public List<String> getElementi() {
        return elementi;
    }

    /**
     * Estrae un numero specificato di elementi unici da `allElementi` e li aggiunge alla lista `elementi`.
     * 
     * Il metodo esegue un'estrazione casuale di indici da `allElementi`, assicurandosi che ogni indice 
     * estratto sia unico, in modo da evitare duplicazioni. Gli indici estratti vengono memorizzati nella 
     * lista `interiEstratti`, mentre gli elementi corrispondenti agli indici estratti vengono aggiunti alla lista 
     * `elementi`. Questo processo garantisce che la lista `elementi` contenga esattamente `numElementi` 
     * elementi unici selezionati casualmente da `allElementi`.
     */
    
    private List<String> estraiElementi(int numElementi) {
        List<Integer> interiEstratti = new ArrayList<>();
        List<String> elementi = new ArrayList<>();
        for (int i = 0; i < numElementi; i++) {
            int interoEstratto;
            do {
                interoEstratto = RandomDraws.drawInteger(0, allElementi.length - 1);
            } while (interiEstratti.contains(interoEstratto));
            interiEstratti.add(interoEstratto);
            elementi.add(allElementi[interoEstratto]);
        }
        return elementi;
    }

    private void generaEquilibrio() {
        int tentativi;
        boolean equilibrato = false;
        int intEstratto;

        while (!equilibrato) {
            // Inizializza la mappa con mappe vuote per ogni elemento
            for (String elemento : elementi) {
                equilibrioMap.put(elemento, new HashMap<>());
                // Inizializza tutte le interazioni a 0
                for (String altroElemento : elementi) {
                    equilibrioMap.get(elemento).put(altroElemento, 0);
                }
            }

            for (int i = 0; i < elementi.size(); i++) {
                String elementoI = elementi.get(i);
                for (tentativi = 0; tentativi < CostantiString.MAX_TENTATIVI &&
                        (sommaInterazioni(elementoI) != 0 || intNullaElemDiversi(elementoI)); tentativi++) {

                    for (int j = i + 1; j < elementi.size(); j++) {
                        String elementoJ = elementi.get(j);
                        intEstratto = RandomDraws.drawInteger(1, CostantiString.MASSIMO_POTENZA);

                        // Assegna un valore casuale positivo o negativo
                        int valore = RandomDraws.drawBoolean() ? intEstratto : -intEstratto;

                        // Imposta i valori simmetrici nella mappa
                        equilibrioMap.get(elementoI).put(elementoJ, valore);
                        equilibrioMap.get(elementoJ).put(elementoI, -valore);
                    }
                }

                if (tentativi == CostantiString.MAX_TENTATIVI) {
                    equilibrato = false;
                    break;
                } else {
                    equilibrato = true;
                }
            }
        }
    }

    private int sommaInterazioni(String elemento) {
        return equilibrioMap.get(elemento)
                            .values()
                            .stream()
                            .mapToInt(Integer::intValue)
                            .sum();
    }

    private boolean intNullaElemDiversi(String elemento) {
        boolean zeri = false ;

        for(String elemI: elementi) {
            if(!elemI.equals(elemento)) {
                if(equilibrioMap.get(elemento).getOrDefault(elemI, 0) == 0) {
                    zeri = true;
                    break;
                }
            }
        }

        return zeri;
    }

    @Override
    public String toString() {
        StringBuilder tabella = new StringBuilder();
        String lineaSeparatrice = CostantiString.LINEA_ORIZZONTALE.repeat(CostantiString.LARGHEZZA_COLONNA);
        String lineaOrizzontale = CostantiString.LINEA_ORIZZONTALE.repeat(CostantiString.LARGHEZZA_COLONNA);
        final String lineaLegenda = CostantiString.LINEA_VERTICALE.repeat(11);

        String[][] legendaItems = {
            {AnsiColors.YELLOW.toString(), CostantiString.TESTO_ATTACCO_DEBOLE},
            {AnsiColors.RED.toString(), CostantiString.TESTO_ATTACCO_FORTE},
            {AnsiColors.CYAN.toString(), CostantiString.TESTO_DIFESA_DEBOLE},
            {AnsiColors.BLUE.toString(), CostantiString.TESTO_DIFESA_FORTE}
        };

        tabella.append(CostantiString.ANGOLO_SUPERIORE_SINISTRO_LEGENDA).append(lineaLegenda)
              .append(CostantiString.TITOLO_LEGENDA)
              .append(lineaLegenda)
              .append(CostantiString.ANGOLO_SUPERIORE_DESTRO_LEGENDA)
              .append("\n");

        for (String[] item : legendaItems) {
            tabella.append(CostantiString.BORDO_VERTICALE_LEGENDA).append(" ")
                  .append(item[0])
                  .append(centra((int)(CostantiString.LARGHEZZA_COLONNA * 2.5), item[1]))
                  .append(AnsiColors.RESET)
                  .append(CostantiString.BORDO_VERTICALE_LEGENDA)
                  .append("\n");
        }

        tabella.append(CostantiString.ANGOLO_INFERIORE_SINISTRO_LEGENDA)
              .append(CostantiString.LINEA_VERTICALE.repeat(31))
              .append(CostantiString.ANGOLO_INFERIORE_DESTRO_LEGENDA)
              .append("\n\n")
              .append(CostantiString.INTESTAZIONE_INTERPRETAZIONE).append("\n")
              .append(CostantiString.RIGA_ELEMENTI_ATTACCANTI).append("\n")
              .append(CostantiString.COLONNA_ELEMENTI_DIFESA).append("\n\n");

        tabella.append(CostantiString.ANGOLO_SUPERIORE_SINISTRO_TABELLA).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(CostantiString.INCROCIO_SUPERIORE).append(lineaOrizzontale);
        }
        tabella.append(CostantiString.ANGOLO_SUPERIORE_DESTRO_TABELLA).append("\n");

        tabella.append(CostantiString.BORDO_VERTICALE_TABELLA)
              .append(" ".repeat(4))
              .append(CostantiString.FRECCIA_GIU)
              .append(CostantiString.DIF)
              .append(" ".repeat(4));
        for (String elemento : elementi) {
            tabella.append(CostantiString.BORDO_VERTICALE_TABELLA)
                  .append(centra(CostantiString.LARGHEZZA_COLONNA, elemento));
        }
        tabella.append(CostantiString.BORDO_VERTICALE_TABELLA).append("\n");

        tabella.append(CostantiString.INCROCIO_SINISTRO).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(CostantiString.INCROCIO_CENTRALE).append(lineaOrizzontale);
        }
        tabella.append(CostantiString.INCROCIO_DESTRO).append("\n");

        for (int i = 0; i < elementi.size(); i++) {
            String attaccante = elementi.get(i);
            tabella.append(CostantiString.BORDO_VERTICALE_TABELLA)
                  .append(CostantiString.ATT)
                  .append(CostantiString.FRECCIA_DESTRA)
                  .append(centra(CostantiString.LARGHEZZA_COLONNA - 4, attaccante));

            for (int j = 0; j < elementi.size(); j++) {
                String difensore = elementi.get(j);
                int valore = attaccante.equals(difensore) ? 0 :
                             equilibrioMap.get(attaccante).getOrDefault(difensore, 0);
                tabella.append(CostantiString.BORDO_VERTICALE_TABELLA)
                      .append(formattaNumero(valore))
                      .append(" ".repeat(6));
            }
            tabella.append(CostantiString.BORDO_VERTICALE_TABELLA).append("\n");

            if (i < elementi.size() - 1) {
                tabella.append(CostantiString.INCROCIO_SINISTRO).append(lineaSeparatrice);
                for (int k = 0; k < elementi.size(); k++) {
                    tabella.append(CostantiString.INCROCIO_CENTRALE).append(lineaOrizzontale);
                }
                tabella.append(CostantiString.INCROCIO_DESTRO).append("\n");
            }
        }

        tabella.append(CostantiString.ANGOLO_INFERIORE_SINISTRO_TABELLA).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(CostantiString.INCROCIO_INFERIORE).append(lineaOrizzontale);
        }
        tabella.append(CostantiString.ANGOLO_INFERIORE_DESTRO_TABELLA)
              .append("\n\n");

        return tabella.toString();
    }

    private String formattaNumero(int numero) {
        String formato;
        String risultato;
        String simbolo;
        
        if (numero == 0) {
            formato = CostantiString.FORMATO_NUMERO_ZERO;
            risultato = String.format(formato, numero);
        } else {
            formato = CostantiString.FORMATO_NUMERO_NON_ZERO;
            simbolo = numero > 0 ? CostantiString.SIMBOLO_ATTACCO : CostantiString.SIMBOLO_DIFESA;
            if (numero < 0) {
                if (numero >= -CostantiString.SOGLIA_DEBOLE) {
                    risultato = AnsiColors.CYAN + simbolo + String.format(formato, numero) + AnsiColors.RESET;
                } else {
                    risultato = AnsiColors.BLUE + simbolo + String.format(formato, numero) + AnsiColors.RESET;
                }
            } else {
                if (numero <= CostantiString.SOGLIA_DEBOLE) {
                    risultato = AnsiColors.YELLOW + simbolo + String.format(formato, numero) + AnsiColors.RESET;
                } else {
                    risultato = AnsiColors.RED + simbolo + String.format(formato, numero) + AnsiColors.RESET;
                }
            }
        }
        
        return risultato;
    }

    private String centra(int larghezza, String nome) {
        if (nome.length() > larghezza - 2) {
            nome = nome.substring(0, larghezza - 3) + "-";
        }
        
        int spaziTotali = larghezza - nome.length();
        int spaziPrima = spaziTotali / 2;
        int spaziDopo = spaziTotali - spaziPrima;
        
        return " ".repeat(spaziPrima) + nome + " ".repeat(spaziDopo);
    }
}