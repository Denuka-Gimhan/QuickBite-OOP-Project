# QuickBite - Online Food Delivery System (Team Package)

Welcome to the restructured, redesigned **QuickBite** online food delivery web application built with Java Servlets, JSP, Maven, and Tomcat 10.1 (Jakarta Servlet 6.0, JDK 17).

---

## Directory Organization
- `QuickBite-Combined/`: The unified runnable Maven web application (open this directory in IntelliJ IDEA).
- `Member_1_Customer_Management/`: Flat layout (`model/`, `dao/`, `servlet/`, `util/`, `pages/`).
- `Member_2_Food_Menu_Management/`: Flat layout (`model/`, `dao/`, `servlet/`, `util/`, `pages/`).
- `Member_3_Cart_Management/`: Flat layout (`model/`, `dao/`, `servlet/`, `pages/`).
- `Member_4_Order_Management/`: Flat layout (`model/`, `dao/`, `servlet/`, `util/`, `pages/`).
- `Member_5_Delivery_Management/`: Flat layout (`model/`, `dao/`, `servlet/`, `pages/`).
- `Member_6_Payment_and_Reports/`: Flat layout (`model/`, `dao/`, `servlet/`, `pages/`).
- `Shared_Project_Files/`: Shared utilities (`Web.java`, `FileHandler.java`), design system (`style.css`), app scripts (`app.js`), global templates (`header.jspf`, `top.jspf`, `index.jsp`, `footer.jspf`), configs (`web.xml`, `pom.xml`), and docs.
- `tools/`:
  - `sync-to-combined.ps1` & `sync-to-combined.sh`: Synchronizes all flat member folders into `QuickBite-Combined`.
  - `verify.ps1` & `verify.sh`: Validates 100% byte-for-byte consistency between member folders and `QuickBite-Combined`.

---

## Key Features & Redesign Highlights
1. **Modern Appetizing Design System**: Warm palette, responsive typography, sticky blurred navigation bar, accessible modals, and toast notifications.
2. **Dynamic User Avatar (Part C)**: Automatic uppercase first-letter avatar circle with stable user color hashing, admin badge, and interactive profile dropdown.
3. **Food Photos Everywhere (Part D)**: Upload from laptop (max 5 MB, JPG/PNG/WEBP/GIF), live preview, cover-zoom cards on menu, thumbnails in cart, checkout, and printable invoice.
4. **Dark Mode Toggle**: Built-in toggle remembered across sessions in `localStorage`.
5. **Interactive Business Reports**: Pure CSS/JS animated 7-day sales revenue bar chart, best-seller rankings, promo usage, customer review moderation, and CSV data export.

---

## How to Build and Run
- **Prerequisites**: JDK 17+ and Apache Tomcat 10.1.x.
- **Maven Build**:
  ```bash
  cd QuickBite-Combined
  mvn clean package
  ```
  *(If Maven is not installed in your terminal PATH, use IntelliJ IDEA's built-in Maven tool panel: double-click `clean`, then `package`)*
- **Deploy**: Copy `QuickBite-Combined/target/quickbite.war` to Tomcat's `webapps/` folder, or run directly via IntelliJ Smart Tomcat / Tomcat Run Configuration.
- **Access URL**: [http://localhost:8080/quickbite/](http://localhost:8080/quickbite/)

---

## Default Credentials & Promo Codes
- **Administrator Login**: `admin` / `admin123`
- **Default Customer**: `nimal` / `nimal123`
- **Promo Codes**:
  - `WELCOME10`: 10% discount on orders over Rs. 1,000
  - `QUICK20`: 20% discount on orders over Rs. 3,000
- **Data Storage**: Automatically created at `<user home>/quickbite-data/`.
