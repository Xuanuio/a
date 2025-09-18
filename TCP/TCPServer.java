package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(2207);
        System.out.println("TCP Server is running on 2207 ...");
        while(true){
            // Kết nối
            Socket conn = server.accept();
            System.out.println("client: " + conn);
            // Gửi về client 2 số nguyeen a, b
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeInt((int)(Math.random()*100));
            dos.writeInt((int)(Math.random()*100));
            // conn.getOutputStream();
            // nhận về tổng
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            int sum = dis.readInt();
            System.out.println("sum: " + sum);
            // Đánh giá T/F
            
            // Đóng kết nối
            dos.close();
            dis.close();
            conn.close();
        }
    }
}
