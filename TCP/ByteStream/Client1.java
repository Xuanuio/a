package TCP.ByteStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client1 {
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "RJ8f13q5";
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
            int m;
            
            while((m = is.read(data)) != -1){
                buffer.write(data, 0, m);
                if(m < data.length) break;
            }
            
            String rs = buffer.toString().trim();
            String[] s = rs.split("\\|");
            int n = s.length;
            int sum = 0;
            int[] a = new int[n];
            for(int i=0; i<n; i++){
                a[i] = Integer.parseInt(s[i]);
                sum += a[i];
            }
            
            os.write((sum + "").getBytes());
            os.flush();
        }
    }
}
