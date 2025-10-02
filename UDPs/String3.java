package UDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;

public class iVtJpJKM {
    public static void main(String[] args) {

        String ip="203.162.10.109";
        int port=2208;
        String qcode="iVtJpJKM";
        String studentCode="B22DCCN109";

        try(DatagramSocket socket=new DatagramSocket()){
            String rq=";"+studentCode+";"+qcode;
            DatagramPacket packet=new DatagramPacket(rq.getBytes(),rq.getBytes().length, InetAddress.getByName(ip),port);

            socket.send(packet);

            byte[] buff =new byte[1024];
            DatagramPacket recieve= new DatagramPacket(buff,buff.length);

            socket.receive(recieve);

            String rs=new String(recieve.getData(),0,recieve.getLength());
            System.out.println("Nhan duoc: "+rs);
            String[] ok=rs.split(";");
            String rqID=ok[0];
            String[] arr=ok[1].split("\\s+");

            Arrays.sort(arr, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return -o1.toLowerCase().compareTo(o2.toLowerCase());
                }
            });

            String end=String.join(",",arr);
            end=rqID+";"+end;

            DatagramPacket sendpacket=new DatagramPacket(end.getBytes(),end.getBytes().length, InetAddress.getByName(ip),port);

            socket.send(sendpacket);

            System.out.println(end);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

// - Sắp xếp mảng chuỗi theo thứ tự giảm dần (không phân biệt hoa/thường)