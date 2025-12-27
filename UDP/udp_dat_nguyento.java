package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class udp_dat_nguyento {

    static boolean isPrime(int x) {
        if (x < 2) return false;
        for (int i = 2; i * i <= x; i++) if (x % i == 0) return false;
        return true;
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;

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
            String[] parts = resp.split(";");
            String requestId = parts[0].trim();

            String right = parts[1].trim();          
            int n = Integer.parseInt(right.split("\\s*,\\s*")[0]);

            StringBuilder sb = new StringBuilder();
            sb.append(requestId).append(";");

            int count = 0, x = 2;
            while (count < n) {
                if (isPrime(x)) {
                    if (count > 0) sb.append(",");
                    sb.append(x);
                    count++;
                }
                x++;
            }

            byte[] send2 = sb.toString().getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(send2, send2.length, ip, port));
        }
    }
}
