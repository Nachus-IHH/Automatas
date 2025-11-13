package lexicon;

import io.SourceReader;

import core.DFA;
import core.FA;

/**
 * Lee un archivo caracter por caracter, sin procesar nada
 */
public class Lexical_v1 {
    public static StringBuilder currentLexeme = new StringBuilder();

    /**
     * Lee un archivo carácter por carácter y valida con un autómata.
     * @param filePath Ruta del archivo fuente
     * @param automata Automáta finito (DFA, NFA, o NFAE)
     */
    public static boolean analyze(String filePath, FA fa) {
        String currentState = fa.initialState;
        try (SourceReader sr = new SourceReader(filePath)) {
            
            while (!sr.isEOF()) {
                char symbol = sr.nextChar();
                // Fin de archivo
                if (sr.peekChar() == (char) -1) break;

                // Validar si pertenece al alfabeto
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
            System.err.println("Error en Lexical_v1: " + e.getMessage());
            e.printStackTrace();
        }
        if (fa instanceof DFA dfa) {
            return dfa.isFinal(currentState);
        }
        return false;
    }

}
