package org.example;

import java.nio.ByteBuffer;

public class Receiver {
    Packet packet;

    Receiver() {

    }

    public void receive_packet(Packet packet) {
        this.packet = packet;
        check_correctness();
    }

    public void check_correctness() {
        check_info_part();
        check_message_part();
    }

    private void check_info_part() {
        byte[] packet = this.packet.getBytes();
        byte[] byte_arr = new byte[
                Byte.BYTES * 2 + Long.BYTES + Integer.BYTES
                ];
        for(int i=0; i<byte_arr.length; i++)
            byte_arr[i] = packet[i];

        short crc_1 = (short)this.packet.convert_to_crc16(byte_arr);

        ByteBuffer byteBuffer = ByteBuffer.wrap(packet, byte_arr.length, 2);
        short num = byteBuffer.getShort();
        System.out.println(crc_1 == num);
    }

    private void check_message_part() {
        byte[] packet = this.packet.getBytes();
        byte[] byte_arr = new byte[
                this.packet.getBytes().length - 16 - 2
                ];
        for(int i=0; i<byte_arr.length; i++)
            byte_arr[i] = packet[i + 16];

        short crc_1 = (short)this.packet.convert_to_crc16(byte_arr);

        ByteBuffer byteBuffer = ByteBuffer.wrap(packet, 16 + byte_arr.length, 2);
        short num = byteBuffer.getShort();
        System.out.println(crc_1 == num);
    }
}
