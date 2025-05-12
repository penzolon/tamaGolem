package it.unibs.fp.tamaGolem;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;

public class JsonReader {
    private final List<File> jsonFiles;

    public JsonReader(String directoryPath) {
        Path path = Path.of(directoryPath);
        File directory = new File(path.toUri());
        File defaultFile = new File(Costanti.ELEMENTI_DEFAULT);
        File[] allFiles = directory.listFiles();
        jsonFiles = new ArrayList<>();

        if (!(allFiles == null)) {
            for (File file : allFiles) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    try (FileReader reader = new FileReader(file)) {
                        JsonElement element = JsonParser.parseReader(reader);

                        if (element.isJsonArray()) {
                            JsonArray array = element.getAsJsonArray();

                            if (array.size() >= 10) {
                                boolean tuttoStringhe = true;

                                for (JsonElement elemento : array) {
                                    if (!elemento.isJsonPrimitive() || !elemento.getAsJsonPrimitive().isString()) {
                                        tuttoStringhe = false;
                                        break;
                                    }
                                }

                                if (tuttoStringhe) {
                                    jsonFiles.add(file);
                                }
                            }
                        }
                    } catch (IOException fileError) {}
                }
            }
        }
        jsonFiles.add(defaultFile);
    }

    @Override
    public String toString() {
        StringBuilder listaFile = new StringBuilder();

        for (int i = 0; i < jsonFiles.size(); i++) {
            listaFile.append(i+1).append(". ").append(jsonFiles.get(i).getName()).append("\n");
        }

        return listaFile.toString();
    }

    public File getFile(int index) {
        return jsonFiles.get(index) ;
    }

    public int getSize() {
        return jsonFiles.size() ;
    }

    public static String[] parseFile(File file) {
        String[] elementi ;

        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            elementi = gson.fromJson(reader, String[].class);
        } catch (IOException fileError) {
            System.err.println("Errore nella lettura del file") ;
            elementi = new String[0];
        }

        return elementi;
    }
}
