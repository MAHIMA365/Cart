package com.example.Cart;

/**
 * Represents a single item in the shopping cart.
 */
public class CartItem {
    private final String sku;  // 🧠 Refactor: made sku final — it's an identifier, should not change.
    private int quantity;
    private double price;

    // 🧠 Refactor: Added validation logic inside constructor
    public CartItem(String sku, int quantity, double price) {
        if (sku == null || sku.isEmpty()) {
            throw new IllegalArgumentException("SKU cannot be null or empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
    }

    // 🧠 Refactor: Added getters and setters for proper encapsulation
    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    // 🧠 Refactor: Added validation inside setters to prevent invalid updates
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.price = price;
    }

    // 🧠 Refactor: Added a convenience method to calculate subtotal
    public double getSubtotal() {
        return price * quantity;
    }

    // 🧠 Refactor: Added a clean toString() override for debugging/logging
    @Override
    public String toString() {
        return String.format("CartItem{sku='%s', quantity=%d, price=%.2f}", sku, quantity, price);
    }
}

