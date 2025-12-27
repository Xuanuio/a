package WS;

import java.util.*;
import vn.medianews.*;

public class ws_cha_case {
    static String capFirst(String w) {
        if (w.isEmpty()) return w;
        return Character.toUpperCase(w.charAt(0)) + w.substring(1).toLowerCase();
    }

    public static void main(String[] args) throws Exception {
        CharacterService_Service service = new CharacterService_Service();
        CharacterService port = service.getCharacterServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        String s = port.requestString(studentCode, qCode);
        String[] words = s.trim().split("[ _]+");

        StringBuilder pascal = new StringBuilder();
        for (String w : words) pascal.append(capFirst(w));

        StringBuilder camel = new StringBuilder();
        if (words.length > 0) {
            camel.append(words[0].toLowerCase());
            for (int i = 1; i < words.length; i++) camel.append(capFirst(words[i]));
        }

        StringBuilder snake = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (words[i].isEmpty()) continue;
            if (snake.length() > 0) snake.append("_");
            snake.append(words[i].toLowerCase());
        }

        List<String> ans = Arrays.asList(pascal.toString(), camel.toString(), snake.toString());
        port.submitCharacterStringArray(studentCode, qCode, ans);
    }
}
