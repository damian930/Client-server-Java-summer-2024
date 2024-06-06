package org.example;

import java.io.IOException;

public class Main_1 {
    public static void main( String[] args ) throws IOException {
        StoreServerTCP s1 = new StoreServerTCP();
        s1.start(1);

        s1.stop();

    }
}
