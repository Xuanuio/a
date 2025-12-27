package TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class tcp_cha_tansuatxuathien {
    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2208;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

            bw.write(studentCode + ";" + qCode);
            bw.newLine();
            bw.flush();

            String s = br.readLine();
            if (s == null) return;

            LinkedHashMap<Character, Integer> cnt = new LinkedHashMap<>();
            for (char c : s.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    cnt.put(c, cnt.getOrDefault(c, 0) + 1);
                }
            }

            StringBuilder ans = new StringBuilder();
            for (Map.Entry<Character, Integer> e : cnt.entrySet()) {
                if (e.getValue() > 1) {
                    ans.append(e.getKey()).append(":").append(e.getValue()).append(",");
                }
            }

            bw.write(ans.toString());
            bw.newLine();
            bw.flush();
        }
    }
}
