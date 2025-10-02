package UDP.Object;

import UDP.Product;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

public class Client {

    // Đảo thứ tự từ: "T520 thinkpad lenovo" -> "lenovo thinkpad T520"
    static String fixName(String s) {
        String[] a = s.trim().split("\\s+");
        String ans = a[a.length-1] + " ";
        for(int i = 1; i<a.length-1; i++){
            ans += a[i];
            ans += " ";
        }
        ans += a[0];
        return ans;
    }

    // Đảo chữ số: 1899 -> 9981
    static int fixQuantity(int x) {
        int t = Math.abs(x);
        String rev = new StringBuilder(Integer.toString(t)).reverse().toString();
        int v = Integer.parseInt(rev);
        return x < 0 ? -v : v;
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2209;
        String hello = ";";

        try (DatagramSocket sock = new DatagramSocket()) {
            sock.setSoTimeout(6000);

            // Gửi chào
            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, InetAddress.getByName(host), port));

            // Nhận: [8 byte reqId][Object Product]
            byte[] buf = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            sock.receive(pk);

            byte[] data = pk.getData();
            int len = pk.getLength();
            byte[] reqId = Arrays.copyOfRange(data, 0, 8);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, len - 8));
            Product p = (Product) ois.readObject();

            // Sửa dữ liệu
            p.setName(fixName(p.getName()));
            p.setQuantity(fixQuantity(p.getQuantity()));

            // Gửi lại: [8 byte reqId][Object Product đã sửa]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(reqId);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(p);
            oos.flush();

            byte[] reply = baos.toByteArray();
            sock.send(new DatagramPacket(reply, reply.length, pk.getAddress(), pk.getPort()));
        }
    }
}
