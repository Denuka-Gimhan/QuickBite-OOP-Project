# Member 6 - Payment, Billing and Reports

This folder contains ONLY your module's code in a simple, flat structure. It is not an independent runnable app; the full runnable app is in `QuickBite-Combined/` (open that one in IntelliJ).

## Your Tasks
- Payment transaction records tracking (`payments.txt`) and revenue reconciliation
- Admin payment actions: Mark payment as Paid, process refunds for cancelled orders
- Official printable commercial invoice (`bill.jsp`) displaying dish photo thumbnails, item prices, promo discounts, delivery fees, and amount due
- Customer 5-star order reviews and ratings (`ratings.txt`), rating averages, and review moderation
- Comprehensive business analytics dashboard (`reports.jsp`):
  - 7-day revenue trend bar chart (pure CSS/JS animation)
  - Top best-selling dishes ranking
  - Order status and delivery type distributions
  - Promo code utilization table
  - Customer 5-star rating breakdown chart
  - Full orders dataset export to CSV (`/reports?format=csv`)

## Your Branch Name
`member6-payments`

## Your File List
- **model/**:
  - `model/Payment.java` (transaction status, method, and amount model)
  - `model/Rating.java` (customer star rating and comment model)
  - `model/Bill.java` (invoice view model combining order and payment records)
- **dao/**:
  - `dao/PaymentDAO.java` (CRUD persistence on `payments.txt`)
  - `dao/RatingDAO.java` (CRUD persistence on `ratings.txt`)
- **servlet/**:
  - `servlet/PaymentServlet.java` (handles `/billing` GET list/invoice, POST markPaid/refund)
  - `servlet/RatingServlet.java` (handles `/ratings` POST add/delete)
  - `servlet/ReportServlet.java` (handles `/reports` GET HTML analytics and CSV export)
- **pages/**:
  - `pages/billing.jsp` (admin revenue and payment transactions dashboard)
  - `pages/bill.jsp` (print-friendly customer invoice with food photo thumbnails)
  - `pages/reports.jsp` (business intelligence analytics with animated charts)
  - `pages/order-payment.jspf` (payment badge and bill link included in orders.jsp)
  - `pages/order-rating.jspf` (star rating widget included in orders.jsp)
  - `pages/billing.css` (billing dashboard styling)
  - `pages/bill.css` (printable invoice and media-print styling)
  - `pages/reports.css` (dashboard metrics and CSS chart bar styling)
  - `pages/billing.js` (billing interactions)
  - `pages/reports.js` (chart animation script)

## How to Work (Git Workflow)
1. Clone the team repository.
2. Checkout your feature branch:
   ```bash
   git checkout -b member6-payments
   ```
3. Edit your files in `Member_6_Payment_and_Reports/` or directly in `QuickBite-Combined/`.
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
   git commit -m "Member 6: Updated billing, invoice, reports and charts"
   git push -u origin member6-payments
   ```
7. Open a Pull Request on GitHub targeting `main`.
