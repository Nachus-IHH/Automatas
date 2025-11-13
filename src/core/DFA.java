/*
 * DFA.java
 * Automata DFA
 */
package core;


import java.util.Set;

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
     * Dice si un estado es final
     * @param state estado a validar
     * @return boolean true esta en finalStates : false no esta en finalStates
     */
    public boolean isFinal(String state){
        return finalStates.contains(state);
    }

    /**
     * Valida si una cadena es valida para un DFA
     * @param testString Cadena a validar
     */
    @Override
    public void validateString(String str) {
        String state = initialState;
        System.out.println("START -> " + state);

        for (int i = 0; i<str.length(); i++) {
            char symbol = str.charAt(i);

            state = delta(state, symbol);
            if (state == null) {
                return ;
            }
            System.out.print(" -" + symbol + "-> " + state);
        }
        // Valida sí cadenaPrueba pertenece al lenguaje
        System.out.print("\nPor lo tanto " + state);
        System.out.println(
            isFinal(state) ? " es valida" : " NO es valida"
        );
    }

    /**
     * Funcion de transicion δ(q, a)
     * Retorna el siguiente estado desde un estado dado aplicando un simbolo
     * 
     * @param state estado actual
     * @param symbol simbolo de entrada
     * @return Estado siguiente definido por δ(state, symbol)
     */
    public String delta(String state, char symbol){
        if (isSymbolValid(symbol)) {
            return transitionTable.get(state).get(symbol).iterator().next();
        }
        //System.out.println("No válida por contener símbolo no perteneciente al alfabeto { " + symbol + " }");
        return null;
    }

}
