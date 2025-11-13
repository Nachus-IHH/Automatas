package lexicon;

import core.DFA;
import core.FA;
import io.SourceReader;

/**
 * Lee un archivo caracter por caracter, elimina espacios en blanco y comentarios (UNA_LINEA, MULTILINEA)
 * Usar maquina de estados para optimizar (enum con swith - NORMAL, ONELINE, MULTILINE)
 */
public class Lexical_v3 {
    private static StringBuilder currentLexeme = new StringBuilder();
    private static char SLASH = '/';
    private static char ASTERISK = '*';

    /**
     * Lee un archivo carácter por carácter y valida con un autómata.
     * @param filePath Ruta del archivo fuente
     * @param automata Automáta finito (DFA, NFA, o NFAE)
     */
    public static boolean analyze(String filePath, FA fa) {
        String currentState = fa.initialState;
        boolean isMultilineComment = false;
        try (SourceReader sr = new SourceReader(filePath)) {
            
            while (!sr.isEOF()) {
                char symbol = sr.nextChar();

                // Fin de archivo
                if (symbol == (char) -1) break;

                // si tiene caracteres en blanco los omite
                if (Character.isWhitespace(symbol)) continue;
                // si hay '//' pasa a la siguiente linea    (ONELINE_COMMENT)
                if (symbol == SLASH) {
                    if (sr.peekChar() == SLASH) {
                        if (sr.readNextLine()) continue;
                        break;
                    }
                    if (sr.peekChar() == ASTERISK) {
                        symbol = sr.nextChar();         //pasa a ASTERISK = '*'
                        isMultilineComment = true;
                        continue;
                    }
                }

                // si hay '/* */' pasa a la siguiente linea    (MULTILINE_COMMENT)
                if (isMultilineComment) {
                    if (symbol == ASTERISK && sr.peekChar() == SLASH) {
                        symbol = sr.nextChar();         //pasa a SLASH = '/'
                        isMultilineComment = false;
                    }
                    continue;
                }

                // Procesar el simbolo con el automata
                if (fa instanceof DFA dfa) {
                    currentState = dfa.delta(currentState, symbol);
                    if (currentState == null) {
                        System.out.printf("Línea %d, Columna %d -> '%c'%n",
                        sr.getLineNumber(), sr.getColumnNumber(), symbol);
                        return false;    // rompe el metodo
                    }
                }

                // Agregar al lexema actual
                currentLexeme.append(symbol);
                System.out.print(symbol);
            }
        } catch (Exception e) {
            System.err.println("Error en Lexical_v3: " + e.getMessage());
            e.printStackTrace();
        }
        if (fa instanceof DFA dfa) {
            return dfa.isFinal(currentState);
        }
        return false;
    }

}
