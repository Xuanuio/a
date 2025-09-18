package TCP.DataStream;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client3 {
    public static String ck(String str, int k){
        String[] s = Arrays.stream(str.split(",")).map(String::trim).toArray(String[]::new);
        int n = s.length;
        for(int i=0; i<n; i+=k){
            int l = i;
            int r = Math.min(i + k - 1, n - 1);
            while(l < r){
                String tmp = s[l];
                s[l] = s[r];
                s[r] = tmp;
                l++; r--;
            }
        }
        return String.join(",", s);
    }
    
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "0Lqt4DJf";
        int port = 2207;

        String idLine = msv + ";" + qCode;

        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            dos.writeUTF(idLine);
            dos.flush();
            
            int k = dis.readInt();
            String s = dis.readUTF().trim();
            String ans = ck(s, k);
            
            dos.writeUTF(ans);
            dos.flush();
        }
    }
}
