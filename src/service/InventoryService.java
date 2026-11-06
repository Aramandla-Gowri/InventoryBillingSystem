package service;

import exception.InsufficientStockException;
import exception.ProductNotFoundException;
import model.Product;
import util.FileStorage;

import java.util.*;
import java.util.stream.Collectors;

public class InventoryService {
    private final Map<Integer, Product> products = new LinkedHashMap<>();
    private int nextId = 1;

    public InventoryService() {
        for (Product p : FileStorage.loadProducts()) {
            products.put(p.getId(), p);
            if (p.getId() >= nextId) {
                nextId = p.getId() + 1;
            }
        }
    }

    public Product addProduct(String name, String category, double price, int quantity) {
        Product p = new Product(nextId++, name, category, price, quantity);
        products.put(p.getId(), p);
        save();
        return p;
    }

    public Product getProduct(int id) throws ProductNotFoundException {
        Product p = products.get(id);
        if (p == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }
        return p;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void updateProduct(int id, String name, double price, int quantity)
            throws ProductNotFoundException {
        Product p = getProduct(id);
        p.setName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        save();
    }

    public void deleteProduct(int id) throws ProductNotFoundException {
        getProduct(id);
        products.remove(id);
        save();
    }

    public List<Product> searchProducts(String keyword) {
        String key = keyword.toLowerCase();
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(key)
                        || p.getCategory().toLowerCase().contains(key))
                .collect(Collectors.toList());
    }

    public List<Product> getLowStockProducts(int threshold) {
        return products.values().stream()
                .filter(p -> p.getQuantity() <= threshold)
                .collect(Collectors.toList());
    }

    public void reduceStock(int id, int qty)
            throws ProductNotFoundException, InsufficientStockException {
        Product p = getProduct(id);
        if (p.getQuantity() < qty) {
            throw new InsufficientStockException("Only " + p.getQuantity()
                    + " units of '" + p.getName() + "' available.");
        }
        p.setQuantity(p.getQuantity() - qty);
        save();
    }

    private void save() {
        FileStorage.saveProducts(getAllProducts());
    }
}