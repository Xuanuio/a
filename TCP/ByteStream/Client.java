package TCP.ByteStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "3RjL8RxJ";
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
            String[] s = rs.split(",");
            int n = s.length;
            int sum = 0;
            int[] a = new int[n];
            for(int i=0; i<n; i++){
                a[i] = Integer.parseInt(s[i]);
                sum += a[i];
            }
            double tb = (2.0 * sum) / n;
            int n1 = 0, n2 = 0;
            double Min = 99999999.9;
            for(int i=0; i<n-1; i++){
                for(int j=i+1; j<n; j++){
                    double tmp = Math.abs(a[i] + a[j] - tb);
                    if(tmp < Min){
                        Min = tmp;
                        n1 = a[i];
                        n2 = a[j];
                    }
                }
            }
            String ans = "";
            if(n1 > n2) ans = n2 + "," + n1;
            else ans = n1 + "," + n2;
            os.write(ans.getBytes());
            os.flush();
        }
    }
}
