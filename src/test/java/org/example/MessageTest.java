package org.example;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testDefaultConstructor() {
        String text = "flopper died";
        Message m = new Message(1, 2, text.getBytes(StandardCharsets.UTF_8));
        assertEquals(1, m.getcType());
        assertEquals(2, m.getbUserId());
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), m.getText());
    }

    @Test
    void testByteArrayConstructor() {
        String text = "flopper";
        byte[] message_as_bytes = {0,0,0,1, 0,0,0,2, 102,108,111,112,112,101,114};
        Message m = new Message(message_as_bytes);
        assertEquals(1, m.getcType());
        assertEquals(2, m.getbUserId());
        assertArrayEquals(text.getBytes(StandardCharsets.UTF_8), m.getText());
    }

    @Test
    void testMessage_ToBytes_Conversion() {
        // [70,78,67,83, 32, 119,97,115, 32, 102,117,110, 32, 116,104,105,115, 32, 119,101,101,107,101,110,100]
        // "     FNCS    __      was     __      fun      __         this      __            weekend          "
        String text = "FNCS was fun this weekend";
        Message m = new Message(13, 4, text.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(new byte[] {
                0,0,0,13                        // int
                ,0,0,0,4                        // int
                ,70,78,67,83,                   // FNCS
                32,                             // __
                119,97,115,                     // was
                32,                             // __
                102,117,110,                    // fun
                32,                             // __
                116,104,105,115,                // this
                32,                             // __
                119,101,101,107,101,110,100     // weekend
        }, m.getBytes());
    }

    @Test
    void testMessage_ToBytes_Conversion_EmptyString() {
        String text = "";
        Message m = new Message(13, 4, text.getBytes(StandardCharsets.UTF_8));
        assertArrayEquals(new byte[] {}, text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void testEqualityWithItself() {
        Message m = new Message(1, 2, "flopper".getBytes());
        assertEquals(m, m);
    }

    @Test
    void testNullEquality() {
        Message m = new Message(1, 2, "flopper".getBytes());
        assertNotEquals(null, m);
    }

    @Test
    void testSymmetry() {
        Message m1 = new Message(1, 2, "flopper".getBytes());
        Message m2 = new Message(1, 2, "flopper".getBytes());
        assertEquals(m1, m2);
        assertEquals(m2, m1);
    }

    @Test
    void testTransitivity() {
        Message m1 = new Message(1, 2, "flopper".getBytes());
        Message m2 = new Message(1, 2, "flopper".getBytes());
        Message m3 = new Message(1, 2, "flopper".getBytes());
        assertEquals(m1, m2);
        assertEquals(m2, m3);
        assertEquals(m1, m3);
    }

    @Test
    void testEquality_With_DifferentType() {
        Message m = new Message(1, 2, "flopper".getBytes());
        String str = "flopper";
        assertNotEquals(m, str);
    }

}
