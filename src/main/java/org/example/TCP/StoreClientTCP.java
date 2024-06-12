package org.example.TCP;

import org.example.Encryptor;
import org.example.Message;
import org.example.Packet;

import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
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

    public void startConnection(String ip, int port) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, ClassNotFoundException, InvalidKeyException, InvalidKeySpecException, InterruptedException {
        System.out.println("\nStarting connection");
        this.clientSocket = new Socket(ip, port);
        this.encryptor = new Encryptor();

        System.out.println("Started connection with ip " + ip + ", port " + port);

        System.out.println("Waiting for the public key");

        this.in = new DataInputStream(this.clientSocket.getInputStream());
        byte[] keyBytes = new byte[2048];
        int bytesRead = this.in.read(keyBytes);
        byte[] keyBytesTrimmed = Arrays.copyOf(keyBytes, bytesRead);

        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytesTrimmed));

        System.out.println("Received the public key");
        this.encryptor.setPublicKey(publicKey);
        System.out.println("Encrypted packet");
    }

    public byte[] sendPacket(Packet packet) throws IOException, InterruptedException {
        System.out.println("Sending packet");
        byte[] encrypted_packet = this.encryptor.encrypt(packet);

        this.out = new DataOutputStream(this.clientSocket.getOutputStream());
        this.out.write(encrypted_packet);
        this.out.flush();
        System.out.println("Packet was sent");

        return encrypted_packet;
    }

    public Message receiveMessage() throws IOException {
        //this.in = new DataInputStream(this.clientSocket.getInputStream());
        System.out.println("Receiving message");
        return new Message(receiveMessage(clientSocket.getInputStream()));
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

    public void stopConnection() throws IOException, InterruptedException {
        sendPacket(new Packet(new Message(1, 2, "disconnect me".getBytes()), (byte) 3));
        in.close();
        out.close();
        clientSocket.close();
        System.out.println("Disconnected");
    }
}
