package TCP;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class tcp_byt_lis {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2206;

        String studentCode = "B22DCCNxxx";
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

            int[] dp = new int[a.length];
            Arrays.fill(dp, 1);

            int maxLen = 0;
            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (a[i] > a[j]) dp[i] = Math.max(dp[i], dp[j] + 1);
                }
                maxLen = Math.max(maxLen, dp[i]);
            }

            os.write(String.valueOf(maxLen).getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
