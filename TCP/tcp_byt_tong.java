package tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tcp_byt_tong{
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2206;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            String payload = studentCode + ";" + qCode;
            out.write(payload.getBytes(StandardCharsets.UTF_8));
            out.flush();

            byte[] buf = new byte[1024];
            int n = in.read(buf);                 // server thường trả 1 dòng ngắn dạng 2|5|9|11
            if (n <= 0) return;

            String recv = new String(buf, 0, n, StandardCharsets.UTF_8).trim();

            String[] parts = recv.split("\\|");
            long sum = 0;
            for (String p : parts) {
                p = p.trim();
                if (!p.isEmpty()) sum += Long.parseLong(p);
            }

            out.write(String.valueOf(sum).getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }
}
