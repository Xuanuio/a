package RMI;

import java.rmi.registry.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class rmi_cha_tansuatjson {

    static String buildResult(String s) {
        if (s == null) s = "";

        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
        for (char c : s.toCharArray()) map.put(c, map.getOrDefault(c, 0) + 1);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<Character, Integer> e : map.entrySet()) {
            if (!first) sb.append(", ");
            first = false;

            char ch = e.getKey();
            String key;
            if (ch == '\\') key = "\\\\";
            else if (ch == '"') key = "\\\"";
            else if (ch == '\n') key = "\\n";
            else if (ch == '\r') key = "\\r";
            else if (ch == '\t') key = "\\t";
            else key = String.valueOf(ch);

            sb.append("\"").append(key).append("\": ").append(e.getValue());
        }
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        CharacterService svc = (CharacterService) registry.lookup("RMICharacterService");

        String input = svc.requestCharacter(studentCode, qCode);
        String answer = buildResult(input);

        svc.submitCharacter(studentCode, qCode, answer);
    }
}
