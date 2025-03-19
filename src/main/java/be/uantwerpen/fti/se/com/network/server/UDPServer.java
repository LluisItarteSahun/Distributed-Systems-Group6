package be.uantwerpen.fti.se.com.network.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer extends Thread {
    public static void main(String[] args) {
        int port = 6000;
        byte[] buffer = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP SERVER LISTENING TO PORT" + port);

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String fileName = new String(request.getData(), 0, request.getLength()).trim();
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                File file = new File(fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] fileBuffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = fis.read(fileBuffer)) != -1) {
                        DatagramPacket response = new DatagramPacket(fileBuffer, bytesRead, clientAddress, clientPort);
                        socket.send(response);
                    }
                    fis.close();
                    System.out.println("File SenT" + fileName);
                } else {
                    String errorMsg = "ERROR: file not found";
                    DatagramPacket response = new DatagramPacket(errorMsg.getBytes(), errorMsg.length(), clientAddress, clientPort);
                    socket.send(response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
