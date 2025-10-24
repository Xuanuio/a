package RMI;
import java.rmi.*;
import java.rmi.registry.*;
import RMI.ByteService;

// Byte
public class mhCaesar {
    public static void main(String[] args) throws Exception {
        Registry rg = LocateRegistry.getRegistry("203.162.10.109", 1099);
        ByteService sv = (ByteService) rg.lookup("RMIByteService");
 
        String msv = "B22DCCN925", qCode = "";
        byte[] a = sv.requestData(msv, qCode);

        int shift = a.length;
        byte[] ans = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            ans[i] = (byte) (a[i] + shift);
        }

        sv.submitData(msv, qCode, ans);
    }
}
