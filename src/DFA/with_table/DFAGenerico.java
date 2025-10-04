package DFA.with_table;

/*
 * hacer que se puedan probar otras cadenas en ejecucion
 * cambiar automata
 * cambiar estado especifico o todo el mapa interno
 * estados finales
 * 
 * agregar validacion de que existen los estados introducidos en las operaciones
 * 
 * regex
 */

import java.util.Scanner;

public class DFAGenerico {
    static LogicaDFA dfa;
    static Scanner scanner = new Scanner(System.in);

    public static void automataManual() {
        dfa = new LogicaDFA(
            // symbols
            new String[] {"0", "1"},
            // states
            new String[] {"q0", "q1", "q2"},
            // finals
            new String[] {"q2"},
            // inital state
            "q0"
        );
    }

    public static void main(String[] args) throws Exception {

        int option = 0;
        do {
            System.out.println("1.Cargar automata del JSON\n2.Cargar automata manualmente\n3.Ver tabla transicion\n4.Validar cadena\n5.Ver alfabeto, estados, finales, estado inicial\n0.Salir");
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1:
                    String path = "src/files/automata.json";
                    dfa = LogicaDFA.loadFromJSON(path);
                    break;
                case 2:
                    automataManual();
                    dfa.initializeTransitionTable(scanner);
                    break;
                case 3:
                    System.out.println(dfa.seeTransitionTable());
                    break;
                case 4:
                    System.out.print("Ingresa cadena a validar: ");
                    String testString = scanner.nextLine();
                    dfa.validateStringDFA(testString);
                    break;
                case 5:
                    System.out.println("Sigma: " + dfa.alphabet.toString());
                    System.out.println("Q:" + dfa.states.toString());
                    System.out.println("F:" + dfa.finalStates.toString());
                    System.out.println("Q0" + dfa.initialState);
                    break;
                default:
                    break;
            }
            System.out.println("");
        } while (option != 0);
        scanner.close();
     
        
    }
}