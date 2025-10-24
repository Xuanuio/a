package UDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Data type
public class TinhTongChuSo {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        String host = "203.162.10.109";
        int port = 2207;
        String hi = "";
        
        try(DatagramSocket sock = new DatagramSocket()){
            sock.setSoTimeout(5000);
            InetAddress addr = InetAddress.getByName(host);
            
            byte[] out1 = hi.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, InetAddress.getByName(host), port));
            
            byte[] buff = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buff, buff.length);
            sock.receive(pk);
            String msg = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8);
            
            String[] parts = msg.split(";");
            String rid = parts[0];
            String s = parts[1];
            
            int ans = 0;
            for(int i=0; i<s.length(); i++){
                ans += s.charAt(i) - '0';
            }
            
            String reply = rid + ";" + ans;
            byte[] out2 = reply.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out2, out2.length, addr, port));
        }
    }
}
