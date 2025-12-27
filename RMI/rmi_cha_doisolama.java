package RMI;

import java.rmi.registry.*;

public class rmi_cha_doisolama {

    static String toRoman(int n) {
        int[] v = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] r = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            while (n >= v[i]) { sb.append(r[i]); n -= v[i]; }
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        CharacterService svc = (CharacterService) registry.lookup("RMICharacterService");

        int num = Integer.parseInt(svc.requestCharacter(studentCode, qCode).trim());
        svc.submitCharacter(studentCode, qCode, toRoman(num));
    }
}
