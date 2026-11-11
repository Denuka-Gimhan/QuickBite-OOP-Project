# Member 5 - Delivery Management & Fleet Roster

This folder contains ONLY your module's code in a simple, flat structure. It is not an independent runnable app; the full runnable app is in `QuickBite-Combined/` (open that one in IntelliJ).

## Your Tasks
- Rider management: register new delivery personnel, track vehicle types (Motorbike, Scooter, Bicycle, Car)
- Rider availability tracking (Available vs Off Duty vs Busy On Delivery)
- Assign free riders to orders marked as Preparing
- Manage delivery status lifecycle: Assigned &rarr; Picked Up &rarr; Delivered
- Order delivery information fragment (`order-delivery.jspf`) displaying assigned rider details on order cards
- Delivery dispatch dashboard (`delivery.jsp`) with sticky table headers and quick action buttons

## Your Branch Name
`member5-delivery`

## Your File List
- **model/**:
  - `model/Delivery.java` (delivery tracking record with rider link and timestamps)
  - `model/DeliveryPerson.java` (rider contact, vehicle, and availability model)
- **dao/**:
  - `dao/DeliveryDAO.java` (CRUD persistence on `deliveries.txt`)
  - `dao/DeliveryPersonDAO.java` (CRUD persistence on `delivery_persons.txt`)
- **servlet/**:
  - `servlet/DeliveryServlet.java` (handles `/delivery` assign, pickup, deliver, unassign, addRider, toggleRider, deleteRider)
- **pages/**:
  - `pages/delivery.jsp` (dispatch dashboard with orders waiting, active deliveries, and rider roster)
  - `pages/order-delivery.jspf` (rider info banner included in orders.jsp)
  - `pages/delivery.css` (delivery dispatch layout and rider badge styling)
  - `pages/delivery.js` (delivery client script)

## How to Work (Git Workflow)
1. Clone the team repository.
2. Checkout your feature branch:
   ```bash
   git checkout -b member5-delivery
   ```
3. Edit your files in `Member_5_Delivery_Management/` or directly in `QuickBite-Combined/`.
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
   git commit -m "Member 5: Updated delivery dispatch and rider management"
   git push -u origin member5-delivery
   ```
7. Open a Pull Request on GitHub targeting `main`.
