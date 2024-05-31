package org.example;

import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptorTest {

    @Test
    void testEncryption() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException, IllegalBlockSizeException, BadPaddingException {
        Encryptor e = new Encryptor();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        e.setPublicKey(publicKey);
        Packet p = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);
        byte[] encrypted_packet = e.encrypt(p);

        // manually decrypt
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        ByteBuffer buffer = ByteBuffer.wrap(encrypted_packet);
        int message_size = buffer.getInt(10);
        byte[] text = new byte[message_size - 8];
        buffer.get(24, text, 0, text.length);
        byte[] decrypted_text = cipher.doFinal(text);

        // need to change the long value manually, cause it increments every time a packet is created;
       ByteBuffer decryptedP = ByteBuffer.wrap(new byte[1 + 1 + 8 + 4 + 2 + 4 + 4 + decrypted_text.length + 2]);
       decryptedP.put((byte) 13);
       decryptedP.put((byte) 3);
       decryptedP.putLong(Packet.getMessageCounter() - 1);
       decryptedP.putInt(decrypted_text.length + 4 + 4);
       decryptedP.putShort(Packet.convert_to_crc16(decryptedP.array(), 0, 14));
       decryptedP.putInt(1);
       decryptedP.putInt(2);
       decryptedP.put(decrypted_text);
       decryptedP.putShort(Packet.convert_to_crc16(decryptedP.array(), 16, 4 + 4 + decrypted_text.length));

        // encryption works if the encrypted variant is not the same as the original, and it can be decrypted into the original form
        boolean b1 = Arrays.equals(encrypted_packet, decrypted_text); // should be false
        boolean b2 = Arrays.equals(p.getBytes(), decryptedP.array()); // should be true

        assertTrue(!b1 && b2);
    }

    @Test
    void testEncryption_NullPointerException_Thrown() throws NoSuchPaddingException, NoSuchAlgorithmException {
        Encryptor e = new Encryptor();
        Packet p = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);

        assertThrows(NullPointerException.class, () -> e.encrypt(p));
    }

    @Test
    void testEncryption_RuntimeException_Thrown() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InterruptedException {
        Encryptor e = new Encryptor();
        byte[] arr = new byte[300];

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        e.setPublicKey(publicKey);

        assertThrows(RuntimeException.class, () -> e.encrypt(new Packet(arr)));
    }

}
