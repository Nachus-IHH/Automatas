/*
 * FAValidator.java
 * Valida cadenas, reutilizable
 */
package utils;

import core.FA;
import core.MainTest;
import core.NFA;
import core.NFAE;

/**
 * Clase intermediaria para saber si una cadena es aceptada por algun FA
 */
public class FAValidator {

    /**
     * Valida si una cadena es valida para un FA, en caso de ser instancia de NFA
     * pregunta si hacer backtraking
     * 
     * @param fa automata con el que se validara una cadena
     */
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

    /**
     * Valida si una cadena es valida para un FA, en caso de ser instancia de NFA lo
     * resolvera por simulacion de conjuntos
     * 
     * @param fa      automata con el que se validara una cadena
     * @param strTest cadena a valida
     */
    public static void validateString(FA fa, String strTest) {
        if (fa instanceof NFAE nfae) {
            nfae.setUseBacktraking(false);
        }
        fa.validateString(strTest);
    }
}
