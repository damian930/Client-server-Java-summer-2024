package org.example.TCP;

import org.example.Message;
import org.example.Packet;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main_2 {
    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InterruptedException, IllegalBlockSizeException, BadPaddingException, ClassNotFoundException, InvalidKeySpecException {
        Message m;

        StoreClientTCP c1 = new StoreClientTCP();
        c1.startConnection("localhost", 1);
        c1.sendPacket(new Packet(new Message(1, 2, "stop the server".getBytes()), (byte) 3));
        m = c1.receiveMessage();
        System.out.println(new String(m.getText()));

        c1.stopConnection();

        /*c1.sendPacket(new Packet(new Message(1, 2, "falls".getBytes()), (byte) 3));
        m = c1.receiveMessage();
        System.out.println(new String(m.getText()));

        c1.stopConnection();


        StoreClientTCP c2 = new StoreClientTCP();
        c2.startConnection("localhost", 1);
        c2.sendPacket(new Packet(new Message(1, 2, "stop the server".getBytes()), (byte) 3));
        m = c2.receiveMessage();
        System.out.println(new String(m.getText()));

        c2.stopConnection();*/
    }
}
