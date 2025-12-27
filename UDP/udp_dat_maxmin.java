package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class udp_dat_maxmin {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;
        String studentCode = "B22DCCN913";
        String qCode = "TbZog93F";

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            byte[] req = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(req, req.length, InetAddress.getByName(host), port));

            byte[] buf = new byte[8192];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            socket.receive(pk);

            String resp = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8).trim();
            String[] parts = resp.split(";", 3);
            String requestId = parts[0];

            String data = (parts.length > 1) ? parts[1].trim() : "";
            if (data.isEmpty() && parts.length == 3) data = parts[2].trim();

            String[] nums = data.split(",");
            int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;

            for (String x : nums) {
                x = x.trim();
                if (x.isEmpty()) continue;
                int v = Integer.parseInt(x);
                if (v > max) max = v;
                if (v < min) min = v;
            }

            String ans = requestId + ";" + max + "," + min;
            byte[] out = ans.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(out, out.length, InetAddress.getByName(host), port));
        }
    }
}
