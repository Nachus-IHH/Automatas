package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase que gestiona la lectura carácter por carácter de un archivo fuente.
 * Mantiene información de línea y columna, y permite mirar el siguiente carácter
 * sin avanzar (peek) o retroceder un carácter (unread).
 */
public class SourceReader implements AutoCloseable {
    private BufferedReader reader;
    private String currentLine;
    private int lineNumber;
    private int columnNumber;
    private boolean eofReached;

    public SourceReader(String filePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(filePath));
        this.lineNumber = 0;
        this.columnNumber = 0;
        this.eofReached = false;
        readNextLine();
    }

    /**
     * Devuelve el siguiente carácter y avanza el puntero.
     * Si llega al final de la línea, avanza a la siguiente.
     */
    public char nextChar() throws IOException {
        if (eofReached) return (char) -1;

        if (currentLine == null || columnNumber >= currentLine.length()) {
            if (!readNextLine()) {
                eofReached = true;
                return (char) -1;   // Fin del archivo
            }
        }

        char c = currentLine.charAt(columnNumber++);
        return c;
    }

    /**
     * Devuelve el siguiente carácter sin avanzar el puntero.
     */
    public char peekChar() throws IOException {
        if (eofReached) return (char) -1;

        if (currentLine == null || columnNumber >= currentLine.length()) {
            reader.mark(1);
            if (!readNextLine()) {
                eofReached = true;
                return (char) -1;
            }
            reader.reset();
        }

        return currentLine.charAt(columnNumber);
    }

    /**
     * Retrocede un carácter (siempre que no sea inicio de archivo).
     */
    public void unreadChar() {
        if (columnNumber > 0) {
            columnNumber--;
        }
    }

    /**
     * Indica si quedan más caracteres por leer.
     */
    public boolean hasNext() throws IOException {
        if (eofReached) return false;
        if (currentLine == null) return false;
        return !(columnNumber >= currentLine.length() && !reader.ready());
    }

    /**
     * Lee la siguiente línea completa del archivo.
     * @return true si se leyó correctamente, false si fue fin de archivo.
     */
    public boolean readNextLine() throws IOException {
        currentLine = reader.readLine();
        if (currentLine != null) {
            currentLine += "\n"; // mantener salto de línea
            lineNumber++;
            columnNumber = 0;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtiene el número de línea actual.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Obtiene el número de columna actual.
     */
    public int getColumnNumber() {
        return columnNumber + 1;
    }

    /**
     * Cierra el archivo.
     */
    public void close() throws IOException {
        reader.close();
    }

    /**
     * Indica si se ha llegado al final del archivo.
     */
    public boolean isEOF() {
        return eofReached;
    }

    /**
     * Devuelve todo el archivo como un String completo
     * @return String archivo convertido en String
     * @throws IOException
     */
    public String readAll() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = nextChar()) != -1) {
            sb.append((char) c);
        }
        return sb.toString();
    }
}
