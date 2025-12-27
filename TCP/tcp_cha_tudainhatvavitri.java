package TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tcp_cha_tudainhatvavitri {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2208;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket client = new Socket(host, port)) {
            client.setSoTimeout(5000);

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));

            out.write(studentCode + ";" + qCode);
            out.newLine();
            out.flush();

            String s = in.readLine();
            if (s == null) return;

            String[] words = s.trim().split("\\s+");
            String best = "";
            for (String w : words) {
                if (w.length() > best.length()) best = w;
            }

            int pos = s.indexOf(best);

            out.write(best);
            out.newLine();
            out.flush();

            out.write(String.valueOf(pos));
            out.newLine();
            out.flush();
        }
    }
}
