package RMI;

import java.rmi.registry.*;

public class rmi_cha_xor {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        CharacterService svc = (CharacterService) registry.lookup("RMICharacterService");

        String s = svc.requestCharacter(studentCode, qCode);
        String[] parts = s.split(";", 2);

        String key = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            sb.append((char) (data.charAt(i) ^ key.charAt(i % key.length())));
        }

        svc.submitCharacter(studentCode, qCode, sb.toString());
    }
}
