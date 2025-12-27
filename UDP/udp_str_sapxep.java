package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class udp_str_sapxep {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] req = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(req, req.length, InetAddress.getByName(host), port));

            byte[] buf = new byte[4096];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            socket.receive(pk);

            String resp = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8).trim();
            String[] parts = resp.split(";", 2);
            String requestId = parts[0];
            String body = parts.length > 1 ? parts[1].trim() : "";

            String[] items = body.isEmpty() ? new String[0] : body.split(",");
            String[] outArr = new String[items.length];

            for (String it : items) {
                String[] kv = it.split(":");
                String text = kv[0];
                int pos = Integer.parseInt(kv[1].trim()) - 1;
                if (pos >= 0 && pos < outArr.length) outArr[pos] = text;
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < outArr.length; i++) {
                if (outArr[i] == null) continue;
                if (sb.length() > 0) sb.append(",");
                sb.append(outArr[i]);
            }

            String ans = requestId + ";" + sb;
            byte[] out = ans.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(out, out.length, InetAddress.getByName(host), port));
        }
    }
}
