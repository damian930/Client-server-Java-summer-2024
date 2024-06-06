
package org.example;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Message {
    private final int cType;      // command code
    private final int bUserId;    // used id
    private final byte[] text;    // text

    Message(int command_code, int user_id, byte[] text) {
        this.cType = command_code;
        this.bUserId = user_id;
        this.text = new byte[text.length];
        System.arraycopy(text, 0, this.text, 0, text.length);
    }

    Message(byte[] byte_arr) {
        ByteBuffer buffer = ByteBuffer.wrap(byte_arr);
        this.cType = buffer.getInt(0);
        this.bUserId = buffer.getInt(4);
        this.text = new byte[buffer.array().length - 8];
        System.arraycopy(buffer.array(), 8, this.text, 0, this.text.length);
    }

    public byte[] getBytes() {
        byte[] byte_arr = new byte[Integer.BYTES * 2 + this.text.length];   // byte arr[cType + bUserId + message]
        ByteBuffer byteBuffer = ByteBuffer.wrap(byte_arr);                  // big-endian by default
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Message)) {
            return false;
        }

        Message m = (Message) obj;

        return this.cType == m.cType &&
                this.bUserId == m.bUserId &&
                Arrays.equals(this.text, m.text);
    }

}
