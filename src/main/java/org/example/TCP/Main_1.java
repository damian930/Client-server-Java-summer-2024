package org.example.TCP;

import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main_1 {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InterruptedException {
        StoreServerTCP serverTCP = new StoreServerTCP();
        serverTCP.start(1);

        serverTCP.stop();

    }
}
