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
        byte[] packet_encr = e.encrypt(new MessageGenerator().create_packet("flopper"));
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

}
