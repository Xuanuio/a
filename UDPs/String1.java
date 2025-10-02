package UDP;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class AgOky3PZ {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109";
        int serverPort = 2208;
        String studentCode = "B22DCCN109";
        String qCode = "UDP.AgOky3PZ";

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

            String normalizedData = normalizeString(data);

            String sendStr = requestId + ";" + normalizedData;
            byte[] sendData = sendStr.getBytes(StandardCharsets.UTF_8);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(serverAddress), serverPort);
            socket.send(sendPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String normalizeString(String input) {
        String[] words = input.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                sb.append(Character.toUpperCase(words[i].charAt(0)));
                if (words[i].length() > 1) {
                    sb.append(words[i].substring(1).toLowerCase());
                }
                if (i < words.length - 1) sb.append(" ");
            }
        }
        return sb.toString();
    }
}

// - Chuẩn hóa dữ liệu data (chữ cái đầu viết hoa, còn lại viết thường)