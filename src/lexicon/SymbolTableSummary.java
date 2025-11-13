package lexicon;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestiona los resultados de los tokens
 */
public class SymbolTableSummary {

    HashMap<String, TokenSummary> symbolTable;

    public SymbolTableSummary() {
        this.symbolTable = new HashMap<>();
    }

    /**
     * Agrega una nueva aparición de un token, actualizando el resumen si ya existe
     * el lexema.
     */
    public void addTokenOccurrence(String name, String lexeme, String automataType, String typeToken, int line,
            int column) {

        if (symbolTable.containsKey(lexeme)) {
            TokenSummary existingSummary = symbolTable.get(lexeme);
            existingSummary.addOccurrence(line, column);
        } else {
            TokenSummary newSummary = new TokenSummary(name, automataType, typeToken, line, column);
            symbolTable.put(lexeme, newSummary);
        }
    }

    /**
     * Convierte la tabla de simbolos en una cadena con formato de tabla
     * 
     * @return String tabla de simbolos en formato tabla
     */
    public String exportTable() {
        StringBuilder output = new StringBuilder();
        output.append("Token Name\tLexeme\tFA Type\tType Token\tNumber\tCount\tLocations (Line, Col)");

        for (Map.Entry<String, TokenSummary> entry : symbolTable.entrySet()) {
            TokenSummary summary = entry.getValue();

            output.append("\n");
            output.append(summary.name).append("\t")
                    .append(entry.getKey()).append("\t") // Lexeme (clave)
                    .append(summary.automataType).append("\t")
                    .append(summary.typeToken).append("\t")
                    .append(summary.typeToken.equals("number")
                            ? ConverterStringNumber.binaryToDecimal(entry.getKey())
                            : "--").append("\t")
                    .append(summary.count).append("\t");

            // 2. Lista de ubicaciones
            output.append("[");
            output.append(String.join("; ", summary.locations));
            output.append("]");
        }

        return output.toString();
    }
}
