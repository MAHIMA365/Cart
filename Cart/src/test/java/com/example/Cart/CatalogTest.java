package com.example.Cart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Catalog service tests
 * Tests for both GREEN and REFACTOR phases
 */
@DisplayName("Catalog Service Tests")
public class CatalogTest {

    private Catalog catalog;

    @BeforeEach
    @DisplayName("Setup - Initialize catalog before each test")
    public void setUp() {
        catalog = new Catalog();
    }

    @Test
    @DisplayName("Should add product to catalog and retrieve it by SKU")
    public void testAddProductToCatalog() {
        // Arrange
        Product product = new Product("SKU-001", "Laptop", 999.99);

        // Act
        catalog.addProduct(product);
        Product retrieved = catalog.findProductBySku("SKU-001");

        // Assert
        assertNotNull(retrieved, "Product should be found in catalog");
        assertEquals("SKU-001", retrieved.getSku(), "SKU should match");
        assertEquals("Laptop", retrieved.getName(), "Name should match");
        assertEquals(999.99, retrieved.getPrice(), 0.01, "Price should match");
    }

    @Test
    @DisplayName("Should add multiple products and retrieve each by SKU")
    public void testAddMultipleProductsToCatalog() {
        // Arrange
        Product laptop = new Product("SKU-001", "Laptop", 999.99);
        Product mouse = new Product("SKU-002", "Mouse", 29.99);
        Product keyboard = new Product("SKU-003", "Keyboard", 79.99);

        // Act
        catalog.addProduct(laptop);
        catalog.addProduct(mouse);
        catalog.addProduct(keyboard);

        // Assert
        assertNotNull(catalog.findProductBySku("SKU-001"), "Laptop should be found");
        assertNotNull(catalog.findProductBySku("SKU-002"), "Mouse should be found");
        assertNotNull(catalog.findProductBySku("SKU-003"), "Keyboard should be found");

        assertEquals("Laptop", catalog.findProductBySku("SKU-001").getName());
        assertEquals("Mouse", catalog.findProductBySku("SKU-002").getName());
        assertEquals("Keyboard", catalog.findProductBySku("SKU-003").getName());
    }

    @Test
    @DisplayName("Should return null when searching for non-existent SKU")
    public void testFindProductBySkuReturnsNullForMissingSku() {
        // Arrange
        Product product = new Product("SKU-001", "Laptop", 999.99);
        catalog.addProduct(product);

        // Act
        Product notFound = catalog.findProductBySku("SKU-999");

        // Assert
        assertNull(notFound, "Should return null for non-existent SKU");
    }

    @Test
    @DisplayName("Should return null when searching in empty catalog")
    public void testFindProductBySkuInEmptyCatalog() {
        // Act
        Product notFound = catalog.findProductBySku("SKU-001");

        // Assert
        assertNull(notFound, "Should return null when catalog is empty");
    }

    @Test
    @DisplayName("Should update existing product when adding duplicate SKU")
    public void testAddProductWithDuplicateSkuUpdatesExisting() {
        // Arrange
        Product original = new Product("SKU-001", "Laptop", 999.99);
        Product updated = new Product("SKU-001", "Gaming Laptop", 1499.99);

        // Act
        catalog.addProduct(original);
        catalog.addProduct(updated);
        Product retrieved = catalog.findProductBySku("SKU-001");

        // Assert
        assertNotNull(retrieved, "Product should exist");
        assertEquals("Gaming Laptop", retrieved.getName(), "Name should be updated");
        assertEquals(1499.99, retrieved.getPrice(), 0.01, "Price should be updated");
    }

    @Test
    @DisplayName("Should throw exception when adding null product")
    public void testAddNullProductThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> catalog.addProduct(null),
                "Should throw exception when adding null product"
        );
        assertTrue(exception.getMessage().contains("Product"), "Exception message should mention Product");
    }

    @Test
    @DisplayName("Should throw exception when searching with null SKU")
    public void testFindProductByNullSkuThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> catalog.findProductBySku(null),
                "Should throw exception for null SKU"
        );
        assertTrue(exception.getMessage().contains("SKU"), "Exception message should mention SKU");
    }

    @Test
    @DisplayName("Should return null when searching with empty SKU")
    public void testFindProductByEmptySkuReturnsNull() {
        // Act
        Product notFound = catalog.findProductBySku("");

        // Assert
        assertNull(notFound, "Should return null for empty SKU");
    }
}