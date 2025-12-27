package tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tcp_byt_chuoicondainhat {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2206;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            out.write((studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8));
            out.flush();

            byte[] buf = new byte[4096];
            int n = in.read(buf);
            if (n <= 0) return;

            String resp = new String(buf, 0, n, StandardCharsets.UTF_8).trim();

            String[] parts = resp.split("\\s*,\\s*");
            List<Integer> list = new ArrayList<>();
            for (String p : parts) {
                if (!p.isEmpty()) list.add(Integer.parseInt(p));
            }

            Collections.sort(list);

            int minDiff = Integer.MAX_VALUE;
            int a = 0, b = 0;
            for (int i = 0; i < list.size() - 1; i++) {
                int diff = list.get(i + 1) - list.get(i);
                if (diff < minDiff) {
                    minDiff = diff;
                    a = list.get(i);
                    b = list.get(i + 1);
                }
            }

            String ans = minDiff + "," + a + "," + b;
            out.write(ans.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }
}
