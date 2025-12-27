package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;

public class udp_dat_timgtln {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2207;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        InetAddress ip = InetAddress.getByName(host);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000);

            String req = ";" + studentCode + ";" + qCode;
            byte[] sb = req.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(sb, sb.length, ip, port));

            byte[] buf = new byte[65507];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String resp = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8).trim();
            String[] parts = resp.split(";");
            String requestId = parts[0].trim();
            int n = Integer.parseInt(parts[1].trim());
            int k = Integer.parseInt(parts[2].trim());

            String[] numsStr = parts[3].trim().split(",");
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = Integer.parseInt(numsStr[i].trim());

            StringBuilder ans = new StringBuilder();
            Deque<Integer> dq = new ArrayDeque<>();

            for (int i = 0; i < n; i++) {
                while (!dq.isEmpty() && dq.peekFirst() <= i - k) dq.pollFirst();
                while (!dq.isEmpty() && a[dq.peekLast()] <= a[i]) dq.pollLast();
                dq.addLast(i);

                if (i >= k - 1) {
                    if (ans.length() > 0) ans.append(",");
                    ans.append(a[dq.peekFirst()]);
                }
            }

            String sendBack = requestId + ";" + ans;
            byte[] out = sendBack.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(out, out.length, ip, port));
        }
    }
}
