package org.example;

public class Processor {
    private Message message;

    public void process(Message message) {
        this.message = message;
    }

    public Message getAnswer() {
        return new Message(0, 0, "OK".getBytes());
    }


}
