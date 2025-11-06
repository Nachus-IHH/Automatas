package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LectorTXT {

    public static String fromTXTToString(String filePath) {
        Path path = Paths.get(filePath);
        String allContent = "";

        try {
            allContent = Files.readString(path);
        } 
        catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
        return allContent;
    }

    public static List<String> fromTXTToLines(String filePath) {
        Path path = Paths.get(filePath);
        List<String> allLines = new ArrayList<>();

        try {
            allLines = Files.readAllLines(path);
        } 
        catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            e.printStackTrace();
        }
        return allLines;
    }
    
    
}
