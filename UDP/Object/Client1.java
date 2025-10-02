package UDP.Object;

import UDP.Product;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client1 {
    static String fix1(String s){
        String[] a = s.split("\\s+");
        int n = a.length;
        String ans = a[n-1] + " ";
        for(int i=1; i<n-1; i++){
            ans += a[i];
            ans += " ";
        }
        ans += a[0];
        return ans;
    }
    
    static int fix2(int n){
        int m = 0;
        while(n > 0){
            m = m * 10 + n % 10;
            n /= 10;
        }
        return m;
    }
    
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, ClassNotFoundException {
        String host = "203.162.10.109";
        int port = 2209;
        String hello = ";";
        
        try(DatagramSocket sock = new DatagramSocket()){
            sock.setSoTimeout(5000);
            
            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, InetAddress.getByName(host), port));
            
            byte[] buff = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buff, buff.length);
            sock.receive(pk);
            
            byte[] data = pk.getData();
            int n = pk.getLength();
            byte[] rid = Arrays.copyOfRange(data, 0, 8);
            
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, n-8));
            Product p = (Product) ois.readObject();
            
            p.setName(fix1(p.getName()));
            p.setQuantity(fix2(p.getQuantity()));
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(rid);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(p);
            oos.flush();
            
            byte[] reply = baos.toByteArray();
            sock.send(new DatagramPacket(reply, reply.length, pk.getAddress(), pk.getPort()));
        }
    }
}
