package UDP;

import java.net.*;
import java.math.BigInteger;

public class String4 {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int serverPort = 2207;
        String studentCode = "B22DCCN109";
        String qCode = "VHjCzr3X";

        try (DatagramSocket socket = new DatagramSocket()) {
            String requestStr = ";" + studentCode + ";" + qCode;
            byte[] requestData = requestStr.getBytes();
            DatagramPacket requestPacket = new DatagramPacket(
                    requestData, requestData.length,
                    InetAddress.getByName(serverAddress), serverPort
            );
            socket.send(requestPacket);

            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            String receivedStr = new String(receivePacket.getData(), 0, receivePacket.getLength());

            System.out.println(receivedStr);
            String[] parts = receivedStr.split(";");

            String requestId = parts[0];
            BigInteger a = new BigInteger(parts[1]);
            BigInteger b = new BigInteger(parts[2]);

            BigInteger sum = a.add(b);
            BigInteger difference = a.subtract(b);

            System.out.println(sum);
            System.out.println(difference);

            String sendStr = requestId + ";" + sum.toString() + "," + difference.toString();
            System.out.println(sendStr);
            byte[] sendData = sendStr.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(serverAddress), serverPort);
            socket.send(sendPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// - Thực hiện phép cộng (a + b) và trừ (a - b)