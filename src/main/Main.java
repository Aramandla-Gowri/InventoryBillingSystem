package main;

import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.Bill;

import model.Product;
import service.BillingService;
import service.InventoryService;
import service.ReportService;
import util.InputHelper;

import java.util.List;

public class Main {
    private static final InventoryService inventory = new InventoryService();
    private static final BillingService billing = new BillingService(inventory);
    private static final ReportService reports = new ReportService();

    public static void main(String[] args) {
        System.out.println("Inventory & Billing System Started!");
        boolean running = true;

        while (running) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Add Product");
            System.out.println("2. View All Products");
            System.out.println("3. Search Product");
            System.out.println("4. Update Product");
            System.out.println("5. Delete Product");
            System.out.println("6. Create Bill");
            System.out.println("7. View All Bills");
            System.out.println("8. Reports");
            System.out.println("9. Exit");
            int choice = InputHelper.readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> viewProducts(inventory.getAllProducts());
                case 3 -> searchProduct();
                case 4 -> updateProduct();
                case 5 -> deleteProduct();
                case 6 -> createBill();
                case 7 -> viewBills();
                case 8 -> reports.printSalesReport(billing.getAllBills(),
                        inventory.getLowStockProducts(5));
                case 9 -> {
                    System.out.println("Thank you! Data saved. Goodbye.");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addProduct() {
        String name = InputHelper.readString("Product name: ");
        String category = InputHelper.readString("Category: ");
        double price = InputHelper.readDouble("Price: ");
        int qty = InputHelper.readInt("Quantity: ");
        if (price < 0 || qty < 0) {
            System.out.println("Price and quantity cannot be negative.");
            return;
        }
        Product p = inventory.addProduct(name, category, price, qty);
        System.out.println("Product added with ID: " + p.getId());
    }

    private static void viewProducts(List<Product> list) {
        if (list.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.println();
        System.out.printf("%-5s %-20s %-15s %10s %8s%n", "ID", "Name", "Category", "Price", "Qty");
        System.out.println("-----------------------------------------------------------");
        list.forEach(System.out::println);
    }

    private static void searchProduct() {
        String keyword = InputHelper.readString("Enter name or category to search: ");
        viewProducts(inventory.searchProducts(keyword));
    }

    private static void updateProduct() {
        try {
            int id = InputHelper.readInt("Enter product ID to update: ");
            Product existing = inventory.getProduct(id);
            System.out.println("Current: " + existing);
            String name = InputHelper.readString("New name: ");
            double price = InputHelper.readDouble("New price: ");
            int qty = InputHelper.readInt("New quantity: ");
            if (price < 0 || qty < 0) {
                System.out.println("Price and quantity cannot be negative.");
                return;
            }
            inventory.updateProduct(id, name, price, qty);
            System.out.println("Product updated successfully.");
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteProduct() {
        try {
            int id = InputHelper.readInt("Enter product ID to delete: ");
            inventory.deleteProduct(id);
            System.out.println("Product deleted successfully.");
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void createBill() {
        viewProducts(inventory.getAllProducts());
        if (inventory.getAllProducts().isEmpty()) {
            return;
        }

        String more;
        do {
            try {
                int id = InputHelper.readInt("Enter product ID: ");
                int qty = InputHelper.readInt("Enter quantity: ");
                if (qty <= 0) {
                    System.out.println("Quantity must be greater than 0.");
                } else {
                    billing.addToCart(id, qty);
                    System.out.println("Added to cart.");
                }
            } catch (ProductNotFoundException | InsufficientStockException e) {
                System.out.println("Error: " + e.getMessage());
            }
            more = InputHelper.readString("Add another item? (y/n): ");
        } while (more.equalsIgnoreCase("y"));

        if (billing.getCart().isEmpty()) {
            System.out.println("Cart is empty. Bill cancelled.");
            return;
        }

        String customer = InputHelper.readString("Customer name: ");
        try {
            Bill bill = billing.generateBill(customer);
            billing.printBill(bill);
        } catch (ProductNotFoundException | InsufficientStockException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            billing.clearCart();
        }
    }

    private static void viewBills() {
        List<Bill> bills = billing.getAllBills();
        if (bills.isEmpty()) {
            System.out.println("No bills generated yet.");
            return;
        }
        bills.forEach(billing::printBill);
    }
}