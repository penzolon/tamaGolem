package it.unibs.fp.tamaGolem;

import it.unibs.fp.myutil.inputOutput.InputData;

import java.io.File;

public class InterfacciaUtente {
    public static void sceltaElementi() {
        int elementi = 0 ;
        int numFile = 1 ;
        File file = null ;
        Equilibrio equilibrio ;
        JsonReader jsonReader = new JsonReader(Costanti.ELEMENTI_PATH) ;

        do {
            if(elementi != 0) {
                if(InputData.readYesOrNo("Vuoi cambiare il numero di elementi")) {
                    elementi = InputData.readIntegerBetween("Inserisci il numero di elementi da considerare: ", Costanti.MIN_ELEMENTI, Costanti.MAX_ELEMENTI) ;
                }
            } else {
                System.out.println("Benvenuto in TamaGolem!") ;
                elementi = InputData.readIntegerBetween("Inserisci il numero di elementi da considerare: ", Costanti.MIN_ELEMENTI, Costanti.MAX_ELEMENTI) ;
            }
            if(file != null && jsonReader.getSize() > 1) {
                if(InputData.readYesOrNo("Inserire un nuovo file di elementi")) {
                    numFile = InputData.readIntegerBetween("Inserisci il numero del file da usare:\n\n"+jsonReader.toString(), 1, jsonReader.getSize()) ;
                }
            } else {
                if (jsonReader.getSize() == 1) {
                    System.out.println("Verrà utilizzato il file di default.\n") ;
                } else {
                    numFile = InputData.readIntegerBetween("Inserisci il numero del file da usare:\n\n"+jsonReader.toString(), 1, jsonReader.getSize()) ;
                }
            }
            file = jsonReader.getFile(numFile - 1) ;
            equilibrio = new Equilibrio(elementi, file) ;
//           System.out.println(equilibrio.toString());
        } while(InputData.readYesOrNo("Continuare")) ;
    }
}
