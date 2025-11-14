package lexicon;

import java.util.List;

import io.LectorTXT;

public class Lexico_Lines {

    // limpia la cadena de comentarios
    public static String Lexical_v1(String filePath) {
        List<String> content = LectorTXT.fromTXTToLines(filePath);
        StringBuilder str = new StringBuilder();
        for (String line : content) {
            for (int i = 0; i < line.length(); i++) {
                str.append(line.charAt(i));
            }
        }
        return str.toString();
    }

    // limpia la cadena de espacios en blanco
    public static String Lexical_v2(String filePath) {
        return Lexical_v1(filePath)
            .replaceAll("\\s", "");
    }

    // limpia la cadena de comentarios
    public static String Lexical_v3(String filePath) {
        final String ONELINE_COMMENT = "//";
        final String MULTILINE_COMMENT_START = "/*";
        final String MULTILINE_COMMENT_END = "*/";
        List<String> content = LectorTXT.fromTXTToLines(filePath);
        
        StringBuilder str = new StringBuilder();
        boolean isMultiline = false;

        for (int i = 0; i < content.size(); i++) {
            String line = content.get(i);
            // se puede pasar dentro del while?
            if (isMultiline) {
                int endIndex = line.indexOf(MULTILINE_COMMENT_END);
                if (endIndex == -1) {
                    continue;
                }
                line = line.substring(endIndex + 2);
                isMultiline = false;
            }

            while(true) {
                int startIndex = line.indexOf(MULTILINE_COMMENT_START);
                if (startIndex != -1) {
                    
                    int endIndex = line.indexOf(MULTILINE_COMMENT_END);
                    if(endIndex != -1){
                        line = line.substring(0, startIndex) + line.substring(endIndex + 2);
                        continue;
                    }
                    // caso para N lineas
                    line = line.substring(0, startIndex);
                    isMultiline = true;
                }
                // no hay multicomentario o no se cerro
                break;
            }

            // comentario de una linea
            if (line.contains(ONELINE_COMMENT)) {
                line = line.substring(0, line.indexOf(ONELINE_COMMENT));
            }
            str.append(line).append("\n");
        }
        return str.toString().replaceAll("\\s", "");
    }
}
