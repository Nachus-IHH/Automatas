package exceptions;

/**
 * Excepcion lanzada cuando se alcanza el EOF de un archivo
 * sin cerrar un comentario multilinea '*./'
 */
public class UnclosedCommentException extends RuntimeException {
    private final int lineNumber;
    private final int columnNumber;

    public UnclosedCommentException(String message, int lineNumber, int columnNumber) {
        super(message);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }
}
