package it.unibs.fp.tamaGolem;

import it.unibs.fp.myutil.inputOutput.AnsiColors;
import it.unibs.fp.myutil.inputOutput.RandomDraws;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Equilibrio {
    private final Map<String, Map<String, Integer>> equilibrioMap;
    private static String[] allElementi;
    private final List<String> elementi;

    /**
     * Costruttore che riceve il numero di elementi e il file JSON di definizione.
     * Non utilizza alcun file di default.
     */
    public Equilibrio(int numElementi, File file) {
        allElementi = JsonReader.parseFile(file);
        if (allElementi.length < numElementi) {
            throw new IllegalArgumentException("Numero di elementi richiesto superiore a quelli definiti nel file.");
        }

        equilibrioMap = new HashMap<>();
        elementi = new ArrayList<>();
        estraiElementi(numElementi);
        generaEquilibrio();
    }

    private void estraiElementi(int numElementi) {
        List<Integer> interiEstratti = new ArrayList<>();
        for (int i = 0; i < numElementi; i++) {
            int interoEstratto;
            do {
                interoEstratto = RandomDraws.drawInteger(0, allElementi.length - 1);
            } while (interiEstratti.contains(interoEstratto));
            interiEstratti.add(interoEstratto);
            elementi.add(allElementi[interoEstratto]);
        }
    }

    private void generaEquilibrio() {
        boolean equilibrato = false;
        int tentativiGlobali = 0;

        while (!equilibrato) {
            tentativiGlobali++;
            if (tentativiGlobali > Costanti.MAX_TENTATIVI) {
                throw new IllegalStateException("Impossibile generare un equilibrio stabile dopo " + Costanti.MAX_TENTATIVI + " tentativi globali.");
            }

            equilibrioMap.clear();
            // Inizializza mappe vuote per ogni elemento
            for (String el : elementi) {
                equilibrioMap.put(el, new HashMap<>());
            }

            // Genera interazioni mantenendo antisimmetria e prova a bilanciare riga per riga
            boolean tutteRigheOk = true;
            for (String elemI : elementi) {
                Map<String, Integer> mappaI = equilibrioMap.get(elemI);
                int tentativiRiga = 0;

                do {
                    mappaI.clear();
                    for (String elemJ : elementi) {
                        if (!elemI.equals(elemJ)) {
                            int potenza = RandomDraws.drawInteger(-Costanti.MASSIMO_POTENZA, Costanti.MASSIMO_POTENZA);
                            if (potenza == 0) potenza = 1;
                            equilibrioMap.get(elemI).put(elemJ, potenza);
                            equilibrioMap.get(elemJ).put(elemI, -potenza);
                        }
                    }
                    tentativiRiga++;
                } while ((sommaInterazioni(elemI) != 0 || tutteNulle(elemI)) && tentativiRiga < Costanti.MAX_TENTATIVI);

                if (tentativiRiga >= Costanti.MAX_TENTATIVI) {
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
        String lineaSeparatrice = Costanti.LINEA_ORIZZONTALE.repeat(Costanti.LARGHEZZA_COLONNA);
        String lineaOrizzontale = Costanti.LINEA_ORIZZONTALE.repeat(Costanti.LARGHEZZA_COLONNA);
        final String lineaLegenda = Costanti.LINEA_VERTICALE.repeat(11);

        String[][] legendaItems = {
            {AnsiColors.YELLOW.toString(), Costanti.TESTO_ATTACCO_DEBOLE},
            {AnsiColors.RED.toString(), Costanti.TESTO_ATTACCO_FORTE},
            {AnsiColors.CYAN.toString(), Costanti.TESTO_DIFESA_DEBOLE},
            {AnsiColors.BLUE.toString(), Costanti.TESTO_DIFESA_FORTE}
        };

        tabella.append(Costanti.ANGOLO_SUPERIORE_SINISTRO_LEGENDA).append(lineaLegenda)
              .append(Costanti.TITOLO_LEGENDA)
              .append(lineaLegenda)
              .append(Costanti.ANGOLO_SUPERIORE_DESTRO_LEGENDA)
              .append("\n");

        for (String[] item : legendaItems) {
            tabella.append(Costanti.BORDO_VERTICALE_LEGENDA).append(" ")
                  .append(item[0])
                  .append(centra((int)(Costanti.LARGHEZZA_COLONNA * 2.5), item[1]))
                  .append(AnsiColors.RESET)
                  .append(Costanti.BORDO_VERTICALE_LEGENDA)
                  .append("\n");
        }

        tabella.append(Costanti.ANGOLO_INFERIORE_SINISTRO_LEGENDA)
              .append(Costanti.LINEA_VERTICALE.repeat(31))
              .append(Costanti.ANGOLO_INFERIORE_DESTRO_LEGENDA)
              .append("\n\n")
              .append(Costanti.INTESTAZIONE_INTERPRETAZIONE).append("\n")
              .append(Costanti.RIGA_ELEMENTI_ATTACCANTI).append("\n")
              .append(Costanti.COLONNA_ELEMENTI_DIFESA).append("\n\n");

        // Intestazione tabella
        tabella.append(Costanti.ANGOLO_SUPERIORE_SINISTRO_TABELLA).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(Costanti.INCROCIO_SUPERIORE).append(lineaOrizzontale);
        }
        tabella.append(Costanti.ANGOLO_SUPERIORE_DESTRO_TABELLA).append("\n");

        // Nomi colonne
        tabella.append(Costanti.BORDO_VERTICALE_TABELLA)
              .append(" ".repeat(4))
              .append(Costanti.FRECCIA_GIU)
              .append(Costanti.DIF)
              .append(" ".repeat(4));
        for (String elemento : elementi) {
            tabella.append(Costanti.BORDO_VERTICALE_TABELLA)
                  .append(centra(Costanti.LARGHEZZA_COLONNA, elemento));
        }
        tabella.append(Costanti.BORDO_VERTICALE_TABELLA).append("\n");

        // Riga separatrice
        tabella.append(Costanti.INCROCIO_SINISTRO).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(Costanti.INCROCIO_CENTRALE).append(lineaOrizzontale);
        }
        tabella.append(Costanti.INCROCIO_DESTRO).append("\n");

        // Corpo tabella
        for (int i = 0; i < elementi.size(); i++) {
            String attaccante = elementi.get(i);
            tabella.append(Costanti.BORDO_VERTICALE_TABELLA)
                  .append(Costanti.ATT)
                  .append(Costanti.FRECCIA_DESTRA)
                  .append(centra(Costanti.LARGHEZZA_COLONNA - 4, attaccante));

            for (int j = 0; j < elementi.size(); j++) {
                String difensore = elementi.get(j);
                int valore = attaccante.equals(difensore) ? 0 :
                             equilibrioMap.get(attaccante).getOrDefault(difensore, 0);
                tabella.append(Costanti.BORDO_VERTICALE_TABELLA)
                      .append(formattaNumero(valore))
                      .append(" ".repeat(6));
            }
            tabella.append(Costanti.BORDO_VERTICALE_TABELLA).append("\n");

            if (i < elementi.size() - 1) {
                tabella.append(Costanti.INCROCIO_SINISTRO).append(lineaSeparatrice);
                for (int k = 0; k < elementi.size(); k++) {
                    tabella.append(Costanti.INCROCIO_CENTRALE).append(lineaOrizzontale);
                }
                tabella.append(Costanti.INCROCIO_DESTRO).append("\n");
            }
        }

        // Chiusura tabella
        tabella.append(Costanti.ANGOLO_INFERIORE_SINISTRO_TABELLA).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(Costanti.INCROCIO_INFERIORE).append(lineaOrizzontale);
        }
        tabella.append(Costanti.ANGOLO_INFERIORE_DESTRO_TABELLA)
              .append("\n\n");

        return tabella.toString();
    }

    private String formattaNumero(int numero) {
        String formato;
        String risultato;
        String simbolo;
        
        if (numero == 0) {
            formato = Costanti.FORMATO_NUMERO_ZERO;
            risultato = String.format(formato, numero);
        } else {
            formato = Costanti.FORMATO_NUMERO_NON_ZERO;
            simbolo = numero > 0 ? Costanti.SIMBOLO_ATTACCO : Costanti.SIMBOLO_DIFESA;
            if (numero < 0) {
                if (numero >= -Costanti.SOGLIA_DEBOLE) {
                    risultato = AnsiColors.CYAN + simbolo + String.format(formato, numero) + AnsiColors.RESET;
                } else {
                    risultato = AnsiColors.BLUE + simbolo + String.format(formato, numero) + AnsiColors.RESET;
                }
            } else {
                if (numero <= Costanti.SOGLIA_DEBOLE) {
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