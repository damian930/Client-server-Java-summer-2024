package org.example.UPD;

import javax.crypto.NoSuchPaddingException;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main_1 {
    public static void main(String[] args) throws SocketException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        StoreServerUDP server = new StoreServerUDP();
        server.start();




    }

}
