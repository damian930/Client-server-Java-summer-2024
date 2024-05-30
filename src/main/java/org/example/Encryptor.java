package org.example;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class Encryptor {
    private ByteBuffer packet_buffer = null;
    private static final String ALG = "RSA";
    private Cipher cipher;
    private PublicKey publicKey = null;


    Encryptor() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(ALG);
    }

    public byte[] encrypt(Packet packet) throws InterruptedException {
        if(this.publicKey == null)
            throw new NullPointerException("Could not encrypt a packet, because the key was null.\n" +
                    "To set the key use void setPublicKey(PublicKey key) throws InvalidKeyException");

        this.packet_buffer = ByteBuffer.wrap(packet.getBytes());
        byte[][] packet_info = new byte[1][];
        byte[][] encrypted_message = new byte[1][];
        Thread thread1 = new Thread(() -> packet_info[0] = get_packet_info());  // without crc_1

        Thread thread2 = new Thread(() -> {     // without crc_2
            try {
                encrypted_message[0] = encrypt_message();
            } catch (IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        thread2.join();
        thread1.join();

        this.packet_buffer = ByteBuffer.wrap(new byte[packet_info[0].length + encrypted_message[0].length + 2 + 2]);

        this.packet_buffer.put(packet_info[0], 0, packet_info[0].length - 4);
        this.packet_buffer.putInt(encrypted_message[0].length);

        this.packet_buffer.putShort((short) 4);
        this.packet_buffer.put(encrypted_message[0]);
        this.packet_buffer.putShort((short) 4);

        short[] crc = new short[2];
        Thread thread3 = new Thread(() -> crc[0] = crc_1());
        Thread thread4 = new Thread(() -> crc[1] = crc_2());

        thread3.start();
        thread4.start();

        thread3.join();
        thread4.join();

        this.packet_buffer.putShort(14, crc[0]);
        this.packet_buffer.putShort(packet_info[0].length + 2 + encrypted_message[0].length, crc[1]);

        return this.packet_buffer.array();
    }

    private byte[] get_packet_info() {
        byte[] info_bytes = new byte[14];
        synchronized (this.packet_buffer) {
            this.packet_buffer.get(0, info_bytes, 0, info_bytes.length);
        }
        return info_bytes;
    }

    private byte[] encrypt_message() throws IllegalBlockSizeException, BadPaddingException {
        int message_size;
        message_size = this.packet_buffer.getInt(10);

        byte[] text_bytes = new byte[message_size - 8];
        this.packet_buffer.get(24, text_bytes, 0, text_bytes.length);

        byte[] text_encrypted = cipher.doFinal(text_bytes);

        ByteBuffer buffer = ByteBuffer.wrap(new byte[4 + 4 + text_encrypted.length /*+ 2*/]);
        buffer.putInt(this.packet_buffer.getInt(16));
        buffer.putInt(this.packet_buffer.getInt(20));
        buffer.put(text_encrypted);

        return buffer.array();
    }

    private short crc_1() {
        return Packet.convert_to_crc16(this.packet_buffer.array(), 0, 14);
    }

    private short crc_2() {
        byte[] buffer_arr = this.packet_buffer.array();
        return Packet.convert_to_crc16(buffer_arr, 16, buffer_arr.length - 2 - 16);
    }

    public void setPublicKey(PublicKey key) throws InvalidKeyException {
        this.publicKey = key;
        this.cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
    }

}
