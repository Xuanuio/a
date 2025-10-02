package UDP.DataType;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client3 {
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
            
            String[] parts = msg.split(";");
            String rid = parts[0];
            String[] nums = parts[1].split(",");
            
            int n = nums.length;
            int[] a = new int[n];
            for(int i=0; i<n; i++){
                a[i] = Integer.parseInt(nums[i]);
            }
            Arrays.sort(a);
            
            String reply = rid + ";" + a[n-2] + "," + a[1];
            byte[] out2 = reply.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out2, out2.length, addr, port));
        }
    }
}
