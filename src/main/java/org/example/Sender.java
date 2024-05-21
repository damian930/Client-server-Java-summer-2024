package org.example;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Sender {
    private Packet packet;
    private final Cipher cipher;
    private static final String ALG = "RSA";
    private PublicKey receiversKey;

    Sender() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(ALG);
        this.receiversKey = null;
    }

    public byte[] send_packet() {
        return this.packet.getBytes();
    }

    public void create_packet(int command_code, int user_id, String plain_text, byte client_application_number) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] encrypted_text_as_bytes = cipher_message(plain_text);
        Message message = new Message(command_code, user_id, encrypted_text_as_bytes);
        this.packet = new Packet(message, client_application_number);
    }

    private byte[] cipher_message(String text) throws IllegalBlockSizeException, BadPaddingException {
        if(this.receiversKey == null)
            throw new NullPointerException("Couldn't cipher the text. Cipher key was null. Set key using setReceiversKey(PublicKey key) function. ");
        return this.cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
    }

    public void setReceiversKey(PublicKey key) throws InvalidKeyException {
        this.receiversKey = key;
        this.cipher.init(Cipher.ENCRYPT_MODE, this.receiversKey);
    }
}
