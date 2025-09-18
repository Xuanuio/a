package TCP.CharacterStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static String sx(String str){
        String[] s = str.trim().split("\\s+");
        int m = 0, n = s.length;
        for(int i=0; i<n; i++){
            s[i] = s[i].trim();
            if(s[i].length() > m) m = s[i].length();
        }
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j <= m; j++) {
            for (String x : s) {
                if (!x.isEmpty() && x.length() == j) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(x);
                }
            }
        }
        return sb.toString();
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
            
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            bw.write(idLine);
            bw.newLine();
            bw.flush();
            
            String s = br.readLine();
            String ans = sx(s);
            
            bw.write(ans);
            bw.newLine();
            bw.flush();

            br.close();
            bw.close();
        }
    }
}
