package com.example.Cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RED PHASE - These tests are expected to FAIL
 * Product model validation tests
 *
 * Run these tests - they will fail because Product class doesn't exist yet.
 * This is the RED phase of TDD.
 */
@DisplayName("Product Model Tests (RED Phase)")
public class ProductTest {

    @Test
    @DisplayName("Should create a valid product with SKU, name, and price")
    public void testCreateValidProduct() {
        // Arrange & Act
        Product product = new Product("SKU-001", "Laptop", 999.99);

        // Assert
        assertEquals("SKU-001", product.getSku(), "SKU should match");
        assertEquals("Laptop", product.getName(), "Name should match");
        assertEquals(999.99, product.getPrice(), 0.01, "Price should match");
    }

    @Test
    @DisplayName("Should throw exception when SKU is null")
    public void testCreateProductWithNullSkuThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product(null, "Laptop", 999.99),
                "Should throw exception for null SKU"
        );
        assertTrue(exception.getMessage().contains("SKU"));
    }

    @Test
    @DisplayName("Should throw exception when SKU is empty")
    public void testCreateProductWithEmptySkuThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("", "Laptop", 999.99),
                "Should throw exception for empty SKU"
        );
        assertTrue(exception.getMessage().contains("SKU"));
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    public void testCreateProductWithNullNameThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("SKU-001", null, 999.99),
                "Should throw exception for null name"
        );
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    public void testCreateProductWithEmptyNameThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("SKU-001", "", 999.99),
                "Should throw exception for empty name"
        );
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    @DisplayName("Should throw exception when price is negative")
    public void testCreateProductWithNegativePriceThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Product("SKU-001", "Laptop", -10.00),
                "Should throw exception for negative price"
        );
        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("Should allow zero price for free products")
    public void testCreateProductWithZeroPrice() {
        // Arrange & Act
        Product product = new Product("SKU-002", "Free Sample", 0.0);

        // Assert
        assertEquals(0.0, product.getPrice(), 0.01, "Zero price should be allowed");
    }

    @Test
    @DisplayName("Should have matching SKUs for products with same SKU")
    public void testProductEquality() {
        // Arrange
        Product product1 = new Product("SKU-001", "Laptop", 999.99);
        Product product2 = new Product("SKU-001", "Different Laptop", 1299.99);

        // Assert - Products with same SKU should have matching SKU values
        assertEquals(product1.getSku(), product2.getSku(), "SKUs should match");
    }
}