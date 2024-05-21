package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class ReceiverTest {

    @Test
    void testReceiving_ValidPacket() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Receiver r = new Receiver();
        Sender s = new Sender();
        s.setReceiversKey(r.getPublicKey());
        s.create_packet(1, 2, "flopper", (byte) 3);
        Message message_received = r.receive_packet(s.send_packet());

        Message correct_message = new Message(1, 2, "flopper".getBytes());
        assertEquals(correct_message, message_received);
    }

    @Test
    void testReceiving_Corrupted_Or_Null_Packet() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Receiver r = new Receiver();
        Sender s = new Sender();
        s.setReceiversKey(r.getPublicKey());
        s.create_packet(1, 2, "flopper", (byte) 3);
        byte[] corrupted = s.send_packet();
        corrupted[5] = 123;
        Message message_received = r.receive_packet(corrupted);

        assertNull(message_received);
    }
}