package TCP.ByteStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client2 {
    public static int isPrime(int n){
        if(n < 2) return 0;
        for(int i=2; i*i<=n; i++){
            if(n % i == 0) return 0;
        }
        return n;
    }
    
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "jvy98NB4";
        int port = 2206;
        
        String idLine = msv + ";" + qCode;
        
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 3000);
            socket.setSoTimeout(5000);
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            dos.write(idLine.getBytes());
            dos.flush();
            
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            
            while((bytesRead = dis.read(data)) != -1){
                buffer.write(data, 0, bytesRead);
                if(bytesRead < data.length) break;
            }
            
            String rs = buffer.toString().trim();
            
            String[] a = rs.split(",");
            int sum = 0;
            for(int i=0; i<a.length; i++){
                int x = Integer.parseInt(a[i]);
                sum += isPrime(x);
            }
            
            dos.write((sum + "").getBytes());
            dos.flush();
            
            dos.close();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }
}
