package org.example;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class App {
    public static void main( String[] args ) throws Exception {
        Sender s = new Sender();
        Receiver r = new Receiver();
        Message m;
        s.setReceiversKey(r.getPublicKey());

        {   // valid packet
            s.create_packet(1, 3, "flopper died way back in season 2", (byte) 9);
            m = r.receive_packet(s.send_packet());

            System.out.println("Received:");
            System.out.println("\tCommand type: " + m.getcType());
            System.out.println("\tUser Id: " + m.getbUserId());
            System.out.println("\tText: " + new String(m.getText()));
        }

        System.out.println('\n');

        {   // valid packet
            s.create_packet(5, 65, "when i lost my g pro i was really sad(", (byte) 103);
            m = r.receive_packet(s.send_packet());

            System.out.println("Received:");
            System.out.println("\tCommand type: " + m.getcType());
            System.out.println("\tUser Id: " + m.getbUserId());
            System.out.println("\tText: " + new String(m.getText()));
        }

        System.out.println('\n');

        {   // invalid packet
            s.create_packet(3, 104, "FNCS was fun this weekend", (byte) 31);
            byte[] corrupted_packet = s.send_packet();
            corrupted_packet[3] = 13;   // random corruption
            m = r.receive_packet(corrupted_packet);

        }

        /*Sender s = new Sender();
        Receiver r = new Receiver();
        s.setReceiversKey(r.getPublicKey());
        s.create_packet(1, 2, "flopper", (byte) 3);
        System.out.println(Arrays.toString(s.send_packet()));*/


    }
}
