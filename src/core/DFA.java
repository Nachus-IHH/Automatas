/*
 * DFA.java
 * Automata DFA
 */
package core;


import java.util.Set;

import java.util.ArrayList;

/**
 * DFA hereda de FA
 * Provee la estructura y metodos comunes para un Automata Finito Determinista (DFA)
 */
public class DFA extends FA {

    /**
     * {@inheritDoc}
     * <p>Especificamente, construye la estructura base de un <b>DFA<b>
     * y llama al constructor de la clase padre<p>
     */
    public DFA(Set<String> states, Set<String> finalStates, Set<String> alphabet, String initialState, String description) {
        super(states, finalStates, alphabet, initialState, description);
    }

    /**
     * Valida si una cadena es valida para un DFA
     * @param testString Cadena a validar
     */
    @Override
    public void validateString(String strTest) {
        if(!super.isValidStringInput(strTest)) {
            return ;
        }
        String currentState = super.initialState;
        
        // Mapeo strTest
        System.out.print("START -> " + currentState);
        for (int i = 0; i < strTest.length(); i++) {
            ArrayList<String> nextState = new ArrayList(super.transitionTable.get(currentState).get(strTest.charAt(i)));
            currentState = nextState.get(0);
            System.out.print(" -> " + currentState);
        }

        // Valida sí cadenaPrueba pertenece al lenguaje
        System.out.print("\nPor lo tanto " + strTest);
        System.out.println(
            finalStates.contains(currentState) ? " es valida" : " NO es valida"
        );
    }

}
