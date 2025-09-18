package TCP.CharacterStream;

import static TCP.CharacterStream.Client1.find;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Client2 {
    public static String sx(String s){
        List<String> l = Arrays.stream(s.trim().split("\\s+"))
                               .collect(Collectors.toList());
        l.sort(Comparator.comparingInt(String::length));
        return String.join(", ", l);
    }

    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "ehazx9hQ";
        int port = 2208;

        String idLine = msv + ";" + qCode;

        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            
            bw.write(idLine);
            bw.newLine();
            bw.flush();

            String s = br.readLine();
           
            String res = sx(s);
            
            bw.write(res);
            bw.newLine();
            bw.flush();
            
            br.close();
            bw.close();
        }
    }
}