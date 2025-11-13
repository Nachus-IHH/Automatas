package lexicon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import core.DFA;
import core.FA;
import core.NFA;
import io.SourceReader;

/**
 * Maneja tabla de simbolos
 */
public class Lexical_v5 {
    private static final char SLASH = '/';
    private static final char ASTERISK = '*';
    private static final Set<Character> DELIMITERS = new HashSet<>(Arrays.asList(';', ' ', '.', '\n', '\r', '\t'));

    /**
     * Lee un archivo carácter por carácter y valida con un autómata.
     * 
     * @param filePath  Ruta del archivo fuente
     * @param automatas Lista de Automátas finitos (DFA, NFA, o NFAE)
     */
    public static String analyze(String filePath, FA[] automatas, String[] typeTokens) {
        // Token (name, extends FA)
        StringBuilder currentLexeme = new StringBuilder();
        // boolean isMultilineComment = false;
        ArrayList<HashSet<String>> currentStates = new ArrayList<>(automatas.length);

        StringBuilder output = new StringBuilder("String\tToken\tLexeme\tisValid\tType of FA");
        initInitialStates(automatas, currentStates);

        try (SourceReader sr = new SourceReader(filePath)) {

            while (!sr.isEOF()) {
                char symbol = sr.nextChar();

                // Fin de archivo
                if (symbol == (char) -1)
                    break;

                // Quita comentarios
                if (symbol == SLASH) {
                    if (sr.peekChar() == SLASH) {
                        if (sr.readNextLine()) {
                            continue;
                        }
                        break;
                    }
                    if (sr.peekChar() == ASTERISK) {
                        try {
                            ignoreMultilineComment(sr);    
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        continue;
                    }
                }

                // Detectar delimitador - termino un lexeme
                if (DELIMITERS.contains(symbol)) {
                    if (currentLexeme.length() > 0) {
                        boolean isFinal = false;
                        for (int i = 0; i < automatas.length; i++) {

                            try {
                                for (String state : currentStates.get(i)) {
                                    if (automatas[i].finalStates.contains(state)) {
                                        isFinal = true;
                                        break;
                                    }
                                }
                            } catch (IndexOutOfBoundsException | NullPointerException e) {
                                continue;
                            }

                            if (isFinal) {
                                appendOutput(output, typeTokens[i], currentLexeme, true, automatas[i].getClass().getSimpleName());
                                break;
                            }
                        }

                        if (!isFinal) {
                            // System.out.println("The string <" + currentLexeme + "> does not belong to any language");
                            appendOutput(output, "ERROR", currentLexeme, false, "?");
                        }
                        currentLexeme = new StringBuilder();
                    }

                    // Reiniciar los estados de TODOS los autómatas
                    currentStates.clear();
                    initInitialStates(automatas, currentStates);
                    continue;
                }

                // Procesar el simbolo para cada automata
                for (int i = 0; i < automatas.length; i++) {
                    HashSet<String> currentSet = currentStates.get(i);

                    if (currentSet.isEmpty()) {
                        continue;
                    }

                    HashSet<String> nextStateSet = new HashSet<>();

                    if (automatas[i] instanceof DFA dfa) {
                        String currentState = currentSet.iterator().next();
                        String nextState = dfa.delta(currentState, symbol);
                        if (nextState != null) {
                            nextStateSet.add(nextState);
                        }
                    } else if (automatas[i] instanceof NFA nfa) {
                        nextStateSet = nfa.deltaSet(currentSet, symbol);

                    }
                    currentStates.set(i, nextStateSet);
                }
                currentLexeme.append(symbol);
            }
        } catch (Exception e) {
            System.err.println("Error en Lexical_v5: " + e.getMessage());
            e.printStackTrace();
        }

        return output.toString();
    }

    /**
     * Inicializa currentStates con el estado inicial para cualquier FA
     * 
     * @param automatas     array de FA
     * @param currentStates ArrayLis<HashSet<String>> lista de estados a inicializar
     */
    private static void initInitialStates(FA[] automatas, ArrayList<HashSet<String>> currentStates) {
        for (int i = 0; i < automatas.length; i++) {
            HashSet<String> initialStateSet = new HashSet<>();
            initialStateSet.add(automatas[i].initialState);
            currentStates.add(initialStateSet);
        }
    }

    private static void ignoreMultilineComment(SourceReader sr) throws IOException {
        try {
            if (sr.peekChar() == ASTERISK) {
                sr.nextChar();
            }
            
            char symbol = sr.nextChar();

            while (true) {
                if (symbol == (char) -1) {
                    System.out.println("No se cerro un comentario multilinea");
                    return;
                }

                // Busca el final del comentario '*/'
                if (symbol == ASTERISK && sr.peekChar() == SLASH) {
                    symbol = sr.nextChar();
                    return;
                }
                symbol = sr.nextChar();
            }
        } catch (IOException e) {
            System.err.println("Error en Lexical_v5 en comentario multilinea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void appendOutput(StringBuilder output, String token, StringBuilder lexeme, boolean isValid, String typeFA) {
        output.append("\n" + lexeme + "\t" + token + "\t" + lexeme + "\t" + isValid + "\t" + typeFA);
    }
}