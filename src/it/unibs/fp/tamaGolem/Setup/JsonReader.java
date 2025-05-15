package it.unibs.fp.tamaGolem.Setup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import it.unibs.fp.tamaGolem.Costanti.CostantiPartita;
import it.unibs.fp.tamaGolem.Costanti.CostantiString;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {

    private final List<File> jsonFiles;

    public JsonReader(String directoryPath) {
        this.jsonFiles = new ArrayList<>();
        File directory = validateDirectory(directoryPath);
        loadValidJsonFiles(directory);
        if (jsonFiles.isEmpty()) {
            throw new IllegalStateException(CostantiString.ERROR_NO_VALID_FILES + directoryPath);
        }
    }

    private File validateDirectory(String directoryPath) {
        File directory = Path.of(directoryPath).toFile();
        if (!directory.isDirectory() || !directory.canRead()) {
            throw new IllegalStateException(CostantiString.ERROR_INVALID_DIRECTORY + directoryPath);
        }
        return directory;
    }

    private void loadValidJsonFiles(File directory) {
        File[] allFiles = directory.listFiles();
        if (allFiles == null) return;

        for (File file : allFiles) {
            if (isValidJsonFile(file)) {
                jsonFiles.add(file);
            }
        }
    }

    private boolean isValidJsonFile(File file) {
        if (!file.isFile() || !file.getName().endsWith(CostantiString.JSON_EXTENSION)) {
            return false;
        }
        try (FileReader reader = new FileReader(file)) {
            JsonElement element = JsonParser.parseReader(reader);
            return isValidJsonArray(element);
        } catch (IOException ex) {
            System.err.println(CostantiString.ERROR_PARSING_FILE + file.getName());
            ex.printStackTrace();
            return false;
        }
    }

    private boolean isValidJsonArray(JsonElement element) {
        if (!element.isJsonArray()) return false;

        JsonArray array = element.getAsJsonArray();
        if (array.size() < CostantiPartita.MIN_ELEMENTI) return false;

        for (JsonElement e : array) {
            if (!e.isJsonPrimitive() || !e.getAsJsonPrimitive().isString()) {
                return false;
            }
        }
        return true;
    }

    public File getFile(int index) {
        if (index < 0 || index >= jsonFiles.size()) {
            throw new IndexOutOfBoundsException(CostantiString.ERROR_INVALID_INDEX + index);
        }
        return jsonFiles.get(index);
    }

    public int getSize() {
        return jsonFiles.size();
    }

    public static String[] parseFile(File file) {
        try (FileReader reader = new FileReader(file)) {
            return new Gson().fromJson(reader, String[].class);
        } catch (IOException ex) {
            System.err.println(CostantiString.ERROR_PARSING_FILE + file.getName());
            ex.printStackTrace();
            return new String[0];
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonFiles.size(); i++) {
            sb.append(i + 1).append(CostantiString.PUNTO).append(jsonFiles.get(i).getName()).append("\n");
        }
        return sb.toString();
    }
}