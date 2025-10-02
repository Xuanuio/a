package UDP;

import java.io.*;
import java.net.*;

public class _3Ik1jOaR {
    public static void main(String[] args) {
        String qCode="3Ik1jOaR";
        String studentCode="B22DCCN109";
        int port=2209;
        String ip="203.162.10.109";

        try(DatagramSocket socket=new DatagramSocket()){
            String ok=";"+studentCode+";"+qCode;

            DatagramPacket packet=new DatagramPacket(ok.getBytes(),ok.getBytes().length, InetAddress.getByName(ip),port);
            socket.send(packet);

            byte[] buff=new byte[1024];
            DatagramPacket rc=new DatagramPacket(buff, buff.length);
            socket.receive(rc);

            String rq= new String(rc.getData(),0,8);
            ByteArrayInputStream inputStream=new ByteArrayInputStream(rc.getData(),8,rc.getLength()-8);
            ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
            Employee e=(Employee) objectInputStream.readObject();

            e.fix();
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            byteArrayOutputStream.write(rq.getBytes());
            ObjectOutputStream outputStream=new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(e);

            outputStream.flush();
            byteArrayOutputStream.flush();
            DatagramPacket spacket=new DatagramPacket(byteArrayOutputStream.toByteArray(),byteArrayOutputStream.toByteArray().length, InetAddress.getByName(ip),port);

            socket.send(spacket);



        } catch (RuntimeException | SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
