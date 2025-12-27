package RMI;

import java.io.*;
import java.util.*;
import java.rmi.*;
import java.net.*;
import java.util.stream.Collectors;

public class rmi_dat_tohop {
    
    private static int[] parse(String s) {
        if (s == null) return new int[0];
        s = s.trim();
        if (s.isEmpty()) return new int[0];
        return Arrays.stream(s.split("[^0-9-]+"))
                .filter(p -> !p.isEmpty() && !p.equals("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private static void nextPerm(int[] a) {
        if (a == null || a.length <= 1) return;
        int i = a.length - 2;
        while (i >= 0 && a[i] >= a[i + 1]) i--;
        if (i < 0) { Arrays.sort(a); return; }
        int j = a.length - 1;
        while (a[j] <= a[i]) j--;
        int t = a[i]; a[i] = a[j]; a[j] = t;
        for (int l = i + 1, r = a.length - 1; l < r; l++, r--) {
            t = a[l]; a[l] = a[r]; a[r] = t;
        }
    }

    private static String join(int[] a) {
        return Arrays.stream(a).mapToObj(String::valueOf).collect(Collectors.joining(","));
    }

    public static void main(String[] args) throws Exception {
        String url = "rmi://203.162.10.109:1099/RMIDataService";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        DataService svc = (DataService) Naming.lookup(url);
        int[] a = parse(String.valueOf(svc.requestData(studentCode, qCode)));
        nextPerm(a);
        svc.submitData(studentCode, qCode, join(a));
    }
}
