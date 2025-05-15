package it.unibs.fp.tamaGolem.Setup;

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
    private static String[] allElementi;
    private final List<String> elementi;

    public Equilibrio(int numElementi, File file) {
        allElementi = caricaElementiDaFile(file);
        validaNumeroElementi(numElementi);
        equilibrioMap = new HashMap<>();
        elementi = estraiElementi(numElementi);
        generaEquilibrio();
    }

    private String[] caricaElementiDaFile(File file) {
        return JsonReader.parseFile(file);
    }

    private void validaNumeroElementi(int numElementi) {
        if (allElementi.length < numElementi) {
            throw new IllegalArgumentException(CostantiString.ERRORE_NUMERO_ELEMENTI);
        }
    }

    public List<String> getElementi() {
        return elementi;
    }

    public Map<String, Map<String, Integer>> getEquilibrioMap() {
        return equilibrioMap;
    }

    /**
     * Estrae un numero specificato di elementi unici da `allElementi` e li aggiunge alla lista `elementi`.
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
                interoEstratto = RandomDraws.drawInteger(CostantiPartita.ZERO, allElementi.length - CostantiPartita.UNO);
            } while (interiEstratti.contains(interoEstratto));
            interiEstratti.add(interoEstratto);
            elementi.add(allElementi[interoEstratto]);
        }
        return elementi;
    }

    /**
	 * Genera l'equilibrio tra gli elementi selezionati, creando una mappa che rappresenta
	 * le interazioni tra ogni coppia di elementi. L'equilibrio è generato in modo casuale,
	 * rispettando le seguenti condizioni:
	 * <ul>
	 *   <li>La somma delle interazioni per ogni elemento deve essere pari a zero.</li>
	 *   <li>Ogni elemento deve avere almeno un'interazione non nulla con un altro elemento.</li>
	 * </ul>
	 *
	 * <p>Il metodo esegue i seguenti passaggi:</p>
	 * <ol>
	 *   <li><b>Inizializzazione della mappa:</b> Per ogni elemento, crea una sotto-mappa
	 *       con interazioni inizializzate a zero per tutti gli altri elementi.</li>
	 *   <li><b>Generazione delle interazioni:</b> Per ogni elemento:
	 *       <ul>
	 *         <li>Genera valori casuali positivi o negativi per le interazioni con gli altri elementi.</li>
	 *         <li>Imposta i valori simmetrici nella mappa per garantire la coerenza
	 *             (ad esempio, se A infligge +3 a B, allora B infligge -3 ad A).</li>
	 *         <li>Ripete il processo fino a soddisfare le condizioni di equilibrio.</li>
	 *       </ul>
	 *   </li>
	 *   <li><b>Verifica dell'equilibrio:</b> Controlla che la somma delle interazioni per ogni elemento
	 *       sia pari a zero e che ogni elemento abbia almeno un'interazione non nulla.</li>
	 *   <li><b>Ripetizione in caso di errore:</b> Se le condizioni non sono soddisfatte dopo un numero massimo
	 *       di tentativi, rigenera l'intera mappa.</li>
	 * </ol>
	 */
    private void generaEquilibrio() {
        int tentativi;
        boolean equilibrato = false;
        int intEstratto;

        while (!equilibrato) {
            for (String elemento : elementi) {
                equilibrioMap.put(elemento, new HashMap<>());
                for (String altroElemento : elementi) {
                    equilibrioMap.get(elemento).put(altroElemento, CostantiPartita.ZERO);
                }
            }

            for (int i = 0; i < elementi.size(); i++) {
                String elementoI = elementi.get(i);
                for (tentativi = 0; tentativi < CostantiString.MAX_TENTATIVI &&
                        (sommaInterazioni(elementoI) != CostantiPartita.ZERO || intNullaElemDiversi(elementoI)); tentativi++) {

                    for (int j = i + 1; j < elementi.size(); j++) {
                        String elementoJ = elementi.get(j);
                        intEstratto = RandomDraws.drawInteger(CostantiPartita.UNO, CostantiPartita.MASSIMO_POTENZA);

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
                if(equilibrioMap.get(elemento).getOrDefault(elemI, CostantiPartita.ZERO) == CostantiPartita.ZERO) {
                    zeri = true;
                    break;
                }
            }
        }

        return zeri;
    }

    /**
     * Calcola l'interazione tra due elementi specificati.
     *
     * @param elemento1 il primo elemento.
     * @param elemento2 il secondo elemento.
     * @return il valore dell'interazione tra i due elementi.
     * @throws IllegalArgumentException se uno degli elementi non è presente o l'interazione non è definita.
     */
    public int calcolaInterazione(String elemento1, String elemento2) {
        if (!equilibrioMap.containsKey(elemento1)) {
            throw new IllegalArgumentException("Elemento non trovato: " + elemento1);
        }
        if (!equilibrioMap.containsKey(elemento2)) {
            throw new IllegalArgumentException("Elemento non trovato: " + elemento2);
        }

        Integer interazione = equilibrioMap.get(elemento1).get(elemento2);
        if (interazione == null) {
            throw new IllegalArgumentException("Interazione non definita tra " + elemento1 + " e " + elemento2);
        }

        return interazione;
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

        tabella.append(CostantiString.A_CAPO).append(CostantiString.ANGOLO_SUPERIORE_SINISTRO_LEGENDA).append(lineaLegenda)
              .append(CostantiString.TITOLO_LEGENDA)
              .append(lineaLegenda)
              .append(CostantiString.ANGOLO_SUPERIORE_DESTRO_LEGENDA)
              .append(CostantiString.A_CAPO);

        for (String[] item : legendaItems) {
            tabella.append(CostantiString.BORDO_VERTICALE_LEGENDA).append(CostantiString.SPAZIO)
                  .append(item[0])
                  .append(centra((int)(CostantiString.LARGHEZZA_COLONNA * 2.5), item[1]))
                  .append(AnsiColors.RESET)
                  .append(CostantiString.BORDO_VERTICALE_LEGENDA)
                  .append(CostantiString.A_CAPO);
        }

        tabella.append(CostantiString.ANGOLO_INFERIORE_SINISTRO_LEGENDA)
              .append(CostantiString.LINEA_VERTICALE.repeat(31))
              .append(CostantiString.ANGOLO_INFERIORE_DESTRO_LEGENDA)
              .append("\n\n")
              .append(CostantiString.INTESTAZIONE_INTERPRETAZIONE).append(CostantiString.A_CAPO)
              .append(CostantiString.RIGA_ELEMENTI_ATTACCANTI).append(CostantiString.A_CAPO)
              .append(CostantiString.COLONNA_ELEMENTI_DIFESA).append(CostantiString.A_CAPO + CostantiString.A_CAPO);

        tabella.append(CostantiString.ANGOLO_SUPERIORE_SINISTRO_TABELLA).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(CostantiString.INCROCIO_SUPERIORE).append(lineaOrizzontale);
        }
        tabella.append(CostantiString.ANGOLO_SUPERIORE_DESTRO_TABELLA).append(CostantiString.A_CAPO);

        tabella.append(CostantiString.BORDO_VERTICALE_TABELLA)
              .append(CostantiString.SPAZIO.repeat(4))
              .append(CostantiString.FRECCIA_GIU)
              .append(CostantiString.DIF)
              .append(CostantiString.SPAZIO.repeat(4));
        for (String elemento : elementi) {
            tabella.append(CostantiString.BORDO_VERTICALE_TABELLA)
                  .append(centra(CostantiString.LARGHEZZA_COLONNA, elemento));
        }
        tabella.append(CostantiString.BORDO_VERTICALE_TABELLA).append(CostantiString.A_CAPO);

        tabella.append(CostantiString.INCROCIO_SINISTRO).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(CostantiString.INCROCIO_CENTRALE).append(lineaOrizzontale);
        }
        tabella.append(CostantiString.INCROCIO_DESTRO).append(CostantiString.A_CAPO);

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
                      .append(CostantiString.SPAZIO.repeat(6));
            }
            tabella.append(CostantiString.BORDO_VERTICALE_TABELLA).append(CostantiString.A_CAPO);

            if (i < elementi.size() - 1) {
                tabella.append(CostantiString.INCROCIO_SINISTRO).append(lineaSeparatrice);
                for (int k = 0; k < elementi.size(); k++) {
                    tabella.append(CostantiString.INCROCIO_CENTRALE).append(lineaOrizzontale);
                }
                tabella.append(CostantiString.INCROCIO_DESTRO).append(CostantiString.A_CAPO);
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
            nome = nome.substring(0, larghezza - 3) + CostantiString.BARRA;
        }
        
        int spaziTotali = larghezza - nome.length();
        int spaziPrima = spaziTotali / 2;
        int spaziDopo = spaziTotali - spaziPrima;
        
        return CostantiString.SPAZIO.repeat(spaziPrima) + nome + CostantiString.SPAZIO.repeat(spaziDopo);
    }
}