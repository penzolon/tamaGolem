package it.unibs.fp.tamaGolem.Costanti;
public class CostantiString {
    public static final String ELEMENTI_PATH = "config/elementi" ;
    public static final String ELEMENTI_DEFAULT = "src/file/cosmicHorror.json" ;
    public static final int MIN_ELEMENTI = 3 ;
    public static final int MAX_ELEMENTI = 10 ;
    public static final int MASSIMO_POTENZA = 5;

    // Costanti per la formattazione della tabella
    public static final int LARGHEZZA_COLONNA = 12;
    public static final int MAX_TENTATIVI = 1000;
    
    // Costanti per i formati numerici
    public static final String FORMATO_NUMERO_ZERO = "%6d";
    public static final String FORMATO_NUMERO_NON_ZERO = "%+4d";
    
    // Costanti per i simboli
    public static final String SIMBOLO_ATTACCO = "▲ ";
    public static final String SIMBOLO_DIFESA = "▼ ";
    public static final String FRECCIA_GIU = "↓";
    public static final String FRECCIA_DESTRA = "→";
    public static final String DIF = "Dif";
    public static final String ATT = "Att";

    public static final int SOGLIA_DEBOLE = 4;
    
    // Costanti per i messaggi della legenda
    public static final String TITOLO_LEGENDA = " LEGENDA ";
    public static final String TESTO_ATTACCO_DEBOLE = "▲ 1-4: Attacco moderato";
    public static final String TESTO_ATTACCO_FORTE = "▲ 5: Attacco forte";
    public static final String TESTO_DIFESA_DEBOLE = "▼ -1/-4: Difesa moderata";
    public static final String TESTO_DIFESA_FORTE = "▼ -5: Difesa forte";
    
    // Costanti per l'interpretazione della matrice
    public static final String INTESTAZIONE_INTERPRETAZIONE = "Come interpretare la matrice:";
    public static final String RIGA_ELEMENTI_ATTACCANTI = "► Righe: Elementi attaccanti";
    public static final String COLONNA_ELEMENTI_DIFESA = "► Colonne: Elementi in difesa";
    
    // Costanti per i caratteri di bordo
    public static final String LINEA_ORIZZONTALE = "═";
    public static final String LINEA_VERTICALE = "─";
    
    public static final String ANGOLO_SUPERIORE_SINISTRO_LEGENDA = "┌";
    public static final String ANGOLO_SUPERIORE_DESTRO_LEGENDA = "┐";
    public static final String ANGOLO_INFERIORE_SINISTRO_LEGENDA = "└";
    public static final String ANGOLO_INFERIORE_DESTRO_LEGENDA = "┘";
    public static final String BORDO_VERTICALE_LEGENDA = "│";

    public static final String ANGOLO_SUPERIORE_SINISTRO_TABELLA = "╔";
    public static final String ANGOLO_SUPERIORE_DESTRO_TABELLA = "╗";
    public static final String ANGOLO_INFERIORE_SINISTRO_TABELLA = "╚";
    public static final String ANGOLO_INFERIORE_DESTRO_TABELLA = "╝";
    public static final String INCROCIO_SUPERIORE = "╦";
    public static final String INCROCIO_SINISTRO = "╠";
    public static final String INCROCIO_DESTRO = "╣";
    public static final String INCROCIO_INFERIORE = "╩";
    public static final String INCROCIO_CENTRALE = "╬";
    public static final String BORDO_VERTICALE_TABELLA = "║";
}