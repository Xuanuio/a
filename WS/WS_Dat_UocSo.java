package WS;

import java.util.*;
import vn.medianews.*;

public class WS_Dat_UocSo {
    public static void main(String[] args) throws Exception {
        DataService_Service service = new DataService_Service();
        DataService port = service.getDataServicePort();

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode"; 

        double b = port.getDataDouble(studentCode, qCode);

        int n = (int) b;

        List<Integer> small = new ArrayList<>();
        List<Integer> large = new ArrayList<>();

        for (int i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                small.add(i);
                int other = n / i;
                if (other != i) large.add(other);
            }
        }

        Collections.reverse(large);

        List<Integer> ans = new ArrayList<>(1 + small.size() + large.size());
        ans.add(small.size() + large.size());
        ans.addAll(small);
        ans.addAll(large);

        port.submitDataIntArray(studentCode, qCode, ans);
    }
}