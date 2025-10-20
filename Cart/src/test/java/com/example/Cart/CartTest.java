package com.example.Cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartTest {

    private Catalog catalog;
    private Cart cart;
    private Product laptop;
    private Product mouse;
    private InventoryService inventoryService;  // 🔴 RED: Added inventory service

    @BeforeEach
    void setUp() {
        catalog = new Catalog();
        inventoryService = mock(InventoryService.class);  // 🔴 RED: Mock the service
        cart = new Cart(catalog, inventoryService);  // 🔴 RED: Cart now needs InventoryService

        laptop = new Product("SKU001", "Laptop", 999.99);
        mouse = new Product("SKU002", "Mouse", 29.99);

        catalog.addProduct(laptop);
        catalog.addProduct(mouse);
    }

    // ========== EXISTING TESTS (updated with inventory mocks) ==========

    @Test
    void testAddItemToCart() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);  // 🔴 RED: Mock sufficient inventory

        cart.addItem("SKU001", 2);

        assertEquals(1, cart.getItemCount());
        assertTrue(cart.hasItem("SKU001"));

        verify(inventoryService).getAvailable("SKU001");  // 🔴 RED: Verify inventory was checked
    }

    @Test
    void testAddItemWithInvalidSKU() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("INVALID_SKU", 1);
        });

        assertTrue(exception.getMessage().contains("Product not found in catalog"));
    }

    @Test
    void testAddItemWithZeroQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU001", 0);
        });

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void testAddItemWithNegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("SKU001", -5);
        });

        assertEquals("Quantity must be greater than 0", exception.getMessage());
    }

    @Test
    void testRemoveItemFromCart() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);

        cart.addItem("SKU001", 2);
        cart.removeItem("SKU001");

        assertFalse(cart.hasItem("SKU001"));
        assertEquals(0, cart.getItemCount());
    }

    @Test
    void testRemoveNonExistentItem() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cart.removeItem("SKU999");
        });

        assertTrue(exception.getMessage().contains("Item not found in cart"));
    }

    @Test
    void testCalculateTotalWithSingleItem() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);

        cart.addItem("SKU001", 2);

        assertEquals(1999.98, cart.getTotal(), 0.01);
    }

    @Test
    void testCalculateTotalWithMultipleItems() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(inventoryService.getAvailable("SKU002")).thenReturn(20);

        cart.addItem("SKU001", 1);  // 999.99
        cart.addItem("SKU002", 3);  // 89.97

        assertEquals(1089.96, cart.getTotal(), 0.01);
    }

    @Test
    void testEmptyCartTotal() {
        assertEquals(0.0, cart.getTotal(), 0.01);
    }

    @Test
    void testUpdateQuantityOfExistingItem() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);

        cart.addItem("SKU001", 1);
        cart.addItem("SKU001", 2);  // Should update to total quantity 3

        assertEquals(1, cart.getItemCount());
        assertEquals(2999.97, cart.getTotal(), 0.01);  // 3 * 999.99
    }

    // ========== 🔴 RED PHASE: NEW INVENTORY TESTS ==========

    @Test
    void testAddItemWithSufficientInventory() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(100);

        cart.addItem("SKU001", 5);

        assertTrue(cart.hasItem("SKU001"));
        verify(inventoryService).getAvailable("SKU001");
    }

    @Test
    void testAddItemWithExactInventoryMatch() {
        when(inventoryService.getAvailable("SKU002")).thenReturn(3);

        cart.addItem("SKU002", 3);

        assertTrue(cart.hasItem("SKU002"));
        verify(inventoryService).getAvailable("SKU002");
    }

    @Test
    void testAddItemWithInsufficientInventory() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(2);

        InsufficientInventoryException exception = assertThrows(
                InsufficientInventoryException.class,
                () -> cart.addItem("SKU001", 5)
        );

        assertEquals("SKU001", exception.getSku());
        assertEquals(5, exception.getRequestedQuantity());
        assertEquals(2, exception.getAvailableQuantity());
        assertFalse(cart.hasItem("SKU001"));  // Should not add to cart
        verify(inventoryService).getAvailable("SKU001");
    }

    @Test
    void testAddItemWithZeroInventory() {
        when(inventoryService.getAvailable("SKU002")).thenReturn(0);

        InsufficientInventoryException exception = assertThrows(
                InsufficientInventoryException.class,
                () -> cart.addItem("SKU002", 1)
        );

        assertEquals(0, exception.getAvailableQuantity());
        assertFalse(cart.hasItem("SKU002"));
    }

    @Test
    void testAddMultipleItemsWithMixedInventory() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);
        when(inventoryService.getAvailable("SKU002")).thenReturn(1);

        cart.addItem("SKU001", 5);  // Should succeed

        InsufficientInventoryException exception = assertThrows(
                InsufficientInventoryException.class,
                () -> cart.addItem("SKU002", 5)  // Should fail
        );

        assertTrue(cart.hasItem("SKU001"));
        assertFalse(cart.hasItem("SKU002"));
        assertEquals(1, cart.getItemCount());
    }

    @Test
    void testUpdateExistingItemRespectsTotalInventory() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(10);

        cart.addItem("SKU001", 3);  // First add: 3 items

        // When adding more, should check total quantity (3 + 8 = 11 > 10)
        InsufficientInventoryException exception = assertThrows(
                InsufficientInventoryException.class,
                () -> cart.addItem("SKU001", 8)
        );

        // Cart should still have the original 3 items
        assertTrue(cart.hasItem("SKU001"));
        verify(inventoryService, times(2)).getAvailable("SKU001");
    }

    @Test
    void testInventoryCheckHappensAfterCatalogLookup() {
        when(inventoryService.getAvailable("SKU001")).thenReturn(0);

        assertThrows(InsufficientInventoryException.class,
                () -> cart.addItem("SKU001", 1)
        );

        verify(inventoryService).getAvailable("SKU001");
    }

    @Test
    void testCartConstructorRequiresInventoryService() {
        assertThrows(IllegalArgumentException.class,
                () -> new Cart(catalog, null)
        );
    }
}