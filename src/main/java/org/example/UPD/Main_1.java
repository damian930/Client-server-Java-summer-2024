package org.example.UPD;

import org.example.Message;
import org.example.Packet;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main_1 {
    public static void main(String[] args) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, InterruptedException {
        StoreServerUDP server = new StoreServerUDP();
        server.start();

        StoreClientUDP c1 = new StoreClientUDP();
        Packet p = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);
        Message m;
        m = c1.sendEcho(p);

        System.out.println(new String(m.getText()));

    }

}
