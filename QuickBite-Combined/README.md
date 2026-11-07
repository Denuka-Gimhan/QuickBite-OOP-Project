# QuickBite - Online Food Delivery Management System

Java Servlets + JSP (Jakarta Servlet 6.0, Tomcat 10.1, Java 17+), text file data storage in `<user home>/quickbite-data/`.

## Architecture & Module Ownership
- **Member 1 (Customer Management)**: User authentication, registration, profile, avatar initial circles with stable color, role permissions.
- **Member 2 (Food Menu Management)**: Food CRUD, photo uploads/storage/serving, category filtering, recursive Quick Sort by price (`util/QuickSort.java`).
- **Member 3 (Cart Management)**: Session cart, promo code verification, multi-step checkout, live order tracking timeline.
- **Member 4 (Order Management)**: Order lifecycle persistence, FIFO kitchen queue (`model/OrderQueue.java`), status workflows (`util/OrderFlow.java`).
- **Member 5 (Delivery Management)**: Rider roster, vehicle allocation, dispatch tracking (`/delivery`).
- **Member 6 (Payment and Reports)**: Payment settlement, refunds, printable invoice with dish photos (`/billing`), ratings, business analytics dashboard (`/reports`).
- **Shared Files**: Thread-safe file handler (`util/FileHandler.java`), singleton web provider (`util/Web.java`), design system tokens (`css/style.css`), client framework (`js/app.js`).

## How to Build and Run
1. Maven CLI:
   ```bash
   mvn clean package
   ```
2. Or use IntelliJ IDEA's Maven tool panel: double-click `clean`, then `package`.
3. The generated WAR file will be located at `target/quickbite.war`.
4. Deploy to Apache Tomcat 10.1+ at `http://localhost:8080/quickbite/`.

## Logins & Promo Codes
- Admin: `admin` / `admin123`
- Regular: `nimal` / `nimal123`
- Promos: `WELCOME10` (10% off over Rs. 1,000), `QUICK20` (20% off over Rs. 3,000)
