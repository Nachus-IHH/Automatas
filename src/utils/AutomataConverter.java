/*
 * AutomataConverter.java
 * Maneja conversiones NFAE -> NFA -> DFA
 */
package utils;

import core.DFA;
import core.NFA;
import core.NFAE;

/**
 * Maneja las conversiones entre diferentes tipos de Autómatas Finitos
 * (NFAE $\to$ NFA $\to$ DFA).
 * <p>Esta clase actúa como una fachada para los métodos de conversión definidos en las clases de autómata. 
 * No debe ser instanciada; todos sus métodos son estáticos.</p>
 */
public class AutomataConverter {
    /**
     * Invoca el metodo de la instancia para convertir un NFAE a NFA
     * equivalente (sin transiciones $\varepsilon$).
     * @param nfae instancia NFAE que se convertira
     * @return NFA nueva instancia de NFA equivalente
     */
    public static NFA convertNFAEtoNFA(NFAE nfae){
        return nfae.toNFA();
    }
    
    /**
     * Invoca el metodo de la instancia para convertir un NFA a DFA equivalente
     * @param nfa instancia NFA que se convertira
     * @return DFA nueva instancia de DFA equivalente
     */
    public static DFA convertNFAtoDFA(NFA nfa) {
        return nfa.toDFA();
    }
}
