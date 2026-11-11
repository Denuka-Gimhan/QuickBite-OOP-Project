# Member 4 - Order Processing & Kitchen Queue

This folder contains ONLY your module's code in a simple, flat structure. It is not an independent runnable app; the full runnable app is in `QuickBite-Combined/` (open that one in IntelliJ).

## Your Tasks
- Order placement, validation, and lifecycle persistence in `orders.txt`
- Order polymorphism: `StandardOrder` (standard ETA and delivery fee) and `ExpressOrder` (rush priority and fee)
- Kitchen preparation FIFO queue management (`OrderQueue.java`)
- Order workflow orchestrations (`OrderFlow.java`) notifying queue, payment, and delivery modules
- Customer order history view and cancellation of pending orders
- Admin order management: status updates (Pending, Preparing, On the way, Delivered, Cancelled) and record deletion
- Modern order cards with items breakdown, fees, status badges, and embedded tracking fragments

## Your Branch Name
`member4-orders`

## Your File List
- **model/**:
  - `model/Order.java` (abstract base order with items serialization)
  - `model/StandardOrder.java`
  - `model/ExpressOrder.java`
  - `model/OrderItem.java` (snapshot of item name, quantity, price, emoji, image)
  - `model/OrderFactory.java` (factory pattern for order types)
  - `model/OrderQueue.java` (in-memory FIFO kitchen order queue)
- **dao/**:
  - `dao/OrderDAO.java` (CRUD persistence on `orders.txt`)
- **servlet/**:
  - `servlet/OrderServlet.java` (handles `/orders` place, cancel, status, delete, process next)
- **util/**:
  - `util/OrderFlow.java` (lifecycle hooks for queue, payments, and deliveries)
- **pages/**:
  - `pages/orders.jsp` (order list and kitchen queue management view)
  - `pages/orders.css` (order cards and timeline stepper styling)
  - `pages/orders.js` (order interaction and auto-scroll script)

## How to Work (Git Workflow)
1. Clone the team repository.
2. Checkout your feature branch:
   ```bash
   git checkout -b member4-orders
   ```
3. Edit your files in `Member_4_Order_Management/` or directly in `QuickBite-Combined/`.
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
   git commit -m "Member 4: Updated order processing and kitchen queue"
   git push -u origin member4-orders
   ```
7. Open a Pull Request on GitHub targeting `main`.
