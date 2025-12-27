package RMI;

import java.io.*;
import java.util.*;
import java.net.*;
import java.rmi.*;
import java.rmi.registry.*;

public class rmi_dat_phantuthuk {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B2DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        DataService svc = (DataService) registry.lookup("RMIDataService");

        String resp = String.valueOf(svc.requestData(studentCode, qCode)).trim();

        String[] parts = resp.split("\\s*;\\s*", 2);
        int k = Integer.parseInt(parts[1].trim());

        int[] arr = Arrays.stream(parts[0].trim().split("\\s*,\\s*"))
                .filter(x -> !x.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();

        Arrays.sort(arr);
        int ans = arr[arr.length - k]; 

        svc.submitData(studentCode, qCode, ans); 
    }
}
