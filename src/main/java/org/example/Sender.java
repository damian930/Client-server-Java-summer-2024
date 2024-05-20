package org.example;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class Sender {
    private Packet packet;
    private final Cipher cipher;
    private static final String alg = "RSA";
    private final PublicKey publicKey;
    private final PrivateKey privateKey;
    private PublicKey receiversKey;
    Sender() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(alg);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // key size
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public byte[] send_packet() {
        return this.packet.getBytes();
    }

    public void create_packet(int command_code, int user_id, String plain_text, byte client_application_number) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] encrypted_text_as_bytes = cipher_message(plain_text);
        Message message = new Message(command_code, user_id, encrypted_text_as_bytes);
        this.packet = new Packet(message, client_application_number);
    }

    private byte[] cipher_message(String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        this.cipher.init(Cipher.ENCRYPT_MODE, this.receiversKey);
        return this.cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
    }

    public void setReceiversKey(PublicKey key) {
        this.receiversKey = key;
    }
}
