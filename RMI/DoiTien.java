package RMI;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;
import RMI.DataService;

public class DoiTien {
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry("203.162.10.109", 1099);
        DataService sv = (DataService) rg.lookup("RMIDataService");

        String msv = "B22DCCN925", qCode = "";
        Object obj = sv.requestData(msv, qCode);
        int amount = Integer.parseInt(obj.toString());

        int[] coins = {10, 5, 2, 1};
        ArrayList<Integer> used = new ArrayList<>();
        int count = 0, remain = amount;

        for (int c : coins) {
            while (remain >= c) {
                remain -= c;
                used.add(c);
                count++;
            }
        }

        String res = (remain == 0)
            ? count + "; " + String.join(",", used.stream().map(String::valueOf).toArray(String[]::new))
            : "-1";

        sv.submitData(msv, qCode, res);
    }
}
