package service;

import model.Bill;
import model.BillItem;
import model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReportService {

    public double totalSales(List<Bill> bills) {
        return bills.stream().mapToDouble(Bill::getGrandTotal).sum();
    }

    public double todaySales(List<Bill> bills) {
        LocalDate today = LocalDate.now();
        return bills.stream()
                .filter(b -> b.getDateTime().toLocalDate().equals(today))
                .mapToDouble(Bill::getGrandTotal)
                .sum();
    }

    public Optional<Map.Entry<String, Integer>> topSellingProduct(List<Bill> bills) {
        return bills.stream()
                .flatMap(b -> b.getItems().stream())
                .collect(Collectors.groupingBy(BillItem::getProductName,
                        Collectors.summingInt(BillItem::getQuantity)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    public void printSalesReport(List<Bill> bills, List<Product> lowStock) {
        System.out.println("\n=============== SALES REPORT ===============");
        System.out.println("Total bills generated : " + bills.size());
        System.out.printf("Total sales           : %.2f%n", totalSales(bills));
        System.out.printf("Today's sales         : %.2f%n", todaySales(bills));

        Optional<Map.Entry<String, Integer>> top = topSellingProduct(bills);
        if (top.isPresent()) {
            System.out.println("Top selling product   : " + top.get().getKey()
                    + " (" + top.get().getValue() + " units sold)");
        } else {
            System.out.println("Top selling product   : No sales yet");
        }

        System.out.println("\n--- Low Stock Products (quantity <= 5) ---");
        if (lowStock.isEmpty()) {
            System.out.println("All products have sufficient stock.");
        } else {
            lowStock.forEach(System.out::println);
        }
        System.out.println("============================================");
    }
}