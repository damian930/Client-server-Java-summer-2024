package org.example;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class App {
    public static void main(String[] args ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InterruptedException {
        /*Decrypter d = new Decrypter();
        byte[] packet_bytes = create_encrypted_packet("flopper", d.getPublicKey());
        d.decrypt(packet_bytes);
        Message m = d.getDecrypted_message();

        System.out.println("\nReceived:");
        System.out.println("\tCommand type: " + (m != null ? m.getcType() : "null"));
        System.out.println("\tUser Id: " + (m != null ? m.getbUserId() : "null"));
        System.out.println("\tText: " + (m != null ? new String(m.getText()) : "null"));

        System.out.println("------------------------------");

        packet_bytes = create_encrypted_packet("dipper pines", d.getPublicKey());
        d.decrypt(packet_bytes);
        m = d.getDecrypted_message();

        System.out.println("\nReceived:");
        System.out.println("\tCommand type: " + (m != null ? m.getcType() : "null"));
        System.out.println("\tUser Id: " + (m != null ? m.getbUserId() : "null"));
        System.out.println("\tText: " + (m != null ? new String(m.getText()) + '\n' : "null\n"));*/

        Encryptor e = new Encryptor();
        Decrypter d = new Decrypter();
        e.setPublicKey(d.getPublicKey());
        byte[] packet_encr = e.encrypt(new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3));
        d.decrypt(packet_encr);
        Message m = d.getDecrypted_message();

        System.out.println("\nReceived:");
        System.out.println("\tCommand type: " + (m != null ? m.getcType() : "null"));
        System.out.println("\tUser Id: " + (m != null ? m.getbUserId() : "null"));
        System.out.println("\tText: " + (m != null ? new String(m.getText()) + '\n' : "null\n"));

        Processor p = new Processor();
        p.process(m);
        Message answer = p.getAnswer();

        System.out.println("Received Answer: " + new String(answer.getText()));
        Sender s = new Sender();
        s.sendMessage(m.getBytes(), "230.175.48.203");


    }

    public static byte[] create_encrypted_packet(String text, PublicKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted_text = cipher.doFinal(text.getBytes());

        ByteBuffer buffer = ByteBuffer.wrap(new byte[20 + 4 + 4 + encrypted_text.length + 2]);

        // CRC_1  (works)
        buffer.put((byte) 13);
        buffer.put((byte) 1);
        buffer.putLong(Packet.getMessageCounter());
        buffer.putInt(encrypted_text.length + 4 + 4);
        byte[] for_crc_1 = new byte[14];
        System.arraycopy(buffer.array(), 0, for_crc_1, 0, for_crc_1.length);
        buffer.putShort(Packet.convert_to_crc16(for_crc_1));

        // CRC_2  (works)
        buffer.putInt(1);
        buffer.putInt(2);
        buffer.put(encrypted_text);
        byte[] for_crc_2 = new byte[8 + encrypted_text.length];
        System.arraycopy(buffer.array(), 16, for_crc_2, 0, for_crc_2.length);
        buffer.putShort(Packet.convert_to_crc16(for_crc_2));
        return buffer.array();
    }
}
