package be.uantwerpen.fti.se.com.network.server;
import java.io.*;
import java.net.*;

public class MultiThreadedTCPServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("TCP server multithreaded initiaded in port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String fileName = in.readLine();
            File file = new File(fileName);

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
                out.write("ERROR: File not found".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
