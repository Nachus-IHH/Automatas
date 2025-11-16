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
 * Incluye tabla de simbolos y si es un binario, se convierte a decimal
 * Contiene todo para recibir operaciones aritmeticas
 */
public class Lexical_v10_SAE {
    private static final Set<Character> SEPARATOR_ESPECIAL = new HashSet<>(Arrays.asList(';', '$'));
    private static final Set<Character> SEPARATOR = new HashSet<>(Arrays.asList(' ', '\n', '\r', '\t'));

    /**
     * Lee un archivo carácter por carácter y valida con distintos FAs.
     * @param filePath Ruta del archivo a leer
     * @param automatas array de automatas con los que se trabajara
     * @param nameTokens array de nombres de los tokens - ej. Op_arithmetic
     * @param typeTokens array de tipo de tokens -ej. number
     * @return
     */
    public static SymbolTableSummary analyze(String filePath, FA[] automatas, String[] nameTokens, String[] typeTokens) {
        StringBuilder currentLexeme = new StringBuilder();
        ArrayList<HashSet<String>> currentStates = new ArrayList<>();
        SymbolTableSummary symbolTable = new SymbolTableSummary();
        currentStates.addAll(AutomataProcessor.initialStates(automatas));

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

                // Ignora los espacios en blanco
                if (SEPARATOR.contains(symbol)) {
                    continue;
                }
                // Detectar delimitador - termino un lexeme
                if (SEPARATOR_ESPECIAL.contains(symbol)) {
                    if (currentLexeme.length() > 0) {
                        int index = AutomataProcessor.isLexemeAccepted(currentStates, automatas);
                        if (index != -1) {
                            symbolTable.addTokenOccurrence(nameTokens[index], currentLexeme.toString(), automatas[index].getClass().getSimpleName(), typeTokens[index],sr.getLineNumber(), sr.getColumnNumber()-3);
                        } else {
                            symbolTable.addTokenOccurrence("TOKEN_ERROR", currentLexeme.toString(), "?", "?", sr.getLineNumber(), sr.getColumnNumber()-3);
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
            System.err.println("ERROR FATAL (Lexical_v10_SAE): " + e.getMessage());
            System.err.printf("Analisis abortado en Linea %d, Columna %d.%n",
                    e.getLineNumber(), e.getColumnNumber());
            return symbolTable;
        } catch (IOException e) {
            System.err.println("Error de lectura de archivo (Lexical_v10_SAE)" + e.getMessage());
            e.printStackTrace();
            return symbolTable;
        }

        return symbolTable;
    }

}
