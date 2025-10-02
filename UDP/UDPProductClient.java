package UDP;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class UDPProductClient {
    public static void main(String[] args) {
        String serverAddress = "203.162.10.109"; 
        int serverPort = 2209;
        String studentCode = "B22DCCN";
        String qCode = "";

        try (DatagramSocket socket = new DatagramSocket()) {

            String requestStr = ";" + studentCode + ";" + qCode;
            byte[] requestData = requestStr.getBytes(StandardCharsets.UTF_8);
            DatagramPacket requestPacket = new DatagramPacket(
                    requestData, requestData.length,
                    InetAddress.getByName(serverAddress), serverPort
            );
            socket.send(requestPacket);

            byte[] buffer = new byte[4096];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            byte[] requestIdBytes = new byte[8];
            System.arraycopy(buffer, 0, requestIdBytes, 0, 8);

            ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 8, receivePacket.getLength() - 8);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Product product = (Product) ois.readObject();


            product.setName(fixName(product.getName()));
            product.setQuantity(reverseInt(product.getQuantity()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(requestIdBytes);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(product);
            oos.flush();

            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                    InetAddress.getByName(serverAddress), serverPort);
            socket.send(sendPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String fixName(String name) {
        String[] parts = name.split(" ");
        if (parts.length < 2) return name;
        String temp = parts[0];
        parts[0] = parts[parts.length - 1];
        parts[parts.length - 1] = temp;
        return String.join(" ", parts);
    }

    private static int reverseInt(int number) {
        int reversed = 0;
        while (number != 0) {
            reversed = reversed * 10 + (number % 10);
            number /= 10;
        }
        return reversed;
    }
}
