package TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class tcp_cha_sapxep {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2208;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            out.write(studentCode + ";" + qCode);
            out.newLine();
            out.flush();

            String s = in.readLine();
            if (s == null) return;

            String[] words = s.trim().split("\\s+");
            Arrays.sort(words);

            String ans = String.join(" ", words);

            out.write(ans);
            out.newLine();
            out.flush();
        }
    }
}
