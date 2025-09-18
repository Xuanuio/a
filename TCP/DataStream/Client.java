package TCP.DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

public class Client{
    public static int ck(String str){
        String[] s = Arrays.stream(str.split(",")).map(String::trim).toArray(String[]::new);
        int n = s.length;
        int k = 1;
        int cnt = 0;
        int[] a = new int[n];
        a[0] = Integer.parseInt(s[0]);
        for(int i=1; i<n; i++){
            a[i] = Integer.parseInt(s[i]);
            if(i == 1 && a[1] < a[0]) k = -1;
            int m = a[i] - a[i-1];
            if(m * k < 0){
                k = -k;
                cnt++;
            }
        }
        return cnt;
    }
    
    public static int sum(String str){
        String[] s = Arrays.stream(str.split(",")).map(String::trim).toArray(String[]::new);
        int n = s.length;
        int cnt = 0;
        int[] a = new int[n];
        a[0] = Integer.parseInt(s[0]);
        for(int i=1; i<n; i++){
            a[i] = Integer.parseInt(s[i]);
            cnt += Math.abs(a[i] - a[i-1]);
        }
        return cnt;
    }
    
    
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "TequHGaf";
        int port = 2207;
        
        String idLine = msv + ";" + qCode;
        
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            dos.writeUTF(idLine);
            dos.flush();
            
            String s = dis.readUTF().trim();
            int ans = ck(s);
            int res = sum(s);
            dos.writeInt(ans);
            dos.writeInt(res);
            dos.flush();
        }
    }
}