package TCP;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

// Data Stream
public class ChuyenDoiNP {
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        int port = 2207;
        String hi = "";
        
        try(Socket sock = new Socket()){
            sock.connect(new InetSocketAddress(host, port), 5000);
            sock.setSoTimeout(5000);
            
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
            
            dos.writeUTF(hi);
            dos.flush();
            
            int n = dis.readInt();
            String ans = Integer.toBinaryString(n);
            
            dos.writeUTF(ans);
            dos.flush();
        }
    }
}
