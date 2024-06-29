package org.example.back.data_base;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Represents a product entity in the application.
 *
 * <p>Each product has a unique identifier (ID), a unique Universal Product Code (UPC),
 * a name that must be non-blank and unique, a selling price that must be greater than or equal to 0,
 * a quantity that must be greater or equal to 0, characteristics describing the product,
 * and belongs to a category.
 */
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String UPC;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @DecimalMin(value = "0.0", inclusive = true, message = "Selling price must be greater than or equal to 0")
    @Column(nullable = false)
    private double sellingPrice;

    @Min(value = 0, message = "Quantity must be greater or equal to 0")
    @Column(nullable = false)
    private int quantity;

    @NotBlank
    @Column
    private String characteristics;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    public Product() {}

    public Product(String UPC, String name, double sellingPrice, int quantity, String characteristics, Category category) {
        this.UPC = UPC;
        this.name = name;
        this.sellingPrice = sellingPrice;
        this.quantity = quantity;
        this.characteristics = characteristics;
        this.category = category;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", UPC='" + UPC + '\'' +
                ", name='" + name + '\'' +
                ", sellingPrice=" + sellingPrice +
                ", quantity=" + quantity +
                ", characteristics='" + characteristics + '\'' +
                ", category='" + category.getName() + '\'' +
                '}';
    }
}