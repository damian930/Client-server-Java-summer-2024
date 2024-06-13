package org.example.TCP;

import org.example.Encryptor;
import org.example.Message;
import org.example.Packet;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class StoreClientTCP {
    private Socket clientSocket;
    private DataOutputStream out;
    private DataInputStream in;
    private Encryptor encryptor;
    private int attempts = 1;
    private String ip;
    private int port;
    private Packet lastSentPacket;

    public void startConnection(String ip, int port) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, InterruptedException {
        try {
            this.ip = ip;
            this.port = port;
            this.clientSocket = new Socket(ip, port);
            this.encryptor = new Encryptor();
            System.out.println("Started connection with ip " + ip + ", port " + port);

            this.in = new DataInputStream(this.clientSocket.getInputStream());
            byte[] keyBytes = new byte[2048];
            int bytesRead = this.in.read(keyBytes);
            byte[] keyBytesTrimmed = Arrays.copyOf(keyBytes, bytesRead);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytesTrimmed));
            System.out.println("Received public key");

            this.encryptor.setPublicKey(publicKey);
            System.out.println("Encrypted packet");
            this.attempts = 0;
        }
        catch (SocketException e) { // if the server is unreachable
            System.out.println("Couldn't connect ip " + ip + ", port " + port);
            if(this.attempts == 5) {
                System.out.println("\tExiting");
                throw new SocketException();
            }
            System.out.println("\tAttempt " + ++this.attempts);
            TimeUnit.SECONDS.sleep(3);
            startConnection(ip, port); // if the server breaks
        }

    }

    public void sendPacket(Packet packet) throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        byte[] encrypted_packet;
        //System.out.println("Sleeping 3");
        while(true) {
            try {
                //TimeUnit.SECONDS.sleep(3);
                encrypted_packet = this.encryptor.encrypt(packet);
                this.out = new DataOutputStream(this.clientSocket.getOutputStream());
                this.out.write(encrypted_packet);
                this.out.flush();
                break;
            }
            catch (SocketException e) {
                System.out.println("Couldn't sent packet");
                System.out.println("\tReconnecting to the server again");
                this.attempts = 0;
                startConnection(this.ip, this.port);
                // it will either crash when reconnecting to the server completely or reconnect and send packet
            }
        }

        System.out.println("Sent encrypted packet");
        this.attempts = 0; // for receive message next function to not go recursively forever
        this.lastSentPacket = packet;
    }

    public Message receiveMessage() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        Message m;
        System.out.println("Waiting 3");
        //TimeUnit.SECONDS.sleep(3);
        try {
            m = new Message(receiveMessage(clientSocket.getInputStream()));
        }
        catch (SocketException e) {
            System.out.println("Couldn't receive packet");
            sendPacket(this.lastSentPacket);
            if(this.attempts == 3)  // in case if recursively forever. It shouldn't, but in case it does go wild
                throw new SocketException();
            m = new Message(receiveMessage(clientSocket.getInputStream()));
            // if when trying to receive something happens to the server
            // then user tries to reconnect and send it all over again
            // the order won`t change
        }
        System.out.println("Message received");
        return m;
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

    public void stopConnection() throws IOException, InterruptedException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        sendPacket(new Packet(new Message(1, 2, "disconnect me".getBytes()), (byte) 3));
        in.close();
        out.close();
        clientSocket.close();
        System.out.println("Disconnected");
    }
}
