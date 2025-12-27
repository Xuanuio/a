package RMI;

import java.nio.charset.StandardCharsets;
import java.rmi.registry.*;

public class rmi_byt_batphan {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        ByteService svc = (ByteService) registry.lookup("RMIByteService");

        byte[] data = svc.requestData(studentCode, qCode);

        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(String.format("%03o", b & 0xFF));
        }

        byte[] out = sb.toString().getBytes(StandardCharsets.US_ASCII);
        svc.submitData(studentCode, qCode, out);
    }
}
