package lexicon;


import core.DFA;
import core.FA;
import core.NFAE;
import io.FAParser;

public class LexMain {
    static FA fa;

    public static void main(String[] args) {
        loadLexical_v8();
    }

    // Verificado
    public static FA loadFromJSON(String filePath) {
        FA fa = null;
        try {
            fa = FAParser.loadFA(filePath);
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
        return fa;
    }

    // Verificado metodo tmp
    public static <T extends FA> T loadFromJSONDFA(String filePath) {
        T fa = null;
        try {
            fa = FAParser.loadFA(filePath);
        } catch (Exception e) {
            System.out.println("Error al cargar el archivo: " + e.getMessage());
        }
        return fa;
    }
    // Verificado
    public static void loadLexical_v1() {
        String filePath = "src/files/lexv1.txt";
        fa = loadFromJSON("src/files/dfa1.json");
        System.out.println(fa.showInfo());
        System.out.println(fa.getTransitionTableString());
        System.out.println("\t" + Lexical_v1.analyze(filePath, fa));
    }

    // Verificado
    public static void loadLexical_v2() {
        String filePath = "src/files/lexv2.txt";
        fa = loadFromJSON("src/files/dfa1.json");
        System.out.println(fa.showInfo());
        System.out.println(fa.getTransitionTableString());
        System.out.println("\t" + Lexical_v2.analyze(filePath, fa));
    }

    // Verificado
    public static void loadLexical_v3() {
        String filePath = "src/files/lexv3.txt";
        fa = loadFromJSON("src/files/dfa1.json");
        System.out.println(fa.showInfo());
        System.out.println(fa.getTransitionTableString());
        System.out.println("\t" + Lexical_v3.analyze(filePath, fa));
    }

    // Verificado
    public static void loadLexical_v4() {
        String filePath = "src/files/lexv4.txt";
        DFA fa1 = loadFromJSONDFA("src/files/end00.json");
        DFA fa2 = loadFromJSONDFA("src/files/end01.json");
        System.out.println("FA Ends '00'\n" + fa1.showInfo());
        System.out.println("FA Ends '01'\n" + fa2.showInfo());

        DFA[] automatas = {fa1, fa2};
        String[] nameTokens = {"end00", "end01"};    // podria ser una clase (name, <extends FA>)
        System.out.println("\n\n" + Lexical_v4.analyze(filePath, automatas, nameTokens));
    }

    // Verificado
    public static void loadLexical_v5() {
        String filePath = "src/files/lexv5.txt";
        DFA fa1 = loadFromJSONDFA("src/files/end00.json");
        DFA fa2 = loadFromJSONDFA("src/files/end01.json");
        NFAE nfae1 = loadFromJSONDFA("src/files/nfae1.json");
        System.out.println("\n" + fa1.showInfo());
        System.out.println("\n" + fa2.showInfo());
        System.out.println("\n" + nfae1.showInfo());

        FA[] automatas = {fa1, fa2, nfae1};
        String[] nameTokens = {"end00", "end01", "parAyB"};    // podria ser una clase (name, <extends FA>)
        System.out.println("\n\n" + Lexical_v5.analyze(filePath, automatas, nameTokens));
    }

    // Verificado
    public static void loadLexical_v6() {
        String filePath = "src/files/lexv5.txt";
        DFA fa1 = loadFromJSONDFA("src/files/end00.json");
        DFA fa2 = loadFromJSONDFA("src/files/end01.json");
        NFAE nfae1 = loadFromJSONDFA("src/files/nfae1.json");
        System.out.println("\n" + fa1.showInfo());
        System.out.println("\n" + fa2.showInfo());
        System.out.println("\n" + nfae1.showInfo());

        FA[] automatas = {fa1, fa2, nfae1};
        String[] nameTokens = {"end00", "end01", "parAyB"};    // podria ser una clase (name, <extends FA>)
        System.out.println("\n\n" + Lexical_v6.analyze(filePath, automatas, nameTokens));
    }

    // Verificado
    public static void loadLexical_v7() {
        String filePath = "src/files/lexv5.txt";
        DFA fa1 = loadFromJSONDFA("src/files/end00.json");
        DFA fa2 = loadFromJSONDFA("src/files/end01.json");
        NFAE nfae1 = loadFromJSONDFA("src/files/nfae1.json");
        System.out.println("\n" + fa1.showInfo());
        System.out.println("\n" + fa2.showInfo());
        System.out.println("\n" + nfae1.showInfo());

        FA[] automatas = {fa1, fa2, nfae1};
        String[] nameTokens = {"end00", "end01", "parAyB"};    // podria ser una clase (name, <extends FA>)
        String[] typeTokens = {"number", "number", "variable"};
        SymbolTableSummary symbolTableSummary = Lexical_v7.analyze(filePath, automatas, nameTokens, typeTokens);
        System.out.println("\n\n" + symbolTableSummary.exportTable());
    }

    // Verificado
    public static void loadLexical_v8() {
        String filePath = "src/files/lexv5.txt";
        DFA fa1 = loadFromJSONDFA("src/files/end00.json");
        DFA fa2 = loadFromJSONDFA("src/files/end01.json");
        NFAE nfae1 = loadFromJSONDFA("src/files/nfae1.json");
        System.out.println("\n" + fa1.showInfo());
        System.out.println("\n" + fa2.showInfo());
        System.out.println("\n" + nfae1.showInfo());

        FA[] automatas = {fa1, fa2, nfae1};
        String[] nameTokens = {"end00", "end01", "parAyB"};    // podria ser una clase (name, <extends FA>)
        String[] typeTokens = {"number", "number", "variable"};
        SymbolTableSummary symbolTableSummary = Lexical_v8.analyze(filePath, automatas, nameTokens, typeTokens);
        System.out.println("\n\n" + symbolTableSummary.exportTable());
    }

    public static void loadLexical_v9() {
        String filePath = "src/files/lexv9.txt";
        DFA fa1 = loadFromJSONDFA("src/files/end00.json");
        DFA fa2 = loadFromJSONDFA("src/files/end01.json");
        NFAE nfae1 = loadFromJSONDFA("src/files/nfae1.json");
        System.out.println("\n" + fa1.showInfo());
        System.out.println("\n" + fa2.showInfo());
        System.out.println("\n" + nfae1.showInfo());

        FA[] automatas = {fa1, fa2, nfae1};
        String[] nameTokens = {"end00", "end01", "parAyB"};    // podria ser una clase (name, <extends FA>)
        String[] typeTokens = {"number", "number", "variable"};
        SymbolTableSummary symbolTableSummary = Lexical_v7.analyze(filePath, automatas, nameTokens, typeTokens);
        System.out.println("\n\n" + symbolTableSummary.exportTable());
    }
}
