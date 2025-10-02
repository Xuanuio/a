package UDP.Object;

import UDP.Customer;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Client2 {

    // a) "nguyen van hai duong" -> "DUONG, Nguyen Van Hai"
    static String fix1(String s) {
        String[] a = s.trim().replaceAll("\\s+", " ").split(" ");
        String last = a[a.length - 1].toUpperCase(Locale.ROOT);
        if (a.length == 1) return last;
        StringBuilder given = new StringBuilder();
        for (int i = 0; i < a.length - 1; i++) {
            String w = a[i].toLowerCase(Locale.ROOT);
            given.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
            if (i < a.length - 2) given.append(' ');
        }
        return last + ", " + given;
    }

    // b) "MM-dd-yyyy" -> "dd/MM/yyyy"
    static String fix2(String s) {
        DateTimeFormatter inF = DateTimeFormatter.ofPattern("MM-dd-uuuu");
        DateTimeFormatter outF = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        return LocalDate.parse(s, inF).format(outF);
    }

    // c) "nguyen van hai duong" -> "nvhduong" (bỏ dấu, chữ thường)
    static String fix3(String s) {
        String[] a = s.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder u = new StringBuilder();
        for (int i = 0; i < a.length - 1; i++) u.append(a[i].charAt(0));
        u.append(a[a.length - 1]);
        String ascii = Normalizer.normalize(u.toString(), Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return ascii.replaceAll("[^a-z0-9]", "");
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

            // Nhận: [8 byte reqId][Object Customer]
            byte[] buf = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            sock.receive(pk);

            byte[] data = pk.getData();
            int n = pk.getLength();
            byte[] rid = Arrays.copyOfRange(data, 0, 8);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, n - 8));
            Customer c = (Customer) ois.readObject();

            // Lấy tên gốc để sinh username rồi mới format name
            String rawName = c.getName();
            c.setUserName(fix3(rawName));
            c.setName(fix1(rawName));
            c.setDayOfBirth(fix2(c.getDayOfBirth()));

            // Gửi lại: [8 byte reqId][Object Customer đã sửa]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(rid);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(c);
            oos.flush();

            byte[] reply = baos.toByteArray();
            sock.send(new DatagramPacket(reply, reply.length, pk.getAddress(), pk.getPort()));
        }
    }
}