package UDP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class LBnkU00B {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int serverPort = 2209;
        String studentCode = "B22DCCN109";
        String qCode = "LBnkU00B";

        try(DatagramSocket socket=new DatagramSocket()){
            String ok=";"+studentCode+";"+qCode;
            DatagramPacket packet=new DatagramPacket(ok.getBytes(),ok.getBytes().length, InetAddress.getByName(serverAddress),serverPort);

            socket.send(packet);

            byte[] buff=new byte[1024];

            DatagramPacket re=new DatagramPacket(buff, buff.length);
            socket.receive(re);

            String reqId=new String(re.getData(),0,8);

            ByteArrayInputStream bais=new ByteArrayInputStream(re.getData(),8,re.getLength()-8);
            ObjectInputStream ois=new ObjectInputStream(bais);

            Student student=(Student) ois.readObject();

            student.fixName();
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            baos.write(reqId.getBytes());
            ObjectOutputStream oos=new ObjectOutputStream(baos);

            oos.writeObject(student);
            oos.flush();

            DatagramPacket sendpacket=new DatagramPacket(baos.toByteArray(),baos.size(), InetAddress.getByName(serverAddress),serverPort);

            socket.send(sendpacket);







        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
