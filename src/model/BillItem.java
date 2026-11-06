package model;

import java.io.Serializable;

public class BillItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public BillItem(int productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotal() {
        return unitPrice * quantity;
    }
}