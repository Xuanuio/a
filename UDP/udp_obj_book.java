package UDP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import UDP.Book;

public class udp_obj_book {

    static String titleCaseWords(String s) {
        s = (s == null) ? "" : s.trim().replaceAll("\\s+", " ").toLowerCase();
        if (s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) {
            sb.append(Character.toUpperCase(w.charAt(0)));
            if (w.length() > 1) sb.append(w.substring(1));
            sb.append(' ');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    static String authorFmt(String s) {
        s = (s == null) ? "" : s.trim().replaceAll("\\s+", " ");
        if (s.isEmpty()) return "";
        String[] p = s.split(" ");
        String last = p[0].toUpperCase();              // HỌ = từ đầu tiên
        if (p.length == 1) return last + ", ";
        StringBuilder rest = new StringBuilder();
        for (int i = 1; i < p.length; i++) {
            String w = p[i].toLowerCase();
            rest.append(Character.toUpperCase(w.charAt(0)));
            if (w.length() > 1) rest.append(w.substring(1));
            if (i < p.length - 1) rest.append(" ");
        }
        return last + ", " + rest;
    }

    static String isbnFmt(String s) {
        String d = (s == null) ? "" : s.replaceAll("\\D", "");
        return d.length() == 13
                ? d.substring(0, 3) + "-" + d.substring(3, 4) + "-" + d.substring(4, 6) + "-" + d.substring(6, 12) + "-" + d.substring(12)
                : (s == null ? "" : s.trim());
    }

    static String dateFmt(String s) {
        String[] p = (s == null ? "" : s.trim()).split("-");
        if (p.length < 2) return (s == null ? "" : s.trim());
        String mm = p[1];
        if (mm.length() == 1) mm = "0" + mm;
        return mm + "/" + p[0];
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2209;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        InetAddress ip = InetAddress.getByName(host);

        try (DatagramSocket sock = new DatagramSocket()) {
            sock.setSoTimeout(5000);

            byte[] req = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(req, req.length, ip, port));

            byte[] buf = new byte[65507];
            DatagramPacket pkt = new DatagramPacket(buf, buf.length);
            sock.receive(pkt);

            byte[] all = Arrays.copyOfRange(pkt.getData(), 0, pkt.getLength());
            byte[] rid = Arrays.copyOfRange(all, 0, 8);
            byte[] obj = Arrays.copyOfRange(all, 8, all.length);

            Book b;
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(obj))) {
                b = (Book) ois.readObject();
            }

            b.setTitle(titleCaseWords(b.getTitle()));
            b.setAuthor(authorFmt(b.getAuthor()));
            b.setIsbn(isbnFmt(b.getIsbn()));
            b.setPublishDate(dateFmt(b.getPublishDate()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(b);
                oos.flush();
            }
            byte[] outObj = baos.toByteArray();

            byte[] out = new byte[8 + outObj.length];
            System.arraycopy(rid, 0, out, 0, 8);
            System.arraycopy(outObj, 0, out, 8, outObj.length);

            sock.send(new DatagramPacket(out, out.length, ip, port));
        }
    }
}
