package org.example.TCP;

import org.example.Decrypter;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class StoreServerTCP {
    private ServerSocket serverSocket;
    private final Decrypter decrypter = new Decrypter();

    public StoreServerTCP() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
    }

    public void start(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server started");
        while (true) {
            new EchoClientHandler(this.serverSocket.accept(), this.decrypter).start();

            // dont know how to stop the server
        }
    }

    public void stop() throws IOException {
        serverSocket.close();
        System.out.println("Server stopped");
    }

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;
        private final Decrypter decrypter;

        public EchoClientHandler(Socket socket, Decrypter decrypter) {
            this.clientSocket = socket;
            this.decrypter = decrypter;
            System.out.println("\nUser added.");
        }

        public void run() {
            try {
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                out.write(this.decrypter.getPublicKey().getEncoded());
                out.flush();
                System.out.println("Sent public key");

                do {
                    byte[] encrypted = receiveMessage(in);
                    this.decrypter.decrypt(encrypted);
                    System.out.println("Received encrypted packet");

                    out.write(decrypter.getDecrypted_message().getBytes());
                    out.flush();
                    System.out.println("Sent echo message");

                } while (!new String(decrypter.getDecrypted_message().getText()).equals("disconnect me"));

                in.close();
                out.close();
                clientSocket.close();
                System.out.println("User deleted");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private byte[] receiveMessage(InputStream inputStream) throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, bytesRead);
                if (bytesRead < 1024) {
                    break;
                }
            }
            buffer.flush();
            return buffer.toByteArray();
        }

    }


}
