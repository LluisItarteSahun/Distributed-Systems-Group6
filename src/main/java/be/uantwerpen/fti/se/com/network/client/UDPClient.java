package be.uantwerpen.fti.se.com.network.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 6000;
        String fileName = "test.txt";

        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] requestData = fileName.getBytes();
            DatagramPacket request = new DatagramPacket(requestData, requestData.length, InetAddress.getByName(serverIP), serverPort);
            socket.send(request);

            byte[] buffer = new byte[4096];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);

            FileOutputStream fos = new FileOutputStream("TestUDP_Download.txt");
            while (true) {
                socket.receive(response);
                fos.write(response.getData(), 0, response.getLength());
                if (response.getLength() < 4096) break;
            }
            fos.close();
            System.out.println("file saved and received as TestUDP_Download.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
