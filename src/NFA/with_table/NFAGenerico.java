package NFA.with_table;

public class NFAGenerico {
    public static void main(String[] args) throws Exception {
        String filePath = "src/files/automatanfa.json";
        Logica nfa = Logica.loadFromJSON(filePath);
        System.out.println("\n\n" +  nfa.seeTransitionTable());
    }
}
