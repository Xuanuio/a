package TCP.GPT;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(2207);

        while (true) {
            Socket conn = server.accept();

            Thread t = new Thread(new ClientHandler(conn));
            t.start();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream())
        ) {
            int a = (int)(Math.random() * 100);
            int b = (int)(Math.random() * 100);
            dos.writeInt(a);
            dos.writeInt(b);
            dos.flush();

            int sum = dis.readInt();

            dos.flush();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignore) {}
        }
    }
}
