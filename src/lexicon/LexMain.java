package lexicon;


import java.util.Scanner;

import core.FA;
import io.FAParser;
import utils.FAValidator;

public class LexMain {
    public static Scanner scanner = new Scanner(System.in);
    static FA fa;
    static String pathTXT;

    public static void main(String[] args) {
        pathTXT = "src/files/lexv3.txt";
        loadFromJSON();
        int opcion;
        do {
            System.out.println("1. LexV1\n2.LexV2\n3.LexV3\n4.Carga FA\n5.Carga TXT\n6.Validar cadena\n0.Salir");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    if(pathTXT != null) System.out.println(Lexico.Lexical_v1(pathTXT));
                    break;
                case 2:
                    if(pathTXT != null) System.out.println(Lexico.Lexical_v2(pathTXT));
                    break;
                case 3:
                    if(pathTXT != null) System.out.println(Lexico.Lexical_v3(pathTXT));
                    break;
                case 4:
                    loadFromJSON();
                    break;
                case 5:
                    
                    break;
                case 6:
                    if (pathTXT ==null || fa == null) {
                        System.out.println("Antes ingresa el FA y TXT");
                        break;
                    }
                    if (fa.isValidStringInput(pathTXT)) {
                        FAValidator.validateString(fa, pathTXT);    
                    }
                    break;
                default:
                    break;
            }
        } while (opcion != 0);
        scanner.close();
    }

    public static void loadFromJSON() {
        try {
            //String filePath = inString("Ruta del archivo FA (JSON) ");
            fa = FAParser.loadFA("src/files/nfae1.json");
            
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
    }

    public static String inString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
