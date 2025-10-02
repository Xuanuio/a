package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class String5 {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int serverPort = 2207;

        String studentCode = "B22DCCN";
        String qCode = "";

        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket();

            String messageToServer = ";" + studentCode + ";" + qCode;
            byte[] sendData = messageToServer.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length,
                    InetAddress.getByName(serverAddress), serverPort
            );
            socket.send(sendPacket);

            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);

            String[] parts = response.split(";", 2);
            String requestId = parts[0];
            String[] numbersStr = parts[1].split(",");
            int[] numbers = Arrays.stream(numbersStr).mapToInt(Integer::parseInt).toArray();

            int max = Arrays.stream(numbers).max().orElseThrow();
            int min = Arrays.stream(numbers).min().orElseThrow();

            String resultMessage = requestId + ";" + max + "," + min;
            byte[] resultData = resultMessage.getBytes(StandardCharsets.UTF_8);
            DatagramPacket resultPacket = new DatagramPacket(resultData, resultData.length,
                    InetAddress.getByName(serverAddress), serverPort);
            socket.send(resultPacket);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }

    }
}

// - Tìm giá trị lớn nhất (max) và nhỏ nhất (min)