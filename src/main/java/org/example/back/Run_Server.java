package org.example.back;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import org.example.back.data_base.*;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import static org.example.back.html.WebPages.*;

public class Run_Server {
    public static void main(String[] args) throws IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, KeyManagementException {
        char[] keystorePassword = "password".toCharArray();
        char[] keyPassword = "password".toCharArray();
        KeyStore keyStore = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("keystore.jks");
        keyStore.load(fis, keystorePassword);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyPassword);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);

        HttpsServer httpsServer = HttpsServer.create(new InetSocketAddress(8000), 0);
        httpsServer.setHttpsConfigurator(new HttpsConfigurator(sslContext));


        httpsServer.createContext("/categories", exchange -> {
            System.out.println("entered categories");
            try {
                System.out.println(0);
                Category_DAO categoryDAO = new Category_DAO();
                System.out.println(1);
                List<Category> categories = categoryDAO.findAll();
                System.out.println(2);
                String dynamicHtml = generateDynamicHTMLForCategories(categories);
                System.out.println("generated");
                exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);
                System.out.println("sent");
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(dynamicHtml.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            }
        });

        httpsServer.createContext("/add/category", exchange -> {
            try {
                String dynamicHtml = generateAddCategoryHTML();

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(dynamicHtml.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            } finally {
                exchange.close();
            }
        });

        httpsServer.createContext("/insert/category", exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try (InputStream is = exchange.getRequestBody()) {

                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();

                    Map<String, String> formDataMap = parseFormData(formData);
                    String categoryName = formDataMap.get("name");

                    if (categoryName == null || categoryName.isEmpty()) {
                        exchange.sendResponseHeaders(400, 0); // Bad Request
                        return;
                    }

                    Category_DAO categoryDAO = new Category_DAO();
                    if (categoryDAO.existsByName(categoryName)) {
                        exchange.getResponseHeaders().set("Location", "/categories");
                        exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                        return;
                    }

                    Category newCategory = new Category(categoryName);

                    categoryDAO.save(newCategory);

                    String redirectUrl = "/categories";
                    exchange.getResponseHeaders().set("Location", redirectUrl);
                    exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                } catch (IOException e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                // Method not allowed
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            }
        });

        httpsServer.createContext("/edit/category", exchange -> {
            try {
                Category_DAO categoryDAO = new Category_DAO();

                String dynamicHtml = generateEditCategoryHTML(categoryDAO.findAll());

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(dynamicHtml.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            } finally {
                exchange.close();
            }
        });

        httpsServer.createContext("/save/category", exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try (InputStream is = exchange.getRequestBody()) {
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();

                    Map<String, String> formDataMap = parseFormData(formData);
                    String categoryId = formDataMap.get("categoryId");
                    String newCategoryName = formDataMap.get("newName");

                    Category_DAO categoryDAO = new Category_DAO();
                    Category categoryToUpdate = categoryDAO.findById(Integer.parseInt(categoryId));

                    if (categoryToUpdate != null) {
                        Category existingCategory = categoryDAO.findByName(newCategoryName);
                        if (existingCategory != null && existingCategory.getId() != categoryToUpdate.getId()) {
                            exchange.sendResponseHeaders(409, 0); // Conflict
                        } else {
                            categoryToUpdate.setName(newCategoryName);

                            categoryDAO.update(categoryToUpdate);

                            String redirectUrl = "/categories"; // Adjust the redirect URL as needed
                            exchange.getResponseHeaders().set("Location", redirectUrl);
                            exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                        }
                    } else {
                        exchange.sendResponseHeaders(404, 0); // Not Found
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            }
        });

        httpsServer.createContext("/delete/category", exchange -> {
            try {
                String dynamicHtml = generateDeleteCategoryHTML(new Category_DAO().findAll());

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(dynamicHtml.getBytes());
                }
                System.out.println("Inside Delete Menu");
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            } finally {
                exchange.close();
            }
        });

        httpsServer.createContext("/deleting_category", exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try (InputStream is = exchange.getRequestBody()) {
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();

                    Map<String, String> formDataMap = parseFormData(formData);
                    String categoryId = formDataMap.get("categoryId");

                    Category_DAO categoryDAO = new Category_DAO();
                    Category categoryToDelete = categoryDAO.findById(Integer.parseInt(categoryId));

                    if (categoryToDelete != null) {
                        categoryDAO.delete(categoryToDelete);

                        String redirectUrl = "/categories";
                        exchange.getResponseHeaders().set("Location", redirectUrl);
                        exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                    } else {
                        exchange.sendResponseHeaders(404, 0); // Not Found
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                // Method not allowed
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            }
        });


    // ------------------------------------------------------------------------------------ \\

        httpsServer.createContext("/products", exchange -> {
            Map<String, String> queryParams = parseQueryParams_products(exchange.getRequestURI().getQuery());
            String categoryName = queryParams.get("category");

            Product_DAO productDAO = new Product_DAO();
            List<Product> products = productDAO.findByCategory(categoryName);

            String dynamicHtml = generateDynamicHTMLForProducts(categoryName, products);

            exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(dynamicHtml.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            }
        });

        httpsServer.createContext("/product", exchange -> {
            try {
                Map<String, String> queryParams = parseQueryParams_product(exchange.getRequestURI().getQuery());
                int productId = Integer.parseInt(queryParams.get("id"));

                Product_DAO productDAO = new Product_DAO();
                Product product = productDAO.findById(productId);


                if (product != null) {
                    if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                        String dynamicHtml = generateDynamicHTMLForProduct(product);
                        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                        exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);
                        try (OutputStream os = exchange.getResponseBody()) {
                            os.write(dynamicHtml.getBytes());
                        }
                    }

                    if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        String formData = br.readLine();

                        JsonObject json = new JsonParser().parse(formData).getAsJsonObject();

                        if (json.has("quantity")) {
                            int buyQuantity = json.get("quantity").getAsInt();

                            if (buyQuantity <= 0 || buyQuantity > product.getQuantity()) {
                                exchange.sendResponseHeaders(400, 0); // Bad Request
                                return;
                            }

                            productDAO.purchaseProduct(productId, buyQuantity);

                            exchange.getResponseHeaders().set("Location", "/product?id=" + productId);
                            exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                        } else if (json.has("amount")) {
                            int addAmount = json.get("amount").getAsInt();
                            if (addAmount <= 0) {
                                exchange.sendResponseHeaders(400, 0); // Bad Request
                                return;
                            }
                            productDAO.addQuantity(productId, addAmount);

                            exchange.getResponseHeaders().set("Location", "/product?id=" + productId);
                            exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                        } else {
                            exchange.sendResponseHeaders(400, 0); // Bad Request
                        }
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0); // Not Found
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            } finally {
                exchange.close(); // Close the exchange after handling
            }
        });

        httpsServer.createContext("/edit/product", exchange -> {
            try {
                Map<String, String> queryParams = parseQueryParams_product(exchange.getRequestURI().getQuery());
                int productId = Integer.parseInt(queryParams.get("id"));

                Product_DAO productDAO = new Product_DAO();
                Product product = productDAO.findById(productId);

                Category_DAO categoryDAO = new Category_DAO();
                List<Category> categories = categoryDAO.findAll();

                if (product != null) {
                    String dynamicHtml = generateEditProductHTML(product, categories);

                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);

                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(dynamicHtml.getBytes());
                    }
                } else {
                    exchange.sendResponseHeaders(404, 0); // Not Found
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            } finally {
                exchange.close(); // Close the exchange after handling
            }
        });

        httpsServer.createContext("/save/product", exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try (InputStream is = exchange.getRequestBody()) {
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();

                    Map<String, String> formDataMap = parseFormData(formData);

                    String productId = formDataMap.get("productId");
                    String productName = formDataMap.get("name");
                    String categoryName = formDataMap.get("category");
                    String description = formDataMap.get("description");
                    String price = formDataMap.get("price");

                    Product_DAO productDAO = new Product_DAO();
                    Product productToUpdate = productDAO.findById(Integer.parseInt(productId));

                    if (productToUpdate != null) {
                        productToUpdate.setName(productName);
                        Category_DAO categoryDAO = new Category_DAO();
                        Category category = categoryDAO.findByName(categoryName);

                        if (category != null) {
                            productToUpdate.setCategory(category);
                        } else {
                            exchange.sendResponseHeaders(404, 0); // Not Found
                            return;
                        }

                        productToUpdate.setCharacteristics(description);
                        productToUpdate.setSellingPrice(Double.parseDouble(price));

                        productDAO.update(productToUpdate);

                        String redirectUrl = "/product?id=" + productToUpdate.getId();
                        exchange.getResponseHeaders().set("Location", redirectUrl);
                        exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                    } else {
                        exchange.sendResponseHeaders(404, 0); // Not Found
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            }
        });

        httpsServer.createContext("/delete/product", exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
                try {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder jsonBody = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        jsonBody.append(line);
                    }

                    JsonParser parser = new JsonParser();
                    JsonObject jsonObject = parser.parse(jsonBody.toString()).getAsJsonObject();
                    int productId = jsonObject.get("productId").getAsInt();

                    Product_DAO productDAO = new Product_DAO();
                    Product productToDelete = productDAO.findById(productId);

                    if (productToDelete != null) {
                        productDAO.delete(productToDelete);
                        exchange.sendResponseHeaders(200, 0); // OK
                    } else {
                        exchange.sendResponseHeaders(404, 0); // Not Found
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            }
            exchange.close();
        });

        httpsServer.createContext("/add/product", exchange -> {
            try {
                Category_DAO categoryDAO = new Category_DAO();
                List<Category> categories = categoryDAO.findAll();

                String dynamicHtml = generateAddProductHTML(categories);

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(dynamicHtml.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            } finally {
                exchange.close();
            }
        });

        httpsServer.createContext("/insert/product", exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                try (InputStream is = exchange.getRequestBody()) {
                    InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                    BufferedReader br = new BufferedReader(isr);
                    String formData = br.readLine();

                    Map<String, String> formDataMap = parseFormData(formData);

                    String productName = formDataMap.get("name");
                    String categoryName = formDataMap.get("category");
                    String description = formDataMap.get("description");
                    String priceStr = formDataMap.get("price");
                    String upc = formDataMap.get("upc");
                    String quantityStr = formDataMap.get("quantity");

                    if (productName == null || categoryName == null || description == null ||
                            priceStr == null || upc == null || quantityStr == null) {
                        exchange.sendResponseHeaders(400, 0); // Bad Request
                        return;
                    }

                    double price = Double.parseDouble(priceStr);
                    int quantity = Integer.parseInt(quantityStr);

                    Product_DAO productDAO = new Product_DAO();
                    if (productDAO.existsByUPC(upc) || productDAO.existsByName(productName)) {
                        exchange.getResponseHeaders().set("Location", "/categories");
                        exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                        return;
                    }

                    Category_DAO categoryDAO = new Category_DAO();
                    Category category = categoryDAO.findByName(categoryName);

                    if (category != null) {
                        Product newProduct = new Product();
                        newProduct.setName(productName);
                        newProduct.setCategory(category);
                        newProduct.setCharacteristics(description);
                        newProduct.setSellingPrice(price);
                        newProduct.setUPC(upc);
                        newProduct.setQuantity(quantity);

                        productDAO.save(newProduct);

                        String redirectUrl = "/products?category=" + newProduct.getCategory().getName();
                        exchange.getResponseHeaders().set("Location", redirectUrl);
                        exchange.sendResponseHeaders(302, -1); // 302 Found (redirect)
                    } else {
                        exchange.sendResponseHeaders(404, -1);
                    }
                } catch (NumberFormatException | IOException e) {
                    e.printStackTrace();
                    exchange.sendResponseHeaders(500, 0); // Internal Server Error
                }
            } else {
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
            }
        });

        httpsServer.createContext("/stats", exchange -> {
            try {
                Product_DAO productDAO = new Product_DAO();
                Category_DAO categoryDAO = new Category_DAO();
                List<Product> products = productDAO.findAll();
                List<Category> categories = categoryDAO.findAll();

                double totalInventoryValue = 0.0;
                for (Product product : products) {
                    totalInventoryValue += product.getQuantity() * product.getSellingPrice();
                }

                String dynamicHtml = generateDynamicHTMLForStats(products, categories, totalInventoryValue);

                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, dynamicHtml.getBytes().length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(dynamicHtml.getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(500, 0); // Internal Server Error
            }
        });


        httpsServer.start();
        System.out.println("Server started on port 8000");
    }




    

}
