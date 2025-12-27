package RMI;

import java.io.*;
import java.util.*;
import java.rmi.*;

public class rmi_dat_pytago {
    static List<List<Integer>> solve(int n) {
        List<List<Integer>> res = new ArrayList<>();

        for (int a = 1; a <= n; a++) {
            long a2 = 1L * a * a;
            for (int b = a + 1; b <= n; b++) {
                long sum = a2 + 1L * b * b;
                int c = (int) Math.sqrt(sum);
                if (c > b && c <= n && 1L * c * c == sum) {
                    List<Integer> t = new ArrayList<>(3);
                    t.add(a); t.add(b); t.add(c);
                    res.add(t);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        DataService ds = (DataService) Naming.lookup("rmi://203.162.10.109:1099/RMIDataService");

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Object obj = ds.requestData(studentCode, qCode);
        int n = Integer.parseInt(String.valueOf(obj).trim());

        List<List<Integer>> answer = solve(n);
        ds.submitData(studentCode, qCode, answer);
    }
}
