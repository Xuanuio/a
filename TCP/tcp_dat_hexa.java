package TCP;

import java.io.*;
import java.net.*;
import java.util.*;

public class tcp_dat_hexa {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF(studentCode + ";" + qCode);
            out.flush();

            int n = in.readInt();

            String bin = Integer.toBinaryString(n);
            String hex = Integer.toHexString(n).toUpperCase();

            out.writeUTF(bin + ";" + hex);
            out.flush();
        }
    }
}
