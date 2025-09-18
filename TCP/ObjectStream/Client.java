package TCP.ObjectStream;

import TCP.Address;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client {
    public static String ad(String s){
        String clean = s.replaceAll("[^\\p{L}\\p{Nd}\\s]", " ");
        clean = clean.trim().replaceAll("\\s+", " ");
        StringBuilder sb = new StringBuilder(clean.length());
        for(String w : clean.split(" ")){
            sb.append(Character.toUpperCase(w.charAt(0)));
            if(w.length() > 1) sb.append(w.substring(1).toLowerCase());
            sb.append(' ');
        }
        return sb.toString().trim();
    }
    
    public static String pc(String s){
        return s.replaceAll("[^0-9-]", "");
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "metHFXdW";
        int port = 2209;
        
        String idLine = msv + ";" + qCode;
        
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);
            
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            
            oos.writeObject(idLine);
            oos.flush();
            
            Object obj = ois.readObject();
            Address add = (Address) obj;
            
            String s1 = ad(add.getAddressLine());
            String s2 = pc(add.getPostalCode());
            add.setAddressLine(s1);
            add.setPostalCode(s2);
            
            oos.writeObject(add);
            oos.flush();
        }
    }
}
