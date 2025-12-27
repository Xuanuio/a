package RMI;

import java.io.*;
import java.util.*;
import java.net.*;
import java.rmi.*;

public class rmi_dat_songuyento {
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }

    public static void main(String[] args) throws Exception {
        DataService ds = (DataService) Naming.lookup("rmi://203.162.10.109:1099/RMIDataService");

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Object obj = ds.requestData(studentCode, qCode);
        int n = Integer.parseInt(String.valueOf(obj).trim());

        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) if (isPrime(i)) primes.add(i);

        ds.submitData(studentCode, qCode, primes);
    }
}
