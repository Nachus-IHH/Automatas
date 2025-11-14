package regulars;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import core.NFAE;
import utils.AutomataUtils;

/**
 * Genera las estructuras para un NFAE,
 * NOTA: si se quieren usar NFAE precargados, asegurarse de que no coincidan los
 * nombres de los estados
 */
public class GeneraAutomata {
    private static int numStates = 0;

    // Automatas iniciales
    /**
     * Genera el NFAE para fi (r = fi)
     * 
     * @return NFAE - r=fi
     */
    public static NFAE emptySetAutomaton() {
        String q0 = "q" + numStates++;
        String qf = "q" + numStates++;

        return new NFAE(
                new HashSet<>(Arrays.asList(q0, qf)),
                new HashSet<>(Arrays.asList(qf)),
                new HashSet<>(),
                q0,
                null);
    }

    /**
     * Genera el NFAE para epsilon (r = epsilon)
     * 
     * @return NFAE r = epsilon
     */
    public static NFAE epsilonAutomaton() {
        String q0 = "q" + numStates++;

        return new NFAE(
                new HashSet<>(Arrays.asList(q0)),
                new HashSet<>(Arrays.asList(q0)),
                new HashSet<>(),
                q0,
                null);
    }

    /**
     * Genera el NFAE para un simbolo (r = a)
     * 
     * @param symbol simbolo para la transicion
     * @return new NFAE con la transicion para symbol - r=a
     */
    public static NFAE symbolAutomaton(String symbol) {
        String q0 = "q" + numStates++;
        String qf = "q" + numStates++;

        NFAE nfae = new NFAE(
                new HashSet<>(Arrays.asList(q0, qf)),
                new HashSet<>(Arrays.asList(qf)),
                new HashSet<>(Arrays.asList(symbol)),
                q0,
                null);

        // Transicion q0 -> qf
        HashMap<Character, HashSet<String>> transition = new HashMap<>();
        transition.put(symbol.charAt(0), new HashSet<>(Arrays.asList(qf)));
        nfae.transitionTable.put(q0, transition);

        return nfae;
    }

    // Automatas precargados
    /**
     * Hace la union de dos NFAE (r1 + r2)
     * 
     * @param r1 NFAE r1 a unir
     * @param r2 NFAE r2 a unir
     * @return new NFAE que tiene la union de ambos NFAE con transiciones epsilon -
     *         r1+r2
     */
    public static NFAE joinNFAE(NFAE r1, NFAE r2) {
        String q0 = "q" + numStates++;
        String qf = "q" + numStates++;

        NFAE nfae = new NFAE(
                new HashSet<>(Arrays.asList(q0, qf)),
                new HashSet<>(Arrays.asList(qf)),
                new HashSet<>(),
                q0,
                null);
        AutomataUtils.addAttributesFA(nfae, r1);
        AutomataUtils.addAttributesFA(nfae, r2);

        // Transiciones q0 -> qx
        AutomataUtils.createTransitionEpsilon(nfae, q0, Arrays.asList(r1.initialState, r2.initialState));

        // Transiciones qx -> qf
        String r1finalState = r1.finalStates.iterator().next();
        String r2finalState = r2.finalStates.iterator().next();

        AutomataUtils.createTransitionEpsilon(nfae, r1finalState, Arrays.asList(qf));
        AutomataUtils.createTransitionEpsilon(nfae, r2finalState, Arrays.asList(qf));

        return nfae;
    }

    /**
     * Concatenacion de dos NFAE (r1 r2)
     * 
     * @param r1 NFAE r1 a concatenar
     * @param r2 NFAE r2 a concatenar
     * @return new NFAE que tiene la concatenacion en el orden r1 r2
     */
    public static NFAE concatenateNFAE(NFAE r1, NFAE r2) {
        String q0 = "q" + numStates++;
        String qf = "q" + numStates++;

        NFAE nfae = new NFAE(
                new HashSet<>(Arrays.asList(q0, qf)),
                new HashSet<>(Arrays.asList(qf)),
                new HashSet<>(),
                q0,
                null);
        AutomataUtils.addAttributesFA(nfae, r1);
        AutomataUtils.addAttributesFA(nfae, r2);

        // Transicion q0 -> r1.q0
        AutomataUtils.createTransitionEpsilon(nfae, q0, Arrays.asList(r1.initialState));

        // Transicion r1.qf -> r2.q0
        String r1FinalState = r1.finalStates.iterator().next();
        AutomataUtils.createTransitionEpsilon(nfae, r1FinalState, Arrays.asList(r2.initialState));

        // Transicion r2.qf -> qf
        String r2FinalState = r2.finalStates.iterator().next();
        AutomataUtils.createTransitionEpsilon(nfae, r2FinalState, Arrays.asList(qf));

        return nfae;
    }

    /**
     * Genera la cerradura abierta para un NFAE (r1 ^+)
     * 
     * @param r1 automata NFAE
     * @return new NFAE con la cerradura abierta r1^+
     */
    public static NFAE openLock(NFAE r1) {
        String q0 = "q" + numStates++;
        String qf = "q" + numStates++;

        NFAE nfae = new NFAE(
                new HashSet<>(Arrays.asList(q0, qf)),
                new HashSet<>(Arrays.asList(qf)),
                new HashSet<>(),
                q0,
                null);
        AutomataUtils.addAttributesFA(nfae, r1);

        // transicion r1.qf -> r1.q0, qf
        String r1FinalState = r1.finalStates.iterator().next();
        AutomataUtils.createTransitionEpsilon(nfae, r1FinalState, Arrays.asList(r1.initialState, qf));

        // transicion q0 -> r1.q0
        AutomataUtils.createTransitionEpsilon(nfae, q0, Arrays.asList(r1.initialState));

        return nfae;
    }

    /**
     * Genera la cerradura de Kleene para un NFAE (r1 ^*)
     * 
     * @param r1 automata NFAE
     * @return new NFAE r1^*
     */
    public static NFAE kleeneLock(NFAE r1) {
        NFAE nfae = openLock(r1);
        String qf = nfae.finalStates.iterator().next();

        // transicion q0 -> qf
        AutomataUtils.createTransitionEpsilon(nfae, nfae.initialState, Arrays.asList(qf, r1.initialState));

        return nfae;
    }

    /**
     * Regresa la cerradura especifica de una potencia de un automata r1 ^n
     * 
     * @param alphabet alfabeto del automata
     * @param n        potencia que se quiere obtener
     * @return new NFAE con la potencia especifica solicitada r1^n
     */
    public static NFAE nLock(Set<String> alphabet, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("No se permiten numeros negativos para la cerradura L^x");
        }
        if (n == 0) {
            return epsilonAutomaton();
        }

        String q0 = "q" + numStates++;
        String qf = "q" + numStates++;

        NFAE nfae = new NFAE(
                new HashSet<>(Arrays.asList(q0, qf)),
                new HashSet<>(Arrays.asList(qf)),
                new HashSet<>(alphabet),
                q0,
                null);

        return nfae;
    }

}
