package TCP.ByteStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client4 {
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "MzMuRY9p";
        int port = 2206;
        
        String idLine = msv + ";" + qCode;
        
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);
            
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            
            os.write(idLine.getBytes());
            os.flush();
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            
            while((bytesRead = is.read(data)) != -1){
                buffer.write(data, 0, bytesRead);
                if(bytesRead < data.length) break;
            }
            
            String rs = buffer.toString().trim();
            
            String[] s = rs.split(",");
            int n = s.length;
            int[] a = new int[n];
            int l = 0, r = 0, m = 0, al = 0, ar = 0, j = 0;
            for(int i=0; i<n; i++){
                a[i] = Integer.parseInt(s[i]);
                r += a[i];
            }
            r -= a[0];
            m = r;
            al = 0; 
            ar = r;
            for(int i=1; i<n; i++){
                l += a[i-1];
                r -= a[i];
                int cl = Math.abs(r - l);
                if(cl < m){
                    j = i;
                    al = l;
                    ar = r;
                    m = cl;
                }
            }
            String ans = j + "," + al +"," + ar + "," + m;
            os.write(ans.getBytes());
            os.flush();
        }
    }
}
