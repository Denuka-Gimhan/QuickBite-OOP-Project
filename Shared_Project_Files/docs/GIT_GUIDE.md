# Git & GitHub Team Guide (6 Members - No Conflicts)

## 1. Repository Structure Overview
- `QuickBite-Combined/`: The unified, runnable Maven WAR web application.
- `Member_1_Customer_Management/` to `Member_6_Payment_and_Reports/`: Flat individual member directories (`model/`, `dao/`, `servlet/`, `util/`, `pages/`).
- `Shared_Project_Files/`: Shared utilities, configs, documents, and styles.
- `tools/`: Automated synchronization (`sync-to-combined`) and validation (`verify`) scripts.

---

## 2. Team Branches
Each member has their designated Git branch:
- Member 1: `member1-customer`
- Member 2: `member2-menu`
- Member 3: `member3-cart`
- Member 4: `member4-orders`
- Member 5: `member5-delivery`
- Member 6: `member6-payments`

---

## 3. Team Leader Initial Setup (One-time)
```bash
git init -b main
git add .
git commit -m "Initial commit: QuickBite team split with flat layout & modern UI"
git remote add origin <github-repo-url>
git push -u origin main
```
In GitHub: Settings &rarr; Collaborators &rarr; Add the 5 team members.
Settings &rarr; Branches &rarr; Enable branch protection for `main` (require pull request reviews).

---

## 4. Daily Member Workflow
1. Pull the latest updates from `main`:
   ```bash
   git checkout main
   git pull origin main
   git checkout <your-branch-name>
   git merge main
   ```
2. Edit your files in your flat folder or in `QuickBite-Combined/`.
3. If you edited in your flat member folder, sync to combined:
   - Windows: `.\tools\sync-to-combined.ps1`
   - Linux/Mac: `./tools/sync-to-combined.sh`
4. Run the verification script:
   - Windows: `.\tools\verify.ps1`
   - Linux/Mac: `./tools/verify.sh`
5. Test the build in `QuickBite-Combined/`:
   ```bash
   mvn clean package
   ```
6. Commit only your changed files and push:
   ```bash
   git status
   git add .
   git commit -m "Member X: <descriptive message>"
   git push -u origin <your-branch-name>
   ```
7. Open a Pull Request on GitHub into `main`.

---

## 5. Recommended Pull Request Merge Order
Because certain modules depend on earlier foundation models, merge in this sequence:
1. `member1-customer` (User model & auth)
2. `member2-menu` (Food model & photo handling)
3. `member3-cart` (Cart, promos & checkout)
4. `member4-orders` (Order lifecycle & kitchen queue)
5. `member5-delivery` (Rider dispatch)
6. `member6-payments` (Billing, invoices & reports)

---

## 6. What NOT to Commit
Never commit runtime artifacts or personal data:
- `quickbite-data/`
- `target/`
- `.idea/` or `*.iml`
- `.DS_Store`
