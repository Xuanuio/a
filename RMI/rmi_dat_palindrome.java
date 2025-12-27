package RMI;

import java.io.*;
import java.util.*;
import java.rmi.*;

public class rmi_dat_palindrome {
    static boolean isPal(int x) {
        String s = String.valueOf(x);
        int l = 0, r = s.length() - 1;
        while (l < r) if (s.charAt(l++) != s.charAt(r--)) return false;
        return true;
    }

    public static void main(String[] args) throws Exception {
        DataService ds = (DataService) Naming.lookup("rmi://203.162.10.109:1099/RMIDataService");

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        String resp = String.valueOf(ds.requestData(studentCode, qCode)).trim();
        String[] parts = resp.split("\\s*;\\s*");

        int n = Integer.parseInt(parts[0].trim());
        int k = Integer.parseInt(parts[1].trim());

        List<Integer> ans = new ArrayList<>();
        for (int i = n; i < k; i++) if (isPal(i)) ans.add(i);

        ds.submitData(studentCode, qCode, ans);
    }
}
