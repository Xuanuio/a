package TCP.DataStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client1{
    public static void main(String[] args) throws IOException {
        String host = "203.162.10.109";
        String msv = "B22DCCN925";
        String qCode = "FkJe2oh2";
        int port = 2207;
        
        String idLine = msv + ";" + qCode;
        
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);
            
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            dos.writeUTF(idLine);
            dos.flush();
            
            int a = dis.readInt();
            int b = dis.readInt();
            int ans = a + b;
            int res = a * b;
            dos.writeInt(ans);
            dos.writeInt(res);
            dos.flush();
        }
    }
}