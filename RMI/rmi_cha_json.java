package RMI;

import java.rmi.registry.*;
import java.util.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class rmi_cha_json {

    static String solve(String json) {
        json = json.trim();

        Pattern p = Pattern.compile("\"(.*?)\"\\s*:\\s*(\"(.*?)\"|[^,}]+)");
        Matcher m = p.matcher(json);

        List<String> even = new ArrayList<>();
        List<String> odd = new ArrayList<>();

        int idx = 0;
        while (m.find()) {
            String key = m.group(1);
            String rawVal = m.group(2).trim();

            String val;
            if (rawVal.startsWith("\"") && rawVal.endsWith("\"")) {
                val = rawVal.substring(1, rawVal.length() - 1);
            } else {
                val = rawVal;
            }

            String pair = key + ": " + val;
            if (idx % 2 == 0) even.add(pair);
            else odd.add(pair);
            idx++;
        }

        return String.join(", ", even) + "; " + String.join(", ", odd);
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        CharacterService svc = (CharacterService) registry.lookup("RMICharacterService");

        String json = svc.requestCharacter(studentCode, qCode);
        String answer = solve(json);

        svc.submitCharacter(studentCode, qCode, answer);
    }
}
