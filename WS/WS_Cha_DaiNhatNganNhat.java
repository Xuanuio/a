package WS;

import java.util.*;
import vn.medianews.*;

public class WS_Cha_DaiNhatNganNhat {
    public static void main(String[] args) throws Exception {
        CharacterService_Service service = new CharacterService_Service();
        CharacterService port = service.getCharacterServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";    

        List<String> list = port.requestStringArray(studentCode, qCode);
        if (list == null) list = new ArrayList<>();

        String maxStr = null;
        String minStr = null;
        int maxLength = Integer.MIN_VALUE;
        int minLength = Integer.MAX_VALUE;

        for (String x : list) {
            if (x == null) continue;

            int len = x.length();
            if (len > maxLength) {
                maxLength = len;
                maxStr = x;
            }
            if (len < minLength) {
                minLength = len;
                minStr = x;
            }
        }

        if (maxStr == null) maxStr = "";
        if (minStr == null) minStr = "";

        String answer = maxStr + ";" + minStr;
        port.submitCharacterString(studentCode, qCode, answer);
    }
}