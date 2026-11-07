# Who owns which file (Group G21 - QuickBite)

Package root: `src/main/java/com/quickbite/`   Web pages: `src/main/webapp/`
**Rule: edit only YOUR files.** Shared files are already wired for everyone - ask the team before changing them.

## Member 1 - Customer Management
- model: User, AdminUser, RegularUser, UserFactory
- dao: UserDAO   - servlet: UserServlet   - util: PasswordUtil
- pages: login.jsp, register.jsp, profile.jsp, users.jsp
- Tasks done: signup/login (username OR email), unique email, salted password hash, profile edit, admin user list/delete

## Member 2 - Menu and Food Item Management
- model: Food, Beverage, Dessert, FastFood, TraditionalFood, FoodFactory
- dao: FoodDAO   - servlet: FoodServlet, **FoodImageServlet** (new, /food-image)   - util: **QuickSort** (allocated algorithm: sort food by price)
- pages: foods.jsp, food-form.jsp
- Tasks done: CRUD, search, category filter, Quick Sort by price, out-of-stock flag, **food photo upload** (JPG/PNG/WEBP/GIF, max 5 MB, saved in quickbite-data/images)

## Member 3 - Order Placement and Tracking
- model: Cart, CartItem, **PromoCode** (new)   - dao: **PromoCodeDAO** (new)   - servlet: CartServlet
- pages: cart.jsp, checkout.jsp, order-tracking.jspf
- Tasks done: cart, promo codes (WELCOME10, QUICK20), live price/stock refresh, order tracking steps, food photo thumbnail in the cart

## Member 4 - Order Processing
- model: Order, ExpressOrder, StandardOrder, OrderItem, OrderFactory, **OrderQueue** (allocated data structure: FIFO queue)
- dao: OrderDAO   - servlet: OrderServlet   - util: **OrderFlow** (what happens to queue/payment/delivery when an order changes)
- pages: orders.jsp
- Tasks done: place/cancel/status/delete, kitchen queue + "Process next order" button

## Member 5 - Delivery Management
- model: **DeliveryPerson, Delivery**   - dao: **DeliveryPersonDAO, DeliveryDAO**   - servlet: **DeliveryServlet** (/delivery)
- pages: delivery.jsp, order-delivery.jspf
- Tasks done: rider CRUD, assign rider, picked up, delivered, rider availability

## Member 6 - Payment, Billing and Reports
- model: **Payment, Rating, Bill**   - dao: **PaymentDAO, RatingDAO**   - servlet: **PaymentServlet** (/billing), **RatingServlet** (/ratings), **ReportServlet** (/reports)
- pages: billing.jsp, bill.jsp, reports.jsp, order-payment.jspf, order-rating.jspf
- Tasks done: payment records, printable invoice, refund, ratings, sales/best-seller/promo/rating reports, CSV export

## Shared (do not edit alone)
FileHandler.java, Web.java, header.jspf, top.jspf, css/style.css (card/photo polish), WEB-INF/web.xml (jsp-config UTF-8), pom.xml

## How the parts talk to each other
Order placed -> `OrderFlow.onPlaced` -> enqueue in OrderQueue + create Payment (Member 4 -> 6)
Admin "Process next order" -> oldest Pending order becomes Preparing (FIFO)
Delivery: assign rider (Preparing orders only) -> Picked up (order "On the way") -> Delivered (order "Delivered", payment "Paid", rider free)
Cancel -> queue entry removed, payment Cancelled/Refunded, rider freed.

## Default logins / codes
admin / admin123   |   promo codes: WELCOME10 (10% over Rs.1,000), QUICK20 (20% over Rs.3,000)
