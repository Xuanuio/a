package UDP.Object;

import UDP.Student;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.*;

public class Client3 {

    // "nguyen van tuan nam" -> "Nguyen Van Tuan Nam"
    static String fix1(String s){
        String[] a = s.trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            String w = a[i];
            sb.append(Character.toUpperCase(w.charAt(0)));
            if (w.length() > 1) sb.append(w.substring(1));
            if (i < a.length - 1) sb.append(' ');
        }
        return sb.toString();
    }

    // Email PTIT: lấy tên (họ cuối) + các chữ cái đầu của họ/đệm, bỏ dấu → @ptit.edu.vn
    // "nguyen van tuan nam" -> "namnvt@ptit.edu.vn"
    static String fix2(String s){
        String[] a = s.trim().toLowerCase(Locale.ROOT).split("\\s+");
        String last = a[a.length - 1];
        StringBuilder local = new StringBuilder(last);
        for (int i = 0; i < a.length - 1; i++) local.append(a[i].charAt(0));
        String ascii = Normalizer.normalize(local.toString(), Normalizer.Form.NFD)
                                 .replaceAll("\\p{M}", "")      // bỏ dấu
                                 .replaceAll("[^a-z0-9]", "");  // chỉ giữ a-z0-9
        return ascii + "@ptit.edu.vn";
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2209;
        String hello = ";";

        try (DatagramSocket sock = new DatagramSocket()) {
            sock.setSoTimeout(5000);

            // 1) Gửi chào
            byte[] out1 = hello.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, InetAddress.getByName(host), port));

            // 2) Nhận: [8 byte requestId][Object Student]
            byte[] buf = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            sock.receive(pk);

            byte[] data = pk.getData();
            int len = pk.getLength();
            byte[] rid = Arrays.copyOfRange(data, 0, 8);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, len - 8));
            Student st = (Student) ois.readObject();

            // 3) Chuẩn hóa và tạo email
            st.setName(fix1(st.getName()));
            st.setEmail(fix2(st.getName()));

            // 4) Gửi lại: [8 byte requestId][Object Student đã sửa]
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(rid);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(st);
            oos.flush();

            byte[] reply = baos.toByteArray();
            sock.send(new DatagramPacket(reply, reply.length, pk.getAddress(), pk.getPort()));
        }
    }
}