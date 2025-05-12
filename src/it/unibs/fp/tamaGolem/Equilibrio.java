package it.unibs.fp.tamaGolem;
import it.unibs.fp.myutil.inputOutput.AnsiColors;
import it.unibs.fp.myutil.inputOutput.RandomDraws;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class Equilibrio {
    private final int[][] equilibrio;
    private static String[] allElementi;
    private final List<String> elementi;

    public Equilibrio(int numElementi, File file) {
        allElementi = JsonReader.parseFile(file);
        equilibrio = new int[numElementi][numElementi];
        elementi = new ArrayList<>();
        estraiElementi(numElementi);
        generaEquilibrio();
    }

    public Equilibrio(int numElementi) {
        if(allElementi == null) {
            Path path = Path.of(Costanti.ELEMENTI_DEFAULT);
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(path.toFile())) {
                allElementi = gson.fromJson(reader, String[].class);
            } catch (IOException fileError) {
                System.err.println("Errore nella lettura del file di default\n") ;
                fileError.printStackTrace();
            }
        }
        equilibrio = new int[numElementi][numElementi];
        elementi = new ArrayList<>();
        estraiElementi(numElementi);
        generaEquilibrio();
    }

    private void estraiElementi(int numElementi) {
        List<Integer> interiEstratti = new ArrayList<>();
        int interoEstratto;
        for (int i = 0; i < numElementi; i++) {
            do {
                interoEstratto = RandomDraws.drawInteger(0, allElementi.length - 1);
            } while(interiEstratti.contains(interoEstratto));
            interiEstratti.add(interoEstratto);
            elementi.add(allElementi[interoEstratto]);
        }
    }

    private void generaEquilibrio() {
        int tentativi;
        boolean equilibrato = false;

        while(!equilibrato) {
            for (int[] riga: equilibrio) {
                Arrays.fill(riga, 0);
            }

            for(int i = 0; i < equilibrio.length; i++) {
                for(tentativi = 0; tentativi < Costanti.MAX_TENTATIVI && (sommaRiga(i) != 0 || rigaTuttaZero(i)); tentativi++) {
                    for(int j = i + 1; j < equilibrio.length; j++) {
                        equilibrio[i][j] = RandomDraws.drawInteger(-Costanti.MASSIMO_POTENZA, Costanti.MASSIMO_POTENZA);
                        equilibrio[j][i] = -equilibrio[i][j];
                    }
                }
                if(tentativi == Costanti.MAX_TENTATIVI) {
                    equilibrato = false;
                    break;
                } else {
                    equilibrato = true;
                }
            }
        }
    }

    private int sommaRiga(int riga) {
        int somma = 0;
        for (int i = 0; i < equilibrio.length; i++) {
            somma += equilibrio[riga][i];
        }
        return somma;
    }

    private boolean rigaTuttaZero(int riga) {
        for (int i = 0; i < equilibrio.length; i++) {
            if (equilibrio[riga][i] != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder tabella = new StringBuilder();
        String lineaSeparatrice = Costanti.LINEA_ORIZZONTALE.repeat(Costanti.LARGHEZZA_COLONNA);
        String lineaOrizzontale = Costanti.LINEA_ORIZZONTALE.repeat(Costanti.LARGHEZZA_COLONNA);
        final String lineaLegenda = Costanti.LINEA_VERTICALE.repeat(11);
        
        // Legenda decorata
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
      .append("\n").append("\n")
      .append(Costanti.INTESTAZIONE_INTERPRETAZIONE).append("\n")
      .append(Costanti.RIGA_ELEMENTI_ATTACCANTI).append("\n")
      .append(Costanti.COLONNA_ELEMENTI_DIFESA).append("\n\n");

        // Intestazione tabella
        tabella.append(Costanti.ANGOLO_SUPERIORE_SINISTRO_TABELLA).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(Costanti.INCROCIO_SUPERIORE).append(lineaOrizzontale);
        }
        tabella.append(Costanti.ANGOLO_SUPERIORE_DESTRO_TABELLA).append("\n");

        // Prima riga con i nomi delle colonne
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

        // Linea orizzontale dopo i nomi delle colonne
        tabella.append(Costanti.INCROCIO_SINISTRO).append(lineaSeparatrice);
        for (int k = 0; k < elementi.size(); k++) {
            tabella.append(Costanti.INCROCIO_CENTRALE).append(lineaOrizzontale);
        }
        tabella.append(Costanti.INCROCIO_DESTRO).append("\n");

        // Contenuto tabella
        for (int i = 0; i < elementi.size(); i++) {
            tabella.append(Costanti.BORDO_VERTICALE_TABELLA)
          .append(Costanti.ATT)
          .append(Costanti.FRECCIA_DESTRA)
          .append(centra(Costanti.LARGHEZZA_COLONNA - 4, elementi.get(i)));
        
        for (int j = 0; j < equilibrio[i].length; j++) {
            tabella.append(Costanti.BORDO_VERTICALE_TABELLA)
              .append(formattaNumero(equilibrio[i][j]))
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
      .append("\n")
      .append("\n");
    
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