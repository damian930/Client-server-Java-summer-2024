package org.example.UPD;

import org.example.Encryptor;
import org.example.Message;
import org.example.Packet;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class StoreClientUDP extends Thread {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;
    private Encryptor encryptor;


    public StoreClientUDP() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByName("localhost");
    }

    public Message sendEcho(Packet custom_packet) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException, NoSuchPaddingException {
        // sending message to the server
        buf = "start connection".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);

        // receiving public key from the server
        buf = new byte[2048];
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        byte[] keyBytes = packet.getData();
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
        System.out.println("Public key received");

        // sending encrypted custom_packet
        this.encryptor = new Encryptor();
        encryptor.setPublicKey(publicKey);
        buf = encryptor.encrypt(custom_packet); // 274 bytes
        packet = new DatagramPacket(buf, buf.length, address, 4445);
        socket.send(packet);
        System.out.println("Sent encrypted custom_packet");

        // receiving an echo message
        buf = new byte[custom_packet.getBytes().length - 18]; // 26 is number of bytes inside custom_packet other than the message part
        packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        return new Message(packet.getData());

    }

    public void close() {
        socket.close();
    }

}
