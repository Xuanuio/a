package UDP.Object;

import UDP.Book;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

public class Client4 {

    // === Chuẩn hoá ngay trong client, theo đúng logic hàm fix() anh đưa ===
    static void fix(Book b) {
        // 1) Title: Viết hoa chữ đầu mỗi từ
        String[] nameArr = b.getTitle().trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder namers = new StringBuilder();
        for (int i = 0; i < nameArr.length; i++) {
            String w = nameArr[i];
            namers.append(Character.toUpperCase(w.charAt(0)));
            if (w.length() > 1) namers.append(w.substring(1));
            if (i != nameArr.length - 1) namers.append(' ');
        }
        b.setTitle(namers.toString());

        // 2) Author: "HỌ, Ten Dem Ten" (từ đầu viết HOA toàn bộ, sau đó Title Case)
        String[] au = b.getAuthor().trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder auname = new StringBuilder(au[0].toUpperCase(Locale.ROOT)).append(",");
        for (int i = 1; i < au.length; i++) {
            String t = au[i];
            auname.append(" ")
                  .append(Character.toUpperCase(t.charAt(0)));
            if (t.length() > 1) auname.append(t.substring(1));
        }
        b.setAuthor(auname.toString());

        // 3) publishDate: yyyy-MM-dd -> MM/yyyy
        String[] datearr = b.getPublishDate().split("-");
        b.setPublishDate(datearr[1] + "/" + datearr[0]);

        // 4) ISBN: định dạng 3-1-2-6-1 (giả định chuỗi isbn là 13 ký tự số)
        String isbn = b.getIsbn();
        b.setIsbn(isbn.substring(0,3) + "-" + isbn.substring(3,4) + "-" +
                  isbn.substring(4,6) + "-" + isbn.substring(6,12) + "-" +
                  isbn.substring(12));

        // (Tuỳ chọn) In kiểm tra
        // System.out.println(b.getTitle());
        // System.out.println(b.getAuthor());
        // System.out.println(b.getPublishDate());
        // System.out.println(b.getIsbn());
    }

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
            int len     = pk.getLength();
            byte[] rid  = Arrays.copyOfRange(data, 0, 8);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, len - 8));
            Book b = (Book) ois.readObject();

            // c) Chuẩn hoá theo fix() ngay trong client
            fix(b);

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
