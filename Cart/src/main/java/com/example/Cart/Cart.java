package com.example.Cart;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a shopping cart that holds multiple CartItem objects.
 * Now includes inventory validation before adding items.
 */
public class Cart {

    private final Catalog catalog;
    private final InventoryService inventoryService;  // 🔴 RED: Added inventory service
    private final Map<String, CartItem> items;

    /**
     * 🔴 RED: Constructor now requires InventoryService
     */
    public Cart(Catalog catalog, InventoryService inventoryService) {
        if (catalog == null) {
            throw new IllegalArgumentException("Catalog cannot be null");
        }
        if (inventoryService == null) {
            throw new IllegalArgumentException("InventoryService cannot be null");
        }
        this.catalog = catalog;
        this.inventoryService = inventoryService;  // 🔴 RED: Store inventory service
        this.items = new HashMap<>();
    }

    /**
     * Adds or updates an item in the cart.
     * 🔴 RED: Now checks inventory before adding.
     */
    public void addItem(String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Product product = catalog.findProductBySku(sku);
        if (product == null) {
            throw new IllegalArgumentException("Product not found in catalog: " + sku);
        }

        // 🔴 RED: Check inventory availability
        // This is the NEW logic that doesn't exist yet - tests will FAIL here
        int availableQuantity = inventoryService.getAvailable(sku);
        int currentCartQuantity = items.containsKey(sku) ? items.get(sku).getQuantity() : 0;
        int totalRequiredQuantity = currentCartQuantity + quantity;

        if (totalRequiredQuantity > availableQuantity) {
            throw new InsufficientInventoryException(sku, totalRequiredQuantity, availableQuantity);
        }

        // Only add to cart if inventory check passes
        items.merge(sku, new CartItem(sku, quantity, product.getPrice()), (existing, newItem) -> {
            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
            return existing;
        });
    }

    /**
     * Removes an item from the cart by SKU.
     */
    public void removeItem(String sku) {
        if (!items.containsKey(sku)) {
            throw new IllegalArgumentException("Item not found in cart: " + sku);
        }
        items.remove(sku);
    }

    /**
     * Calculates the total price of all items in the cart.
     */
    public double getTotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    /**
     * Returns the number of distinct items in the cart.
     */
    public int getItemCount() {
        return items.size();
    }

    /**
     * Checks whether a specific item exists in the cart.
     */
    public boolean hasItem(String sku) {
        return items.containsKey(sku);
    }

    /**
     * Returns an unmodifiable view of the cart items.
     */
    public Map<String, CartItem> getItems() {
        return Collections.unmodifiableMap(items);
    }
}