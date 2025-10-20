package com.example.Cart;

import com.example.Cart.impl.SimpleInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 🔴 RED PHASE: Tests for InventoryService implementation.
 * These tests define the expected behavior of the inventory service.
 */
class InventoryServiceTest {

    private SimpleInventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new SimpleInventoryService();
    }

    // ========== Basic Functionality Tests ==========

    @Test
    void testGetAvailableReturnsZeroForUnknownSku() {
        int available = inventoryService.getAvailable("UNKNOWN_SKU");

        assertEquals(0, available, "Should return 0 for unknown SKU");
    }

    @Test
    void testSetAndGetInventory() {
        inventoryService.setInventory("SKU001", 100);

        int available = inventoryService.getAvailable("SKU001");

        assertEquals(100, available, "Should return the set inventory quantity");
    }

    @Test
    void testSetInventoryForMultipleSkus() {
        inventoryService.setInventory("SKU001", 50);
        inventoryService.setInventory("SKU002", 75);
        inventoryService.setInventory("SKU003", 100);

        assertEquals(50, inventoryService.getAvailable("SKU001"));
        assertEquals(75, inventoryService.getAvailable("SKU002"));
        assertEquals(100, inventoryService.getAvailable("SKU003"));
    }

    @Test
    void testUpdateExistingInventory() {
        inventoryService.setInventory("SKU001", 100);
        inventoryService.setInventory("SKU001", 50);  // Update

        assertEquals(50, inventoryService.getAvailable("SKU001"),
                "Should update existing inventory");
    }

    @Test
    void testSetInventoryToZero() {
        inventoryService.setInventory("SKU001", 100);
        inventoryService.setInventory("SKU001", 0);

        assertEquals(0, inventoryService.getAvailable("SKU001"),
                "Should allow setting inventory to zero");
    }

    // ========== Validation Tests ==========

    @Test
    void testSetInventoryWithNegativeQuantityThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.setInventory("SKU001", -10);
        });

        assertEquals("Inventory quantity cannot be negative", exception.getMessage());
    }

    @Test
    void testSetInventoryWithNullSkuThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            inventoryService.setInventory(null, 100);
        });
    }

    // ========== Decrease Inventory Tests ==========

    @Test
    void testDecreaseInventory() {
        inventoryService.setInventory("SKU001", 100);
        inventoryService.decreaseInventory("SKU001", 30);

        assertEquals(70, inventoryService.getAvailable("SKU001"),
                "Should decrease inventory by specified amount");
    }

    @Test
    void testDecreaseInventoryToExactlyZero() {
        inventoryService.setInventory("SKU001", 50);
        inventoryService.decreaseInventory("SKU001", 50);

        assertEquals(0, inventoryService.getAvailable("SKU001"),
                "Should allow decreasing to exactly zero");
    }

    @Test
    void testDecreaseInventoryBeyondAvailableStopsAtZero() {
        inventoryService.setInventory("SKU001", 30);
        inventoryService.decreaseInventory("SKU001", 50);  // Try to decrease more than available

        assertEquals(0, inventoryService.getAvailable("SKU001"),
                "Should not go below zero, should stop at zero");
    }

    @Test
    void testDecreaseInventoryForNonExistentSkuDoesNothing() {
        inventoryService.decreaseInventory("UNKNOWN_SKU", 10);

        assertEquals(0, inventoryService.getAvailable("UNKNOWN_SKU"),
                "Should remain at 0 for non-existent SKU");
    }

    @Test
    void testMultipleDecreasesOnSameSku() {
        inventoryService.setInventory("SKU001", 100);

        inventoryService.decreaseInventory("SKU001", 20);
        assertEquals(80, inventoryService.getAvailable("SKU001"));

        inventoryService.decreaseInventory("SKU001", 30);
        assertEquals(50, inventoryService.getAvailable("SKU001"));

        inventoryService.decreaseInventory("SKU001", 10);
        assertEquals(40, inventoryService.getAvailable("SKU001"));
    }

    // ========== Clear Inventory Tests ==========

    @Test
    void testClearInventory() {
        inventoryService.setInventory("SKU001", 100);
        inventoryService.setInventory("SKU002", 50);

        inventoryService.clear();

        assertEquals(0, inventoryService.getAvailable("SKU001"));
        assertEquals(0, inventoryService.getAvailable("SKU002"));
    }

    @Test
    void testClearEmptyInventory() {
        inventoryService.clear();  // Should not throw exception

        assertEquals(0, inventoryService.getAvailable("SKU001"));
    }

    // ========== Edge Case Tests ==========

    @Test
    void testLargeInventoryQuantity() {
        inventoryService.setInventory("SKU001", Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, inventoryService.getAvailable("SKU001"));
    }

    @Test
    void testEmptyStringSkuIsValid() {
        // Empty string should be treated as a valid SKU (business decision)
        inventoryService.setInventory("", 100);

        assertEquals(100, inventoryService.getAvailable(""));
    }

    @Test
    void testWhitespaceSkuIsValid() {
        // Whitespace SKU should be treated as valid (business decision)
        inventoryService.setInventory("  ", 100);

        assertEquals(100, inventoryService.getAvailable("  "));
    }

    @Test
    void testSkuIsCaseSensitive() {
        inventoryService.setInventory("SKU001", 100);
        inventoryService.setInventory("sku001", 50);

        assertEquals(100, inventoryService.getAvailable("SKU001"));
        assertEquals(50, inventoryService.getAvailable("sku001"),
                "SKU should be case-sensitive");
    }

    // ========== Realistic Scenario Tests ==========

    @Test
    void testRealisticInventoryManagementScenario() {
        // Initial stock
        inventoryService.setInventory("LAPTOP-001", 50);
        inventoryService.setInventory("MOUSE-002", 200);

        // Customer 1 buys items
        inventoryService.decreaseInventory("LAPTOP-001", 2);
        inventoryService.decreaseInventory("MOUSE-002", 1);

        assertEquals(48, inventoryService.getAvailable("LAPTOP-001"));
        assertEquals(199, inventoryService.getAvailable("MOUSE-002"));

        // Restock laptops
        inventoryService.setInventory("LAPTOP-001", 100);
        assertEquals(100, inventoryService.getAvailable("LAPTOP-001"));

        // Customer 2 buys more
        inventoryService.decreaseInventory("LAPTOP-001", 5);
        inventoryService.decreaseInventory("MOUSE-002", 10);

        assertEquals(95, inventoryService.getAvailable("LAPTOP-001"));
        assertEquals(189, inventoryService.getAvailable("MOUSE-002"));
    }

    @Test
    void testConcurrentInventoryOperations() {
        inventoryService.setInventory("SKU001", 100);

        // Simulate multiple operations
        inventoryService.decreaseInventory("SKU001", 10);
        inventoryService.setInventory("SKU002", 50);
        inventoryService.decreaseInventory("SKU001", 20);
        inventoryService.decreaseInventory("SKU002", 5);

        assertEquals(70, inventoryService.getAvailable("SKU001"));
        assertEquals(45, inventoryService.getAvailable("SKU002"));
    }
}