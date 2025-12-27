package WS;

import java.util.*;
import vn.medianews.*;

public class WS_Cha_NguyenAm {

    private static int demSoNguyenAm(String s) {
        int count = 0;
        String vowels = "ueoaiUEOAI";
        for (char c : s.toCharArray()) {
            if (vowels.indexOf(c) != -1) count++;
        }
        return count;
    }

    public static void main(String[] args) throws Exception {
        CharacterService_Service service = new CharacterService_Service();
        CharacterService port = service.getCharacterServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        List<String> input = port.requestStringArray(studentCode, qCode);
        if (input == null) input = new ArrayList<>();

        Map<Integer, List<String>> groups = new TreeMap<>();

        for (String word : input) {
            int v = demSoNguyenAm(word);
            groups.computeIfAbsent(v, k -> new ArrayList<>()).add(word);
        }

        List<String> output = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> e : groups.entrySet()) {
            List<String> words = e.getValue();
            Collections.sort(words);
            output.add(String.join(", ", words));
        }

        port.submitCharacterStringArray(studentCode, qCode, output);
    }
}