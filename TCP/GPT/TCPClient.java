package TCP.GPT;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 2207;

        try (
            Socket client = new Socket(host, port);
            DataInputStream dis = new DataInputStream(client.getInputStream());
            DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        ) {
            int a = dis.readInt();
            int b = dis.readInt();
            
            int sum = a + b;
            dos.writeInt(sum);
            dos.flush();
            
            String result = dis.readUTF();
            
        } catch (IOException e) {
            System.err.println("❌ Lỗi client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
