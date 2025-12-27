package TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class tcp_byt_daycontanglientiep {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2206;

        String studentCode = "B2DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port);
             OutputStream os = socket.getOutputStream();
             InputStream is = socket.getInputStream()) {

            socket.setSoTimeout(5000);

            os.write((studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8));
            os.flush();

            byte[] buf = new byte[4096];
            int n = is.read(buf);
            if (n <= 0) return;

            String s = new String(buf, 0, n, StandardCharsets.UTF_8).trim();
            int[] a = Arrays.stream(s.split("\\s*,\\s*")).mapToInt(Integer::parseInt).toArray();

            int best = 0, cur = 0;
            for (int i = 0; i < a.length; i++) {
                if (i == 0 || a[i] > a[i - 1]) cur++;
                else cur = 1;
                if (cur > best) best = cur;
            }

            os.write(String.valueOf(best).getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
