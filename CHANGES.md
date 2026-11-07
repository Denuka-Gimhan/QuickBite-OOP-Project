# QuickBite Refactoring Change Log (Phase 1–7)

## Overview
This document records every file added, modified, and deleted across the QuickBite codebase during the OOP refactoring to strictly enforce the **7 allowed OOP concepts** (Constructors, Encapsulation, Getters/Setters, Abstract Classes, Interfaces, Java Relationships, Error Handling) while removing all forbidden constructs (Factory design patterns, custom generics, lambdas/streams, inner classes, `var`, `Optional`, and swallowed exceptions).

---

## 1. Files Added

### 1.1 Custom Exception Hierarchy (`com.quickbite.exception`)
*(Mirrored into `QuickBite-Combined/src/main/java/com/quickbite/exception/` and `Shared_Project_Files/exception/`)*
1. `QuickBiteException.java` — Checked base exception with message and cause constructor chaining.
2. `ValidationException.java` — Thrown when model attributes violate business constraints (negative prices, invalid formats).
3. `DuplicateRecordException.java` — Thrown when attempting to register duplicate username, email, promo code, or rating.
4. `RecordNotFoundException.java` — Replaces `return null` across all repositories when querying non-existent entities.
5. `InvalidCredentialsException.java` — Thrown during user authentication on credential mismatches.
6. `DataAccessException.java` — Wraps underlying `java.io.IOException` during file persistence.
7. `OutOfStockException.java` — Thrown by cart operations when attempting to purchase unavailable items.
8. `InvalidPromoException.java` — Thrown when a promotional code is inactive or fails minimum order spend rules.

### 1.2 Contract & Repository Interfaces (`com.quickbite.contract`)
*(Mirrored into `QuickBite-Combined/src/main/java/com/quickbite/contract/` and `Shared_Project_Files/contract/`)*
9. `FileStorable.java` — Entity contract for text file serialization (`getId()`, `toFileLine()`).
10. `Cancellable.java` — Lifecycle contract for order cancellation (`isCancellable()`).
11. `Trackable.java` — Progress contract for status and ETA (`getStatus()`, `getEta()`).
12. `Discountable.java` — Promo discount contract (`getDiscount()`).
13. `Payable.java` — Financial liability contract (`getTotal()`).
14. `UserRepository.java` — Repository interface for user persistence.
15. `FoodRepository.java` — Repository interface for food items and search.
16. `OrderRepository.java` — Repository interface for customer orders.
17. `PromoCodeRepository.java` — Repository interface for discount codes.
18. `DeliveryRepository.java` — Repository interface for delivery trips.
19. `DeliveryPersonRepository.java` — Repository interface for delivery riders.
20. `PaymentRepository.java` — Repository interface for payment transactions.
21. `RatingRepository.java` — Repository interface for customer reviews.

### 1.3 Top-Level Data Structure Node
22. `QueueNode.java` — Top-level class replacing the forbidden nested static class in `OrderQueue`. *(Mirrored in `QuickBite-Combined/src/main/java/com/quickbite/model/` and `Member_4_Order_Management/model/`)*

### 1.4 Architecture & Viva Documentation
23. `OOP_AUDIT.md` — Phase 1 comprehensive audit of the codebase.
24. `OOP_CONCEPTS_MAP.md` — Detailed concept-to-class mapping table for university viva preparation.
25. `CHANGES.md` — Complete file inventory of changes.

---

## 2. Files Deleted (Forbidden Constructs)

1. `QuickBite-Combined/src/main/java/com/quickbite/model/UserFactory.java` & `Member_1_Customer_Management/model/UserFactory.java`
   *(Factory pattern removed; replaced by direct `if/else + new` in `UserDAO`)*
2. `QuickBite-Combined/src/main/java/com/quickbite/model/FoodFactory.java` & `Member_2_Food_Menu_Management/model/FoodFactory.java`
   *(Factory pattern removed; replaced by private `createFood` helper in `FoodDAO` and direct constructor calls in `FoodServlet`)*
3. `QuickBite-Combined/src/main/java/com/quickbite/model/OrderFactory.java` & `Member_4_Order_Management/model/OrderFactory.java`
   *(Factory pattern removed; replaced by private `createOrder` helper in `OrderDAO` and direct constructor calls in `OrderServlet`)*

---

## 3. Files Modified & Refactored

### 3.1 Domain Models (`com.quickbite.model`)
*(Mirrored into matching `Member_X/model/` directories)*
- `User.java`: Implements `FileStorable`, `Serializable`. Added default constructor, chained parameterized constructor, input validation in mutators throwing `ValidationException`, commented Inheritance and Association relationships.
- `AdminUser.java`: Chained constructors via `super(...)` and `this(...)`, commented Inheritance relationship.
- `RegularUser.java`: Chained constructors via `super(...)` and `this(...)`, commented Inheritance relationship.
- `Food.java`: Implements `FileStorable`. Added default constructor, chained parameterized constructor, validation in `setName` and `setPrice` throwing `ValidationException`, commented Inheritance and Association relationships.
- `FastFood.java`, `Beverage.java`, `Dessert.java`, `TraditionalFood.java`: Chained constructors via `super(...)` and `this(...)`, commented Inheritance relationships.
- `Order.java`: Implements `FileStorable`, `Cancellable`, `Trackable`, `Discountable`, `Payable`. Added default constructor, chained constructors, validation in `setAddress` and `setPhone`, commented Inheritance, Composition, and Association relationships.
- `StandardOrder.java`, `ExpressOrder.java`: Chained constructors via `super(...)` and `this(...)`, commented Inheritance relationships.
- `OrderItem.java`: Chained constructors via `this(...)`, validation in `setQty` and `setPrice`, commented Composition relationship with `Order`.
- `Cart.java`: Implements `Discountable`, `Payable`, `Serializable`. Commented Composition with `CartItem` and Aggregation with `PromoCode`.
- `CartItem.java`: Implements `Serializable`. Chained constructors via `this(...)`, validation in `setQty`, commented Composition relationship.
- `PromoCode.java`: Implements `FileStorable`, `Serializable`. Chained constructors via `this(...)`, validation in `setPercent`.
- `Delivery.java`: Implements `FileStorable`, `Trackable`. Chained constructors via `this(...)`, validation in `setPersonName`, commented Aggregation and Association relationships.
- `DeliveryPerson.java`: Implements `FileStorable`. Chained constructors via `this(...)`, validation in `setName` and `setPhone`, commented Aggregation relationship.
- `Payment.java`: Implements `FileStorable`, `Payable`. Chained constructors via `this(...)`, validation in `setAmount`, commented Association relationship.
- `Rating.java`: Implements `FileStorable`. Chained constructors via `this(...)`, validation in `setStars` (1–5), commented Association relationship.
- `Bill.java`: Implements `Payable`. Chained constructors via `this(...)`, commented Aggregation relationship.
- `OrderQueue.java`: Refactored to use the top-level `QueueNode` class instead of a nested class.

### 3.2 Repositories & Data Access Objects (`com.quickbite.dao`)
*(Mirrored into matching `Member_X/dao/` directories)*
- `UserDAO.java`: Implements `UserRepository`. Replaced return-false/return-null with `DuplicateRecordException`, `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Removed `UserFactory`. Replaced `removeIf` lambda with a standard loop. Replaced empty catch block with diagnostic logging.
- `FoodDAO.java`: Implements `FoodRepository`. Replaced return-false/return-null with `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Removed `FoodFactory`. Replaced `removeIf` lambda with a standard loop. Replaced Comparator lambdas with non-generic `QuickSort.sort(list, boolean)`. Replaced empty catch block with diagnostic logging.
- `OrderDAO.java`: Implements `OrderRepository`. Replaced return-false/return-null with `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Removed `OrderFactory`. Replaced `removeIf` lambda with a standard loop. Replaced empty catch block with diagnostic logging.
- `PromoCodeDAO.java`: Implements `PromoCodeRepository`. Replaced return-false/return-null with `DuplicateRecordException`, `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Replaced `removeIf` lambda with a standard loop. Replaced empty catch block with diagnostic logging.
- `DeliveryDAO.java`: Implements `DeliveryRepository`. Replaced return-false/return-null with `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Replaced `removeIf` lambdas with standard loops. Replaced empty catch block with diagnostic logging.
- `DeliveryPersonDAO.java`: Implements `DeliveryPersonRepository`. Replaced return-false/return-null with `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Replaced `removeIf` lambda with a standard loop. Replaced empty catch block with diagnostic logging.
- `PaymentDAO.java`: Implements `PaymentRepository`. Replaced return-false/return-null with `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Replaced `removeIf` lambda with a standard loop. Replaced empty catch block with diagnostic logging.
- `RatingDAO.java`: Implements `RatingRepository`. Replaced return-false/return-null with `DuplicateRecordException`, `RecordNotFoundException`, `ValidationException`, and `DataAccessException`. Replaced `removeIf` lambdas with standard loops. Replaced empty catch block with diagnostic logging.

### 3.3 Utilities (`com.quickbite.util`)
- `Web.java`: Simplified framework glue. Eliminated custom generics (`<T>`) and inner interface `IoSupplier<T>`. All DAO accessors now return their respective repository interfaces (`UserRepository`, `FoodRepository`, etc.). Replaced lambda supplier passing with plain if-null-create + cast.
- `QuickSort.java`: Eliminated custom generic parameter `<T>` and `Comparator` lambdas. Replaced with direct imperative sorting on `List<Food>` by price and dish name.
- `OrderFlow.java`: Updated to interact exclusively through repository interfaces and handle domain exceptions cleanly.

### 3.4 Servlets (`com.quickbite.servlet`)
*(Mirrored into matching `Member_X/servlet/` directories)*
- `UserServlet.java`: References `UserRepository` interface. Replaced manual null checks with structured `try/catch` catching `InvalidCredentialsException`, `DuplicateRecordException`, `RecordNotFoundException`, and `ValidationException`. Preserved all user-facing redirect parameters and error messages.
- `FoodServlet.java`: References `FoodRepository` interface. Direct constructor instantiation replacing `FoodFactory`. Replaced null checks with structured `try/catch` catching `RecordNotFoundException` and `ValidationException`. Preserved all user-facing redirect parameters and error messages.
- `CartServlet.java`: References `FoodRepository` and `PromoCodeRepository` interfaces. Replaced null checks with `try/catch` catching `OutOfStockException`, `InvalidPromoException`, and `RecordNotFoundException`. Preserved all user-facing redirect parameters and error messages.
- `OrderServlet.java`: References `OrderRepository` interface. Replaced `OrderFactory` with direct `StandardOrder`/`ExpressOrder` instantiation. Replaced status filtering lambda with standard loop. Replaced null checks with `try/catch`. Preserved all user-facing redirect parameters and error messages.
- `DeliveryServlet.java`: References `OrderRepository`, `DeliveryRepository`, `DeliveryPersonRepository` interfaces. Replaced sorting lambda with plain loop sort. Replaced null checks with `try/catch`. Preserved all user-facing redirect parameters and error messages.
- `PaymentServlet.java`: References `PaymentRepository` and `OrderRepository` interfaces. Replaced status filtering lambda with standard loop. Replaced null checks with `try/catch`. Preserved all user-facing redirect parameters and error messages.
- `RatingServlet.java`: References `RatingRepository` and `OrderRepository` interfaces. Replaced return-false check with `try/catch` on `DuplicateRecordException`. Preserved all user-facing redirect parameters and error messages.
- `ReportServlet.java`: Eliminated all lambdas, method references (`Integer::sum`), and `computeIfPresent` functional interfaces in favor of standard map lookups, mutations, and plain loop sorting.

### 3.5 Synchronization Tools & Documentation
- `tools/verify.ps1`, `tools/sync-to-combined.ps1`, `tools/verify.sh`, `tools/sync-to-combined.sh`: Updated to synchronize and verify the newly introduced `Shared_Project_Files/contract/` and `Shared_Project_Files/exception/` directories.
- `Member_1..6/README.md`: Updated each member's README with owned files and detailed breakdown of 7 OOP concepts demonstrated.
- `Shared_Project_Files/MEMBERS.md`, `Shared_Project_Files/docs/MEMBERS.md`, `QuickBite-Combined/MEMBERS.md`: Synchronized with latest team assignments.

---

## 4. Verification Check
- **Front-end Files Modified**: **0** (No changes made to any `.jsp`, `.jspf`, `.css`, `.js`, or `web.xml` file).
- **Maven Build Status**: **SUCCESS** (`mvn clean package` generates deployable `target/quickbite.war`).
- **Codebase Synchronization Tool**: **100% PASS** (`verify.ps1` validated 113 files across all member modules and combined application with 0 discrepancies).
