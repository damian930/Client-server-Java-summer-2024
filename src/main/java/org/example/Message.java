package org.example;

import java.nio.ByteBuffer;

public class Message {
    private final int cType;      // command code

    private final int bUserId;    // used id
    private final byte[] text; // text

    Message(int command_code, int user_id, byte[] text) {
        this.cType = command_code;
        this.bUserId = user_id;
        this.text = new byte[text.length];
        System.arraycopy(text, 0, this.text, 0, text.length);
    }

    public byte[] getBytes() {
        byte[] byte_arr = new byte[Integer.BYTES * 2 + this.text.length];   // byte arr[cType + bUserId + message]
        ByteBuffer byteBuffer = ByteBuffer.wrap(byte_arr);  // big-endian by default
        byteBuffer.putInt(this.cType).putInt(this.bUserId).put(this.text);
        return byte_arr;
    }

    public int getcType() {
        return cType;
    }

    public int getbUserId() {
        return bUserId;
    }

    public byte[] getText() {
        return text;
    }
}
