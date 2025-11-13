/*
 FA.java
 Estructura base para herencia de: DFA, NFA, NFA-e
 */
package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Estructura base para herencia de Automatas Finitos (FA)
 * Provee la estructura minima y los metodos comunes para
 * DFA, NFA, NFA-e
 */
public abstract class FA {
    public Set<String> states;
    public Set<String> finalStates;
    public Set<String> alphabet;
    public String initialState;
    public String description;
    //              state           symbol      nextStates  
    public HashMap<String, HashMap<Character, HashSet<String>>> transitionTable;

    /**
     * Constructor del FA
     * <p> inicializa la tabla de transiciones como un HashMap vacio. <p>
     * 
     * @param states Conjunto de estados del automata
     * @param finalStates Conjunto de estados finales
     * @param alphabet Conjunto de simbolos de entrada validos
     * @param initialState Estado inicial (unico)
     * @param description Una breve descripcion para identificar el automata
     */
    public FA(Set<String> states, Set<String> finalStates, Set<String> alphabet, String initialState, String description) {
        this.states = states;
        this.finalStates = finalStates;
        this.alphabet = alphabet;
        this.initialState = initialState;
        this.description = description;
        this.transitionTable = new HashMap<>();
    }

    /**
     * Valida si un simbolo pertenece al alfabeto
     * @param symbol simbolo a validar
     * @return boolean dice sí alphabet contiene symbol
     */
    public final boolean isSymbolValid(char symbol) {
        return alphabet.contains(symbol + "");
    }

    /**
     * Valida si una cadena es valida para un DFA
     * @param testString Cadena a validar
     */
    public abstract void validateString(String strTest);
    
    /**
     * Muestra las transiciones que tiene cada estado
     * @return String una representación de texto con la tabla de transiciones (para depuración).
     */
    public String getTransitionTableString() {
        String seeTable = " --- Tabla de Transiciones ---";
        for (String state : states) {
            seeTable += "\n---------\n" + state + ":";

            if (transitionTable.get(state) != null) {
                for (Character character : transitionTable.get(state).keySet()) {
                    seeTable += "\n\t" + character + "  " + transitionTable.get(state).get(character).toString();
                }
                continue;
            }
            seeTable += "\n\t[Sin transiciones definidas]";
        }
        return seeTable + "\n---------\n";
    }

    /**
     * Muestra informacion basica del FA
     * @return String regresa la informacion basica del FA
     */
    public final String showInfo() {
        return "--- INFORMACIÓN DEL FA ---" +
                "\nDescripcion: " + description +
                "\nEstados: " + states +
                "\nAlfabeto: " + alphabet +
                "\nEstado inicial: " + initialState +
                "\nEstados finales: " + finalStates;
    }
}
