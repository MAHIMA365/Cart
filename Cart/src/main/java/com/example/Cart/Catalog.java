package com.example.Cart;

import java.util.*;
import java.util.stream.Collectors;

public class Catalog {
    private final Map<String, Product> products;

    public Catalog() {
        this.products = new HashMap<>();
    }

    public void addProduct(Product product) {
        validateProduct(product);
        products.put(product.getSku(), product);
    }

    public void addProducts(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Product list cannot be null");
        }
        products.forEach(this::addProduct);
    }

    public Product findProductBySku(String sku) {
        validateSku(sku);
        if (sku.trim().isEmpty()) {
            return null;
        }
        return products.get(sku.trim());
    }

    public List<Product> findProductsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String searchTerm = name.trim().toLowerCase();
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public List<Product> findProductsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
        return products.values().stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public boolean removeProduct(String sku) {
        validateSku(sku);
        return products.remove(sku.trim()) != null;
    }

    public boolean containsProduct(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return false;
        }
        return products.containsKey(sku.trim());
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(new ArrayList<>(products.values()));
    }

    public int size() {
        return products.size();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public void clear() {
        products.clear();
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    private void validateSku(String sku) {
        if (sku == null) {
            throw new IllegalArgumentException("SKU cannot be null");
        }
    }

    @Override
    public String toString() {
        return String.format("Catalog{products=%d}", products.size());
    }
}