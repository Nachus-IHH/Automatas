/*
 * NFAE.java
 * Automata NFAE
 */
package core;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import java.util.Map;
import java.util.Stack;

/**
 * NFAE hereda de FA
 * Provee la estructura y metodos comunes para un Automata Finito NO Determinista
 * que incluye transiciones epsilon (epsilon o $\varepsilon$). (NFAE)
 */
public class NFAE extends NFA {
    HashMap<String, HashSet<String>> epsilonClosures;

    /**
     * {@inheritDoc}
     * <p>Construye estructura base de un NFAE<\p>
     * <p>Ademas de llamar al constructor padre, inicializa el campo<\p>
     * <ul>
     * <li><b>epsilonClosures</b>: Un mapa que almacena en caché los $\varepsilon$-closures
     * de cada estado para optimizar el rendimiento.</li>
     * </ul>
     */
    public NFAE(Set<String> states, Set<String> finalStates, Set<String> alphabet, String initialState, String description) {
        super(states, finalStates, alphabet, initialState, description);
        this.epsilonClosures = new HashMap<>();
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
            validateStringBacktraking(strTest, initialState, "START", new HashSet<>());
            return ;    
        }
        validateStringSetMethod(strTest);
    }
    
    /**
     * Calcula e-Closure para un estado
     * @param state estado del cual obtener $\varepsilon$-Closure
     * @return HashSet<String> El conjunto de estados alcanzables desde el estado inicial solo por transiciones $\varepsilon$.
     */
    public HashSet<String> getEpsilonClosure(String state) {
        // Memo
        if (epsilonClosures.containsKey(state)) {
            return epsilonClosures.get(state);
        }

        HashSet<String> closure = new HashSet<>();
        Stack<String> stack = new Stack<>(); // Pila para rastrear estados a visitar

        closure.add(state);     // chance y quitar
        stack.push(state);

        // transitividad
        while (!stack.isEmpty()) {
            String currentState = stack.pop();

            HashMap<Character, HashSet<String>> transitions = this.transitionTable.get(currentState);

            if (transitions != null && transitions.containsKey('ε')) {
                HashSet<String> epsilonTargets = transitions.get('ε');

                for (String targetState : epsilonTargets) {
                    if (!closure.contains(targetState)) {
                        closure.add(targetState);
                        stack.push(targetState);
                    }
                }
            }
        }

        epsilonClosures.put(state, closure);
        return closure;
    }

    /**
     * Calcula e-Closure para un conjunto de estados
     * @param statesSet conjunto de estados del cual obtener $\varepsilon$-Closure
     * @return HashSet<String> El conjunto de estados alcanzables desde el estado inicial solo por transiciones $\varepsilon$.
     */
    public HashSet<String> getEpsilonClosure(HashSet<String> statesSet) {
        HashSet<String> solutionsSet = new HashSet<>();
        for (String state : statesSet) {
            solutionsSet.addAll(getEpsilonClosure(state));
        }
        return solutionsSet;
    }

    /**
     * Valida por backtraking si una cadena es valida
     * @param strTest cadena a validar
     * @param state estado actual para validar 
     * @param mapeo mapeo obtenido del backtraking
     * @param visitados conjunto de estados visitados, es para evitar bucle infinito por e-Closure
     */
    private void validateStringBacktraking(String strTest, String state,  String mapeo, Set<String> visitados) {
        System.out.println("This method hasn't yet been implemented");
    }
    
    /**
     * Valida por simulacion de conjuntos si una cadena es valida
     * @param strTest cadena a validar
     */
    @Override
    public void validateStringSetMethod(String strTest) {
        final HashSet<String> EMPTY_SET = new HashSet<>();
        Set<String> solutions = new HashSet<>();
        Set<String> temp = new HashSet<>();
        
        temp.addAll(getEpsilonClosure(initialState));
        System.out.println("E-closure(" + initialState + ") = " + temp);

        for (int i = 0; i < strTest.length(); i++) {
            // calculo de rutas posibles
            char character = strTest.charAt(i);
            System.out.print("S^(" + temp.toString() + ", " + character + ") = ");

            for (String state : temp) {
                Map<Character, HashSet<String>> internalMap = transitionTable.get(state);
                if (internalMap != null) {
                    // Si el conjunto es vacio, usar EMPTY_SET en su lugar
                    Set<String> next = internalMap.getOrDefault(character, EMPTY_SET);
                    solutions.addAll(next);
                }
            }
            System.out.println(solutions.toString());

            temp.clear();
            // Calculo de e-closures
            for (String target : solutions) {
                temp.addAll(getEpsilonClosure(target));
            }
            System.out.println("E-closure(" + solutions + ") = " + temp);
            solutions.clear();
        }
        System.out.println("\nConjunto de soluciones " + temp.toString());

        boolean isAccepted = temp.stream().anyMatch(finalStates::contains);
        System.out.print("La cadena '" + strTest + "' ");
        System.out.print(isAccepted ? "es valida" : "NO es valida");
    }

    /**
     * Imprime en la CLI el mapa epsilonClosures
     */
    public void printEpsilonClosures() {
        System.out.println("ε-Closures:");
        for (String state : states) {
            System.out.println("ε-closure(" + state + ") = " + getEpsilonClosure(state));
        }
        System.out.println();
    }

    /**
     * Funcion de transicion δ({qx, qx+1, ...}, a)
     * Retorna el siguiente estado desde un estado dado aplicando un simbolo
     * 
     * @param statesSet conjunto de estados
     * @param symbol simbolo de entrada
     * @return HashSet<String> conjunto de estados siguientes definidos por δ({qi, qj, ...}, a)
     */
    @Override
    public HashSet<String> deltaSet(HashSet<String> statesSet, char symbol) {
        if(!isSymbolValid(symbol)) {
            //System.out.println("No válida por contener símbolo no perteneciente al alfabeto { " + symbol + " }");
            return new HashSet<>();
        }

        final HashSet<String> EMPTY_SET = new HashSet<>();
        HashSet<String> tmpSet = new HashSet<>();
        HashSet<String> solutionsSet = new HashSet<>();
        solutionsSet = getEpsilonClosure(statesSet);

        for (String state : solutionsSet) {
            Map<Character, HashSet<String>> internalMap = transitionTable.get(state);
            if (internalMap != null) {
                Set<String> nextStates = internalMap.getOrDefault(symbol, EMPTY_SET);
                tmpSet.addAll(nextStates);
            }
        }

        solutionsSet = getEpsilonClosure(tmpSet);
        return solutionsSet;
    }

    /**
     * Convierte este NFAE a un DFA equivalente (sin transiciones $\varepsilon$).
     *
     * @return DFA Regresa una nueva instancia de un DFA equivalente.
     */
    @Override
    public DFA toDFA() {
        return null;
    }

    /**
     * Convierte este NFAE a un NFA equivalente (sin transiciones $\varepsilon$).
     *
     * @return NFA Regresa una nueva instancia de un NFA equivalente.
     */
    public NFA toNFA() {
        return null;
    }
}
