package com.example.Cart;

/**
 * Exception thrown when requested quantity exceeds available inventory.
 * Provides detailed information about the inventory shortage.
 */
public class InsufficientInventoryException extends RuntimeException {

  private final String sku;
  private final int requestedQuantity;
  private final int availableQuantity;

  /**
   * Creates a new InsufficientInventoryException.
   *
   * @param sku the product SKU that has insufficient inventory
   * @param requestedQuantity the quantity that was requested
   * @param availableQuantity the quantity that is actually available
   */
  public InsufficientInventoryException(String sku, int requestedQuantity, int availableQuantity) {
    super(String.format("Insufficient inventory for SKU '%s': requested %d, available %d",
            sku, requestedQuantity, availableQuantity));
    this.sku = sku;
    this.requestedQuantity = requestedQuantity;
    this.availableQuantity = availableQuantity;
  }

  public String getSku() {
    return sku;
  }

  public int getRequestedQuantity() {
    return requestedQuantity;
  }

  public int getAvailableQuantity() {
    return availableQuantity;
  }
}