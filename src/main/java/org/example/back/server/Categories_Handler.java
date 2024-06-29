package org.example.back.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Categories_Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("GET".equals(exchange.getRequestMethod())) {
            serverCategoriesOverviewPage(exchange);
        }
    }

    private void serverCategoriesOverviewPage(HttpExchange exchange) throws IOException {
        String authorizationHeader = exchange.getResponseHeaders().getFirst("Authorization");

        try {
            String htmlPath =
                    "D:\\Admin\\Desktop\\Collage year 2\\Client server Java\\website\\src\\main\\resources\\categories.html";
            byte[] htmlBytes = Files.readAllBytes(Paths.get(htmlPath));

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, htmlBytes.length);

            OutputStream os = exchange.getResponseBody();
            os.write(htmlBytes);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
        }

    }
}
