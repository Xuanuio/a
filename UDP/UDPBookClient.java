package UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPBookClient {
    public static void main(String[] args) {
        String ip="203.162.10.109";
        String qcode="";
        String studenCode="B22DCCN";

        int port=2209;

        try(DatagramSocket socket=new DatagramSocket()){
            String ok=";"+studenCode+";"+qcode;

            DatagramPacket packet=new DatagramPacket(ok.getBytes(),ok.getBytes().length, InetAddress.getByName(ip),port);

            socket.send(packet);
            byte[] buf=new byte[1024];
            DatagramPacket rc=new DatagramPacket(buf, buf.length);

            socket.receive(rc);
            String rqId= new String(rc.getData(),0,8);
            ByteArrayInputStream bais=new ByteArrayInputStream(rc.getData(),8,rc.getLength()-8);
            ObjectInputStream ois=new ObjectInputStream(bais);

            Book customer=(Book) ois.readObject();

            customer.fix();

            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            baos.write(rqId.getBytes());
            ObjectOutputStream oos=new ObjectOutputStream(baos);

            oos.writeObject(customer);

            oos.flush();
            DatagramPacket spacket=new DatagramPacket(baos.toByteArray(),baos.toByteArray().length, InetAddress.getByName(ip),port);

            socket.send(spacket);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
