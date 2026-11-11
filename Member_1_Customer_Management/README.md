# Member 1 – Customer Management

> **Branch:** `member1-customer` | **Module:** User Registration, Login, Profile & Admin Management

---

## 📁 File Structure

```
Member_1_Customer_Management/
├── model/
│   ├── User.java           ← Abstract base class (Encapsulation + Polymorphism)
│   ├── AdminUser.java      ← Concrete subclass – Admin role
│   ├── RegularUser.java    ← Concrete subclass – Customer role
│   └── UserFactory.java    ← Factory Pattern for object creation
├── dao/
│   └── UserDAO.java        ← Full CRUD on users.txt (file-based persistence)
├── servlet/
│   └── UserServlet.java    ← HTTP controller – routes all /users requests
├── util/
│   └── PasswordUtil.java   ← SHA-256 salted password hashing utility
└── pages/
    ├── login.jsp           ← Split-screen login page
    ├── register.jsp        ← Split-screen registration page
    ├── profile.jsp         ← User profile & account management
    ├── users.jsp           ← Admin user roster & management
    ├── login.css
    ├── register.css
    ├── profile.css
    ├── users.css
    └── auth.js             ← Password visibility toggle & validation
```

---

## ✅ Features Implemented

| Feature | Description |
|---------|-------------|
| **User Registration** | Username/email/phone validation, duplicate check, account creation |
| **User Login** | Supports login via username **or** email |
| **Password Security** | Salted SHA-256 hashing – never stored as plain text |
| **Profile Update** | Update email, phone, address and password |
| **Account Deletion** | Users can delete own account; Admins can delete any account |
| **Admin User List** | Admins can view, search and manage all user accounts |
| **Dynamic Avatar** | Auto-generated letter avatar with stable color per user |
| **Session Management** | Login/logout with HTTP session, session invalidation on logout |

---

## 🔄 CRUD Operations

### CREATE – Register a New User

**File:** [`UserDAO.java`](dao/UserDAO.java) → `add(User u)` method  
**Triggered by:** [`UserServlet.java`](servlet/UserServlet.java) → `register()` method  
**Page:** [`register.jsp`](pages/register.jsp)

```java
// UserDAO.java – CREATE
public synchronized boolean add(User u) throws IOException {
    // 1. Check for duplicate username or email
    if (findByUsername(u.getUsername()) != null || findByEmail(u.getEmail()) != null)
        return false;

    // 2. Auto-generate next available ID
    int nextId = 1;
    for (User x : getAll()) nextId = Math.max(nextId, x.getId() + 1);
    u.setId(nextId);

    // 3. Persist as a pipe-separated line in users.txt
    file.appendLine(u.toFileLine());
    return true;
}
```

**Validation checks in `UserServlet.register()`:**
- Username: 3–20 characters, letters/numbers/underscores only
- Email: valid format (`x@x.x`)
- Phone: 9–15 digits
- Password: minimum 6 characters
- Password confirmation match
- Duplicate username & email check

---

### READ – Retrieve User(s)

**File:** [`UserDAO.java`](dao/UserDAO.java)

| Method | Description |
|--------|-------------|
| `getAll()` | Reads every line from `users.txt`, parses & returns `List<User>` |
| `getById(int id)` | Finds a single user by their numeric ID |
| `findByUsername(String)` | Case-insensitive username lookup |
| `findByEmail(String)` | Case-insensitive email lookup |
| `authenticate(String, String)` | Username **or** email + password verification |
| `search(String text)` | Filters users by username/email (admin search box) |
| `countAdmins()` | Counts how many admin accounts exist |

```java
// UserDAO.java – READ (parse from file)
public synchronized List<User> getAll() throws IOException {
    List<User> list = new ArrayList<>();
    for (String line : file.readLines()) {
        String[] p = line.split("\\|", -1);
        if (p.length < 7) continue;
        // UserFactory decides whether to create AdminUser or RegularUser
        list.add(UserFactory.create(
            Integer.parseInt(p[0]), p[1], p[2], p[3], p[4], p[5], p[6]
        ));
    }
    return list;
}
```

```java
// Authenticate: login via username OR email
public synchronized User authenticate(String usernameOrEmail, String password) throws IOException {
    String login = usernameOrEmail == null ? "" : usernameOrEmail.trim();
    User u = login.contains("@") ? findByEmail(login) : findByUsername(login);
    return (u != null && PasswordUtil.matches(password, u.getPasswordHash())) ? u : null;
}
```

---

### UPDATE – Edit Profile

**File:** [`UserDAO.java`](dao/UserDAO.java) → `update(User updated)` method  
**Triggered by:** [`UserServlet.java`](servlet/UserServlet.java) → `update()` method  
**Page:** [`profile.jsp`](pages/profile.jsp)

```java
// UserDAO.java – UPDATE
public synchronized boolean update(User updated) throws IOException {
    List<User> all = getAll();
    for (int i = 0; i < all.size(); i++) {
        if (all.get(i).getId() == updated.getId()) {
            all.set(i, updated);    // replace in-memory
            saveAll(all);           // rewrite the whole file
            return true;
        }
    }
    return false;
}
```

**Fields updatable by user:**
- Email (with duplicate check against other accounts)
- Phone number
- Address
- Password (optional – only updated if a new password is entered)

---

### DELETE – Remove an Account

**File:** [`UserDAO.java`](dao/UserDAO.java) → `delete(int id)` method  
**Triggered by:** [`UserServlet.java`](servlet/UserServlet.java) → `delete()` method  
**Page:** [`profile.jsp`](pages/profile.jsp) & [`users.jsp`](pages/users.jsp)

```java
// UserDAO.java – DELETE
public synchronized boolean delete(int id) throws IOException {
    List<User> all = getAll();
    boolean removed = all.removeIf(u -> u.getId() == id);
    if (removed) saveAll(all);
    return removed;
}
```

**Business rules enforced in `UserServlet.delete()`:**
- A user can only delete their own account
- An admin can delete any user's account
- The **last remaining admin cannot be deleted** (prevents lockout)

---

## 🧠 OOP Concepts Used

### 1. Abstraction

**File:** [`User.java`](model/User.java)

`User` is declared as an **abstract class** – it defines the common structure for all users but cannot be instantiated directly. Three abstract methods force every subclass to provide its own implementation.

```java
public abstract class User implements java.io.Serializable {
    // Abstract methods – every subclass MUST implement these
    public abstract String getRole();       // "ADMIN" or "CUSTOMER"
    public abstract boolean isAdmin();      // true / false
    public abstract String getGreeting();   // welcome message
}
```

---

### 2. Encapsulation

**File:** [`User.java`](model/User.java)

All fields are `private`. External code can only read or modify them through public getters and setters, keeping data safe from direct manipulation.

```java
public abstract class User {
    private int id;             // ← private fields
    private String username;
    private String passwordHash;
    private String email;
    private String phone;
    private String address;

    // ← public getters & setters (controlled access)
    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getUsername()             { return username; }
    public void setUsername(String u)       { this.username = u; }
    public String getPasswordHash()         { return passwordHash; }
    public void setPasswordHash(String h)   { this.passwordHash = h; }
    // ... etc.
}
```

---

### 3. Inheritance

**Files:** [`AdminUser.java`](model/AdminUser.java), [`RegularUser.java`](model/RegularUser.java)

Both `AdminUser` and `RegularUser` **extend** the abstract `User` class. They inherit all fields and common methods (e.g. `getAvatarInitial()`, `toFileLine()`) and only add or override what is different.

```java
// AdminUser.java – inherits from User
public class AdminUser extends User {
    public AdminUser(int id, String username, String passwordHash,
                     String email, String phone, String address) {
        super(id, username, passwordHash, email, phone, address); // calls parent constructor
    }
    @Override public String getRole()      { return "ADMIN"; }
    @Override public boolean isAdmin()     { return true; }
    @Override public String getGreeting()  { return "Admin " + getUsername(); }
}

// RegularUser.java – inherits from User
public class RegularUser extends User {
    @Override public String getRole()      { return "CUSTOMER"; }
    @Override public boolean isAdmin()     { return false; }
    @Override public String getGreeting()  { return "Hi, " + getUsername(); }
}
```

---

### 4. Polymorphism

**Files:** [`UserDAO.java`](dao/UserDAO.java), [`UserServlet.java`](servlet/UserServlet.java)

The `User` reference is used throughout the app. At runtime, Java automatically calls the **correct overridden method** depending on whether the object is an `AdminUser` or `RegularUser` – without the caller needing to know the actual type.

```java
// UserDAO.java – polymorphism in action
public synchronized List<User> getAll() throws IOException {
    List<User> list = new ArrayList<>();
    for (String line : file.readLines()) {
        String[] p = line.split("\\|", -1);
        // UserFactory returns either AdminUser or RegularUser
        User u = UserFactory.create(..., p[6]); // ← User reference
        list.add(u);
    }
    return list;
}

// UserServlet.java – same User reference, different behaviour at runtime
User me = Web.user(req);
if (me.isAdmin()) {         // AdminUser → true,  RegularUser → false
    // show admin panel
}
req.setAttribute("greeting", me.getGreeting()); // different message per type
```

---

### 5. Constructor Chaining

**Files:** [`AdminUser.java`](model/AdminUser.java), [`RegularUser.java`](model/RegularUser.java)

Both subclasses call `super(...)` to pass all parameters up to the `User` constructor, avoiding code duplication.

```java
public AdminUser(int id, String username, String passwordHash,
                 String email, String phone, String address) {
    super(id, username, passwordHash, email, phone, address); // ← constructor chaining
}
```

---

### 6. Factory Pattern

**File:** [`UserFactory.java`](model/UserFactory.java)

`UserFactory` decides which subclass to create based on the role string. This keeps object creation logic in one place and makes `UserDAO` clean.

```java
public final class UserFactory {
    private UserFactory() {}  // ← private constructor, cannot be instantiated

    public static User create(int id, String username, String hash,
                               String email, String phone, String address, String role) {
        return "ADMIN".equals(role)
                ? new AdminUser(id, username, hash, email, phone, address)
                : new RegularUser(id, username, hash, email, phone, address);
    }
}
```

---

### 7. Serialization (Interface Implementation)

**File:** [`User.java`](model/User.java)

`User` implements `java.io.Serializable`, allowing user objects to be stored in the **HTTP session** (`HttpSession`). This is what makes the login state persist across page requests.

```java
public abstract class User implements java.io.Serializable {
    // Objects of this class can be stored in HTTP sessions
}

// UserServlet.java – storing user in session
req.getSession().setAttribute("user", u);  // works because User is Serializable
```

---

### 8. `synchronized` for Thread Safety

**File:** [`UserDAO.java`](dao/UserDAO.java)

All DAO methods are marked `synchronized` to prevent **race conditions** when multiple users submit requests at the same time (e.g. two users registering with the same username simultaneously).

```java
public synchronized boolean add(User u) throws IOException { ... }
public synchronized List<User> getAll() throws IOException { ... }
public synchronized boolean update(User updated) throws IOException { ... }
public synchronized boolean delete(int id) throws IOException { ... }
```

---

## 🔐 Password Security (`PasswordUtil.java`)

Passwords are **never stored as plain text**. Each password gets a unique random salt before hashing.

```
Stored format:  <16-byte hex salt> $ <SHA-256 hex hash>
Example:        a3f8c2d1...  $  9b4e72a1...
```

| Method | Purpose |
|--------|---------|
| `hash(password)` | Generates random salt → computes SHA-256(salt + password) → returns `salt$hash` |
| `matches(password, stored)` | Splits stored string on `$`, re-computes hash with same salt, compares with constant-time comparison |

**Constant-time comparison** (`MessageDigest.isEqual`) prevents timing-based attacks where an attacker could guess characters by measuring response time.

---

## 🌐 Servlet Routing (`UserServlet.java`)

All requests to `/users` are handled by one servlet. The `action` parameter decides what happens:

| HTTP Method | `action` | Handler | Description |
|-------------|----------|---------|-------------|
| `GET` | `login` (default) | — | Show login page |
| `GET` | `register` | — | Show registration page |
| `GET` | `profile` | — | Show profile page (login required) |
| `GET` | `list` | — | Show all users (admin only) |
| `GET` | `logout` | `logout()` | Invalidate session, redirect |
| `POST` | `login` | `login()` | Authenticate user, start session |
| `POST` | `register` | `register()` | Validate & create new account |
| `POST` | `update` | `update()` | Update profile fields |
| `POST` | `delete` | `delete()` | Delete account |

---

## 📄 Data Storage Format

Users are stored in `users.txt` (one user per line, pipe-separated):

```
id|username|passwordHash|email|phone|address|role
```

**Example:**
```
1|admin|a3f8c2...$9b4e72...|admin@quickbite.lk|0770000000|Head office|ADMIN
2|john|b7d1a4...$c8f923...|john@gmail.com|0771234567|Colombo|CUSTOMER
```

---

## 🔗 How This Module Connects to Others

| Other Module | Connection |
|-------------|------------|
| **Order Management (Member 4)** | Orders store `userId` – links back to this `User` |
| **Payment & Reports (Member 6)** | Ratings store `userId` and `username` from this module |
| **Cart Management (Member 3)** | Cart is stored in HTTP session alongside the `User` object |
| **All Modules** | Servlet pages check `Web.user(req)` (from session) for login status |

---

## ⚙️ Git Workflow

```bash
# Switch to your branch
git checkout member1-customer

# After making changes
git add Member_1_Customer_Management/
git commit -m "Member 1: <describe what you changed>"
git push origin member1-customer
```
