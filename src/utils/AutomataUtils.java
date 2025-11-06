package utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import core.FA;
import core.NFAE;

/**
 * Clase de utilidad para Automatas
 * No debe ser instanciada; todos sus métodos son estáticos.
 */
public class AutomataUtils {

    /**
     * Fusiona los atributos del automata 'complement' en el automata 'source'
     * @param <T> 
     * @param source automata al que se agregan atributos
     * @param complement automata del que se toman los atributos
     * @return <T extends FA> asegura que el tipo de retorno es subclase de FA (ej. NFAE)
     */
    public static <T extends FA> T addAttributesFA(T source, FA complement){
        source.states.addAll(complement.states);
        source.alphabet.addAll(complement.alphabet);
        source.transitionTable.putAll(complement.transitionTable);
        return source;
    }

    /**
     * Crea una transicion epsilon para un NFAE
     * @param nfae NFAE al que se creara la transicion $\varepsilon$
     * @param state estado origen
     * @param destinationStates estados destino
     */
    public static void createTransitionEpsilon(NFAE nfae, String state, Collection<String> destinationStates){
        HashMap<Character, HashSet<String>> transition = new HashMap<>();
        transition.put('ε', new HashSet<>(destinationStates));
        nfae.transitionTable.put(state, transition);
        //createTransition(nfae, state, 'e', destinationStates);
    }

    /**
     * Crea una transicion para un FA
     * @param fa FA al que se creara la transicion
     * @param state estado origen
     * @param destinationStates estados destino
     */
    public static void createTransition(FA fa, String state, char symbol, Collection<String> destinationStates){
        HashMap<Character, HashSet<String>> transition = new HashMap<>();
        transition.put(symbol, new HashSet<>(destinationStates));
        fa.transitionTable.put(state, transition);
    }
}
