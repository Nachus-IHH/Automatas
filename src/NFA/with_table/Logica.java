package NFA.with_table;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;

import java.lang.reflect.Type;

public class Logica {
    Set<String> states;
    Set<String> finalStates;
    Set<String> alphabet;
    String initialState;
    //       state           symbol      nextStates
    HashMap<String, HashMap<Character, HashSet<String>>> transitionTable;

    public Logica(String[] states, String[] finalStates, String[] alphabet, String initialState) {
        this.states = new HashSet<>(Arrays.asList(states));
        this.finalStates = new HashSet<>(Arrays.asList(finalStates));
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
        this.initialState = initialState;
        this.transitionTable = new HashMap<>();
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
        /*
        nfa.states = new HashSet<>((List<String>) jsonMap.get("_Q"));
        nfa.finalStates = new HashSet<>((List<String>) jsonMap.get("_F"));
        nfa.alphabet = new HashSet<>((List<String>) jsonMap.get("_S"));
        nfa.initialState = (String) jsonMap.get("_initialState");
        nfa.transitionTable = new HashMap<>();
        */
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
    public void validateString() {
        
    }

    public String seeTransitionTable() {
        String seeTable = " --- Tabla de Trancisiones ---";
        for (String state : states) {
            seeTable += "\n\n" + state;
            HashMap<Character, HashSet<String>> internalMap = transitionTable.get(state);

            if (internalMap != null) {
                for (Character character : internalMap.keySet()) {
                    seeTable += "\n\t" + character + " \t" + internalMap.get(character).toString();
                }
                continue;
            }
            seeTable += "\n\t[Sin transiciones definidas]";
        }
        return seeTable;
    }

}
