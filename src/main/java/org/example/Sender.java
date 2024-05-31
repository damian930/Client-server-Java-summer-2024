package org.example;

public class Sender {
    public void sendMessage(byte[] message_bytes, String internet_address) {
        Message m = new Message(message_bytes);
        System.out.println("Message (" + m.getcType() + ", " + m.getbUserId() + ", " + new String(m.getText()) + ") was sent to " + internet_address + " address.");
    }


}
