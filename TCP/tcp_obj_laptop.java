package TCP;

import java.io.*;
import java.net.Socket;

public class tcp_obj_laptop {
    private static String fixName(String name) {
        String[] p = name.trim().split("\\s+");
        if (p.length <= 1) return name.trim();
        StringBuilder sb = new StringBuilder();
        sb.append(p[p.length - 1]);
        for (int i = 1; i < p.length - 1; i++) sb.append(" ").append(p[i]);
        sb.append(" ").append(p[0]);
        return sb.toString();
    }

    private static int fixQuantity(int q) {
        String s = new StringBuilder(String.valueOf(q)).reverse().toString();
        return Integer.parseInt(s);
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2209;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(studentCode + ";" + qCode);
            out.flush();

            Laptop lap = (Laptop) in.readObject();
            lap.setName(fixName(lap.getName()));
            lap.setQuantity(fixQuantity(lap.getQuantity()));

            out.writeObject(lap);
            out.flush();
        }
    }
}
