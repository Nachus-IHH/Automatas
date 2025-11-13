package lexicon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import core.FA;
import exceptions.UnclosedCommentException;
import io.SourceReader;

/**
 * Refactorizacion de Lexical_v5
 */
public class Lexical_v6 {
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
        ArrayList<HashSet<String>> currentStates = new ArrayList<>();
        currentStates.addAll(AutomataProcessor.initialStates(automatas));

        StringBuilder output = new StringBuilder("String\tToken\tLexeme\tisValid\tType of FA");

        try (SourceReader sr = new SourceReader(filePath)) {

            while (!sr.isEOF()) {

                if (sr.isEOF()) {
                    break;
                }

                char symbol = sr.nextChar();
                // Quita comentarios
                if (CommentHandler.beginComment(sr, symbol)) {
                    char peek = sr.peekChar();
                    if (peek == CommentHandler.SLASH) {
                        CommentHandler.skipSingleLineComment(sr);
                    } else if (peek == CommentHandler.ASTERISK) {
                        CommentHandler.skipMultiLineComment(sr); // Se permite la propagacion del Error
                    }
                    continue;
                }

                // Detectar delimitador - termino un lexeme
                if (DELIMITERS.contains(symbol)) {
                    if (currentLexeme.length() > 0) {
                        int index = AutomataProcessor.isLexemeAccepted(currentStates, automatas);
                        if (index != -1) {
                            appendOutput(output, typeTokens[index], currentLexeme, true,
                                    automatas[index].getClass().getSimpleName());
                        } else {
                            // System.out.println("The string <" + currentLexeme + "> does not belong to any
                            // language");
                            appendOutput(output, "ERROR", currentLexeme, false, "?");
                        }
                        currentLexeme = new StringBuilder();
                    }

                    // Reiniciar los estados de TODOS los autómatas
                    AutomataProcessor.resetToInitialStates(automatas, currentStates);
                    continue;
                }

                // Procesar el simbolo para cada automata
                AutomataProcessor.processSymbol(automatas, currentStates, symbol);
                currentLexeme.append(symbol);
            }
        } catch (UnclosedCommentException e) {
            System.err.println("ERROR FATAL (Lexical_v6): " + e.getMessage());
            System.err.printf("Analisis abortado en Linea %d, Columna %d.%n",
                    e.getLineNumber(), e.getColumnNumber());
            return output.toString();
        } catch (IOException e) {
            System.err.println("Error de lectura de archivo (Lexical_v6)" + e.getMessage());
            e.printStackTrace();
            return output.toString();
        }
        return output.toString();
    }

    private static void appendOutput(StringBuilder output, String token, StringBuilder lexeme, boolean isValid,
            String typeFA) {
        output.append("\n" + lexeme + "\t" + token + "\t" + lexeme + "\t" + isValid + "\t" + typeFA);
    }
}