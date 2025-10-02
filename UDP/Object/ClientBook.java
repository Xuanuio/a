import UDP.Book;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ClientBook {
    public static void main(String[] args) throws Exception {
        String host  = "203.162.10.109";
        int    port  = 2209;
        String hello = ";";

        try (DatagramSocket sock = new DatagramSocket()) {
            sock.setSoTimeout(6000);

            // a) Gửi chào
            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, InetAddress.getByName(host), port));

            // b) Nhận: [8 byte requestId][Object Book]
            byte[] buf = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            sock.receive(pk);

            byte[] data = pk.getData();
            int    len  = pk.getLength();
            byte[] rid  = Arrays.copyOfRange(data, 0, 8);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, len - 8));
            Book b = (Book) ois.readObject();

            // c) Chuẩn hoá theo fix()
            b.fix();

            // d) Gửi lại: [8 byte requestId][Object Book đã sửa]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(rid);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(b);
            oos.flush();

            byte[] reply = baos.toByteArray();
            sock.send(new DatagramPacket(reply, reply.length, pk.getAddress(), pk.getPort()));
        }
    }
}
