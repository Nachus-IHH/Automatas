package regulars;

import java.util.ArrayList;
import java.util.Set;


//a  [d-f0-2]           [a-c]df+[1-3]^* + [djl.com]
//a ( ( d + e + f ) + ( 0 + 1 + 2 ) ) ( ( a + b + c ) ) d f + ( ( 1 + 2 + 3 ) ) ^* + ( d + j + l + . + c + o + m )
/**
 * Divide la expresión en Tokens (partes que pueda procesar el arbol)
 */
public class Tokenizador {
    private static final Set<String> POWER_MODIFIERS = Set.of("*", "+");
    private static final Character LEFT_BRACKET = '[';
    private static final Character RIGHT_BRACKET = ']';
    private static final Character SCRIPT = '-';

    /**
     * Obtiene los tokens definidos para una cadena
     * 
     * @param str cadena de la cual se obtendran los tokens
     * @return String[] arreglo de tokens obtenidos
     */
    public static String[] tokenizeString(String str) {
        str = str.replaceAll(" ", "");
        str = expandCharacterSets(str);
        ArrayList<String> tokens = new ArrayList<>();

        for (int i = 0; i < str.length(); i++) {

            if (str.charAt(i) == '^') {
                String tokenPower = tokenOfPower(str.substring(i));
                tokens.add(tokenPower);
                i += tokenPower.length() - 1;
                continue;
            }

            tokens.add(String.valueOf(str.charAt(i)));
        }

        return tokens.toArray(new String[0]);
    }

    // obtiene el token de una potencia ^x
    private static String tokenOfPower(String substr) {
        int j = 1;
        if (POWER_MODIFIERS.contains(String.valueOf(substr.charAt(j)) )) {
            substr.substring(0, j++);
        }
        while (j < substr.length() && Character.isDigit(substr.charAt(j)) ) {
            j++;
        }

        return substr.substring(0, j);
    }

    // regular expression expansion
    private static String expandCharacterSets(String expresion) {
        StringBuilder expresionExpand = new StringBuilder();

        for (int i = 0; i < expresion.length(); i++) {
            if (expresion.charAt(i) == LEFT_BRACKET) {
                int j = i + 1;
                while (j < expresion.length() && expresion.charAt(j) != RIGHT_BRACKET) {
                    j++;
                }

                StringBuilder expansion = new StringBuilder(parseSetContent(expresion.substring(i+1, j)));
                expresionExpand.append(expansion);
                i = j;
                continue;
            }

            expresionExpand.append(expresion.charAt(i));
        }

        return String.valueOf(expresionExpand);
    }

    // Recibe solo el contenido del conjunto 0-9a-z y lo procesa () + () ...
    public static String parseSetContent(String content) {
        if (content.isEmpty()) {
            return "";
        }

        StringBuilder expansion = new StringBuilder("(");

        for (int i = 0; i < content.length(); i++) {
            char startChar = content.charAt(i);
            String token = "";
            boolean isRange = false;

            if (i+2 < content.length() && content.charAt(i+1) == SCRIPT) {
                char endChar = content.charAt(i+2);
                if (endChar > startChar) {
                    token = expandRange(startChar, endChar);
                    isRange = true;
                    i += 2;
                }
            }

            if (!isRange) {
                token = String.valueOf(startChar);    
            }

            expansion.append(token);
            if (i<content.length()-1) {
                expansion.append("+");
            }
        }
        expansion.append(")");
        return expansion.toString();
    }

    // Recibe dos caracteres y devuelve la cadena (char1 + char2 + ... + charN)
    private static String expandRange(char start, char end) {
        StringBuilder rangeExpansion = new StringBuilder("(");
        
        for (; start <= end; start++) {
            rangeExpansion.append((char) start + "+");
        }
        int lng = rangeExpansion.length();
        rangeExpansion.replace(lng-1, lng, ")");

        return rangeExpansion.toString();
    }

}
