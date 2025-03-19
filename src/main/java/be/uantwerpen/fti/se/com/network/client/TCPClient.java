package be.uantwerpen.fti.se.com.network.client;

import java.io.*;
import java.net.Socket;

public class TCPClient {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1";
        int serverPort = 5000;
        String fileName = "test.txt";

        try (Socket socket = new Socket(serverIP, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             InputStream in = socket.getInputStream()) {

            out.println(fileName);

            FileOutputStream fos = new FileOutputStream("TestTCP_Download.txt");
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            System.out.println("file read and downloaded as TestTCP_Download.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
