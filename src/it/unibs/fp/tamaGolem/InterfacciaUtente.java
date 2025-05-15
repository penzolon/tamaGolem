package it.unibs.fp.tamaGolem;

import it.unibs.fp.myutil.inputOutput.InputData;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;
import it.unibs.fp.tamaGolem.Setup.JsonReader;

import java.io.File;

public class InterfacciaUtente {

	public static int scegliFileElementi(File file, JsonReader jsonReader, int numFile) {
        if(file != null && jsonReader.getSize() > 1) {
            if(InputData.readYesOrNo(CostantiString.INSERIRE_UN_NUOVO_FILE_DI_NUMERO_DI_ELEMENTI)) {
                numFile = getFile(jsonReader);
            }
        } else {
            if (jsonReader.getSize() == 1) {
                System.out.println(CostantiString.VERRÀ_UTILIZZATO_IL_FILE_DI_DEFAULT) ;
            } else {
                numFile = getFile(jsonReader);
            }
        }
        return numFile;
    }

    public static int inserisciNumeroElementi(int numElementi) {
        if(numElementi != 0) {
            if(InputData.readYesOrNo(CostantiString.VUOI_CAMBIARE_IL_NUMERO_DI_ELEMENTI)) {
                numElementi = getNumElementi();
            }
        } else {
            System.out.println(CostantiString.BENVENUTO_IN_TAMA_GOLEM) ;
            numElementi = getNumElementi() ;
        }
        return numElementi;
    }

    private static int getFile(JsonReader jsonReader) {
        int numFile;
        System.out.println(CostantiString.SCELTA_SET_DI_ELEMENTI);
        numFile = InputData.readIntegerBetween(CostantiString.INSERISCI_IL_NUMERO_DEL_FILE_DA_USARE+ jsonReader.toString(), 1, jsonReader.getSize()) ;
        return numFile;
    }

    private static int getNumElementi() {
            int numElementi;
            numElementi = InputData.readIntegerBetween(CostantiString.INSERISCI_IL_NUMERO_DI_ELEMENTI_DA_CONSIDERARE, CostantiPartita.MIN_ELEMENTI, CostantiPartita.MAX_ELEMENTI) ;
            return numElementi;
        }
}
