package TCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class tcp_obj_chuanhoahoten {

    static String normalizeName(String name) {
        if (name == null) return "";
        String[] parts = name.trim().replaceAll("\\s+", " ").split(" ");
        if (parts.length == 0) return "";
        String last = parts[parts.length - 1].toUpperCase();

        StringBuilder rest = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (parts[i].isEmpty()) continue;
            String w = parts[i].toLowerCase();
            rest.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1));
            if (i < parts.length - 2) rest.append(" ");
        }
        return last + ", " + rest;
    }

    static String convertDob(String dob) {
        if (dob == null) return "";
        String[] p = dob.trim().split("-");
        if (p.length != 3) return dob.trim();
        String mm = p[0], dd = p[1], yyyy = p[2];
        if (dd.length() == 1) dd = "0" + dd;
        if (mm.length() == 1) mm = "0" + mm;
        return dd + "/" + mm + "/" + yyyy;
    }

    static String buildUserName(String name) {
        if (name == null) return "";
        String[] parts = name.trim().replaceAll("\\s+", " ").toLowerCase().split(" ");
        if (parts.length == 0) return "";
        String last = parts[parts.length - 1];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) sb.append(parts[i].charAt(0));
        }
        sb.append(last);
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        String host = "203.162.10.109";
        int port = 2209;

        String studentCode = "B22DCCNxxx";
        String qCode = "qCode";

        try (Socket socket = new Socket(host, port)) {
            socket.setSoTimeout(5000);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            out.writeObject(studentCode + ";" + qCode);
            out.flush();

            Customer c = (Customer) in.readObject();

            String rawName = c.getName();
            c.setName(normalizeName(rawName));
            c.setDayOfBirth(convertDob(c.getDayOfBirth()));
            c.setUserName(buildUserName(rawName));

            out.writeObject(c);
            out.flush();
        }
    }
}
