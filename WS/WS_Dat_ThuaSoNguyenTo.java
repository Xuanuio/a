package WS;

import java.util.*;
import vn.medianews.*;

public class WS_Dat_ThuaSoNguyenTo {
    public static String primeFactors(int n) {
        StringBuilder sb = new StringBuilder();

        while (n % 2 == 0) {
            sb.append(2).append(", ");
            n /= 2;
        }

        for (int i = 3; i * i <= n; i += 2) {
            while (n % i == 0) {
                sb.append(i).append(", ");
                n /= i;
            }
        }

        if (n > 1) sb.append(n).append(", ");

        sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";       

        List<Integer> list = port.getData(studentCode, qCode);
        if (list == null) list = new ArrayList<>();

        List<String> ans = new ArrayList<>(list.size());
        for (Integer x : list) {
            ans.add(primeFactors(x));
        }

        port.submitDataStringArray(studentCode, qCode, ans);
    }
}