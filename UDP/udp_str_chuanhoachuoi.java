package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class udp_str_chuanhoachuoi {
    static String normalize(String s) {
        s = s.trim().replaceAll("\\s+", " ").toLowerCase();
        if (s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        String[] words = s.split(" ");
        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            if (!w.isEmpty()) {
                sb.append(Character.toUpperCase(w.charAt(0)));
                if (w.length() > 1) sb.append(w.substring(1));
            }
            if (i < words.length - 1) sb.append(" ");
        }
        return sb.toString();
    }

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
            String[] parts = resp.split(";", 2);
            String requestId = parts[0].trim();
            String data = (parts.length > 1) ? parts[1] : "";

            String fixed = normalize(data);

            String ans = requestId + ";" + fixed;
            byte[] send2 = ans.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(send2, send2.length, ip, port));
        }
    }
}
