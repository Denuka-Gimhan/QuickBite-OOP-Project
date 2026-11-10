# Member 3 - Cart Management & Order Tracking

This folder contains ONLY your module's code in a simple, flat structure. It is not an independent runnable app; the full runnable app is in `QuickBite-Combined/` (open that one in IntelliJ).

## Your Tasks
- Shopping cart session management: add items, increase/decrease quantities, remove items, clear cart
- Price and stock synchronization (`Web.syncCart`) to prevent stale checkout prices
- Promo code application, validation, minimum spend thresholds, and discount calculation (`PromoCodeDAO.java`)
- Modern cart UI with food thumbnail photos, quantity steppers, and sticky summary card
- Multi-step checkout experience (`checkout.jsp`) with delivery speed (Standard vs Express) and payment method choice (Cash vs Card)
- Live order tracking timeline fragment (`order-tracking.jspf`) with colored progress steps and kitchen FIFO queue position

## Your Branch Name
`member3-cart`

## Your File List
- **model/**:
  - `model/Cart.java` (session-backed shopping cart)
  - `model/CartItem.java` (cart item line with food photo/emoji reference)
  - `model/PromoCode.java` (percentage discount model with threshold rules)
- **dao/**:
  - `dao/PromoCodeDAO.java` (persistence and lookup for promo codes)
- **servlet/**:
  - `servlet/CartServlet.java` (handles `/cart` add, inc, dec, remove, clear, promo, removepromo)
- **pages/**:
  - `pages/cart.jsp` (shopping cart view with quantity steppers and promo box)
  - `pages/checkout.jsp` (multi-step checkout with delivery/payment options)
  - `pages/order-tracking.jspf` (live order tracking timeline component included in orders.jsp)
  - `pages/cart.css` (cart page layout and stepper styling)
  - `pages/checkout.css` (checkout radio option cards and step indicators)
  - `pages/cart.js` (promo code chip quick fill)
  - `pages/checkout.js` (live delivery fee and grand total calculation)

## How to Work (Git Workflow)
1. Clone the team repository.
2. Checkout your feature branch:
   ```bash
   git checkout -b member3-cart
   ```
3. Edit your files in `Member_3_Cart_Management/` or directly in `QuickBite-Combined/`.
4. Run the sync and verification tools:
   - Windows PowerShell:
     ```powershell
     .\tools\sync-to-combined.ps1
     .\tools\verify.ps1
     ```
   - Linux / macOS Bash:
     ```bash
     ./tools/sync-to-combined.sh
     ./tools/verify.sh
     ```
5. Build and test: `mvn clean package` inside `QuickBite-Combined/` (or via IntelliJ Maven tool window).
6. Commit and push your branch:
   ```bash
   git add .
   git commit -m "Member 3: Updated cart, checkout and tracking timeline"
   git push -u origin member3-cart
   ```
7. Open a Pull Request on GitHub targeting `main`.
