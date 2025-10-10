package DFA.with_graph;

import java.util.HashMap;

public class Estado {
    String nombre;
    HashMap<Character, String> trancisiones;

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    public void setTrancisiones(HashMap<Character, String> internalMap) {
        this.trancisiones = internalMap;
    }


}