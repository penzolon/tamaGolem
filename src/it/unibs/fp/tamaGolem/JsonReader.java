package it.unibs.fp.tamaGolem;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
    private final List<File> jsonFiles;

    /**
     * Scansiona la directory passata e costruisce la lista di file JSON validi.
     * Non usa più un file di default: considera solo i file presenti nella directory.
     */
    public JsonReader(String directoryPath) {
        jsonFiles = new ArrayList<>();

        // Verifica directory
        File directory = Path.of(directoryPath).toFile();
        File[] allFiles = directory.listFiles();
        if (allFiles == null) {
            System.err.println("[JsonReader] Directory non valida o non accessibile: " + directoryPath);
            throw new IllegalStateException("Directory non valida: " + directoryPath);
        }

        for (File file : allFiles) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                try (FileReader reader = new FileReader(file)) {
                    JsonElement element = JsonParser.parseReader(reader);
                    if (element.isJsonArray()) {
                        JsonArray array = element.getAsJsonArray();
                        if (array.size() >= Costanti.MIN_ELEMENTI) {
                            boolean tuttoStringhe = true;
                            for (JsonElement e : array) {
                                if (!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString()) {
                                    tuttoStringhe = false;
                                    break;
                                }
                            }
                            if (tuttoStringhe) {
                                jsonFiles.add(file);
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.err.println("[JsonReader] Errore parsing file: " + file.getName());
                    ex.printStackTrace();
                }
            }
        }

        if (jsonFiles.isEmpty()) {
            throw new IllegalStateException("Nessun file JSON valido trovato in " + directoryPath);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonFiles.size(); i++) {
            sb.append(i + 1)
              .append(". ")
              .append(jsonFiles.get(i).getName())
              .append("\n");
        }
        return sb.toString();
    }

    public File getFile(int index) {
        if (index < 0 || index >= jsonFiles.size()) {
            throw new IndexOutOfBoundsException("Indice file JSON non valido: " + index);
        }
        return jsonFiles.get(index);
    }

    public int getSize() {
        return jsonFiles.size();
    }

    /**
     * Legge il file JSON e restituisce l'array di elementi (String[]).
     */
    public static String[] parseFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            return new Gson().fromJson(reader, String[].class);
        } catch (IOException ex) {
            System.err.println("[JsonReader] Errore nella lettura del file: " + file.getName());
            ex.printStackTrace();
            return new String[0];
        }
    }
}
