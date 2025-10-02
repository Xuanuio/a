package UDP.DataType;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPClient {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        String msv = "B22DCCN";
        String qCode = "";
        int port = 2207;
        
        try (DatagramSocket sock = new DatagramSocket()) {
            sock.setSoTimeout(7000);
            InetAddress addr = InetAddress.getByName(host);

            // a) Gửi ";studentCode;qCode"
            String hello = ";" + msv + ";" + qCode;
            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, addr, port));

            // b) Nhận "requestId;a1,a2,...,a50"
            byte[] buf = new byte[4096];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            sock.receive(pk);
            String msg = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8).trim();

            // Parse
            String[] parts = msg.split(";", 2);
            String requestId = parts[0];
            String[] nums = parts[1].split(",");

            // c) Tính max, min và gửi "requestId;max,min"
            int mn = Integer.MAX_VALUE, mx = Integer.MIN_VALUE;
            for (String t : nums) {
                int v = Integer.parseInt(t.trim());
                if (v < mn) mn = v;
                if (v > mx) mx = v;
            }
            String reply = requestId + ";" + mx + "," + mn;
            byte[] out2 = reply.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out2, out2.length, addr, port));
        }
    }
}
