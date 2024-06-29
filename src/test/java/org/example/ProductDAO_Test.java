package org.example;

import org.example.back.data_base.Product;
import org.example.back.data_base.Product_DAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAO_Test {

    private Product_DAO productDAO;

    @BeforeEach
    public void setUp() {
        productDAO = new Product_DAO();
    }

    @AfterEach
    public void tearDown() {
        if (productDAO != null) {
            // Delete the test product after each test
            Product productToDelete = productDAO.findByName("asdajadsakas");
            if (productToDelete != null) {
                productDAO.delete(productToDelete);
            }
            productDAO.close();
        }
    }

    @Test
    public void testConcurrentPurchaseAndDelete() throws InterruptedException {
        int nUsers = 15;
        int quantity = 100;

        Product testProduct = new Product();
        testProduct.setName("asdajadsakas");
        testProduct.setUPC("1234567890");
        testProduct.setQuantity(quantity);

        productDAO.save(testProduct);

        int productId = testProduct.getId();

        ExecutorService executorService = Executors.newFixedThreadPool(nUsers);

        for (int i = 0; i < nUsers; i++) {
            executorService.execute(() -> {
                try {
                    productDAO.purchaseProduct(productId, 5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }

        Product product = productDAO.findById(productId);

        System.out.println("Final quantity after concurrent purchases: " + product.getQuantity());

        int expectedFinalQuantity = 100 - 15 * 5;
        assertEquals(expectedFinalQuantity, product.getQuantity(), "Final quantity should match expected");

        productDAO.delete(product);
    }
}
