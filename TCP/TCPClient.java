package TCP;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("localhost", 2207);
        // 172.11.53.45
        System.out.println("connected: " + client);
        // 
        DataInputStream dis = new DataInputStream(client.getInputStream());
        int a = dis.readInt();
        int b = dis.readInt();
        System.out.format("a: %d, b: %d \n", a, b);
        // 
        int sum = a + b;
        // 
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        dos.writeInt(sum);
        
        // 
        dos.close();
        dis.close();
        client.close();
    }
}
