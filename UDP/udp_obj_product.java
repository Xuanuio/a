package UDP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import UDP.Product;

public class udp_obj_product {
    static String fixName(String name){
        if(name==null) return "";
        String[] p = name.trim().replaceAll("\\s+"," ").split(" ");
        if(p.length<=1) return name.trim();
        String tmp=p[0]; p[0]=p[p.length-1]; p[p.length-1]=tmp;
        return String.join(" ", p);
    }
    static int fixQty(int q){
        return Integer.parseInt(new StringBuilder(String.valueOf(q)).reverse().toString());
    }

    public static void main(String[] args) throws Exception {
        String host="203.162.10.109"; int port=2209;
        String studentCode="B22DCCNxxx", qCode="qCode";

        InetAddress ip = InetAddress.getByName(host);
        try(DatagramSocket sock=new DatagramSocket()){
            sock.setSoTimeout(5000);

            byte[] req = (";"+studentCode+";"+qCode).getBytes(StandardCharsets.UTF_8);
            sock.send(new DatagramPacket(req, req.length, ip, port));

            byte[] buf = new byte[65507];
            DatagramPacket pkt = new DatagramPacket(buf, buf.length);
            sock.receive(pkt);

            byte[] all = Arrays.copyOfRange(pkt.getData(), 0, pkt.getLength());
            byte[] rid = Arrays.copyOfRange(all, 0, 8);
            byte[] obj = Arrays.copyOfRange(all, 8, all.length);

            Product pr;
            try(ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(obj))){
                pr=(Product)ois.readObject();
            }

            pr.setName(fixName(pr.getName()));
            pr.setQuantity(fixQty(pr.getQuantity()));

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            try(ObjectOutputStream oos=new ObjectOutputStream(baos)){ oos.writeObject(pr); oos.flush(); }
            byte[] outObj = baos.toByteArray();

            byte[] out = new byte[8 + outObj.length];
            System.arraycopy(rid, 0, out, 0, 8);
            System.arraycopy(outObj, 0, out, 8, outObj.length);

            sock.send(new DatagramPacket(out, out.length, ip, port));
        }
    }
}
