package com.example.Cart.impl;

import com.example.Cart.InventoryService;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory implementation of InventoryService.
 * In production, this would connect to a real inventory database or external service.
 */
public class SimpleInventoryService implements InventoryService {

    private final Map<String, Integer> inventory;

    public SimpleInventoryService() {
        this.inventory = new HashMap<>();
    }

    /**
     * Sets the available inventory for a specific SKU.
     * Useful for testing and initial setup.
     *
     * @param sku the product SKU
     * @param quantity the available quantity
     */
    public void setInventory(String sku, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Inventory quantity cannot be negative");
        }
        inventory.put(sku, quantity);
    }

    /**
     * Decreases inventory by the specified quantity.
     * This would be called after a successful cart checkout in a real system.
     *
     * @param sku the product SKU
     * @param quantity the quantity to decrease
     */
    public void decreaseInventory(String sku, int quantity) {
        int current = inventory.getOrDefault(sku, 0);
        int newQuantity = Math.max(0, current - quantity);
        inventory.put(sku, newQuantity);
    }

    @Override
    public int getAvailable(String sku) {
        return inventory.getOrDefault(sku, 0);
    }

    /**
     * Clears all inventory data.
     */
    public void clear() {
        inventory.clear();
    }
}