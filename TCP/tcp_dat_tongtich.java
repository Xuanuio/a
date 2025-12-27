package TCP;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class tcp_dat_tongtich {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port);
             DataInputStream in = new DataInputStream(socket.getInputStream());
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

            out.writeUTF(studentCode + ";" + qCode);
            out.flush();

            int a = in.readInt();
            int b = in.readInt();

            out.writeInt(a + b);
            out.writeInt(a * b);
            out.flush();
        }
    }
}
