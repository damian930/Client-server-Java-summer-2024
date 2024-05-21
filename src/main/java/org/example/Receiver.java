package org.example;

import javax.crypto.*;
import java.nio.ByteBuffer;
import java.security.*;

public class Receiver {
    private byte[] packet_as_bytes;
    private Message received_message;
    //boolean valid;
    private final Cipher cipher;
    private static final String ALG = "RSA";
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    Receiver() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        this.cipher = Cipher.getInstance(ALG);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();

        this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);

        //this.valid = false;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public Message receive_packet(byte[] arr) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //this.valid = false;
        this.packet_as_bytes = arr;
        if(!check_validity()) {
            System.out.println("Packet was corrupted.");
            return null;
        }
        System.out.println("Packet was fine.");
        save_message();
        //this.valid = true;
        return this.received_message;
    }

    private void save_message() throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        ByteBuffer buffer = ByteBuffer.wrap(this.packet_as_bytes);
        int command_code = buffer.getInt(16);
        int user_id = buffer.getInt(20);
        byte[] text = decrypt_text();
        this.received_message = new Message(command_code, user_id, text);
    }

    private byte[] decrypt_text() throws IllegalBlockSizeException, BadPaddingException {
        ByteBuffer buffer = ByteBuffer.wrap(this.packet_as_bytes);
        int message_size = buffer.getInt(10);
        byte[] str_as_bytes = new byte[message_size - 8];
        System.arraycopy(this.packet_as_bytes, 24, str_as_bytes, 0, str_as_bytes.length);
        return this.cipher.doFinal(str_as_bytes);
    }

    private boolean check_validity() {
        return check_crc_1() && check_crc_2();
    }

    private boolean check_crc_1() {
        byte[] byte_arr = new byte[14];
        System.arraycopy(this.packet_as_bytes, 0, byte_arr, 0, byte_arr.length);
        short crc = ByteBuffer.wrap(packet_as_bytes).getShort(14);
        return Packet.convert_to_crc16(byte_arr) == crc;
    }

    private boolean check_crc_2() {
        int text_size = ByteBuffer.wrap(packet_as_bytes).getInt(10);
        byte[] byte_arr = new byte[text_size];
        System.arraycopy(packet_as_bytes, 16, byte_arr, 0, byte_arr.length);
        short crc = ByteBuffer.wrap(packet_as_bytes).getShort(16 + text_size);
        return Packet.convert_to_crc16(byte_arr) == crc;
    }

    /*public Message getReceived_message() throws Exception {
        if(this.valid)
            return this.received_message;
        throw new Exception();
    }*/
}
