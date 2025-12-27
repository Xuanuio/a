package UDP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class udp_obj_nhanvien {
    static String normName(String s) {
        String[] w = s.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String x : w) {
            if (x.isEmpty()) continue;
            sb.append(Character.toUpperCase(x.charAt(0))).append(x.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    static int sumYearDigits(String hireDate) { 
        int sum = 0;
        for (int i = 0; i < 4 && i < hireDate.length(); i++) {
            char c = hireDate.charAt(i);
            if (c >= '0' && c <= '9') sum += (c - '0');
        }
        return sum;
    }

    static String toDDMMYYYY(String d) { 
        String[] p = d.trim().split("-");
        return p[2] + "/" + p[1] + "/" + p[0];
    }

    public static void main(String[] args) throws Exception {
        InetAddress host = InetAddress.getByName("203.162.10.109");
        int port = 2209;
        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] req = (";" + studentCode + ";" + qCode).getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(req, req.length, host, port));

            byte[] buf = new byte[8192];
            DatagramPacket pk = new DatagramPacket(buf, buf.length);
            socket.receive(pk);

            byte[] data = pk.getData();
            int len = pk.getLength();

            byte[] rid = new byte[8];
            System.arraycopy(data, 0, rid, 0, 8);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data, 8, len - 8));
            Employee e = (Employee) ois.readObject();

            e.setName(normName(e.getName()));
            int x = sumYearDigits(e.getHireDate());
            e.setSalary(e.getSalary() * (1.0 + x / 100.0));
            e.setHireDate(toDDMMYYYY(e.getHireDate()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(rid);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(e);
            oos.flush();

            byte[] out = baos.toByteArray();
            socket.send(new DatagramPacket(out, out.length, host, port));
        }
    }
}
