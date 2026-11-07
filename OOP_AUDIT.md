# QuickBite OOP Architecture Audit (Phase 1)

## Executive Summary
This document provides a comprehensive audit of the **QuickBite** codebase (`projectmy/QuickBite-Combined`), evaluating its compliance against the 7 required Object-Oriented Programming (OOP) concepts and identifying all forbidden patterns (design patterns, lambdas, generics, inner classes, exception swallowing, and return-null/return-false error signaling).

---

## 1. Evaluation of the 7 Allowed OOP Concepts

| # | Concept | Current Status in Codebase | Missing Elements / Required Refactoring |
|---|---------|-----------------------------|-------------------------------------------|
| 1 | **Constructors** | Parameterized constructors exist in most domain models. Some classes have constructor overloads (`OrderItem`, `CartItem`). | Lack of explicit constructor chaining (`this(...)`, `super(...)`) in several models. Constructors do not perform domain validation or throw `ValidationException`. No-arg JavaBean constructors are missing in some classes. |
| 2 | **Encapsulation** | Fields are private; access is mostly mediated via getters/setters. | Setters and constructors perform zero input validation (e.g., negative prices, zero quantities, empty names, malformed emails are accepted). Fields can be set to invalid states without validation exceptions. |
| 3 | **Getters and Setters** | Standard JavaBean naming exists to support JSP EL expressions (e.g., `${food.name}`, `${order.total}`). | Need to ensure 100% preservation of all public getter/boolean-getter names used in JSPs, while adding validation to mutators (setters). |
| 4 | **Abstract Classes** | Three abstract hierarchies exist: `User` (`AdminUser`, `RegularUser`), `Food` (`FastFood`, `Beverage`, `Dessert`, `TraditionalFood`), and `Order` (`StandardOrder`, `ExpressOrder`). | Abstract classes currently contain minimal shared concrete logic. They need clear documentation of concrete shared methods vs. abstract polymorphism hooks, properly chained constructors, and explicit relationship annotations. |
| 5 | **Interfaces** | Almost entirely absent from domain logic. Only standard framework interfaces (`Serializable`, `HttpServlet`) are used. | **Entire contract layer missing.** Must create package `com.quickbite.contract` with: `FileStorable`, `Cancellable`, `Trackable`, `Discountable`, `Payable`, and repository interfaces (`UserRepository`, `FoodRepository`, `OrderRepository`, `DeliveryRepository`, `DeliveryPersonRepository`, `PaymentRepository`, `RatingRepository`, `PromoCodeRepository`). |
| 6 | **Java Relationships** | Implicitly present in architecture but not clearly articulated or typed via interfaces. | Must explicitly structure and comment the 5 relationship types:<br>• **Inheritance (IS-A)**: `User` hierarchy, `Food` hierarchy, `Order` hierarchy.<br>• **Composition (HAS-A, lifecycle bound)**: `Order` owns `OrderItem`s; `Cart` owns `CartItem`s.<br>• **Aggregation (HAS-A, independent lifecycle)**: `Delivery` aggregates `DeliveryPerson`; `Bill` aggregates `Order` & `Payment`.<br>• **Association (uses/related)**: `Order` ↔ `User` (`userId`), `Payment` ↔ `Order`, `Rating` ↔ `Order`/`User`.<br>• **Dependency (uses)**: Servlets depend on Repositories; Repositories depend on `FileHandler`. |
| 7 | **Error Handling** | Fragmented and flawed: `try/catch` is used ad-hoc, exceptions are swallowed (`catch ... ignored`), and errors are signaled via `null` or `false`. | **Custom exception hierarchy missing.** Must create `com.quickbite.exception`: `QuickBiteException` (base), `ValidationException`, `DuplicateRecordException`, `RecordNotFoundException`, `InvalidCredentialsException`, `DataAccessException`, `OutOfStockException`, `InvalidPromoException`. Replace all return-null / return-false error signaling with explicit `throw` and handle them in servlets. |

---

## 2. Forbidden Constructs Identified

### 2.1 Factory Classes (Design Patterns)
Design patterns are forbidden under assignment rules. The following factory classes must be removed:
1. `com.quickbite.model.UserFactory` — Creates `AdminUser` or `RegularUser`. *(Remove class; move instantiation logic into private method in `UserDAO`)*
2. `com.quickbite.model.FoodFactory` — Creates `FastFood`, `Beverage`, `Dessert`, or `TraditionalFood`. *(Remove class; move instantiation logic into private method in `FoodDAO`)*
3. `com.quickbite.model.OrderFactory` — Creates `StandardOrder` or `ExpressOrder`. *(Remove class; move instantiation logic into private method in `OrderDAO` and direct constructor calls)*

### 2.2 Custom Generics
1. `com.quickbite.util.QuickSort` — Uses generic type parameter `<T>`:
   ```java
   public static <T> void sort(List<T> list, Comparator<? super T> cmp)
   private static <T> void quickSort(List<T> a, int lo, int hi, Comparator<? super T> cmp)
   ```
   *(Must be refactored to directly sort `List<Food>` or implement non-generic partition logic without custom generic methods)*
2. `com.quickbite.util.Web` — Uses generic helper:
   ```java
   private interface IoSupplier<T>
   private static <T> T singleton(ServletContext ctx, String key, IoSupplier<T> make)
   ```
   *(Must be replaced with straightforward if-null-create + cast for each DAO/queue)*

### 2.3 Lambdas, Method References & Streams
Lambdas and functional interfaces must be eliminated in favor of plain loops, conditionals, and standard classes:
1. **`DeliveryDAO.java` line 75**: `all.removeIf(d -> d.getId() == id);`
2. **`DeliveryDAO.java` line 82**: `all.removeIf(d -> d.getOrderId() == orderId)`
3. **`DeliveryPersonDAO.java` line 65**: `all.removeIf(d -> d.getId() == id);`
4. **`FoodDAO.java` line 59**: `Comparator<Food> byPrice = Comparator.comparingDouble(Food::getPrice).thenComparing(Food::getName);`
5. **`FoodDAO.java` line 79**: `all.removeIf(f -> f.getId() == id);`
6. **`OrderDAO.java` line 63**: `all.removeIf(o -> o.getId() == id);`
7. **`PaymentDAO.java` line 79**: `all.removeIf(p -> p.getOrderId() == orderId)`
8. **`PromoCodeDAO.java` line 59**: `all.removeIf(p -> p.getCode().equalsIgnoreCase(code));`
9. **`RatingDAO.java` line 58**: `all.removeIf(r -> r.getId() == id);`
10. **`RatingDAO.java` line 65**: `all.removeIf(r -> r.getOrderId() == orderId)`
11. **`UserDAO.java` line 94**: `all.removeIf(u -> u.getId() == id);`
12. **`OrderServlet.java` line 46**: `orders.removeIf(o -> !o.getStatus().equals(status));`
13. **`PaymentServlet.java` line 63**: `list.removeIf(p -> !p.getStatus().equals(status));`
14. **`DeliveryServlet.java` line 56**: `ready.sort(Comparator.comparingInt(Order::getId));`
15. **`ReportServlet.java` line 42**: `byStatus.merge(o.getStatus(), 1, Integer::sum);`
16. **`ReportServlet.java` line 44**: `byType.merge(o.getLabel(), 1, Integer::sum);`
17. **`ReportServlet.java` line 46**: `promoUse.merge(o.getPromoCode(), 1, Integer::sum);`
18. **`ReportServlet.java` line 47**: `itemQty.merge(i.getName(), i.getQty(), Integer::sum);`
19. **`ReportServlet.java` line 52**: `top.sort((a, b) -> b.getValue() != a.getValue().intValue() ? b.getValue() - a.getValue() : a.getKey().compareTo(b.getKey()));`
20. **`ReportServlet.java` line 65**: `perDay.computeIfPresent(day, (k, v) -> v + p.getAmount());`
21. **`Web.java` lines 45-52, 56**: Lambda suppliers passed to `singleton(...)`.
22. **`Web.java` line 59**: `pending.sort(Comparator.comparingInt(Order::getId));`

### 2.4 Inner and Nested Classes
1. `com.quickbite.model.OrderQueue`:
   `private static class Node` — Nested class representing a node in the FIFO queue.
   *(Refactor to a separate public top-level class `com.quickbite.model.QueueNode`)*
2. `com.quickbite.util.Web`:
   `private interface IoSupplier<T>` — Nested generic interface.
   *(Eliminate completely; initialize objects with standard procedural null checks)*

---

## 3. Swallowed Exceptions and Empty Catch Blocks (`catch ... ignored`)

The following files contain empty catch blocks or catch blocks that silently swallow exceptions without logging or propagating:
1. **`UserDAO.java` line 39**:
   ```java
   catch (NumberFormatException ignored) { }
   ```
2. **`FoodDAO.java` line 98**:
   ```java
   catch (NumberFormatException e) { return null; }
   ```
3. **`OrderDAO.java` line 97**:
   ```java
   catch (NumberFormatException e) { return null; }
   ```
4. **`PromoCodeDAO.java` line 36**:
   ```java
   catch (NumberFormatException ignored) { }
   ```
5. **`DeliveryDAO.java` line 35**:
   ```java
   catch (NumberFormatException ignored) { }
   ```
6. **`DeliveryPersonDAO.java` line 37**:
   ```java
   catch (NumberFormatException ignored) { }
   ```
7. **`PaymentDAO.java` line 42**:
   ```java
   catch (NumberFormatException ignored) { }
   ```
8. **`RatingDAO.java` line 37**:
   ```java
   catch (NumberFormatException ignored) { }
   ```
9. **`Web.java` line 81**:
   ```java
   catch (IOException e) { return sessionUser; }
   ```
10. **`Web.java` line 138**:
   ```java
   catch (Exception e) { return 0; }
   ```
11. **`FoodServlet.java` line 198**:
   ```java
   catch (Exception e) { price = -1; }
   ```

---

## 4. Return-null / Return-false Error Signaling Audit

Currently, failures and missing records are signaled using `null` or `false` across all DAOs and servlets, bypassing standard OOP exception handling:

| Class | Method | Current Error Signaling | Refactoring Target |
|---|---|---|---|
| `UserDAO` | `add(User)` | Returns `false` on duplicate username/email | `throws DuplicateRecordException, ValidationException, DataAccessException` |
| `UserDAO` | `getById(int)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `UserDAO` | `findByUsername(String)` | Returns `null` if not found | Returns `User` or `throws RecordNotFoundException` |
| `UserDAO` | `findByEmail(String)` | Returns `null` if not found | Returns `User` or `throws RecordNotFoundException` |
| `UserDAO` | `authenticate(String, String)` | Returns `null` on failed login | `throws InvalidCredentialsException, RecordNotFoundException, DataAccessException` |
| `UserDAO` | `update(User)` | Returns `false` if user not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `UserDAO` | `delete(int)` | Returns `false` if user not found | `throws RecordNotFoundException, DataAccessException` |
| `FoodDAO` | `getById(int)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `FoodDAO` | `update(Food)` | Returns `false` if not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `FoodDAO` | `delete(int)` | Returns `false` if not found | `throws RecordNotFoundException, DataAccessException` |
| `OrderDAO` | `getById(int)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `OrderDAO` | `update(Order)` | Returns `false` if not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `OrderDAO` | `delete(int)` | Returns `false` if not found | `throws RecordNotFoundException, DataAccessException` |
| `PromoCodeDAO` | `add(PromoCode)` | Returns `false` if code exists | `throws DuplicateRecordException, ValidationException, DataAccessException` |
| `PromoCodeDAO` | `findByCode(String)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `PromoCodeDAO` | `update(PromoCode)` | Returns `false` if not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `PromoCodeDAO` | `delete(String)` | Returns `false` if not found | `throws RecordNotFoundException, DataAccessException` |
| `DeliveryDAO` | `getById(int)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `DeliveryDAO` | `update(Delivery)` | Returns `false` if not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `DeliveryDAO` | `delete(int)` | Returns `false` if not found | `throws RecordNotFoundException, DataAccessException` |
| `DeliveryPersonDAO` | `getById(int)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `DeliveryPersonDAO` | `update(DeliveryPerson)` | Returns `false` if not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `DeliveryPersonDAO` | `delete(int)` | Returns `false` if not found | `throws RecordNotFoundException, DataAccessException` |
| `PaymentDAO` | `getById(int)` | Returns `null` if not found | `throws RecordNotFoundException, DataAccessException` |
| `PaymentDAO` | `update(Payment)` | Returns `false` if not found | `throws RecordNotFoundException, ValidationException, DataAccessException` |
| `RatingDAO` | `add(Rating)` | Returns `false` if already rated | `throws DuplicateRecordException, ValidationException, DataAccessException` |
| `RatingDAO` | `delete(int)` | Returns `false` if not found | `throws RecordNotFoundException, DataAccessException` |

---

## 5. Refactoring Roadmap (Phases 2–7)
1. **Phase 2 — Exceptions**: Implement `com.quickbite.exception` package and exception-based error signaling.
2. **Phase 3 — Interfaces**: Implement `com.quickbite.contract` (FileStorable, behavioral contracts, repository interfaces).
3. **Phase 4 — Model Classes**: Validate in constructors/setters, delete factories, comment relationships.
4. **Phase 5 — DAOs, Util, Servlets**: Implement repositories, replace lambdas with loops, top-level `QueueNode`, simplify `Web.java`.
5. **Phase 6 — Member Folders**: Mirror all code into `Member_1..6` folders, update READMEs and shared configs.
6. **Phase 7 — Verification**: Build, automated tests, full workflow tests, sync and clean verification.
