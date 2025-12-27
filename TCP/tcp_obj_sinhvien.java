package TCP;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class tcp_obj_sinhvien {
    static String toLetter(float gpa) {
        if (gpa >= 3.7f) return "A";
        if (gpa >= 3.0f) return "B";
        if (gpa >= 2.0f) return "C";
        if (gpa >= 1.0f) return "D";
        return "F";
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

            Student st = (Student) in.readObject();
            st.setGpaLetter(toLetter(st.getGpa()));

            out.writeObject(st);
            out.flush();
        }
    }
}
