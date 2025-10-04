package DFA.with_table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;

import java.lang.reflect.Type;


public class LogicaDFA {
    //       state           symbol   nextState
    HashMap<String, HashMap<Character, String>> transitionTable;
    Set<String> alphabet;
    Set<String> states;
    Set<String> finalStates;
    String initialState;

    public LogicaDFA(String[] alphabet, String[] states, String[] finalStates, String initialState) {
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
        this.states = new HashSet<>(Arrays.asList(states));
        this.finalStates = new HashSet<>(Arrays.asList(finalStates));
        this.transitionTable = new HashMap<>();
        this.initialState = initialState;
    }

    // CLASS METHODS
    public static LogicaDFA loadFromJSON(String filePath) throws Exception {    // Metodo generado por Gemini
        Gson gson = new Gson();
        
        // Define el tipo complejo del JSON (Map<String, Object>)
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        
        // Lee el archivo JSON en un mapa simple de Java
        Map<String, Object> jsonMap = gson.fromJson(new FileReader(filePath), type);

        // --- Extracción y Conversión de Conjuntos ---
        
        // Gson convierte listas de JSON a ArrayList (List), así que las convertimos a Set
        Set<String> states = new HashSet<>((List<String>) jsonMap.get("Q"));
        Set<String> finals = new HashSet<>((List<String>) jsonMap.get("F"));
        List<String> symbolsList = (List<String>) jsonMap.get("S");
        
        String initialState = (String) jsonMap.get("initialState");
        
        // Convertir la lista de símbolos String a un array de String para el constructor
        String[] symbolsArray = symbolsList.toArray(new String[0]);

        // --- Creación del DFA ---
        LogicaDFA dfa = new LogicaDFA(symbolsArray, 
                                      states.toArray(new String[0]), // Pasamos los Sets como arrays
                                      finals.toArray(new String[0]), 
                                      initialState);
        
        // --- Carga de la Tabla de Transiciones ---
        for (String state : states) {
            // Cada clave (q0, q1, etc.) es un Map anidado en el JSON
            if (jsonMap.containsKey(state)) {
                // Gson devuelve LinkedTreeMap, que implementa Map
                Map<String, String> jsonTransitions = (Map<String, String>) jsonMap.get(state);
                
                for (String symbolStr : jsonTransitions.keySet()) {
                    char symbol = symbolStr.charAt(0);
                    String nextState = jsonTransitions.get(symbolStr);
                    
                    // Inicializa el mapa interno si no existe (patrón computeIfAbsent)
                    dfa.transitionTable.computeIfAbsent(state, k -> new HashMap<>())
                       .put(symbol, nextState);
                }
            }
        }

        return dfa;
    }

    // INSTANCE METHODS
    public void initializeTransitionTable(Scanner scanner) {
        for (String state : states) {
            // state -> mapaInterno
            HashMap<Character, String> internalMap = new HashMap<>();
            for (String character : alphabet) {
                System.out.print("\n" + state + " (" + character + ")  ");
                String nextState = scanner.next();
                internalMap.put(character.charAt(0), nextState);
            }
            transitionTable.put(state, internalMap);
        }
    }

    public void validateStringDFA(String testString) {
        String currentState = initialState;

        // Mapeo testString
        System.out.print("START -> " + currentState);
        for (int i = 0; i < testString.length(); i++) {
            currentState = transitionTable.get(currentState).get(testString.charAt(i));
            System.out.print(" -> " + currentState);
        }

        // Valida sí cadenaPrueba pertenece al lenguaje
        System.out.print("\nPor lo tanto " + testString);
        System.out.println(
            finalStates.contains(currentState) ? " es valida" : " NO es valida"
        );
    }

    public String seeTransitionTable() {
        String seeTable = "S";
        for (String character : alphabet) {
            seeTable += "\t " + character;
        }

        for (String state : states) {
            seeTable += "\n" + state;
            HashMap<Character, String> internalMap = transitionTable.get(state);
            for (String character : alphabet) {
                seeTable += "\t" + internalMap.get(character.charAt(0));
            }
        }
        return seeTable;
    }

}