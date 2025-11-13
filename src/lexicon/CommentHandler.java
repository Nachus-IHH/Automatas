package lexicon;

import java.io.IOException;

import exceptions.UnclosedCommentException;
import io.SourceReader;

/**
 * Administra la lógica para los comentarios de un archivo, regresa el cursor
 * hasta
 * el cierre del comentario
 */
public class CommentHandler {
    public static final char SLASH = '/';
    public static final char ASTERISK = '*';

    /**
     * Verifica si hay un comentario
     * 
     * @param sr     SourceReader que esta leyendo el archivo
     * @param symbol simbolo actual a verificar
     * @return boolean true es comentario, false es operador
     * @throws IOException
     */
    public static boolean beginComment(SourceReader sr, char symbol) throws IOException {
        if (symbol == SLASH) {
            char peek = sr.peekChar();
            if (peek == SLASH || peek == ASTERISK) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ignora el contenido que sigue de una linea
     * Deja posicionado al cursor al inicio de la siguiente linea tras el comentario
     * 
     * @param sr SourceReader que esta leyendo el archivo
     * @throws IOException Si ocurre un error de I/O durante la lectura
     */
    public static void skipSingleLineComment(SourceReader sr) throws IOException {
        sr.nextChar(); // avanza al siguiente simbolo '/'
        sr.readNextLine();
    }

    /**
     * Ignora el contenido de un comentario multilinea
     * 
     * @param sr SourceReader que esta leyendo el archivo
     * @return boolean true si cerro bien comentario, false si llego a EOF sin
     *         cierre
     * @throws IOException              Si ocurre un error de I/O durante la lectura
     * @throws UnclosedCommentException Si llega al EOF del archivo sin encontrar
     *                                  *./
     */
    public static void skipMultiLineComment(SourceReader sr) throws IOException {
        int startLine = sr.getLineNumber();
        int startCol = sr.getColumnNumber();
        sr.nextChar(); // avanza al siguiente simbolo '*'
        char symbol = sr.nextChar();

        while (!sr.isEOF()) {
            // Busca el final del comentario '*/'
            if (symbol == ASTERISK && sr.peekChar() == SLASH) {
                symbol = sr.nextChar(); // Consumo ASTERISK '*'
                sr.nextChar(); // Consumo SLASH '/'
                return;
            }

            symbol = sr.nextChar();
        }

        String msg = String.format("Error: Comentario multilinea no cerrado. Iniciando en Línea %d, Columna %d.",
                startLine, startCol);
        throw new UnclosedCommentException(msg, startLine, startCol);
    }
}
