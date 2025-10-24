package TCP;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Byte Stream
public class BienDoiVe1 {
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        int port = 2206;
        String hi = "";
        
        try (Socket sock = new Socket()) {
            sock.connect(new InetSocketAddress(host, port), 5000);
            sock.setSoTimeout(5000);

            InputStream is = sock.getInputStream();
            OutputStream os = sock.getOutputStream();

            os.write(hi.getBytes(StandardCharsets.UTF_8));
            os.flush();

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int m;
            while ((m = is.read(data)) != -1) {
                buffer.write(data, 0, m);
                if (m < data.length) break;
            }
            String rs = buffer.toString(StandardCharsets.UTF_8).trim();
            int n = Integer.parseInt(rs);

            StringBuilder seq = new StringBuilder();
            int len = 0;
            int cur = n;
            while (true) {
                if (len > 0) seq.append(' ');
                seq.append(cur);
                len++;
                if (cur == 1) break;
                cur = (cur % 2 == 0) ? (cur / 2) : (3 * cur + 1);
            }

            String out = seq + "; " + len; 
            os.write(out.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }
    }
}
