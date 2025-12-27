package TCP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class tcp_byt_biendoive1 {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2206;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            String req = studentCode + ";" + qCode;
            out.write(req.getBytes(StandardCharsets.UTF_8));
            out.flush();

            byte[] buf = new byte[1024];
            int len = in.read(buf);
            int n = Integer.parseInt(new String(buf, 0, len, StandardCharsets.UTF_8).trim());

            StringBuilder sb = new StringBuilder();
            int cnt = 0;
            long x = n;
            while (true) {
                sb.append(x).append(" ");
                cnt++;
                if (x == 1) break;
                x = (x % 2 == 0) ? (x / 2) : (3 * x + 1);
            }

            String ans = sb.toString().trim() + "; " + cnt;
            out.write(ans.getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }
}
