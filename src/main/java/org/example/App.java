package org.example;

public class App {
    public static void main( String[] args ) {
        /*Message m1 = new Message(1, 14754, "get after it");
        System.out.println(Arrays.toString(m1.getBytes()));
        byte[] arr = {0, 0, 57, -94};
        ByteBuffer wrapped = ByteBuffer.wrap(arr); // big-endian by default
        int n = wrapped.getInt();
        System.out.println(n);*/

        Message m2 = new Message(2, 3001, "testing functions");
        Packet p1 = new Packet(m2, (byte)2);
        Receiver r1 = new Receiver();
        r1.receive_packet(p1);



    }
}
