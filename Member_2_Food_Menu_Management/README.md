# Member 2 - Food Menu Management

This folder contains ONLY your module's code in a simple, flat structure. It is not an independent runnable app; the full runnable app is in `QuickBite-Combined/` (open that one in IntelliJ).

## Your Tasks
- Food menu browsing, text search, and category filtering (Fast Food, Traditional, Beverages, Desserts)
- Price sorting using custom recursive Quick Sort algorithm (`QuickSort.java`)
- Food item CRUD: Add dish, Edit dish, Delete dish, In-stock / Out-of-stock toggle
- Photo upload from laptop (max 5 MB, JPG/PNG/WEBP/GIF, stored in `<user home>/quickbite-data/images`, served by `/food-image`)
- Photo preview, replace, and removal in food form
- Modern food cards with photo cover zoom, category tags, prep times, prices (Rs. formatted), and neat non-wrapping admin action bar

## Your Branch Name
`member2-menu`

## Your File List
- **model/**:
  - `model/Food.java` (abstract base model with 8-column file format support)
  - `model/FastFood.java`
  - `model/TraditionalFood.java`
  - `model/Beverage.java`
  - `model/Dessert.java`
  - `model/FoodFactory.java` (factory pattern for food subtypes)
- **dao/**:
  - `dao/FoodDAO.java` (CRUD persistence on `foods.txt`, search and QuickSort)
- **servlet/**:
  - `servlet/FoodServlet.java` (handles `/foods` GET list/new/edit and POST add/update/delete/stock)
  - `servlet/FoodImageServlet.java` (serves `/food-image` with security validation and caching headers)
- **util/**:
  - `util/QuickSort.java` (custom in-place recursive quicksort implementation)
- **pages/**:
  - `pages/foods.jsp` (menu page with responsive card grid and filters)
  - `pages/food-form.jsp` (admin add/edit food form with live image preview)
  - `pages/foods.css` (menu page styling)
  - `pages/food-form.css` (photo upload widget styling)
  - `pages/foods.js` (menu animations and keyboard shortcuts)
  - `pages/food-form.js` (live file preview and size validation)

## How to Work (Git Workflow)
1. Clone the team repository.
2. Checkout your feature branch:
   ```bash
   git checkout -b member2-menu
   ```
3. Edit your files in `Member_2_Food_Menu_Management/` or directly in `QuickBite-Combined/`.
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
   git commit -m "Member 2: Updated menu management and food photo handling"
   git push -u origin member2-menu
   ```
7. Open a Pull Request on GitHub targeting `main`.
