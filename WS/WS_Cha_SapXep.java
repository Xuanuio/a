package WS;

import java.util.*;
import vn.medianews.*;

public class WS_Cha_SapXep {
    public static void main(String[] args) throws Exception {
        CharacterService_Service service = new CharacterService_Service();
        CharacterService port = service.getCharacterServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode"; 

        List<Integer> data = port.requestCharacter(studentCode, qCode);
        if (data == null) data = new ArrayList<>();

        data.removeIf(Objects::isNull);

        Collections.sort(data, Integer::compare);

        port.submitCharacterCharArray(studentCode, qCode, data);
    }
}