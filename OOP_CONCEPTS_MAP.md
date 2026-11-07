# QuickBite OOP Concepts Mapping Reference

This document maps the **7 Allowed Object-Oriented Programming (OOP) Concepts** to their exact implementations in the refactored **QuickBite** codebase. Use this table as a quick revision guide for the university OOP viva examination.

---

## The 7 Core OOP Concepts Matrix

| # | Concept | Class / File | Viva Explanation (One-line Summary) |
|---|---------|--------------|-------------------------------------|
| **1** | **Constructors** | `com.quickbite.model.User` | Demonstrates no-arg default constructor and parameterized constructor chaining using `this(...)`. |
| 1 | Constructors | `com.quickbite.model.AdminUser` | Demonstrates constructor chaining to the superclass hierarchy using `super(...)` and `this(...)`. |
| 1 | Constructors | `com.quickbite.model.RegularUser` | Calls `super(...)` to initialize inherited user attributes with default or supplied credentials. |
| 1 | Constructors | `com.quickbite.model.Food` | Overloaded constructors chaining via `this(...)` to supply default availability and emoji. |
| 1 | Constructors | `com.quickbite.model.FastFood` | Chains `super(...)` to configure FastFood category attributes and preparation specifics. |
| 1 | Constructors | `com.quickbite.model.Beverage` | Invokes `super(...)` with beverage parameters to establish default drink properties. |
| 1 | Constructors | `com.quickbite.model.Dessert` | Chains `super(...)` to initialize dessert item attributes in the base class. |
| 1 | Constructors | `com.quickbite.model.TraditionalFood` | Chained via `super(...)` to initialize traditional Sri Lankan dish fields. |
| 1 | Constructors | `com.quickbite.model.Order` | Chains default and parameterized constructors using `this(...)` to ensure default status and payment. |
| 1 | Constructors | `com.quickbite.model.StandardOrder` | Chained to `super(...)` with standard delivery defaults and order lines. |
| 1 | Constructors | `com.quickbite.model.ExpressOrder` | Chained to `super(...)` with express rush delivery defaults and line items. |
| 1 | Constructors | `com.quickbite.model.OrderItem` | Constructor overloading with `this(...)` chaining to provide default emoji and image paths. |
| 1 | Constructors | `com.quickbite.model.CartItem` | Constructor chaining with `this(...)` initializing item snapshot and default quantity. |
| 1 | Constructors | `com.quickbite.model.PromoCode` | Constructor validates discount percentage boundaries and minimum spend thresholds. |
| 1 | Constructors | `com.quickbite.model.Delivery` | Parameterized constructor initializes trip tracking timestamps and rider references. |
| 1 | Constructors | `com.quickbite.model.DeliveryPerson` | Chained constructor setting initial rider availability and vehicle classification. |
| 1 | Constructors | `com.quickbite.model.Payment` | Initializes payment method, amount, timestamp, and order association. |
| 1 | Constructors | `com.quickbite.model.Rating` | Constructor clamps star ratings within valid 1–5 range and trims customer comments. |
| 1 | Constructors | `com.quickbite.model.Bill` | Constructor overloading chaining `this(order, null)` to `this(order, payment)`. |
| 1 | Constructors | `com.quickbite.model.QueueNode` | Demonstrates node constructor chaining `this(orderId, null)` for linked list creation. |
| 1 | Constructors | `com.quickbite.exception.QuickBiteException` | Exception constructor chaining using `super(message, cause)` for root error causality. |
|---|---|---|---|
| **2** | **Encapsulation** | `com.quickbite.model.User` | Private state fields accessed via mutators; `setUsername` and `setEmail` throw `ValidationException` on empty inputs. |
| 2 | Encapsulation | `com.quickbite.model.Food` | Encapsulates dish pricing; `setPrice` throws `ValidationException` for negative values or NaN/Infinity. |
| 2 | Encapsulation | `com.quickbite.model.Order` | Private delivery and item list fields; `setAddress` and `setPhone` validate non-empty contact information. |
| 2 | Encapsulation | `com.quickbite.model.OrderItem` | Private fields; mutators enforce positive quantity (`qty >= 1`) and non-negative unit price. |
| 2 | Encapsulation | `com.quickbite.model.Cart` | Hides internal `Map<Integer, CartItem>` collection, exposing safe operations (`add`, `change`, `remove`). |
| 2 | Encapsulation | `com.quickbite.model.CartItem` | Private fields; `setQty` validates that quantity cannot be set to a negative number. |
| 2 | Encapsulation | `com.quickbite.model.PromoCode` | Encapsulates promo rules; `setPercent` enforces strict range between 0 and 100%. |
| 2 | Encapsulation | `com.quickbite.model.Delivery` | Encapsulates delivery state transitions, preventing invalid direct state manipulation. |
| 2 | Encapsulation | `com.quickbite.model.DeliveryPerson` | Encapsulates rider contact and duty status behind `isAvailable` and `setAvailable`. |
| 2 | Encapsulation | `com.quickbite.model.Payment` | Enforces that payment amounts cannot be negative via guarded `setAmount` mutator. |
| 2 | Encapsulation | `com.quickbite.model.Rating` | Enforces 1–5 star range invariant in `setStars` by throwing `ValidationException`. |
| 2 | Encapsulation | `com.quickbite.model.OrderQueue` | Hides internal head and tail pointer manipulation with synchronized FIFO queue operations. |
|---|---|---|---|
| **3** | **Getters and Setters** | All `com.quickbite.model.*` classes | Follow standard JavaBean conventions (e.g., `getName()`, `getPrice()`, `isAvailable()`, `isAdmin()`) to enable seamless JSP EL binding (`${food.name}`, `${user.admin}`). |
|---|---|---|---|
| **4** | **Abstract Classes** | `com.quickbite.model.User` | Abstract class declaring abstract methods (`getRole`, `isAdmin`, `getGreeting`) with concrete avatar and hashing logic. |
| 4 | Abstract Classes | `com.quickbite.model.Food` | Abstract class with abstract polymorphic hooks (`getCategory`, `getPreparationTime`, `getDetails`, `getThemeColor`) and concrete pricing/file routines. |
| 4 | Abstract Classes | `com.quickbite.model.Order` | Abstract class with abstract methods (`getType`, `getLabel`, `getDeliveryFee`, `getEta`) combined with concrete total/subtotal calculation. |
|---|---|---|---|
| **5** | **Interfaces** | `com.quickbite.contract.FileStorable` | Implemented by 8 entities (`User`, `Food`, `Order`, `Delivery`, `DeliveryPerson`, `Payment`, `Rating`, `PromoCode`) for text file persistence. |
| 5 | Interfaces | `com.quickbite.contract.Cancellable` | Contract for entities supporting cancellation (`Order`), defining `boolean isCancellable()`. |
| 5 | Interfaces | `com.quickbite.contract.Trackable` | Contract for tracking progress status and ETA (`Order`, `Delivery`), defining `getStatus()` and `getEta()`. |
| 5 | Interfaces | `com.quickbite.contract.Discountable` | Contract for calculating or applying promo discounts (`Order`, `Cart`), defining `double getDiscount()`. |
| 5 | Interfaces | `com.quickbite.contract.Payable` | Contract for monetary liability (`Order`, `Cart`, `Payment`, `Bill`), defining `double getTotal()`. |
| 5 | Interfaces | `com.quickbite.contract.UserRepository` | Repository interface implemented by `UserDAO` and referenced by `UserServlet` and `Web`. |
| 5 | Interfaces | `com.quickbite.contract.FoodRepository` | Repository interface implemented by `FoodDAO` and referenced by `FoodServlet`, `CartServlet`, `Web`. |
| 5 | Interfaces | `com.quickbite.contract.OrderRepository` | Repository interface implemented by `OrderDAO` and referenced by `OrderServlet`, `DeliveryServlet`, `PaymentServlet`, `Web`. |
| 5 | Interfaces | `com.quickbite.contract.PromoCodeRepository` | Repository interface implemented by `PromoCodeDAO` and referenced by `CartServlet` and `Web`. |
| 5 | Interfaces | `com.quickbite.contract.DeliveryRepository` | Repository interface implemented by `DeliveryDAO` and referenced by `DeliveryServlet` and `Web`. |
| 5 | Interfaces | `com.quickbite.contract.DeliveryPersonRepository` | Repository interface implemented by `DeliveryPersonDAO` and referenced by `DeliveryServlet` and `Web`. |
| 5 | Interfaces | `com.quickbite.contract.PaymentRepository` | Repository interface implemented by `PaymentDAO` and referenced by `PaymentServlet`, `OrderFlow`, `Web`. |
| 5 | Interfaces | `com.quickbite.contract.RatingRepository` | Repository interface implemented by `RatingDAO` and referenced by `RatingServlet` and `Web`. |
|---|---|---|---|
| **6** | **Java Relationships** | `User` hierarchy | **Inheritance (IS-A)**: `AdminUser` and `RegularUser` inherit common state and behavior from abstract `User`. |
| 6 | Java Relationships | `Food` hierarchy | **Inheritance (IS-A)**: `FastFood`, `Beverage`, `Dessert`, and `TraditionalFood` inherit from abstract `Food`. |
| 6 | Java Relationships | `Order` hierarchy | **Inheritance (IS-A)**: `StandardOrder` and `ExpressOrder` inherit from abstract `Order`. |
| 6 | Java Relationships | `Order` owns `OrderItem`s | **Composition (HAS-A)**: `Order` owns its `OrderItem` instances; line items are created and destroyed with the parent order. |
| 6 | Java Relationships | `Cart` owns `CartItem`s | **Composition (HAS-A)**: `Cart` manages internal `CartItem` objects that cease to exist when the cart is cleared. |
| 6 | Java Relationships | `Cart` has `PromoCode` | **Aggregation (HAS-A)**: `Cart` optionally holds a `PromoCode` which exists independently in the system. |
| 6 | Java Relationships | `Delivery` has `DeliveryPerson` | **Aggregation (HAS-A)**: `Delivery` references a `DeliveryPerson`; riders exist independently of individual deliveries. |
| 6 | Java Relationships | `Bill` has `Order` & `Payment` | **Aggregation (HAS-A)**: `Bill` aggregates an `Order` and a `Payment` on demand to produce a customer invoice. |
| 6 | Java Relationships | `OrderQueue` has `QueueNode`s | **Aggregation (HAS-A)**: `OrderQueue` links top-level `QueueNode` objects referencing `Order` IDs. |
| 6 | Java Relationships | `Order` ↔ `User` | **Association**: `Order` references `User` via `userId` foreign reference. |
| 6 | Java Relationships | `Payment` ↔ `Order` | **Association**: `Payment` references `Order` via `orderId` foreign reference. |
| 6 | Java Relationships | `Rating` ↔ `Order` & `User` | **Association**: `Rating` links customer review to an `orderId` and a `userId`. |
| 6 | Java Relationships | Servlets depend on Repositories | **Dependency (uses)**: Controllers call repository interfaces to perform domain logic and view dispatching. |
| 6 | Java Relationships | DAOs depend on `FileHandler` | **Dependency (uses)**: Data access objects depend on `FileHandler` to execute atomic file I/O operations. |
| 6 | Java Relationships | `FoodDAO` depends on `QuickSort` | **Dependency (uses)**: `FoodDAO.search` invokes `QuickSort.sort` to reorder dishes by price. |
|---|---|---|---|
| **7** | **Error Handling** | `com.quickbite.exception.QuickBiteException` | Base checked exception with message and cause constructors for domain error propagation. |
| 7 | Error Handling | `com.quickbite.exception.ValidationException` | Thrown when entity invariants fail (e.g. empty names, negative prices, invalid email/phone). |
| 7 | Error Handling | `com.quickbite.exception.DuplicateRecordException` | Thrown when attempting to insert an entity with an existing unique key (username, email, promo code, rating). |
| 7 | Error Handling | `com.quickbite.exception.RecordNotFoundException` | Replaces `return null` across all repositories when querying by ID, code, or username. |
| 7 | Error Handling | `com.quickbite.exception.InvalidCredentialsException`| Thrown during user authentication when username/email or password hashes do not match. |
| 7 | Error Handling | `com.quickbite.exception.DataAccessException` | Wraps low-level `java.io.IOException` during file reads/writes, decoupling caller from file system specifics. |
| 7 | Error Handling | `com.quickbite.exception.OutOfStockException` | Thrown by cart operations when attempting to order an unavailable food item. |
| 7 | Error Handling | `com.quickbite.exception.InvalidPromoException` | Thrown when a promo code is inactive, invalid, or does not satisfy minimum order thresholds. |
| 7 | Error Handling | Servlets (`UserServlet`, `FoodServlet`, etc.) | Comprehensive `try/catch/finally` blocks handling domain exceptions and mapping them to user-facing feedback without swallowing errors. |

---

## Forbidden Patterns Removed Summary
- **No Design Patterns**: All Factory classes (`UserFactory`, `FoodFactory`, `OrderFactory`) deleted; instantiations use standard `new` expressions.
- **No Custom Generics**: Custom `<T>` in `QuickSort` and `Web` removed; algorithms operate directly on domain collections.
- **No Lambdas or Streams**: All `removeIf(->)`, `stream()`, method references (`::`), and Comparator lambdas replaced with imperative for-loops.
- **No Inner/Nested Classes**: `OrderQueue.Node` and `Web.IoSupplier` replaced with top-level `QueueNode` class and direct assignments.
- **No Enums, Records, or `var`**: Pure Java classes with explicit types throughout the entire codebase.
- **Zero Empty Catch Blocks**: Replaced all `catch (NumberFormatException ignored)` with proper logging or exception propagation.
