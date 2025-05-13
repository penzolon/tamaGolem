package it.unibs.fp.tamaGolem;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;

import java.io.File;

public class InterfacciaUtente {

    public static int scegliFileElementi(File file, JsonReader jsonReader, int numFile) {
        if(file != null && jsonReader.getSize() > 1) {
            if(InputData.readYesOrNo("Inserire un nuovo file di numero di elementi")) {
                numFile = getFile(jsonReader);
            }
        } else {
            if (jsonReader.getSize() == 1) {
                System.out.println("Verrà utilizzato il file di default.\n") ;
            } else {
                numFile = getFile(jsonReader);
            }
        }
        return numFile;
    }

    public static int inserisciNumeroElementi(int numElementi) {
        if(numElementi != 0) {
            if(InputData.readYesOrNo("Vuoi cambiare il numero di elementi")) {
                numElementi = getNumElementi();
            }
        } else {
            System.out.println("Benvenuto in TamaGolem!") ;
            numElementi = getNumElementi() ;
        }
        return numElementi;
    }

    private static int getFile(JsonReader jsonReader) {
        int numFile;
        numFile = InputData.readIntegerBetween("Inserisci il numero del file da usare:\n\n"+ jsonReader.toString(), 1, jsonReader.getSize()) ;
        return numFile;
    }

    private static int getNumElementi() {
            int numElementi;
            numElementi = InputData.readIntegerBetween("Inserisci il numero di elementi da considerare: ", CostantiString.MIN_ELEMENTI, CostantiString.MAX_ELEMENTI) ;
            return numElementi;
        }
}
