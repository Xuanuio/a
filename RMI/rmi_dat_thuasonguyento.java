package RMI;

import java.io.*;
import java.util.*;
import java.rmi.*;

public class rmi_dat_thuasonguyento {
    
    static List<Integer> factorize(long n) {
        List<Integer> res = new ArrayList<>();

        while (n % 2 == 0) {
            res.add(2);
            n /= 2;
        }

        for (long i = 3; i * i <= n; i += 2) {
            while (n % i == 0) {
                res.add((int) i);
                n /= i;
            }
        }

        if (n > 1) res.add((int) n);
        return res;
    }

    public static void main(String[] args) throws Exception {
        DataService ds = (DataService) Naming.lookup("rmi://203.162.10.109:1099/RMIDataService");

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Object response = ds.requestData(studentCode, qCode);
        long n = Long.parseLong(String.valueOf(response).trim()); // an toàn hơn

        List<Integer> ans = factorize(n);
        ds.submitData(studentCode, qCode, ans);
    }
}
