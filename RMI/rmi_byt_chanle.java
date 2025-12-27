package RMI;

import java.rmi.registry.*;

public class rmi_byt_chanle {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        Registry registry = LocateRegistry.getRegistry(host, 1099);
        ByteService svc = (ByteService) registry.lookup("RMIByteService");

        byte[] data = svc.requestData(studentCode, qCode);

        byte[] out = new byte[data.length];
        int idx = 0;

        for (byte b : data) {
            if (((b & 0xFF) % 2) == 0) out[idx++] = b;
        }
        for (byte b : data) {
            if (((b & 0xFF) % 2) != 0) out[idx++] = b;
        }

        svc.submitData(studentCode, qCode, out);
    }
}
