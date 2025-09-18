package TCP.CharacterStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Client3 {
    public static String rleEncode(String s) {
        StringBuilder sb = new StringBuilder();
        char prev = s.charAt(0);
        int cnt = 1;
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == prev) cnt++;
            else {
                sb.append(prev);
                if (cnt > 1) sb.append(cnt);
                prev = c;
                cnt = 1;
            }
        }
        sb.append(prev);
        if (cnt > 1) sb.append(cnt);
        return sb.toString();
    }

    public static String transformLine(String line) {
        return Arrays.stream(line.trim().split("\\s+"))
                .filter(w -> !w.isEmpty())
                .map(w -> new StringBuilder(w).reverse().toString())
                .map(Client3::rleEncode)           
                .collect(Collectors.joining(" "));
    }

    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        int port = 2208;
        String msv = "B22DCCN925";
        String qCode = "hpJfd8QZ";
        String idLine = msv + ";" + qCode;

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 3000);
            socket.setSoTimeout(5000);

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

           bw.write(idLine);
           bw.newLine();
           bw.flush();

           String line = br.readLine();
           String result = transformLine(line);

           bw.write(result);
           bw.newLine();
           bw.flush();
        }
    }
}
