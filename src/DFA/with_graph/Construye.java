package DFA.with_graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Construye {
    Set<String> alphabet;
    Set<String> states;
    Set<String> finalStates;
    String initialState;
    List<Estado> trancisiones;

    public Construye(String[] alphabet, String[] states, String[] finalStates, String initialStates) {
        this.alphabet = new HashSet<>(Arrays.asList(alphabet));
        this.states = new HashSet<>(Arrays.asList(states));
        this.finalStates = new HashSet<>(Arrays.asList(finalStates));
        this.initialState = initialStates;
    }

    public void setTrancisiones(ArrayList<String[]> qx) {
        // Verifica que todos los estados tengan una relacion, de lo contrario no hara nada
        if(qx.size() == states.size()) {
            
        }
        System.out.println("No has incluido todas las relaciones de los estados.. o te excediste");
    }

    public void validateStringDFA(String cadena) {

    }
}
