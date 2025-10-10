package NFA.with_table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import DFA.with_table.LogicaDFA;

import java.io.FileReader;

import java.lang.reflect.Type;

public class Logica {
    Set<String> states;
    Set<String> finalStates;
    Set<String> alphabet;
    String initialState;
    //       state           symbol      nextStates
    HashMap<String, HashMap<Character, HashSet<String>>> transitionTable;
    Set<String> setSolutionsBacktraking;
    Set<String> setSolutionsMate;


    public Logica(String[] states, String[] finalStates, String[] alphabet, String initialState) {
        this.states = new HashSet<>(Arrays.asList(states));
        this.finalStates = new HashSet<>(Arrays.asList(finalStates));
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
        this.initialState = initialState;
        this.transitionTable = new HashMap<>();
        this.setSolutionsBacktraking = new HashSet<>();
        this.setSolutionsMate = new HashSet<>();
    }

    // CLASS METHODS
    public static Logica loadFromJSON(String filePath) throws Exception {
        Gson gson = new Gson();
        // Define el tipo complejo del JSON (Map<String, Object>)
        Type rootType = new TypeToken<Map<String, Object>>() {}.getType();

        // Lee el archivo JSON en un mapa simple de Java
        Map<String, Object> jsonMap = gson.fromJson(new FileReader(filePath), rootType);

        // --- Extraccion y conversion de conjuntos ---
        // Inicializacion del NFA
        Logica nfa = new Logica(
            ((List<String>) jsonMap.get("_Q")).toArray(new String[0]),
            ((List<String>) jsonMap.get("_F")).toArray(new String[0]),
            ((List<String>) jsonMap.get("_S")).toArray(new String[0]),
            (String) jsonMap.get("_initialState")
        );

        // Define el tipo EXACTO de la tabla de transiciones anidada
        // Map<simbolo : String, nextStates : HashSet<String>>
        Type transitionType = new TypeToken<Map<String, HashSet<String>>>() {}.getType();

        // --- Carga de la tabla de transiciones ---
        for(String state : nfa.states) {
            if(jsonMap.containsKey(state)) {
                // Obtener el objeto crudo del JSON
                Object rawJsonTransitions = jsonMap.get(state);

                // Serializar a JSON y luego DESERIALIZAR AL TIPO CORRECTO
                String jsonString = gson.toJson(rawJsonTransitions);
                Map<String, HashSet<String>> jsonTransitions = gson.fromJson(jsonString, transitionType);

                for (String str : jsonTransitions.keySet()) {
                    char character = str.charAt(0);

                    // Los estados siguientes son ahora correctamente un HashSet<String>
                    HashSet<String> nextStates = jsonTransitions.get(str);
                    nfa.transitionTable.computeIfAbsent(state, k -> new HashMap<>()).put(character, nextStates);;
                }
            }
        }

        return nfa;
    }

    // INSTANCE METHODS
    public String seeTransitionTable() {
        String seeTable = " --- Tabla de Trancisiones ---";
        for (String state : states) {
            seeTable += "\n---------\n" + state + ":";
            //HashMap<Character, HashSet<String>> internalMap = transitionTable.get(state);

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

    // agregar memo lenguaje ensamblador para ibm pc (color verde)
    public boolean validateStringBacktraking(String strTest, String state,  String mapeo) {
        mapeo += " -> " + state;
        // CASO BASE
        if (strTest.length() == 0) {
            if (finalStates.contains(state)) {
                setSolutionsBacktraking.add(mapeo + " true");
                return true;
            }
            setSolutionsBacktraking.add(mapeo + " false");
            return false;
        }

        //RECURSIVIDAD - BACKTRAKING
        //state   symbol      nextStates
        HashMap<Character, HashSet<String>> transitions = transitionTable.get(state);
        if (transitions != null) {

            HashSet<String> nextStates = transitions.get(strTest.charAt(0));
            if (nextStates == null) {
                setSolutionsBacktraking.add(mapeo + " fi");
                return false;
            }
            
            // Conjunto de posibles estados siguientes
            for (String nextState : transitions.get(strTest.charAt(0))) {

                // crear un nuevo hilo y ponerlo los conjuntos de caracteres para el estado al que se llego
                validateStringBacktraking(strTest.substring(1), nextState, mapeo);
            }
        }

        return false;
    }

    public void validateString(String strTest, boolean backtraking) {
        for (int i = 0; i < strTest.length(); i++) {
            if (!alphabet.contains(strTest.charAt(i) + "") ){
                System.out.println("Hay un simbolo que no pertenece al lenguaje");
                return ;
            }
        }

        if (backtraking) {
            validateStringBacktraking(strTest, initialState, "START");
            return ;    
        }
        validateStringMate(strTest);
    }

    public void validateStringMate(String strTest) {

        for (int i = strTest.length()-1; i>=0; i--) {
            System.out.println((i+1) + "\tw=" + strTest.substring(0, i+1) + 
                "\tw'=" + strTest.substring(0, i) + "\ta=" + strTest.charAt(i)
            );
        }
        System.out.println("");

        setSolutionsMate.add(initialState);
        Set<String> solutionsTemp = new HashSet<>();

        for (int i = 0; i < strTest.length(); i++) {
            solutionsTemp.clear();
            char character = strTest.charAt(i);
            System.out.println(
                "S^(q0, " + strTest.substring(0, i+1) + ") = S(" + setSolutionsMate.toString() + ", " + character + ")"
            );            
            final HashSet<String> EMPTY_SET = new HashSet<>();
            for (String state : setSolutionsMate) {
                Map<Character, HashSet<String>> internalMap = transitionTable.get(state);
                if (internalMap != null) {
                    // Si el conjunto es vacio, usar EMPTY_SET en su lugar
                    Set<String> nextStates = internalMap.getOrDefault(character, EMPTY_SET);
                    solutionsTemp.addAll(nextStates);
                }
                
            }

            setSolutionsMate.clear();
            setSolutionsMate.addAll(solutionsTemp);
        }
        System.out.println("Conjunto de soluciones " + setSolutionsMate.toString());
    }

    public String getSetSolutions() {
        String solutions = "";
        for (String k : setSolutionsBacktraking) {
            solutions += k + "\n";
        }
        return solutions;
    }

    // Regresar un DFA
    public LogicaDFA convertToDFA(Logica nfa) {
        //return new LogicaDFA(this.alphabet, this.states, this.finalStates, initialState);
        return new LogicaDFA(null, null, null, initialState);
    }
    
}
/*
 * modelo de datos orientados a su especifidad
 * significa que cada corporacion va a trabajar con muchas aplicaciones que se beneficien de IA y muchos modelos de datos
 * inventariar eso (para entender que tenemos, de que forma operan, de que forma los modelos fueron entrenados - IMPORTANTE)
 * 
 * cuando saber si una IA esta alucinando (entrega de datos que no sean adecuados/errados) -> Herramientas de monitoreo
 */
