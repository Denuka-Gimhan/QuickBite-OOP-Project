# Member 1 - Customer Management

This folder contains ONLY your module's code in a simple, flat structure. It is not an independent runnable app; the full runnable app is in `QuickBite-Combined/` (open that one in IntelliJ).

## Your Tasks
- User registration and login (supports username or email)
- Email uniqueness validation and phone number validation
- Salted SHA-256 password hashing via `PasswordUtil`
- User profile updates and password reset
- Dynamic user avatar: first letter initial with safe Unicode handling and stable background color
- Admin management: view all user accounts, filter/search users, delete accounts
- Split-screen modern authentication pages with password show/hide toggles

## Your Branch Name
`member1-customer`

## Your File List
- **model/**:
  - `model/User.java` (abstract base class with avatar initial and color generation)
  - `model/AdminUser.java` (admin role privileges)
  - `model/RegularUser.java` (customer role privileges)
  - `model/UserFactory.java` (factory pattern for user instantiation)
- **dao/**:
  - `dao/UserDAO.java` (CRUD persistence on `users.txt`)
- **servlet/**:
  - `servlet/UserServlet.java` (handles `/users` actions: login, register, profile, update, delete, list)
- **util/**:
  - `util/PasswordUtil.java` (SHA-256 salted password hashing)
- **pages/**:
  - `pages/login.jsp` (split-screen sign-in page)
  - `pages/register.jsp` (split-screen registration page)
  - `pages/profile.jsp` (user account management and avatar banner)
  - `pages/users.jsp` (admin user roster and management)
  - `pages/login.css` (login page styling)
  - `pages/register.css` (registration page styling)
  - `pages/profile.css` (profile page styling)
  - `pages/users.css` (user roster styling)
  - `pages/auth.js` (password visibility toggle and validation scripts)

## How to Work (Git Workflow)
1. Clone the team repository.
2. Checkout your feature branch:
   ```bash
   git checkout -b member1-customer
   ```
3. Edit your files in `Member_1_Customer_Management/` or directly in `QuickBite-Combined/`.
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
   git commit -m "Member 1: Updated customer management module"
   git push -u origin member1-customer
   ```
7. Open a Pull Request on GitHub targeting `main`.
