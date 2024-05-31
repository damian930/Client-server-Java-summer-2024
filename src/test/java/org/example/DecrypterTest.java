package org.example;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

public class DecrypterTest {

    @Test
    void testDecryption_CRCs_Valid() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InterruptedException {
        Decrypter d = new Decrypter();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, d.getPublicKey());
        String plain_text = "flopper";
        byte[] encrypted_text = cipher.doFinal(plain_text.getBytes());

        // creating an encrypted packet
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1 + 1 + 8 + 4 + 2 + 4 + 4 + encrypted_text.length + 2]);
        buffer.put((byte) 13);
        buffer.put((byte) 3);
        buffer.putLong(Packet.getMessageCounter() - 1);
        buffer.putInt(encrypted_text.length + 4 + 4);
        buffer.putShort(Packet.convert_to_crc16(buffer.array(), 0, 14));
        buffer.putInt(1);
        buffer.putInt(2);
        buffer.put(encrypted_text);
        buffer.putShort(Packet.convert_to_crc16(buffer.array(), 16, 4 + 4 + encrypted_text.length));

        d.decrypt(buffer.array());
        Message m = d.getDecrypted_message();

        assertEquals(m, new Message(1, 2, "flopper".getBytes()));
    }

    @Test
    void testDecryption_CRC1_InValid() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InterruptedException {
        Decrypter d = new Decrypter();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, d.getPublicKey());
        String plain_text = "flopper";
        byte[] encrypted_text = cipher.doFinal(plain_text.getBytes());

        // creating an encrypted packet
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1 + 1 + 8 + 4 + 2 + 4 + 4 + encrypted_text.length + 2]);
        buffer.put((byte) 13);
        buffer.put((byte) 3);
        buffer.putLong(Packet.getMessageCounter() - 1);
        buffer.putInt(encrypted_text.length + 4 + 4);
        buffer.putShort((short) (Packet.convert_to_crc16(buffer.array(), 0, 14) - 1));
        buffer.putInt(1);
        buffer.putInt(2);
        buffer.put(encrypted_text);
        buffer.putShort(Packet.convert_to_crc16(buffer.array(), 16, 4 + 4 + encrypted_text.length));

        d.decrypt(buffer.array());
        Message m = d.getDecrypted_message();

        assertNull(m);
    }

    @Test
    void testDecryption_CRC2_InValid() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InterruptedException {
        Decrypter d = new Decrypter();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, d.getPublicKey());
        String plain_text = "flopper";
        byte[] encrypted_text = cipher.doFinal(plain_text.getBytes());

        // creating an encrypted packet
        ByteBuffer buffer = ByteBuffer.wrap(new byte[1 + 1 + 8 + 4 + 2 + 4 + 4 + encrypted_text.length + 2]);
        buffer.put((byte) 13);
        buffer.put((byte) 3);
        buffer.putLong(Packet.getMessageCounter() - 1);
        buffer.putInt(encrypted_text.length + 4 + 4);
        buffer.putShort(Packet.convert_to_crc16(buffer.array(), 0, 14));
        buffer.putInt(1);
        buffer.putInt(2);
        buffer.put(encrypted_text);
        buffer.putShort((short) (Packet.convert_to_crc16(buffer.array(), 16, 4 + 4 + encrypted_text.length) - 1));

        d.decrypt(buffer.array());
        Message m = d.getDecrypted_message();

        assertNull(m);
    }

    @Test
    void testDecryption_RuntimeException_Thrown() {
        byte[] encrypted_text = new byte[300];

        ByteBuffer buffer = ByteBuffer.wrap(new byte[1 + 1 + 8 + 4 + 2 + 4 + 4 + encrypted_text.length + 2]);
        buffer.put((byte) 13);
        buffer.put((byte) 3);
        buffer.putLong(Packet.getMessageCounter() - 1);
        buffer.putInt(encrypted_text.length + 4 + 4);
        buffer.putShort(Packet.convert_to_crc16(buffer.array(), 0, 14));
        buffer.putInt(1);
        buffer.putInt(2);
        buffer.put(encrypted_text);
        buffer.putShort((short) (Packet.convert_to_crc16(buffer.array(), 16, 4 + 4 + encrypted_text.length) - 1));

        assertThrows(RuntimeException.class, () -> new Decrypter().decrypt(encrypted_text));
    }
}
