# Inventory & Billing System

A console-based Inventory and Billing application built with **Core Java**.
Data is stored in files using Java serialization (no database or framework).

## Features
- Add, view, search, update and delete products
- Create bills with multiple items (stock reduces automatically)
- Low stock alerts
- Sales reports: total sales, today's sales, top selling product
- Persistent storage using `ObjectOutputStream` / `ObjectInputStream`

## Tech Concepts Used
- OOP (encapsulation, abstraction, packages)
- Collections (`ArrayList`, `LinkedHashMap`)
- Streams API (reports and search)
- File I/O and Serialization
- Custom Exceptions
- Java Date/Time API

## Project Structure
- `model` : Product, Bill, BillItem
- `service` : InventoryService, BillingService, ReportService
- `util` : FileStorage, InputHelper
- `exception` : custom exceptions
- `main` : Main (menu)

## How to Run
1. Install JDK 17 or higher
2. Clone this repository
3. Open the folder in VS Code (with Extension Pack for Java)
4. Run `src/main/Main.java`

## Author
GOWRI ARAMANDLA 