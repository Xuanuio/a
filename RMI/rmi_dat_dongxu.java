package RMI;

import java.io.*;
import java.util.*;
import java.net.*;
import java.rmi.*;

public class rmi_dat_dongxu {

    public static String tinhToanDongXu(int amount) {
        int[] coins = {10, 5, 2, 1};
        List<Integer> used = new ArrayList<>();
        int count = 0;

        for (int c : coins) {
            while (amount >= c) {
                amount -= c;
                used.add(c);
                count++;
            }
        }

        if (amount != 0) {
            return "-1";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(count).append("; ");
        for (int i = 0; i < used.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(used.get(i));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String url = "rmi://203.162.10.109:1099/RMIDataService";
        DataService ds = (DataService) Naming.lookup(url);

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Object obj = ds.requestData(studentCode, qCode);
        int amount = Integer.parseInt(String.valueOf(obj).trim());

        String answer = tinhToanDongXu(amount);
        ds.submitData(studentCode, qCode, answer);
    }
}
