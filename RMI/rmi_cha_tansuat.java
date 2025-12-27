package RMI;

import java.rmi.registry.*;
import java.util.*;

public class rmi_cha_tansuat {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        CharacterService svc = (CharacterService) registry.lookup("RMICharacterService");

        String s = svc.requestCharacter(studentCode, qCode);
        if (s == null) s = "";

        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
        for (char c : s.toCharArray()) map.put(c, map.getOrDefault(c, 0) + 1);

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Character, Integer> e : map.entrySet()) sb.append(e.getKey()).append(e.getValue());

        svc.submitCharacter(studentCode, qCode, sb.toString());
    }
}
