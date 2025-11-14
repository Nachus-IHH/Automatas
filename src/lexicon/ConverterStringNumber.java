package lexicon;

/**
 * Convierte cadenas a numeros
 */
public class ConverterStringNumber {

    /**
     * Convierte un valor binario a decimal
     * 
     * @param str cadena con valor en binario
     * @return int valor en decimal
     */
    public static int binaryToDecimal(String str) {
        final char ONE = '1';
        int number = 0;
        int multiplier = 1;
        
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) == ONE) {
                number += multiplier;
            }            
            multiplier *= 2;
        }
        return number;
    }

    /**
     * Convierte un string a decimal
     * 
     * @param str cadena con valor en decimal
     * @return int valor en decimal
     */
    public static int stringToDecimal(String str) {
        int number = 0;
        int multiplier = 1;
        for (int i = str.length() - 1; i >= 0; i--) {
            int digit = str.charAt(i) - '0';
            number += digit * multiplier;
            multiplier *= 10;
        }
        return number;
    }

}
