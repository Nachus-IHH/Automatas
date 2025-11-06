package io;

import core.DFA;
import core.FA;
import core.NFA;
import core.NFAE;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * FAParser (Finite Automata Parser)
 * Clase de utilidad estatica encargada de cargar y guardar Autómatas Finitos (FA)
 * desde y hacia archivos en formato JSON, utilizando la librería Gson.
 * 
 * <p>Esta clase no debe ser instanciada. Todos sus métodos son estáticos.</p>
 */
public class FAParser {
    private static final Gson gson = new Gson();

    /**
     * Carga un FA de un archivo JSON desde un archivo JSON
     * El tipo específico de autómata (DFA, NFA, NFAE) se determina
     * por el campo "type" dentro del JSON.
     * 
     * @param <T> El tipo especifico del FA esperado, debe heredar de FA
     * @param filePath ruta completo del archivo JSON a cargar
     * @return nueva instancia del automata (DFA, NFA o NFAE) inicializada con los datos
     * @throws Exception captura cualquier error
     */
    @SuppressWarnings("unchecked")
    public static <T extends FA> T loadFA(String filePath) throws Exception {
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> jsonMap = gson.fromJson(new FileReader(filePath), mapType);

        Set<String> states = new HashSet<>((List<String>) jsonMap.get("_Q"));
        Set<String> finals = new HashSet<>((List<String>) jsonMap.get("_F"));
        Set<String> alphabet = new HashSet<>((List<String>) jsonMap.get("_S"));
        String initialState = (String) jsonMap.get("_q0");
        String description = (String) jsonMap.get("_description");
        
        String type = (String) jsonMap.get("type");

        FA fa;
        switch (type.toUpperCase()) {
            case "DFA": fa = new DFA(states, finals, alphabet, initialState, description); break;
            case "NFA": fa = new NFA(states, finals, alphabet, initialState, description); break;
            case "NFAE": fa = new NFAE(states, finals, alphabet, initialState, description); break;
            default: throw new IllegalArgumentException("Tipo desconocido: " + type);
        }

        loadTransitions(fa, jsonMap);
        return (T) fa;
    }

    /**
     * Carga y mapea las transiciones desde el mapa JSON al campo {@code transitionTable} del objeto FA.
     *
     * @param fa el objeto FA (DFA, NFA O NFAE) al que se añadiran sus transiciones
     * @param jsonMap Mapa que contiene los datos de la estructura del autómata, incluyendo las transiciones por estado.
     */
    private static void loadTransitions(FA fa, Map<String, Object> jsonMap) {
        Type transitionType = new TypeToken<Map<String, HashSet<String>>>() {}.getType();
        for (String state : fa.states) {
            if (jsonMap.containsKey(state)) {
                Map<String, HashSet<String>> transitions = 
                    gson.fromJson(gson.toJson(jsonMap.get(state)), transitionType);
                for (String symbol : transitions.keySet()) {
                    char c = symbol.charAt(0);
                    fa.transitionTable
                        .computeIfAbsent(state, k -> new HashMap<>())
                        .put(c, transitions.get(symbol));
                }
            }
        }
    }
}
