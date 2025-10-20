package com.example.Cart;

/**
 * Service interface for checking product inventory availability.
 * Implementations should provide real-time inventory data.
 */
public interface InventoryService {

    /**
     * Returns the available quantity for a given product SKU.
     *
     * @param sku the product SKU to check
     * @return the available quantity, or 0 if not available/not found
     */
    int getAvailable(String sku);
}