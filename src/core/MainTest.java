/*
 * MainTest.java
 * Clase para depurar
 */
package core;

import io.FAParser;
import regulars.GeneraAutomata;
import regulars.Tokenizador;
import ui.FAGraphPanel;
import utils.FAValidator;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainTest {
    public static Scanner scanner = new Scanner(System.in);
    static FA fa;
    
    public static void main (String [] args) {
        
        int option;
        do {
            System.out.println("\n=== Menu FA ===");
            System.out.println("1. Cargar FA desde JSON");
            System.out.println("2. Ver tabla de transiciones");
            System.out.println("3. Ver E-closures (Solo NFAE)");
            System.out.println("4. Validar cadena");
            System.out.println("5. Información del autómata");
            System.out.println("6. Ver grafo");
            System.out.println("7. Convertir");
            System.out.println("8. Automatico");
            System.out.println("9. Tokenizar");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            
            option = scanner.nextInt();
            scanner.nextLine();
            
            switch (option) {
                case 1:
                    loadFromJSON();
                    break;
                case 2:
                    showTransitionTable();
                    break;
                case 3:
                    showEpsilonClosures();
                    break;
                case 4:
                    validateString();
                    break;
                case 5:
                    showAutomataInfo();
                    break;
                case 6:
                    showGraph();
                    break;
                case 7:
                    System.out.println("Por ahora no joven");
                    break;
                case 8:
                    automatico();
                    break;
                case 9:
                    tokenizar();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (option != 0);

        scanner.close();
    }
    
    public static void loadFromJSON() {
        try {
            System.out.print("Ingresa la ruta del archivo JSON: ");
            String filePath = scanner.nextLine();

            //src/files/nfae1.json
            fa = FAParser.loadFA(filePath);
            System.out.println("FA cargado exitosamente desde: " + filePath);   
        }
        catch(Exception e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
    }
    
    private static void showTransitionTable() {
        if(fa == null) {
            System.out.println("Carga primero un automata");
            return ;
        }
        System.out.println("\n--- TABLA DE TRANSICIONES del FA ---");
        System.out.println(fa.getTransitionTableString());
    }

    private static void showEpsilonClosures() {
        if(fa == null) {
            System.out.println("Carga primero un automata");
            return ;
        }
        if (fa instanceof NFAE nfae) {
            nfae.printEpsilonClosures();
            return ;
        }
        System.out.println("Opcion valida solo para NFAE");
    }

    private static void validateString() {
        FAValidator.validateString(fa);
    }

    private static void showAutomataInfo() {
        if(fa == null) {
            System.out.println("Carga primero un automata");
            return ;
        }
        System.out.println(fa.showInfo());
    }

    private static void showGraph() {
        if(fa == null) {
            System.out.println("Carga primero un automata");
            return ;
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Visualización del autómata (" + fa.getClass().getSimpleName() + ")");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(new FAGraphPanel(fa), BorderLayout.CENTER);
            frame.setSize(850, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void automatico() {
        System.out.println("Creando: n = 2");
        Set<String> alphabet = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        fa = GeneraAutomata.nLock(alphabet, 2);
    }

    public static void tokenizar() {
        String str = scanner.nextLine();
        //System.out.println(Tokenizador.parseSetContent(str));
        
        for (String token : Tokenizador.tokenizeString(str)) {
            System.out.print(token + " ");
        }
        
        
    }

}
