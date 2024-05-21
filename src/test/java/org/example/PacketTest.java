package org.example;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class PacketTest {

    @Test
    void testConversionToCrc16() {
        byte[] byte_arr = {102, 108, 111, 112, 112, 101, 114};  // "flopper"
        assertEquals(3435, Packet.convert_to_crc16(byte_arr));
    }

    @Test
    void testMessage_ToBytes_Conversion_1() {
        Packet p = new Packet(new Message(1, 2, "flopper".getBytes()), (byte) 3);

        long counter = Packet.getMessageCounter() - 1;  // after creating p counter was incremented. need to manually decrement
        byte[] correct_arr = {
                13,                                     // bMagic
                3,                                      // bSrc
                0, 0, 0, 0, 0, 0, 0, 0,                 // bPktId
                0, 0, 0, 15,                            // wLen
                -54, 116,                               // crc16 1
                0, 0, 0, 1,                             // command type
                0, 0, 0, 2,                             // user id
                102, 108, 111, 112, 112, 101, 114,      // "flopper"
                23, 23                                  // crc16 2

        };
        byte[] counter_as_bytes = ByteBuffer.wrap(new byte[8]).putLong(counter).array();
        System.arraycopy(counter_as_bytes, 0, correct_arr, 2, counter_as_bytes.length);
        assertArrayEquals(correct_arr, p.getBytes());
    }

    @Test
    void testMessage_ToBytes_Conversion_2() {
        Packet p = new Packet(new Message(6, 49, "follow my twitch damian930".getBytes()), (byte) 19);

        long counter = Packet.getMessageCounter() - 1;  // after creating p counter was incremented. need to manually decrement
        byte[] correct_arr = {
                13,                                     // bMagic
                19,                                     // bSrc
                0, 0, 0, 0, 0, 0, 0, 0,                 // bPktId
                0, 0, 0, 34,                            // wLen
                71, -50,                                // crc16 1
                0, 0, 0, 6,                             // command type
                0, 0, 0, 49,                            // user id
                102, 111, 108, 108, 111, 119, 32,       //
                109, 121, 32, 116, 119, 105, 116,       // text
                99, 104, 32, 100, 97, 109, 105, 97,     //
                110, 57, 51, 48,                        //
                -73, -24                                // crc16 2

        };
        byte[] counter_as_bytes = ByteBuffer.wrap(new byte[8]).putLong(counter).array();
        System.arraycopy(counter_as_bytes, 0, correct_arr, 2, counter_as_bytes.length);
        byte[] t = p.getBytes();
        assertArrayEquals(correct_arr, p.getBytes());
    }
}