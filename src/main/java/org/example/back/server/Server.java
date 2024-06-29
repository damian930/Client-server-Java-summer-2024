package org.example.back.server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final HttpServer server;

    public Server() throws IOException {
        this.server = HttpServer.create();
        this.server.bind(new InetSocketAddress(8080), 0);


    }

    public void start() {
        HttpContext categories = server.createContext("/categories", new Categories_Handler());

        server.start();
    }
}
