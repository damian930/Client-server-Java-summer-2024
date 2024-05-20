package org.example;

public class App {
    public static void main( String[] args ) throws Exception {
        Sender s = new Sender();
        Receiver r = new Receiver();
        Message m;

        s.setReceiversKey(r.getPublicKey());
        {   // valid packet
            s.create_packet(1, 3, "flopper dies way back in season 2", (byte) 9);
            r.receive_packet(s.send_packet());
            m = r.getReceived_message();

            System.out.println("Received:");
            System.out.println("\tCommand type: " + m.getcType());
            System.out.println("\tUser Id: " + m.getbUserId());
            System.out.println("\tText: " + new String(m.getText()));
        }

        System.out.println('\n');

        {   // valid packet
            s.create_packet(5, 65, "when i lost my g pro i was really sad(", (byte) 103);
            r.receive_packet(s.send_packet());
            m = r.getReceived_message();

            System.out.println("Received:");
            System.out.println("\tCommand type: " + m.getcType());
            System.out.println("\tUser Id: " + m.getbUserId());
            System.out.println("\tText: " + new String(m.getText()));
        }

        System.out.println('\n');

        {   // invalid packet
            s.create_packet(3, 104, "FNCS was fun these weekends", (byte) 31);
            byte[] corrupted_packet = s.send_packet();
            corrupted_packet[3] = 13;   // random corruption
            r.receive_packet(corrupted_packet);
            try {
                m = r.getReceived_message();
            }
            catch (Exception e) {
                System.out.println("Corrupted message could not have been read.");
            }
        }



    }
}
