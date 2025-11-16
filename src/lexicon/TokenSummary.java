package lexicon;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos de la Tabla de simbolos sobre un lexema
 */
public class TokenSummary {
    final String name;
    final String automataType;
    final String aliasToken;
    int count;
    List<String> locations;

    public TokenSummary(String name, String automataType, String aliasToken, int line, int column) {
        this.name = name;
        this.automataType = automataType;
        this.aliasToken = aliasToken;
        this.count = 1;
        this.locations = new ArrayList<>();
        this.locations.add("(" + line + ", " + column + ")");
    }

    /**
     * Agrega una nueva aparicion del mismo lexema, aumentando count y agregando su ubicacion
     * @param line linea en la que se encuentra el mismo lexema
     * @param column columna en la que se encuentra el mismo lexema
     */
    public void addOccurrence(int line, int column) {
        this.count++;
        this.locations.add("(" + line + ", " + column + ")");
    }
}
