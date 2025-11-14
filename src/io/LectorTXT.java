package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee un archivo .txt y lo regresa en String
 */
public class LectorTXT {

    /**
     * Convierte un archivo .txt a una cadena
     * @param filePath ruta del archivo
     * @return String contenido de todo el archivo
     */
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

    /**
     * Regresa en una lista de cadenas las lineas de un archivo .txt (cada linea del archivo es un elemento de la lista regresada)
     * @param filePath ruta del archivo
     * @return List<String> lista de cadenas obtenidas del archivo
     */
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
