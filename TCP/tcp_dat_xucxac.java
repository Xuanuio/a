package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tcp_dat_xucxac {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket client = new Socket(host, port);
             DataInputStream in = new DataInputStream(client.getInputStream());
             DataOutputStream out = new DataOutputStream(client.getOutputStream())) {

            client.setSoTimeout(5000);

            out.writeUTF(studentCode + ";" + qCode);
            out.flush();

            int n = in.readInt();
            int[] cnt = new int[7];

            for (int i = 0; i < n; i++) {
                int x = in.readInt();
                if (x >= 1 && x <= 6) cnt[x]++;
            }

            for (int face = 1; face <= 6; face++) {
                out.writeFloat((float) cnt[face] / n);
            }
            out.flush();
        }
    }
}
