package TCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class tcp_obj_discount {

    static int sumDigitsIntPart(double price) {
        long x = (long) Math.floor(price);
        if (x < 0) x = -x;
        int sum = 0;
        if (x == 0) return 0;
        while (x > 0) {
            sum += (int) (x % 10);
            x /= 10;
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2209;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket client = new Socket(host, port)) {
            client.setSoTimeout(5000);

            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            out.writeObject(studentCode + ";" + qCode);
            out.flush();

            Product p = (Product) in.readObject();

            int discount = sumDigitsIntPart(p.getPrice());
            p.setDiscount(discount);

            out.writeObject(p);
            out.flush();
        }
    }
}
