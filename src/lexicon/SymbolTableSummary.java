package lexicon;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestiona los resultados de los tokens
 */
public class SymbolTableSummary {
    HashMap<String, TokenSummary> symbolTable;
    private static final String FORMAT_HEADER = "%-15s | %-25s | %-10s | %-15s | %-7s | %-5s | %s";
    private static final String FORMAT_ROW = "%-15s | %-25s | %-10s | %-15s | %7s | %-5s | %s";

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

        output.append("=".repeat(130)).append("\n");
        output.append(String.format(FORMAT_HEADER, 
            "Token Name", "Lexeme", "FA Type", "Type Token", "Number", "Count", "Locations (Line, Col)")
        ).append("\n");
        output.append("=".repeat(130)).append("\n");


        for (Map.Entry<String, TokenSummary> entry : symbolTable.entrySet()) {
            TokenSummary summary = entry.getValue();
            String lexeme = entry.getKey();
            String numberString = "-";
            
            if (summary.typeToken.equals("binary")) {
                int number = ConverterStringNumber.binaryToDecimal(lexeme);
                numberString = String.valueOf(number);
            }
            else if (summary.typeToken.equals("decimal")) {
                numberString = lexeme;
            }
            String locationsString = String.join("; ", summary.locations);

            output.append(String.format(FORMAT_ROW,
                summary.name,
                lexeme,
                summary.automataType,
                summary.typeToken,
                numberString,
                summary.count,
                locationsString
            )).append("\n");
        }
        output.append("=".repeat(130)).append("\n");

        return output.toString();
    }
}
