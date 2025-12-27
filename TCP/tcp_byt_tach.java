package tcp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tcp_byt_tach {
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

            byte[] buf = new byte[4096];
            int n = in.read(buf);
            String resp = new String(buf, 0, n, StandardCharsets.UTF_8).trim();

            int sum = 0;
            for (String s : resp.split("\\|")) {
                if (!s.isEmpty()) sum += Integer.parseInt(s.trim());
            }

            out.write(String.valueOf(sum).getBytes(StandardCharsets.UTF_8));
            out.flush();
        }
    }
}
