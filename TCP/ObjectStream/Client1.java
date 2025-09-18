package TCP.ObjectStream;

import TCP.Product;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class Client1 {
    public static int sum(int n){
        int s = 0;
        while(n > 0){
            s += (n % 10);
            n /= 10;
        }
        return s;
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "RCrfbMe5";
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
            Product p = (Product) obj;
            double price = p.getPrice();
            
            int in = (int) Math.floor(price);
            int dis = sum(in);
            
            p.setDiscount(dis);
            oos.writeObject(p);
            oos.flush();
        }
    }
}
