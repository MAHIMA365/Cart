package com.example.Cart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Product {
    private final String sku;
    private final String name;
    private final BigDecimal price;

    public Product(String sku, String name, double price) {
        this.sku = validateSku(sku);
        this.name = validateName(name);
        this.price = validatePrice(price);
    }

    private String validateSku(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        return sku.trim();
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        return name.trim();
    }

    private BigDecimal validatePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Product price cannot be negative");
        }
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price.doubleValue();
    }

    public BigDecimal getPriceAsBigDecimal() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(sku, product.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku);
    }

    @Override
    public String toString() {
        return String.format("Product{sku='%s', name='%s', price=$%.2f}",
                sku, name, price.doubleValue());
    }
}