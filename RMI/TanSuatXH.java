package RMI;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import RMI.CharacterService;

// Data
public class TanSuatXH {
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry("203.162.10.109", 1099);
        CharacterService sv = (CharacterService) rg.lookup("RMICharacterService");

        String msv = "B22DCCN925", qCode = "";
        String s = sv.requestCharacter(msv, qCode);

        StringBuilder res = new StringBuilder();
        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();

        for (char c : s.toCharArray())
            map.put(c, map.getOrDefault(c, 0) + 1);

        for (Map.Entry<Character, Integer> e : map.entrySet())
            res.append(e.getKey()).append(e.getValue());

        sv.submitCharacter(msv, qCode, res.toString());
    }
}
