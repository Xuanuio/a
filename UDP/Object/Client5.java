package UDP.Object;

import UDP.Employee;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Client5 {
    static String fix1(String s){
        String[] a = s.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<a.length; i++){
            String w = a[i];
            sb.append(Character.toUpperCase(w.charAt(0)));
            if(w.length() > 1) sb.append(w.substring(1));
            if(i < a.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
    
    static double fix2(double x, String s){
        String[] a = s.split("-");
        int m = 0;
        int n = Integer.parseInt(a[0]);
        while(n > 0){
            m += n % 10;
            n /= 10;
        }
        m += 100;
        x = x * m / 100;
        return Math.round(x * 100.0) / 100.0;
    }
    
    static String fix3(String s){
        DateTimeFormatter in = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        DateTimeFormatter out = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        return LocalDate.parse(s, in).format(out);
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
            Employee e = (Employee) ois.readObject();
            
            e.setName(fix1(e.getName()));
            e.setSalary(fix2(e.getSalary(), e.getHireDate()));
            e.setHireDate(fix3(e.getHireDate()));
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(rid);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(e);
            oos.flush();
            
            byte[] reply = baos.toByteArray();
            sock.send(new DatagramPacket(reply, reply.length, pk.getAddress(), pk.getPort()));
        }
    }
}
