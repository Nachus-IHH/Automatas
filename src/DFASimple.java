public class DFASimple {
    public static void main(String[] args) {
        // q0 -> 0  ;Cada estado por ejemplo q0 se representara solo con el numero (para fines practicos)

        // Estados finales
        int[] finales = {2};
        // Tabla de transición
        int[][] transiciones = {
            //0 1
            {1, 0},     //q0
            {2, 0},     //q1
            {2, 0}      //q2
        };


        String string = "010010";
        int estadoActual = 0;
        boolean esCorrecto = false;
        System.out.print("Cadena a evaluar " + string + "\nSTART");
        
        // Recorre automata
        for (int i = 0; i < string.length(); i++) {
            System.out.print(" -> Q" + estadoActual);
            if (string.charAt(i) == '0') {
                estadoActual = transiciones[estadoActual][0];
                continue;
            }
            // == 1
            estadoActual = transiciones[estadoActual][1];
        }
        System.out.print(" -> Q" + estadoActual + "\n");


        // Checa si esta en un estado final
        for (int fin : finales) {
            if(fin == estadoActual){
                esCorrecto = true;
            }
        }
        System.out.println(esCorrecto ? "Cadena valida" : "Cadena NO valida");

    }
}
