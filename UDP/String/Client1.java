package UDP.String;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client1 {
    public static String xuly(String s){
        String ans = "";
        ans += Character.toUpperCase(s.charAt(0));
        for(int i=1; i<s.length(); i++){
            ans += Character.toLowerCase(s.charAt(i));
        }
        return ans;
    }

    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        String host = "203.162.10.109";
        String msv = "";
        String qCode = "";
        int port = 2208;

        String hello = ";" + msv + ";" + qCode;

        try (DatagramSocket sock = new DatagramSocket()) {
            sock.setSoTimeout(5000);
            InetAddress addr = InetAddress.getByName(host);

            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, addr, port));

            byte[] buff = new byte[4096];
            DatagramPacket pk = new DatagramPacket(buff, buff.length);
            sock.receive(pk);
            String msg = new String(pk.getData(), 0, pk.getLength(), StandardCharsets.UTF_8).trim();

            String[] sep = msg.split(";");
            String rid = sep[0];
            String[] data = sep[1].split(" ");
            String ans = "";
            
            for(String x : data){
                ans += xuly(x);
                ans += " ";
            }

            String reply = rid + ";" + ans;
            byte[] out2 = reply.getBytes(StandardCharsets.UTF_8);
             sock.send(new DatagramPacket(out2, out2.length, addr, port));
        }
    }
}
