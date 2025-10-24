package TCP;

import java.io.*;
import java.net.*;

public class DaoNguoc {

    private static String daoChuoi(String str) {
        String[] words = str.trim().split("\\s+");
        return words[words.length - 1] + " " + 
               String.join(" ", java.util.Arrays.copyOfRange(words, 1, words.length - 1)) + " " + 
               words[0];
    }

    private static int daoSo(int n) {
        int rev = 0;
        while (n > 0) {
            rev = rev * 10 + n % 10;
            n /= 10;
        }
        return rev;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String host = "203.162.10.109";
        int port = 2209;
        String code = "";

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 5000);
            socket.setSoTimeout(5000);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(code);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Laptop laptop = (Laptop) ois.readObject();

            laptop.setQuantity(daoSo(laptop.getQuantity()));
            laptop.setName(daoChuoi(laptop.getName()));

            oos.writeObject(laptop);
            oos.flush();
        }
    }
}
