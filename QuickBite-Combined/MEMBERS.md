# Who Owns Which File (Group G21 - QuickBite)

Package root in runnable app: `QuickBite-Combined/src/main/java/com/quickbite/`
Web pages root: `QuickBite-Combined/src/main/webapp/`
Member folders use a clean flat layout: `model/`, `dao/`, `servlet/`, `util/`, `pages/`.

**Rule: edit only YOUR files.** Run `tools/sync-to-combined` and `tools/verify` before committing.

---

## Member 1 - Customer Management
- **Branch**: `member1-customer`
- **model/**: `User.java`, `AdminUser.java`, `RegularUser.java`, `UserFactory.java`
- **dao/**: `UserDAO.java`
- **servlet/**: `UserServlet.java` (`/users`)
- **util/**: `PasswordUtil.java` (salted SHA-256)
- **pages/**: `login.jsp`, `register.jsp`, `profile.jsp`, `users.jsp`, `login.css`, `register.css`, `profile.css`, `users.css`, `auth.js`
- **Key Features**: Signup/login (username or email), unique email check, salted password hashing, profile edits, dynamic Unicode first-letter avatar circle with stable user color, admin badge, admin user management.

---

## Member 2 - Menu and Food Item Management
- **Branch**: `member2-menu`
- **model/**: `Food.java`, `Beverage.java`, `Dessert.java`, `FastFood.java`, `TraditionalFood.java`, `FoodFactory.java`
- **dao/**: `FoodDAO.java`
- **servlet/**: `FoodServlet.java` (`/foods`), `FoodImageServlet.java` (`/food-image`)
- **util/**: `QuickSort.java` (recursive Quick Sort by price)
- **pages/**: `foods.jsp`, `food-form.jsp`, `foods.css`, `food-form.css`, `foods.js`, `food-form.js`
- **Key Features**: Food CRUD, text search, category filtering, Quick Sort, out-of-stock toggle ribbon, food photo upload from laptop (max 5 MB, JPG/PNG/WEBP/GIF saved to `quickbite-data/images`), photo live preview and removal, non-wrapping admin action bar.

---

## Member 3 - Cart Management & Order Placement
- **Branch**: `member3-cart`
- **model/**: `Cart.java`, `CartItem.java`, `PromoCode.java`
- **dao/**: `PromoCodeDAO.java`
- **servlet/**: `CartServlet.java` (`/cart`)
- **pages/**: `cart.jsp`, `checkout.jsp`, `order-tracking.jspf`, `cart.css`, `checkout.css`, `cart.js`, `checkout.js`
- **Key Features**: Shopping cart session management, item photos in cart, quantity steppers, promo codes (`WELCOME10`, `QUICK20`), live price/stock sync, multi-step checkout with delivery speed (Standard vs Express) and payment choice (Cash vs Card), live status timeline with kitchen queue position.

---

## Member 4 - Order Processing & Kitchen Queue
- **Branch**: `member4-orders`
- **model/**: `Order.java`, `ExpressOrder.java`, `StandardOrder.java`, `OrderItem.java`, `OrderFactory.java`, `OrderQueue.java` (FIFO Queue)
- **dao/**: `OrderDAO.java`
- **servlet/**: `OrderServlet.java` (`/orders`)
- **util/**: `OrderFlow.java` (order lifecycle coordinator)
- **pages/**: `orders.jsp`, `orders.css`, `orders.js`
- **Key Features**: Order placement, order cancellation, status updates, kitchen FIFO queue processing ("Process next order"), order history list with embedded tracking, delivery, payment, and rating fragments.

---

## Member 5 - Delivery Management & Fleet Roster
- **Branch**: `member5-delivery`
- **model/**: `Delivery.java`, `DeliveryPerson.java`
- **dao/**: `DeliveryDAO.java`, `DeliveryPersonDAO.java`
- **servlet/**: `DeliveryServlet.java` (`/delivery`)
- **pages/**: `delivery.jsp`, `order-delivery.jspf`, `delivery.css`, `delivery.js`
- **Key Features**: Rider registration, vehicle tracking (Motorbike, Scooter, Bicycle, Car), rider availability toggle (Available vs Off Duty vs On Delivery), rider assignment to orders, dispatch transitions (Assigned &rarr; Picked Up &rarr; Delivered), delivery banner on order cards.

---

## Member 6 - Payment, Billing and Reports
- **Branch**: `member6-payments`
- **model/**: `Payment.java`, `Rating.java`, `Bill.java`
- **dao/**: `PaymentDAO.java`, `RatingDAO.java`
- **servlet/**: `PaymentServlet.java` (`/billing`), `RatingServlet.java` (`/ratings`), `ReportServlet.java` (`/reports`)
- **pages/**: `billing.jsp`, `bill.jsp`, `reports.jsp`, `order-payment.jspf`, `order-rating.jspf`, `billing.css`, `bill.css`, `reports.css`, `billing.js`, `reports.js`
- **Key Features**: Payment ledger tracking, mark paid, refunds, printable commercial invoice with food photos, 5-star customer ratings and reviews, business intelligence dashboard with 7-day revenue bar charts, best sellers, order status distribution, promo usage, and CSV export.

---

## Shared Project Files (Team Consensus Required)
- `util/Web.java`: Singleton DAOs, user session, cart sync, currency formatting.
- `util/FileHandler.java`: Thread-safe text storage in `<user home>/quickbite-data/`.
- `pages/header.jspf`: Top navigation bar with logo, search, letter avatar dropdown, dark mode toggle, and mobile menu.
- `pages/top.jspf`: Document head, Google Fonts, responsive viewport, script tags.
- `pages/index.jsp`: Landing home page with hero section, category chips, popular dishes, and how-it-works.
- `pages/footer.jspf`: Site-wide multi-column footer.
- `css/style.css`: Design system tokens, warm palette, dark mode styles, buttons, tables, toasts, modals.
- `js/app.js`: Global client features (toasts, theme switch, modal confirmations).
- `config/web.xml`: Jakarta Servlet 6.0 deployment descriptor with UTF-8 JSP config.
- `config/pom.xml`: Maven WAR build configuration.

---

## Synchronizing and Verifying Files
Use the provided automation scripts in `tools/`:
- **Windows PowerShell**:
  ```powershell
  .\tools\sync-to-combined.ps1   # Copies all member flat folders to QuickBite-Combined
  .\tools\verify.ps1             # Verifies 100% byte-for-byte consistency
  ```
- **Linux / macOS Bash**:
  ```bash
  ./tools/sync-to-combined.sh
  ./tools/verify.sh
  ```

## Default Logins & Data
- Admin Account: `admin` / `admin123`
- Active Promo Codes:
  - `WELCOME10`: 10% off on orders over Rs. 1,000
  - `QUICK20`: 20% off on orders over Rs. 3,000
- Data Location: `<user home>/quickbite-data/`
