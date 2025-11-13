/*
 * FAValidator.java
 * Valida cadenas, reutilizable
 */
package utils;

import core.FA;
import core.MainTest;
import core.NFA;
import core.NFAE;

public class FAValidator {

    public static void validateString(FA fa) {
        if (fa == null) {
            System.out.println("Carga primero un automata");
            return;
        }
        
        if (fa instanceof NFA nfa) {
            System.out.println("Es backtraking? (s/n)");
            boolean isBack = MainTest.scanner.nextLine().toLowerCase().startsWith("s");
            nfa.setUseBacktraking(isBack);
        }
        System.out.print("Ingresa la cadena a validar: ");
        String strTest = MainTest.scanner.nextLine();
        fa.validateString(strTest);
    }

    public static void validateString(FA fa, String strTest) {
        if (fa instanceof NFAE nfae) {
            nfae.setUseBacktraking(false);
        }
        fa.validateString(strTest);
    }
}
