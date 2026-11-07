# QuickBite Verification & Testing Checklist

Perform this checklist before submitting or merging pull requests:

## Member 1 - Customer Management
- [ ] Sign up a new customer account with username, email, phone, and delivery address.
- [ ] Verify validation rejects duplicate usernames and existing emails.
- [ ] Log in with username (`admin` / `admin123`).
- [ ] Log in with email address.
- [ ] Check dynamic user avatar circle displays the first letter in uppercase with stable color.
- [ ] Check admin badge displays next to admin name.
- [ ] Edit profile details and change password.
- [ ] View admin user management list and test deleting a test account.

## Member 2 - Food Menu Management
- [ ] Browse menu cards and verify photo cover zoom, category tags, prep times, and prices.
- [ ] Test text search (e.g. "kottu", "burger").
- [ ] Filter by category (Fast Food, Traditional, Beverages, Desserts).
- [ ] Test price sort (Low &rarr; High, High &rarr; Low, Clear).
- [ ] Admin: Add new food item with a laptop photo upload (JPG/PNG/WEBP/GIF, &le; 5 MB).
- [ ] Verify photo preview and removal checkbox.
- [ ] Admin: Toggle food item "Out of stock" &mdash; verify "Out of stock" ribbon and disabled Add button.
- [ ] Admin: Edit and delete food items.

## Member 3 - Cart Management & Tracking
- [ ] Add dishes to cart and verify live badge count increments in navigation.
- [ ] In cart: verify food thumbnail photos and adjust quantities with stepper (+ and &minus;).
- [ ] Apply promo code `WELCOME10` (requires subtotal &ge; Rs. 1,000) and verify 10% discount line.
- [ ] Apply promo code `QUICK20` (requires subtotal &ge; Rs. 3,000).
- [ ] Proceed to checkout: verify pre-filled address and phone number.
- [ ] Select delivery speed: Standard (free over Rs. 5,000) vs Express rush.
- [ ] Select payment method: Cash on delivery vs Card on delivery.
- [ ] Verify live order summary updates delivery fees and totals accurately.
- [ ] Place order and check the tracking timeline stepper (Placed &rarr; Preparing &rarr; On the way &rarr; Delivered).

## Member 4 - Order Processing & Kitchen Queue
- [ ] Verify placed order shows queue position (e.g. "Number 1 in kitchen queue").
- [ ] Cancel a pending order and verify status updates to Cancelled.
- [ ] Admin: Check Kitchen FIFO queue card and click "Process Next Order" &mdash; verify oldest order becomes Preparing.
- [ ] Admin: Update order status to "On the way" and "Delivered".

## Member 5 - Delivery Management
- [ ] View `/delivery` dashboard.
- [ ] In "Orders waiting for a rider": assign an available rider to a Preparing order.
- [ ] In "Active deliveries in progress": click "Picked Up" (order updates to "On the way").
- [ ] Click "Mark Delivered" (order updates to "Delivered", rider is freed, payment updates to "Paid").
- [ ] Add a new rider, toggle available/off-duty status, and test deleting a rider.

## Member 6 - Payment, Billing & Reports
- [ ] Click "View Invoice" on an order: verify printable layout with dish photo thumbnails, totals, and `window.print()`.
- [ ] Admin billing dashboard: check KPI tiles (Paid, Pending, Refunded), Mark Paid, and Refund actions.
- [ ] Submit customer star review on a delivered order.
- [ ] View `/reports` dashboard: check 7-day revenue trend bar chart animation, best-sellers list, order status distributions, promo utilization, and star breakdown.
- [ ] Click "Download Orders CSV" and verify exported dataset.
