package UDP.String;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client2 {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        String host = "203.162.10.109";
        String msv = "";
        String qCode = "";
        int port = 2208;
        
        String hello = ";" + msv + ";" + qCode;
        
        try(DatagramSocket sock = new DatagramSocket()){
            sock.setSoTimeout(5000);
            InetAddress addr = InetAddress.getByName(host);
            
            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, addr, port));
            
            byte[] buff = new byte[4096];
            DatagramPacket pk = new DatagramPacket(buff, buff.length);
            sock.receive(pk);
            String msg = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8).trim();
            
            String[] parts = msg.split(";");
            String rid = parts[0];
            String[] tok = parts[1].split(" ");
            
            String ans = "";
            Arrays.sort(tok, String.CASE_INSENSITIVE_ORDER);
            for(int i=tok.length - 1; i >= 0; i--){
                ans += tok[i].trim();
                if(i != 0) ans += ",";
            }
            
            String reply = rid + ";" + ans;
            byte[] out2 = reply.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out2, out2.length, addr, port));
        }
    }
}