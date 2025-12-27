package RMI;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.rmi.registry.*;


public class rmi_byt_base64 {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        ByteService svc = (ByteService) registry.lookup("RMIByteService");

        byte[] encoded = svc.requestData(studentCode, qCode);
        String b64 = new String(encoded, StandardCharsets.UTF_8).trim();

        byte[] decoded = Base64.getDecoder().decode(b64);
        svc.submitData(studentCode, qCode, decoded);
    }
}
