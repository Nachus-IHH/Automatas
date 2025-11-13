package lexicon;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO de un Token
 */
public class TokenSummary {
    final String name;
    final String automataType;
    final String typeToken;
    int count;
    List<String> locations;
    //boolean isValid

    public TokenSummary(String name, String automataType, String typeToken, int line, int column) {
        this.name = name;
        this.automataType = automataType;
        this.typeToken = typeToken;
        this.count = 1;
        this.locations = new ArrayList<>();
        this.locations.add("(" + line + ", " + column + ")");
    }

    // Método para agregar una nueva aparición
    public void addOccurrence(int line, int column) {
        this.count++;
        this.locations.add(line + ", " + column);
    }
}
