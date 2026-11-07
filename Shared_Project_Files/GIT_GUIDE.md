# Git / GitHub guide for 6 members (no conflicts)

## A. Team leader, once
1. Create an EMPTY repo on GitHub (no README). Add the 5 others: Settings -> Collaborators.
2. Unzip the project, then in the folder:
   git init -b main
   git add .
   git commit -m "Initial project: all files wired"
   git remote add origin <repo-url>
   git push -u origin main
3. GitHub -> Settings -> Branches -> protect `main` (require a pull request before merging).

## B. Every member, once
git clone <repo-url>
cd quickbite-backend            (open this folder in IntelliJ as a Maven project)
git checkout -b member1-customer   (member2-menu, member3-cart, member4-orders, member5-delivery, member6-payments)

## C. Daily routine (repeat)
1. git checkout main && git pull            # get the latest first
2. git checkout member1-customer && git merge main
3. Work ONLY on your files (see MEMBERS.md). Run the app and test.
4. git status                               # check that only your files changed
5. git add <your files>      git commit -m "Member 1: email login"
6. git push -u origin member1-customer
7. GitHub -> Pull request -> main. Another member quickly checks it runs, then Merge.

## D. Before the final merge
- Everyone merges main into their branch, runs the app, and ticks the checklist in TEST_CHECKLIST.md.
- Merge order: 1 -> 2 -> 3 -> 4 -> 5 -> 6 (the later ones use the earlier ones).
- After each merge the leader runs: mvn clean package, deploys the .war, and clicks through the app.

## E. Conflicts
Conflict = two people changed the same lines. If git says CONFLICT: open the file, keep both changes
between <<<<<<< ======= >>>>>>>, delete the markers, `git add <file>`, `git commit`. Ask the file owner if unsure.
Never use `git push --force`.

## F. Do NOT commit
quickbite-data/ (the .txt data lives in your user folder), target/, .idea/
