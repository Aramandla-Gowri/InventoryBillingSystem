package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    private int billId;
    private String customerName;
    private LocalDateTime dateTime;
    private List<BillItem> items;
    private double grandTotal;

    public Bill(int billId, String customerName, List<BillItem> items) {
        this.billId = billId;
        this.customerName = customerName;
        this.dateTime = LocalDateTime.now();
        this.items = new ArrayList<>(items);
        this.grandTotal = items.stream().mapToDouble(BillItem::getTotal).sum();
    }

    public int getBillId() { return billId; }
    public String getCustomerName() { return customerName; }
    public LocalDateTime getDateTime() { return dateTime; }
    public List<BillItem> getItems() { return items; }
    public double getGrandTotal() { return grandTotal; }
}