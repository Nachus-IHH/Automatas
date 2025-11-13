package lexicon;


/**
 * Esta clase convierte cadenas a numeros
 */
public class ConverterStringNumber {
    private static final char ONE = '1';

    /**
     * Convierte un valor binario a decimal
     * @param str cadena con valor en binario
     * @return int valor en decimal
     */
    public static int binaryToDecimal(String str) {
        int number = 0, j=0;
        for (int i = str.length() - 1; i >=0 ; i--, j++) {
            if (str.charAt(i) == ONE) {
                number += Math.pow(2, j);
            }
        }
        return number;
    }

}
