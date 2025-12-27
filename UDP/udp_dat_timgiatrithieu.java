package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class udp_dat_timgiatrithieu {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        InetAddress ip = InetAddress.getByName(host);

        try (DatagramSocket client = new DatagramSocket()) {
            client.setSoTimeout(5000);

            String msg = ";" + studentCode + ";" + qCode;
            byte[] send1 = msg.getBytes(StandardCharsets.UTF_8);
            client.send(new DatagramPacket(send1, send1.length, ip, port));

            byte[] buf = new byte[65507];
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            client.receive(inPacket);

            String resp = new String(inPacket.getData(), 0, inPacket.getLength(), StandardCharsets.UTF_8).trim();
            String[] parts = resp.split(";");
            String requestId = parts[0].trim();
            int n = Integer.parseInt(parts[1].trim());

            boolean[] present = new boolean[n + 1];
            if (parts.length >= 3 && !parts[2].trim().isEmpty()) {
                String[] nums = parts[2].trim().split(",");
                for (String t : nums) {
                    int v = Integer.parseInt(t.trim());
                    if (v >= 1 && v <= n) present[v] = true;
                }
            }

            StringBuilder ans = new StringBuilder();
            ans.append(requestId).append(";");

            boolean first = true;
            for (int i = 1; i <= n; i++) {
                if (!present[i]) {
                    if (!first) ans.append(",");
                    ans.append(i);
                    first = false;
                }
            }

            byte[] send2 = ans.toString().getBytes(StandardCharsets.UTF_8);
            client.send(new DatagramPacket(send2, send2.length, ip, port));
        }
    }
}
