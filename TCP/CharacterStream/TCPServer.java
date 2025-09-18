package TCP.CharacterStream;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(2208)) {
            System.out.println("Dummy server listening on 2208...");
            while (true) {
                Socket s = ss.accept();
                new Thread(() -> handle(s)).start();
            }
        }
    }
    static void handle(Socket s) {
        try (s) {
            s.setSoTimeout(5000); 
            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));

            String id = r.readLine();
            System.out.println("ID: " + id);

            String sample = "giHgWHwkLf0Rd0.io, I7jpjuRw13D.io, wXf6GP3KP.vn, "
                    + "MdpIzhxDVtTFTF.edu, TUHuMfn25chmw.vn, HHjE9.com, "
                    + "4hJld2m2yiweto.vn, y2L4SQwH.vn, s2aUrZGdzS.com, 4hXfJe9giAA.edu";
            w.write(sample);
            w.newLine();
            w.flush();

            String clientReply = r.readLine();
            System.out.println("Client .edu: " + clientReply);
            
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
