package org.example;

import org.example.TCP.StoreClientTCP;
import org.example.TCP.StoreServerTCP;
import org.junit.jupiter.api.*;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class StoreClientTCP_Test {
    private StoreServerTCP server;
    private StoreClientTCP c1, c2, c3;

    @BeforeEach
    void setUp() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException {
        server = new StoreServerTCP();
        new Thread(() -> {
            try {
                server.start(1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        c1 = new StoreClientTCP();
        c2 = new StoreClientTCP();
        c3 = new StoreClientTCP();
    }

    @AfterEach
    void end() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidKeySpecException, InterruptedException {
        //c1.startConnection("localhost", 1);
        //Packet p = new Packet(new Message(1, 2, "stop the server".getBytes()), (byte) 3);
        //c1.sendPacket(p);
    }

    @Test
    void Client_whenServerEchosMessage_thenCorrect() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        Packet p1 = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);
        Packet p2 = new Packet(new Message(1, 2, "falls".getBytes()), (byte) 3);
        Packet p3 = new Packet(new Message(1, 2, "gravity".getBytes()), (byte) 3);


        Message m1, m2, m3;
        String t1, t2, t3;

        c1.startConnection("localhost", 1);
        c2.startConnection("localhost", 1);
        c3.startConnection("localhost", 1);

        c1.sendPacket(p1);
        m1 = c1.receiveMessage();
        t1 = new String(m1.getText());

        c2.sendPacket(p2);
        m2 = c2.receiveMessage();
        t2 = new String(m2.getText());

        c1.stopConnection();

        c3.sendPacket(p3);
        m3 = c3.receiveMessage();
        t3 = new String(m3.getText());

        assertEquals(t1, "flopper");
        assertEquals(t2, "falls");
        assertEquals(t3, "gravity");
    }

    @Test
    void Client_WhenServerDies_WhenConnecting() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        Packet p1 = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);
        Message m1;
        String t1;

        StoreServerTCP s2 = new StoreServerTCP();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                s2.start(2);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        StoreClientTCP c1 = new StoreClientTCP();
        c1.startConnection("localhost", 2);

        c1.sendPacket(p1);
        m1 = c1.receiveMessage();
        t1 = new String(m1.getText());

        assertEquals(t1, "flopper");
    }

    @Test
    void Client_WhenServerDies_WhenSendingPacket() throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InterruptedException {
        Packet p1 = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);
        Message m1;
        String t1;

        StoreServerTCP s2 = new StoreServerTCP();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                s2.start(2);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        StoreClientTCP c1 = new StoreClientTCP();
        c1.startConnection("localhost", 2);

        c1.sendPacket(p1);
        m1 = c1.receiveMessage();
        t1 = new String(m1.getText());

        assertEquals(t1, "flopper");
    }

}
