package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class udp_str_tong2soNP {
    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getByName("203.162.10.109");
        int port = 2208;
        String studentCode = "B22DCCN913";
        String qCode = "gTXDRnWj";

        try (DatagramSocket socket = new DatagramSocket()) {
            String req = ";" + studentCode + ";" + qCode;
            byte[] send = req.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(send, send.length, host, port));

            byte[] buf = new byte[2048];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            socket.receive(pk);

            String resp = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8).trim();
            String[] p = resp.split(";");
            String requestId = p[0];

            String[] nums = p[1].split(",");
            BigInteger a = new BigInteger(nums[0].trim(), 2);
            BigInteger b = new BigInteger(nums[1].trim(), 2);
            BigInteger sum = a.add(b);

            String ans = requestId + ";" + sum.toString(10);
            byte[] out = ans.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(out, out.length, host, port));
        }
    }
}
