package TCP;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

// Character Stream
public class TimKiemTK {
    public static String find(String str){
        String[] s = str.split(",");
        int n = s.length;
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; i++){
            if(s[i].endsWith(".edu")){
                if(sb.length() > 0) sb.append(",");
                sb.append(s[i]);
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        int port = 2208;
        String hi = "";
        
        try(Socket sock = new Socket()){
            sock.connect(new InetSocketAddress(host, port), 5000);
            sock.setSoTimeout(5000);
            
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            
            bw.write(hi);
            bw.newLine();
            bw.flush();
            
            String s = br.readLine();
            String ans = find(s);
            
            bw.write(ans);
            bw.newLine();
            bw.flush();
        }
    }
}
