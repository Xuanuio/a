package UDP.DataType;

import java.io.*;
import java.math.BigInteger;
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
        String msv = "B22DCCN";
        String qCode = "";
        int port = 2207;
        
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
            
            String[] parts = msg.split(";", 2);
            String rid = parts[0];
            String[] nums = parts[1].split(";");
            
            BigInteger a = new BigInteger(nums[0]);
            BigInteger b = new BigInteger(nums[1]);
            
            BigInteger ab = a.add(b);
            BigInteger ba = a.subtract(b);
            
            System.out.println(a + " " + b + " " + ab + " "  + ba);
            
            String reply = rid + ";" + ab.toString() + "," + ba.toString();
            System.out.println(reply);
            byte[] out2 = reply.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out2, out2.length, addr, port));
        }
    }
}
