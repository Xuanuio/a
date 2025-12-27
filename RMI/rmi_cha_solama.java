package RMI;

import java.rmi.registry.*;

public class rmi_cha_solama {
    static int romanToInt(String s) {
        int[] v = new int[26];
        v['I' - 'A'] = 1;   v['V' - 'A'] = 5;
        v['X' - 'A'] = 10;  v['L' - 'A'] = 50;
        v['C' - 'A'] = 100; v['D' - 'A'] = 500;
        v['M' - 'A'] = 1000;

        s = s.trim().toUpperCase();

        int total = 0, prev = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            int cur = v[s.charAt(i) - 'A'];
            total += (cur >= prev) ? cur : -cur;
            prev = cur;
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        CharacterService svc = (CharacterService) registry.lookup("RMICharacterService");

        String roman = svc.requestCharacter(studentCode, qCode);
        String answer = String.valueOf(romanToInt(roman));

        svc.submitCharacter(studentCode, qCode, answer);
    }
}
