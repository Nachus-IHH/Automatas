package lexicon;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import core.DFA;
import io.SourceReader;

/**
 * Lee un archivo caracter por caracter, elimina espacios en blanco y
 * comentarios (UNA_LINEA, MULTILINEA)
 * Usar maquina de estados para optimizar (enum con swith - NORMAL, ONELINE,
 * MULTILINE)
 * para la primera prueba se usaran espacios en blanco
 */
public class Lexical_v4 {
    private static final char SLASH = '/';
    private static final char ASTERISK = '*';
    private static final Set<Character> DELIMITERS = new HashSet<>(Arrays.asList(';', ' ', '.', '\n', '\r', '\t'));

    /**
     * Lee un archivo carácter por carácter y valida con un autómata.
     * 
     * @param filePath  Ruta del archivo fuente
     * @param automatas Lista de Automátas finitos (DFA, NFA, o NFAE)
     */
    public static String analyze(String filePath, DFA[] dfas, String[] typeTokens) {
        // Token (name, extends FA)
        StringBuilder currentLexeme = new StringBuilder();
        boolean isMultilineComment = false;
        String[] currentStates = new String[dfas.length];
        StringBuilder output = new StringBuilder("String\tToken\tLexeme\tisValid");
        for (int i = 0; i < currentStates.length; i++) {
            currentStates[i] = dfas[i].initialState;
        }

        try (SourceReader sr = new SourceReader(filePath)) {

            while (!sr.isEOF()) {
                char symbol = sr.nextChar();

                // Fin de archivo
                if (symbol == (char) -1)
                    break;

                // Quita comentarios
                if (symbol == SLASH) {
                    if (sr.peekChar() == SLASH) {
                        if (sr.readNextLine())
                            continue;
                        break;
                    }
                    if (sr.peekChar() == ASTERISK) {
                        symbol = sr.nextChar();
                        isMultilineComment = true;
                        continue;
                    }
                }
                if (isMultilineComment) {
                    if (symbol == ASTERISK && sr.peekChar() == SLASH) {
                        symbol = sr.nextChar();
                        isMultilineComment = false;
                    }
                    continue;
                }

                // Detectar delimitador - termino un lexeme
                if (DELIMITERS.contains(symbol)) {
                    if (currentLexeme.length() > 0) {
                        for (int i = 0; i < dfas.length; i++) {
                            if (currentStates[i] != null && dfas[i].isFinal(currentStates[i]) == true) {
                                output.append("\n" + currentLexeme
                                        + "\t" + typeTokens[i]
                                        + "\t" + currentLexeme
                                        + "\t" + true);
                                break;
                            }
                        }
                        // reinicio de lexeme
                        currentLexeme = new StringBuilder();
                    }

                    // reinicio de estados
                    for (int i = 0; i < currentStates.length; i++) {
                        currentStates[i] = dfas[i].initialState;
                    }
                    continue;
                }

                // Procesar el simbolo para cada automata
                for (int i = 0; i < dfas.length; i++) {
                    if (currentStates[i] != null) {
                        currentStates[i] = dfas[i].delta(currentStates[i], symbol);
                    }
                }
                currentLexeme.append(symbol);
            }
        } catch (Exception e) {
            System.err.println("Error en Lexical_v4: " + e.getMessage());
            e.printStackTrace();
        }
        return output.toString();
    }

}
/*
 * Manejo de errores
 * if (currentState == null) {
 * System.out.printf("Línea %d, Columna %d -> '%c'%n",
 * sr.getLineNumber(), sr.getColumnNumber(), symbol);
 * return false; // rompe el metodo
 * }
 */