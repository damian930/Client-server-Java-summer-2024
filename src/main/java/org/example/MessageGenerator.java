package org.example;

public class MessageGenerator implements Receiver {

    @Override
    public void receiveMessage() {

    }

    public Packet create_packet(String text) {
        int i = (int)(Math.random() * 100) ;
        Message m = new Message(i, i + 1, text.getBytes());
        return new Packet(m, (byte) 9);
    }
}
