package it.unibs.fp.tamaGolem.Costanti;
public class CostantiString {
    public static final String ELEMENTI_PATH = "config/elementi" ;
    public static final String ELEMENTI_DEFAULT = "src/file/cosmicHorror.json" ;

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

    public static final String MESSAGGIO_GOLEM_PRONTI = "I Golem sono pronti a scagliare le pietre!";
    public static final String MESSAGGIO_PREMI_ENTER = "Premi enter per continuare...\n";
    public static final String DOMANDA_VISUALIZZA_EQUILIBRIO = "Vuoi visualizzare l'equilibrio tra gli elementi";
    public static final String MESSAGGIO_EQUILIBRIO = "Equilibrio tra gli elementi: \n";
    public static final String DOMANDA_GIOCARE_DI_NUOVO = "Vuoi giocare di nuovo";
    public static final String MESSAGGIO_LISTE_IDENTICHE = "La liste delle pietre dei due TamaGolem sono identiche!";
    public static final String MESSAGGIO_REINSERISCI_PIETRE = "Giocatore %d: Reinserisci le pietre!\n\n";
    public static final String MESSAGGIO_PIETRA_LANCIATA = "Il golem del giocatore %d ha lanciato la pietra: %s\n";
    public static final String MESSAGGIO_NESSUN_DANNO = "Nessun danno inflitto, i due tamaGolem hanno scagliato due pietre dello stesso elemento!\n";
    public static final String MESSAGGIO_DANNI_FATALI = "Il TamaGolem del giocatore %d ha subito danni fatali!(%d)\n";
    public static final String MESSAGGIO_ELIMINATO = "Il tamaGolem del giocatore %d è stato eliminato!\n";
    public static final String MESSAGGIO_PARTITA_PERSA = "Il giocatore %d non può più invocare TamaGolem e ha perso la partita!\n";
    public static final String MESSAGGIO_DANNI_SUBITI = "Il TamaGolem del giocatore %d ha subito danni pari a: %d\n";
    public static final String ERRORE_INTERAZIONE_NON_TROVATA = "Interazione non trovata tra %s e %s";
    public static final String MESSAGGIO_GIOCATORE_1_SCELTO = "\nÉ stato scelto il giocatore 1!";
    public static final String MESSAGGIO_GIOCATORE_2_SCELTO = "\nÉ stato scelto il giocatore 2!";
    public static final String MESSAGGIO_MAX_TAMAGOLEM = "Hai già invocato il numero massimo di TamaGolem.";
    public static final String MESSAGGIO_NON_PUOI_INVOCARE = "Non puoi invocare un altro TamaGolem.";
    public static final String MESSAGGIO_LISTA_PIETRE = "[ %s ]\n";
    public static final String VIRGOLA = ", ";
    public static final String MESSAGGIO_INVOCA_ALTRO_GOLEM = "Giocatore %d: Invoca un altro tamaGolem.";
    public static final String MESSAGGIO_GOLEM_RIMASTI = "Tamagolem rimasti: %d";
    public static final String MESSAGGIO_TURNO_GIOCATORE = "Turno del Giocatore %d\n";
    public static final String MESSAGGIO_SCEGLI_PIETRE = "Scegli le pietre da dare in pasto al tuo tamaGolem: ";
    public static final String MESSAGGIO_PIETRE_DISPONIBILI = "\nPietre disponibili:";
    public static final String MESSAGGIO_SELEZIONA_PIETRE = "Seleziona %d pietre per il tuo TamaGolem: \n";
    public static final String MESSAGGIO_PIETRA_NON_DISPONIBILE = "Pietra non disponibile, seleziona un'altra pietra.";
    public static final String MESSAGGIO_PIETRA_SELEZIONATA = "Pietra selezionata: %s";
    public static final String PROMPT_SELEZIONE_PIETRA = "-> ";
    public static final String ERRORE_NUMERO_ELEMENTI = "Numero di elementi richiesto superiore a quelli definiti nel file.";

    public static final String PUNTO = ". ";
    public static final String PARENTESI_APERTA = " (";
    public static final String DISPONIBILI = " disponibili)";
    public static final String PERCENTO_N = "%n";
    public static final String HAI_LANCIATO_LA_PIETRA = "Hai lanciato la pietra: ";
    public static final String JSON_EXTENSION = ".json";
    public static final String ERROR_INVALID_DIRECTORY = "[JsonReader] Directory non valida o non accessibile: ";
    public static final String ERROR_PARSING_FILE = "[JsonReader] Errore parsing file: ";
    public static final String ERROR_NO_VALID_FILES = "Nessun file JSON valido trovato in ";
    public static final String ERROR_INVALID_INDEX = "Indice file JSON non valido: ";
}