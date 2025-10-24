package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class LocKyTu {
    public static void main(String[] args) throws Exception {
        InetAddress sA = InetAddress.getByName("203.162.10.109");
        int sP = 2208;

        String code = ""; 
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            byte[] req = code.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(req, req.length, sA, sP));

            byte[] buf = new byte[65507];
            DatagramPacket in = new DatagramPacket(buf, buf.length);
            socket.receive(in);

            String msg = new String(in.getData(), 0, in.getLength(), StandardCharsets.UTF_8);

            String[] parts = msg.split(";", 3);
            if (parts.length < 3) {

                parts = msg.trim().split(";", 3);
            }
            String rid = parts[0];
            String s1  = parts[1];
            String s2  = parts[2];

            boolean[] ban = new boolean[65536]; // char 16-bit
            for (int i = 0; i < s2.length(); i++) ban[s2.charAt(i)] = true;

            StringBuilder out = new StringBuilder(s1.length());
            for (int i = 0; i < s1.length(); i++) {
                char c = s1.charAt(i);
                if (!ban[c]) out.append(c);
            }
            
            String ans = rid + ";" + out.toString(); 

            byte[] resp = ans.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(resp, resp.length, sA, sP));
        }
    }
}
