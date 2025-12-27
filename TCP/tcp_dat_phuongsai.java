package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class tcp_dat_phuongsai {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            socket.setSoTimeout(5000);

            out.writeUTF(studentCode + ";" + qCode);
            out.flush();

            int n = in.readInt();
            long sum = 0;
            int[] a = new int[n];

            for (int i = 0; i < n; i++) {
                a[i] = in.readInt();
                sum += a[i];
            }

            float mean = (n == 0) ? 0f : (float) sum / n;

            float var = 0f;
            for (int x : a) {
                float d = x - mean;
                var += d * d;
            }
            if (n != 0) var /= n;

            out.writeInt((int) sum);
            out.writeFloat(mean);
            out.writeFloat(var);
            out.flush();
        }
    }
}
