package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class tcp_dat_mhcaesar {
    static String decrypt(String s, int k) {
        k = ((k % 26) + 26) % 26;
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + (c - 'A' - k + 26) % 26));
            } else if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + (c - 'a' - k + 26) % 26));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

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

            String cipher = in.readUTF();
            int k = in.readInt();

            String plain = decrypt(cipher, k);

            out.writeUTF(plain);
            out.flush();
        }
    }
}
