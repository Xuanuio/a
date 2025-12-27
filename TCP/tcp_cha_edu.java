package tcp;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class tcp_cha_edu {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2208;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            out.write(studentCode + ";" + qCode);
            out.newLine();
            out.flush();

            String s = in.readLine();
            if (s == null) return;

            StringBuilder ans = new StringBuilder();
            for (String dom : s.split(",")) {
                dom = dom.trim();
                if (dom.toLowerCase().endsWith(".edu")) {
                    if (ans.length() > 0) ans.append(", ");
                    ans.append(dom);
                }
            }

            out.write(ans.toString());
            out.newLine();
            out.flush();
        }
    }
}
