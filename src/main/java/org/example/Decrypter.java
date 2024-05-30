package org.example;

import javax.crypto.*;
import java.nio.ByteBuffer;
import java.security.*;

public class Decrypter {
    private ByteBuffer packet_buffer = null;
    //private ByteBuffer message_buffer = null;
    private Message decrypted_message = null;
    private static final String ALG = "RSA";
    private Cipher cipher;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    Decrypter() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALG);
        keyPairGenerator.initialize(2048); // key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        this.cipher = Cipher.getInstance(ALG);
        this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
    }

    public void decrypt(byte[] packet_bytes) throws IllegalBlockSizeException, BadPaddingException, InterruptedException {
        this.packet_buffer = ByteBuffer.wrap(packet_bytes);

        if(check_crc_1(packet_bytes)) {
            System.out.println("CRC_1 was valid.");
            if (check_crc_2(packet_bytes)) {
                System.out.println("CRC_2 was valid.\n");

                int[] int_arr = new int[2];             // cType, userId
                byte[][] text_bytes = new byte[1][];    // text (message)

                Thread thread1 = new Thread(() -> {
                    try {
                        text_bytes[0] = get_message_text();
                    } catch (IllegalBlockSizeException | BadPaddingException e) {
                        throw new RuntimeException(e);
                    }
                });

                Thread thread2 = new Thread(() -> get_message_info(int_arr));

                thread1.start();
                thread2.start();

                thread1.join();
                thread2.join();

                this.decrypted_message = new Message(int_arr[0], int_arr[1], text_bytes[0]);
            }
        }
        else {
            System.out.println("Packet was corrupted.");
            this.decrypted_message = null;
        }
    }

    private boolean check_crc_1(byte[] packet_bytes) {
        byte[] crc_1_bytes = new byte[14];
        System.arraycopy(packet_bytes, 0, crc_1_bytes, 0, crc_1_bytes.length);
        short crc = ByteBuffer.wrap(packet_bytes).getShort(14);
        return Packet.convert_to_crc16(crc_1_bytes) == crc;
    }

    private boolean check_crc_2(byte[] packet_bytes) {
        int text_size = this.packet_buffer.getInt(10);
        byte[] crc_2_bytes = new byte[text_size];
        System.arraycopy(packet_bytes, 16, crc_2_bytes, 0, crc_2_bytes.length);
        short crc = ByteBuffer.wrap(packet_bytes).getShort(16 + text_size);
        return Packet.convert_to_crc16(crc_2_bytes) == crc;
    }

    private void get_message_info(int[] int_arr) {
        System.out.println("Started get_message_info.");

        int_arr[0] = this.packet_buffer.getInt(16);
        int_arr[1] = this.packet_buffer.getInt(20);

        System.out.println("Ended get_message_info.");
    }

    private byte[] get_message_text() throws IllegalBlockSizeException, BadPaddingException {
        System.out.println("Started get_message_text.");

        int message_size = packet_buffer.getInt(10);
        byte[] text_as_bytes = new byte[message_size - 8];
        this.packet_buffer.get(24, text_as_bytes, 0, text_as_bytes.length);

        byte[] decrypted_text;
        decrypted_text = this.cipher.doFinal(text_as_bytes);

        System.out.println("Ended get_message_text.");
        return decrypted_text;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public Message getDecrypted_message() {
        return this.decrypted_message;
    }
}
