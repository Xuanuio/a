package TCP.ByteStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client3 {
    public static int so(String s){
        return Integer.parseInt(s);
    }
    
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
            int bytesRead;
            
            while((bytesRead = is.read(data)) != -1){
                buffer.write(data, 0, bytesRead);
                if(bytesRead < data.length) break;
            }
            
            String rs = buffer.toString().trim();
            String a[] = rs.split(",");
            
            int sum = 0, n = a.length;
            int arr[] = new int[n];
            
            for(int i=0; i<n; i++){
                arr[i] = so(a[i]);
                sum += arr[i];
            }
            
            double tb = (2.0 * sum) / n;
            int n1 = arr[0], n2 = arr[1];
            double Min = Math.abs(n1 + n2 - tb);
            for(int i=0; i<n-1; i++){
                for(int j=i+1; j<n; j++){
                    double diff = Math.abs(arr[i] + arr[j] - tb);
                    if(diff < Min || (diff == Min && (arr[i] < n1 || (arr[i] == n1 && arr[j] < n2)))){
                        Min = diff;
                        n1 = arr[i];
                        n2 = arr[j];
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
