package util;

import model.Bill;
import model.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private static final String DIR = "data";
    private static final String PRODUCTS_FILE = DIR + "/products.dat";
    private static final String BILLS_FILE = DIR + "/bills.dat";

    public static void saveProducts(List<Product> products) {
        save(PRODUCTS_FILE, products);
    }

    public static List<Product> loadProducts() {
        return load(PRODUCTS_FILE);
    }

    public static void saveBills(List<Bill> bills) {
        save(BILLS_FILE, bills);
    }

    public static List<Bill> loadBills() {
        return load(BILLS_FILE);
    }

    private static <T> void save(String path, List<T> list) {
        new File(DIR).mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(new ArrayList<>(list));
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> load(String path) {
        File file = new File(path);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}