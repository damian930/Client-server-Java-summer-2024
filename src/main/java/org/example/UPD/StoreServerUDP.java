package org.example.UPD;

import org.example.Decrypter;
import org.example.Message;
import org.example.TCP.StoreClientTCP;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class StoreServerUDP extends Thread{
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[282]; // RSA cipher length
    private Decrypter decrypter;

    public StoreServerUDP() throws SocketException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.socket = new DatagramSocket(4445);
        this.decrypter = new Decrypter();
    }

    public void run() {
        running = true;

        while (running) {
            // receiving "start connection "message from a user
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            PublicKey key = decrypter.getPublicKey();
            buf = key.getEncoded();
            packet = new DatagramPacket(buf, buf.length, address, port);

            // sending public key to a client
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Sent public key");
            // receiving encrypted custom_packet

            buf = new byte[282];
            packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Received packet");


            try {
                decrypter.decrypt(packet.getData());
            } catch (IllegalBlockSizeException | BadPaddingException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Decrypted message");

            // sending decrypted message back (echo)
            Message m = decrypter.getDecrypted_message();
            packet = new DatagramPacket(m.getBytes(), m.getBytes().length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Echo sent");

            // end condition
            if (new String(m.getText()).equals("stop the server")) {
                running = false;
                System.out.println("Server stopped");
                continue;
            }

        }
        socket.close();

    }


}
