package org.example;

import org.example.TCP.StoreServerTCP;
import org.example.UPD.StoreClientUDP;
import org.example.UPD.StoreServerUDP;
import org.junit.jupiter.api.*;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class StoreUDP_Test {

    @Test
    void Client_whenServerEchosMessage_thenCorrect() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, InterruptedException {
        StoreServerUDP server = new StoreServerUDP();
        new Thread(server::start).start();

        TimeUnit.SECONDS.sleep(1);
        StoreClientUDP c1 = new StoreClientUDP();
        Packet p = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);
        Message m;
        m = c1.sendEcho(p);

        assertEquals("flopper", new String(m.getText()));

        m = c1.sendEcho(new Packet(new Message(1, 2, "stop the server".getBytes()), (byte) 3));

        assertEquals("stop the server", new String(m.getText()));

    }
}
