package tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class tcp_byt_chanle {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2206;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket client = new Socket(host, port)) {
            client.setSoTimeout(5000);

            InputStream is = client.getInputStream();
            OutputStream os = client.getOutputStream();

            os.write((studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8));
            os.flush();

            byte[] buf = new byte[4096];
            int n = is.read(buf);
            if (n <= 0) return;

            String response = new String(buf, 0, n, StandardCharsets.UTF_8).trim();
            String[] parts = response.split("\\s*,\\s*");

            List<Integer> even = new ArrayList<>();
            List<Integer> odd = new ArrayList<>();

            for (String p : parts) {
                if (p.isEmpty()) continue;
                int v = Integer.parseInt(p);
                if (v % 2 == 0) even.add(v);
                else odd.add(v);
            }

            Collections.sort(even);
            Collections.sort(odd);

            String result = even.toString() + ";" + odd.toString();
            os.write(result.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
