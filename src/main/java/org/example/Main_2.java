package org.example;

import java.io.IOException;

public class Main_2 {
    public static void main(String[] args) throws IOException {
        StoreClientTCP c1 = new StoreClientTCP();
        c1.startConnection("127.0.0.1", 1);
        System.out.println(c1.sendMessage("flopper"));
        System.out.println(c1.sendMessage("dipper"));
        System.out.println(c1.sendMessage("falls"));
        System.out.println(c1.sendMessage("."));

        StoreClientTCP c2 = new StoreClientTCP();
        c2.startConnection("127.0.0.1", 1);
        System.out.println(c2.sendMessage("gravity"));
        System.out.println(c2.sendMessage("."));


        c1.stopConnection();
        c2.stopConnection();

    }
}
