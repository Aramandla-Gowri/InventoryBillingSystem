package service;

import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.Bill;
import model.BillItem;
import model.Product;
import util.FileStorage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private final InventoryService inventory;
    private final List<Bill> bills;
    private final List<BillItem> cart = new ArrayList<>();
    private int nextBillId = 1;

    public BillingService(InventoryService inventory) {
        this.inventory = inventory;
        this.bills = FileStorage.loadBills();
        for (Bill b : bills) {
            if (b.getBillId() >= nextBillId) {
                nextBillId = b.getBillId() + 1;
            }
        }
    }

    public void addToCart(int productId, int qty)
            throws ProductNotFoundException, InsufficientStockException {
        Product p = inventory.getProduct(productId);

        int alreadyInCart = cart.stream()
                .filter(i -> i.getProductId() == productId)
                .mapToInt(BillItem::getQuantity)
                .sum();

        if (p.getQuantity() < alreadyInCart + qty) {
            throw new InsufficientStockException("Only " + p.getQuantity()
                    + " units of '" + p.getName() + "' available.");
        }

        for (BillItem item : cart) {
            if (item.getProductId() == productId) {
                item.setQuantity(item.getQuantity() + qty);
                return;
            }
        }
        cart.add(new BillItem(p.getId(), p.getName(), p.getPrice(), qty));
    }

    public List<BillItem> getCart() {
        return cart;
    }

    public void clearCart() {
        cart.clear();
    }

    public Bill generateBill(String customerName)
            throws ProductNotFoundException, InsufficientStockException {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty. Add items first.");
        }
        for (BillItem item : cart) {
            inventory.reduceStock(item.getProductId(), item.getQuantity());
        }
        Bill bill = new Bill(nextBillId++, customerName, cart);
        bills.add(bill);
        FileStorage.saveBills(bills);
        cart.clear();
        return bill;
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }

    public void printBill(Bill bill) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("\n==================================================");
        System.out.println("                    BILL RECEIPT");
        System.out.println("==================================================");
        System.out.println("Bill No  : " + bill.getBillId());
        System.out.println("Customer : " + bill.getCustomerName());
        System.out.println("Date     : " + bill.getDateTime().format(fmt));
        System.out.println("--------------------------------------------------");
        System.out.printf("%-20s %8s %8s %10s%n", "Item", "Price", "Qty", "Total");
        System.out.println("--------------------------------------------------");
        for (BillItem item : bill.getItems()) {
            System.out.printf("%-20s %8.2f %8d %10.2f%n",
                    item.getProductName(), item.getUnitPrice(),
                    item.getQuantity(), item.getTotal());
        }
        System.out.println("--------------------------------------------------");
        System.out.printf("%-38s %10.2f%n", "GRAND TOTAL:", bill.getGrandTotal());
        System.out.println("==================================================");
    }
}