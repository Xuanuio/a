package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ajdzkgq0 {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int serverPort = 2207;
        String studentCode = "B22DCCN109";
        String qCode = "UDP.ajdzkgq0";

        try (DatagramSocket socket = new DatagramSocket()) {
            String requestStr = ";" + studentCode + ";" + qCode;
            byte[] requestData = requestStr.getBytes(StandardCharsets.UTF_8);
            DatagramPacket requestPacket = new DatagramPacket(
                    requestData, requestData.length,
                    InetAddress.getByName(serverAddress), serverPort
            );
            socket.send(requestPacket);

            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            String receivedStr = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);

            String[] parts = receivedStr.split(";", 2);
            String requestId = parts[0];
            String data = parts.length > 1 ? parts[1] : "";

            String[] numberStrs = data.split(",");
            int[] numbers = new int[numberStrs.length];
            for (int i = 0; i < numberStrs.length; i++) {
                numbers[i] = Integer.parseInt(numberStrs[i].trim());
            }

            Arrays.sort(numbers);
            int secondMin = numbers.length >= 2 ? numbers[1] : numbers[0];
            int secondMax = numbers.length >= 2 ? numbers[numbers.length - 2] : numbers[numbers.length - 1];

            String sendStr = requestId + ";" + secondMax + "," + secondMin;
            byte[] sendData = sendStr.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(serverAddress), serverPort);
            socket.send(sendPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// - Chuyển dãy số thành mảng int và sắp xếp tăng dần
// - Lấy số lớn thứ 2 (secondMax) và số nhỏ thứ 2 (secondMin)