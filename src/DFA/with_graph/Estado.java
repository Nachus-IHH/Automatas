package DFA.with_graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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