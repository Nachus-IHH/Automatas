package lexicon;

import java.util.ArrayList;
import java.util.HashSet;

import core.DFA;
import core.FA;
import core.NFA;

/**
 * Maneja la simulacion de todos los FA (DFA, NFA, NFAE)
 * Flujo típico de uso:
 * 1. Se inicializan los estados de todos los autómatas con
 * {@link #initialStates(FA[])}.
 * 2. Por cada símbolo leído, se llama a
 * {@link #processSymbol(FA[], ArrayList, char)}.
 * 3. Cuando se encuentra un delimitador, se verifica aceptación con
 * {@link #isLexemeAccepted(ArrayList, FA[])}.
 * 
 * Los autómatas se procesan de forma paralela y mutuamente excluyente por
 * prioridad.
 */
public class AutomataProcessor {

    /**
     * Inicializa una lista con los estados iniciales para cada FA de un array
     * 
     * @param automatas array de automatas
     * @return ArrayList<HashSet<String>> lista con estados iniciales de los FA
     */
    public static ArrayList<HashSet<String>> initialStates(FA[] automatas) {
        ArrayList<HashSet<String>> states = new ArrayList<>();
        for (int i = 0; i < automatas.length; i++) {
            HashSet<String> initialStateSet = new HashSet<>();
            initialStateSet.add(automatas[i].initialState);
            states.add(initialStateSet);
        }
        return states;
    }

    /**
     * Reinicia lista de estados a el estado inicial de cada FA
     * Se usa después de procesar un delimitador para reiniciar la simulación sin
     * necesidad de crear nuevas estructuras.
     * 
     * @param automatas array de FA
     * @param states    lista de estados
     */
    public static void resetToInitialStates(FA[] automatas, ArrayList<HashSet<String>> states) {
        for (int i = 0; i < automatas.length; i++) {
            states.get(i).clear();
            states.get(i).add(automatas[i].initialState);
        }
    }

    /**
     * Procesa un simbolo para un array de FA
     * 
     * @param automatas     array de FA
     * @param currentStates lista del resultado de estados de transiciones, en caso
     *                      que no exista el simbolo en el alfabeto del automata,
     *                      devuele HashSet<String> vacio
     *                      Si el autómata entra en estado inválido, permanece vacío
     *                      hasta el siguiente delimitador.
     * @param symbol        simbolo a procesar
     */
    public static void processSymbol(FA[] automatas, ArrayList<HashSet<String>> currentStates, char symbol) {
        for (int i = 0; i < automatas.length; i++) {
            HashSet<String> currentSet = currentStates.get(i);

            if (currentSet.isEmpty()) {
                continue;
            }

            HashSet<String> nextStateSet = new HashSet<>();

            if (automatas[i] instanceof DFA dfa) {
                String currentState = currentSet.iterator().next();
                String nextState = dfa.delta(currentState, symbol);
                if (nextState != null) {
                    nextStateSet.add(nextState);
                }
            } else if (automatas[i] instanceof NFA nfa) {
                nextStateSet = nfa.deltaSet(currentSet, symbol);

            }
            currentStates.set(i, nextStateSet);
        }
    }

    /**
     * Regresa la posicion donde se encuentra el el lexema valido, es de forma
     * excluyente para los FA, se maneja por prioridad
     * 
     * @param currentStates lista de conjuntos de estados que tiene un FA
     * @param automatas     array de automatas
     * @return int posicion de currentStates donde es valido el lexema, si no es
     *         valido en algun FA retorna -1
     */
    public static int isLexemeAccepted(ArrayList<HashSet<String>> currentStates, FA[] automatas) {
        for (int i = 0; i < automatas.length; i++) {
            try {
                for (String state : currentStates.get(i)) {
                    if (automatas[i].finalStates.contains(state)) {
                        return i;
                    }
                }
            } catch (IndexOutOfBoundsException | NullPointerException e) {
                continue;
            }
        }
        return -1;
    }
}
