package UDP;

import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class udp_dat_tinhtong {

    static int sumDigits(BigInteger number) {
        String s = number.abs().toString();
        int sum = 0;
        for (int i = 0; i < s.length(); i++) sum += s.charAt(i) - '0';
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        InetAddress ip = InetAddress.getByName(host);

        try (DatagramSocket client = new DatagramSocket()) {
            client.setSoTimeout(5000);

            String req = ";" + studentCode + ";" + qCode;
            byte[] send1 = req.getBytes(StandardCharsets.UTF_8);
            client.send(new DatagramPacket(send1, send1.length, ip, port));

            byte[] buf = new byte[65507];
            DatagramPacket pkt = new DatagramPacket(buf, buf.length);
            client.receive(pkt);

            String resp = new String(pkt.getData(), 0, pkt.getLength(), StandardCharsets.UTF_8).trim();
            String[] parts = resp.split(";");
            String requestId = parts[0].trim();
            BigInteger num = new BigInteger(parts[1].trim());

            int sum = sumDigits(num);

            String ans = requestId + ";" + sum;
            byte[] send2 = ans.getBytes(StandardCharsets.UTF_8);
            client.send(new DatagramPacket(send2, send2.length, ip, port));
        }
    }
}
