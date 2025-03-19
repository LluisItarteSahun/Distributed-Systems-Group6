package be.uantwerpen.fti.se.com.network.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor TCP iniciado en el puerto " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());
                handleClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String fileName = in.readLine();
            File file = new File(fileName);

            System.out.println("Looking for the file: " + file.getAbsolutePath());

            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                fis.close();
                System.out.println("File sent: " + fileName);
            } else {
                System.out.println("ERROR: File not found");
                out.write("ERROR: File not found".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

