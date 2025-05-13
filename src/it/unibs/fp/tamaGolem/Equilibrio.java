package it.unibs.fp.tamaGolem;

import it.unibs.fp.myutil.inputOutput.AnsiColors;
import it.unibs.fp.myutil.inputOutput.RandomDraws;
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
        boolean equilibrato = false;
        int tentativiGlobali = 0;

        while (!equilibrato) {
            tentativiGlobali++;
            if (tentativiGlobali > CostantiString.MAX_TENTATIVI) {
                throw new IllegalStateException("Impossibile generare un equilibrio stabile dopo " + CostantiString.MAX_TENTATIVI + " tentativi globali.");
            }

           /**
            * Pulisce la mappa equilibrioMap, che è una mappa principale dove ogni elemento è
            * associato a un'altra mappa (contenente le sue interazioni con gli altri elementi).
            * Questo passaggio è essenziale per resettare le informazioni delle interazioni precedenti
            * prima di iniziare una nuova generazione di equilibrio.
            * Inizializza una nuova mappa vuota per ogni elemento in `elementi`.
            * Ogni elemento avrà una mappa associata che conterrà le interazioni con gli altri elementi.
            */
            
            equilibrioMap.clear();
            for (String el : elementi) {
                equilibrioMap.put(el, new HashMap<>());
            }
            
            /**
             * Viene dichiarata una variabile booleana tutteRigheOk e inizializzata a true.
			 * Questo serve per monitorare se tutte le righe di interazioni sono state generate correttamente.
			 * Se una riga non viene generata correttamente, la variabile verrà impostata su false e il ciclo
			 * principale (quello che genera tutte le interazioni) si interromperà.
			 * Inizia un ciclo for che itera su ogni elemento nella lista elementi.
			 * Per ogni elemento (elemI), il metodo cercherà di generare le sue interazioni con gli altri elementi.
			 * Per ogni elemI, viene recuperata la sua mappa di interazioni dalla equilibrioMap.
			 * La mappa associata a elemI conterrà i valori delle interazioni tra elemI e gli altri elementi.
             */

            boolean tutteRigheOk = true;
            for (String elemI : elementi) {
                Map<String, Integer> mappaI = equilibrioMap.get(elemI);
                int tentativiRiga = 0;

                /**
                 * Viene pulita la mappa di interazioni di elemI (l'elemento corrente).
				 * Questo è importante per assicurarsi che ogni volta che il ciclo tenta di generare 
				 * nuove interazioni, non ci siano dati residui da tentativi precedenti.
				 * Un ciclo che itera su tutti gli elementi nella lista elementi per generare le interazioni tra elemI.
				 * Evita che un elemento venga interagito con sé stesso. Le interazioni sono solo tra elementi differenti.
				 * Se la potenza risultante è 0, viene forzata a 1. Questo evita che l'interazione sia neutra (0)
				 * e garantisce che ci sia sempre un'influenza, sia positiva che negativa.
				 * assegna la potenza opposta (-potenza) per l'interazione inversa, tra elemJ e elemI.
				 * Questo garantisce che se elemI interagisce positivamente con elemJ, elemJ interagirà negativamente con elemI
                 */
                do {
                    mappaI.clear();
                    for (String elemJ : elementi) {
                        if (!elemI.equals(elemJ)) {
                            int potenza = RandomDraws.drawInteger(CostantiString.MASSIMO_POTENZA, CostantiString.MASSIMO_POTENZA);
                            if (potenza == 0) potenza = 1;
                            equilibrioMap.get(elemI).put(elemJ, potenza);
                            equilibrioMap.get(elemJ).put(elemI, -potenza);
                        }
                    }
                    tentativiRiga++;
                } while ((sommaInterazioni(elemI) != 0 || tutteNulle(elemI)) && tentativiRiga < CostantiString.MAX_TENTATIVI);

                if (tentativiRiga >= CostantiString.MAX_TENTATIVI) {
                    tutteRigheOk = false;
                    break;
                }
            }

            equilibrato = tutteRigheOk;
        }
    }

    private int sommaInterazioni(String elemento) {
        return equilibrioMap.get(elemento)
                            .values()
                            .stream()
                            .mapToInt(Integer::intValue)
                            .sum();
    }

    private boolean tutteNulle(String elemento) {
        return equilibrioMap.get(elemento)
                            .values()
                            .stream()
                            .allMatch(val -> val == 0);
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