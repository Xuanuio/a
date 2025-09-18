package TCP.CharacterStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Client1 {
    public static String find(String str){
        String[] s = str.split(",");
        int n = s.length;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; i++){
            if(s[i].trim().toLowerCase().endsWith(".edu")){
                if(sb.length() > 0) sb.append(",");
                sb.append(s[i]);
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "JfCKvvhK";
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
            String ans = find(s);
            
            bw.write(ans);
            bw.newLine();
            bw.flush();
            
            br.close();
            bw.close();
        }
    }
}