/*
 * NFA.java
 * Automata NFA
 */
package core;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * NFA hereda de FA
 * Provee la estructura y metodos comunes para un Automata Finito NO Determinista (NFA)
 */
public class NFA extends FA {
    Set<String> backtrackingPaths;
    Set<String> solutionsStateSet;
    boolean useBacktraking = false;

    /**
     * {@inheritDoc}
     * <p>Construye la estructura base de un <b>NFA<b>
     * Ademas de llamar al constructor del padre, inicializa los siguentes campos internos<\p>
     * <ul>
     * <li><b>backtrackingPaths</b>: Conjunto usado para almacenar las rutas de validación recursiva.</li>
     * <li><b>solutionsStateSet</b>: Conjunto usado para el método de simulación por conjuntos.</li>
     * </ul>
     * 
     */
    public NFA(Set<String> states, Set<String> finalStates, Set<String> alphabet, String initialState, String description) {
        super(states, finalStates, alphabet, initialState, description);
        this.backtrackingPaths = new HashSet<>();
        this.solutionsStateSet = new HashSet<>();
    }

    public void setUseBacktraking(boolean useBacktraking){
        this.useBacktraking = useBacktraking;
    }

    /**
     * Valida si una cadena es valida por el NFA
     * Permite elegir entre el metodo recursivo (backtracking) o el iterativo (por conjuntos).
     * 
     * @param strTest Cadena de entrada a validar
     * @param backtraking sí es {@code true}, usa el metodo recursivo. Si es {@code false}, usa el metodo iterativo por conjuntos
     */
    @Override
    public void validateString(String strTest) {
        if (useBacktraking) {
            validateStringBacktraking(strTest, initialState, "START");
            return ;    
        }
        validateStringSetMethod(strTest);
    }

    /**
     * Valida recursiva (backtraking) de una cadena
     * @param strTest cadena a validar
     * @param state current state
     * @param mapeo muestra la ruta que sigue y su fin (si es valida o NO valida)
     */
    private void validateStringBacktraking(String strTest, String state,  String mapeo) {
        mapeo += " -> " + state;
        // CASO BASE
        if (strTest.length() == 0) {
            backtrackingPaths.add( finalStates.contains(state) 
                ? mapeo + " true" 
                : mapeo + " false"
            );
            return ;
        }

        //RECURSIVIDAD - BACKTRAKING
        //        symbol      nextStates
        HashMap<Character, HashSet<String>> transitions = transitionTable.get(state);
        if (transitions != null) {
            HashSet<String> nextStates = transitions.get(strTest.charAt(0));

            if (nextStates == null) {
                backtrackingPaths.add(mapeo + " fi");
                return ;
            }
            
            // Conjunto de posibles estados siguientes
            for (String nextState : transitions.get(strTest.charAt(0))) {

                // crear un nuevo hilo y ponerlo los conjuntos de caracteres para el estado al que se llego
                validateStringBacktraking(strTest.substring(1), nextState, mapeo);
            }
        }
        // imprime rutas
        System.out.println(getBacktrackingPaths());
    }

    /**
     * Simulacion iterativa por conjuntos de una cadena (Método visto en clase)
     * @param strTest cadena a validar
     */
    private void validateStringSetMethod(String strTest) {

        for (int i = strTest.length()-1; i>=0; i--) {
            System.out.println((i+1) + "\tw=" + strTest.substring(0, i+1) + 
                "\tw'=" + strTest.substring(0, i) + "\ta=" + strTest.charAt(i)
            );
        }
        System.out.println("");

        solutionsStateSet.add(initialState);
        Set<String> solutionsTemp = new HashSet<>();

        for (int i = 0; i < strTest.length(); i++) {
            solutionsTemp.clear();
            char character = strTest.charAt(i);
            System.out.println(
                "S^(q0, " + strTest.substring(0, i+1) + ") = S(" + solutionsStateSet.toString() + ", " + character + ")"
            );            
            final HashSet<String> EMPTY_SET = new HashSet<>();
            for (String state : solutionsStateSet) {
                Map<Character, HashSet<String>> internalMap = transitionTable.get(state);
                if (internalMap != null) {
                    // Si el conjunto es vacio, usar EMPTY_SET en su lugar
                    Set<String> nextStates = internalMap.getOrDefault(character, EMPTY_SET);
                    solutionsTemp.addAll(nextStates);
                }
            }
            solutionsStateSet.clear();
            solutionsStateSet.addAll(solutionsTemp);
        }
        System.out.println("Conjunto de soluciones " + solutionsStateSet.toString());
    }

    /**
     * Regresa el conjunto de soluciones obtenidas por validateStringBacktraking()
     * @return String el conjunto de todas las rutas de aceptación/rechazo encontradas por el método de backtracking.
     */
    public String getBacktrackingPaths() {
        String solutions = "";
        for (String k : backtrackingPaths) {
            solutions += k + "\n";
        }
        return solutions;
    }

    /**
     * Convierte un NFA a un DFA
     * @return DFA regresa un DFA
     */
    public DFA toDFA() {        
        return null;
    }
    
}
