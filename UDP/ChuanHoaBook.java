package UDP;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Object Stream
public class ChuanHoaBook {
    static void fix2(Book b) {
        String[] nameArr = b.getTitle().trim().toLowerCase(Locale.ROOT).split("\\s+");
        StringBuilder title = new StringBuilder();
        for (int i = 0; i < nameArr.length; i++) {
            String w = nameArr[i];
            title.append(Character.toUpperCase(w.charAt(0)));
            if (w.length() > 1) title.append(w.substring(1));
            if (i < nameArr.length - 1) title.append(' ');
        }
        b.setTitle(title.toString());
        
        String[] au = b.getAuthor().trim().toLowerCase(Locale.ROOT).split("\\s+");
        if (au.length > 0) {
            String last = au[0].toUpperCase(Locale.ROOT);
            StringBuilder rest = new StringBuilder();
            for (int i = 1; i < au.length; i++) {
                if (i > 1) rest.append(' ');
                String t = au[i];
                rest.append(Character.toUpperCase(t.charAt(0)));
                if (t.length() > 1) rest.append(t.substring(1));
            }
            b.setAuthor(rest.length() == 0 ? last : (last + ", " + rest));
        }

        String[] d = b.getPublishDate().trim().split("-");
        if (d.length >= 2) b.setPublishDate(d[1] + "/" + d[0]);

        String digits = b.getIsbn().replaceAll("\\D+", "");
        if (digits.length() == 13) {
            b.setIsbn(digits.substring(0,3) + "-" + digits.substring(3,4) + "-" +
                      digits.substring(4,6) + "-" + digits.substring(6,12) + "-" +
                      digits.substring(12));
        }
    }
    
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, ClassNotFoundException {
        String host = "203.162.10.109";
        int port = 2209;
        String hi = "";
        
        try(DatagramSocket sock = new DatagramSocket()){
            sock.setSoTimeout(5000);
            
            byte[] out1 = hi.getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(out1, out1.length, InetAddress.getByName(host), port));
            
            byte[] buff = new byte[65535];
            DatagramPacket pk = new DatagramPacket(buff, buff.length);
            sock.receive(pk);
            
            byte[] data = pk.getData();
            int n = pk.getLength();
            byte[] rid = Arrays.copyOfRange(data, 0, 8);
            
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, n-8));
            Book b = (Book) ois.readObject();
            
            // b.fix1();
            fix2(b);
            
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
