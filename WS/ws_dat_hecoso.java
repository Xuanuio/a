package WS;

import java.util.*;
import vn.medianews.*;

public class ws_dat_hecoso {
    public static void main(String[] args) throws Exception {
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        List<Integer> list = port.getData(studentCode, qCode);
        List<String> ans = new ArrayList<>();

        for (int x : list) {
            String oct = Integer.toOctalString(x);
            String hex = Integer.toHexString(x).toUpperCase();
            ans.add(oct + "|" + hex);
        }

        port.submitDataStringArray(studentCode, qCode, ans);
    }
}
