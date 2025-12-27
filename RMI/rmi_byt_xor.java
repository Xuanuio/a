package RMI;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.rmi.registry.*;

public class rmi_byt_xor {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        ByteService svc = (ByteService) registry.lookup("RMIByteService");

        byte[] data = svc.requestData(studentCode, qCode);

        byte[] key = "PTIT".getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ key[i % key.length]);
        }

        svc.submitData(studentCode, qCode, data);
    }
}