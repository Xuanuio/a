package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class udp_str_loaibokytu {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2208;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        InetAddress ip = InetAddress.getByName(host);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            String req = ";" + studentCode + ";" + qCode;
            byte[] send1 = req.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(send1, send1.length, ip, port));

            byte[] buf = new byte[65507];
            DatagramPacket pkt = new DatagramPacket(buf, buf.length);
            socket.receive(pkt);

            String resp = new String(pkt.getData(), 0, pkt.getLength(), StandardCharsets.UTF_8).trim();
            String[] part = resp.split(";", 3);
            String requestId = part[0];
            String str1 = part.length > 1 ? part[1] : "";
            String str2 = part.length > 2 ? part[2] : "";

            HashSet<Character> ban = new HashSet<>();
            for (char c : str2.toCharArray()) ban.add(c);

            LinkedHashSet<Character> seen = new LinkedHashSet<>();
            StringBuilder out = new StringBuilder();
            for (char c : str1.toCharArray()) {
                if (ban.contains(c)) continue;                 
                if (!Character.isLetterOrDigit(c)) continue;   
                if (seen.add(c)) out.append(c);               
            }

            String ans = requestId + ";" + out;
            byte[] send2 = ans.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(send2, send2.length, ip, port));
        }
    }
}
