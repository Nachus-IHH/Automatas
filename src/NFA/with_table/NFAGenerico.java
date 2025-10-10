package NFA.with_table;

public class NFAGenerico {
    public static void main(String[] args) throws Exception {
        String filePath = "src/files/automatanfa.json";
        Logica nfa = Logica.loadFromJSON(filePath);
        //System.out.println(nfa.seeTransitionTable());
        String str = "01011";
        nfa.validateString(str, true);
        System.out.println(nfa.getSetSolutions() + "\nMates");
        nfa.validateStringMate(str);
    }   
}

/*
 * 
Lenguaje binario:
Construye un DFA que acepte todas las cadenas sobre {0,1} que contengan un número par de 0’s y un número impar de 1’s.

Subcadena específica:
Diseña un DFA que acepte cadenas sobre {a,b} que contengan exactamente una ocurrencia de la subcadena “aba”.

Condición de posición:
Crea un DFA que acepte todas las cadenas sobre {0,1} tales que el tercer símbolo desde el final sea 1.

Palíndromos cortos:
Diseña un NFA que acepte todas las cadenas sobre {a,b} que son palíndromos de longitud ≤ 3.

Múltiples patrones:
Construye un NFA que acepte todas las cadenas sobre {0,1} que empiezan con 1 o terminan con 01.

Presencia de subcadenas:
Diseña un NFA que acepte cadenas sobre {a,b} que contengan “aa” o “bb” pero no ambas.
 */