package UDP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import UDP.Student;

public class udp_obj_mail {
    static String normName(String s){
        s = (s==null) ? "" : s.trim().replaceAll("\\s+"," ").toLowerCase();
        if(s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        for(String w: s.split(" ")){
            sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
        }
        sb.setLength(sb.length()-1);
        return sb.toString();
    }

    static String makeEmail(String normName){
        String[] w = normName.toLowerCase().trim().replaceAll("\\s+"," ").split(" ");
        StringBuilder sb = new StringBuilder(w[w.length-1]);
        for(int i=0;i<w.length-1;i++) sb.append(w[i].charAt(0));
        return sb.append("@ptit.edu.vn").toString();
    }

    public static void main(String[] args) throws Exception {
        String host="203.162.10.109"; int port=2209;
        String studentCode="B22DCCNxxx", qCode="qCode";

        try(DatagramSocket sock=new DatagramSocket()){
            sock.setSoTimeout(5000);
            InetAddress ip=InetAddress.getByName(host);

            byte[] req=(";"+studentCode+";"+qCode).getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(req, req.length, ip, port));

            byte[] buf=new byte[65507];
            DatagramPacket pkt=new DatagramPacket(buf, buf.length);
            sock.receive(pkt);

            byte[] all = Arrays.copyOfRange(pkt.getData(), 0, pkt.getLength());
            byte[] rid = Arrays.copyOfRange(all, 0, 8);
            byte[] obj = Arrays.copyOfRange(all, 8, all.length);

            Student st;
            try(ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(obj))){
                st=(Student)ois.readObject();
            }

            String n = normName(st.getName());
            st.setName(n);
            st.setEmail(makeEmail(n));

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            try(ObjectOutputStream oos=new ObjectOutputStream(baos)){ oos.writeObject(st); oos.flush(); }
            byte[] outObj = baos.toByteArray();

            byte[] out = new byte[8 + outObj.length];
            System.arraycopy(rid, 0, out, 0, 8);
            System.arraycopy(outObj, 0, out, 8, outObj.length);

            sock.send(new DatagramPacket(out, out.length, ip, port));
        }
    }
}
