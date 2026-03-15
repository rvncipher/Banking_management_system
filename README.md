# Bank Management System (Hibernate/JPA + PostgreSQL)

Build a mini “real bank” world: create banks → branches → customers (assigned to a branch) → accounts (opened at a branch) → loans (taken from a branch and linked to an account).

This is a **console** project using **Hibernate as JPA provider** (EntityManager) + PostgreSQL.

## 0) Prerequisites
- Java 21
- PostgreSQL running locally

## 1) Create the database
Run in `psql`:
```sql
CREATE DATABASE bankhibernate;
```

Connection config is here:
- [src/main/resources/META-INF/persistence.xml](src/main/resources/META-INF/persistence.xml)

Defaults used:
- URL: `jdbc:postgresql://localhost:5432/bankhibernate`
- User: `postgres`
- Password: `root`

## 2) Run the project

### Option A: If `mvn` is installed
From this folder:
```bash
mvn clean compile exec:java
```

### Option B: If `mvn` is NOT installed (use Maven Wrapper you already have)
From `springcrud/` folder:
```bat
mvnw.cmd -f ..\bank_management_system\pom.xml clean compile exec:java
```

You will see a menu like:
- Bank CRUD
- Branch CRUD
- Customer CRUD
- Account CRUD
- Loan CRUD

## 3) Fun Roadmap (a “real” scenario)

Follow this in order and you’ll end up with something that feels like a real bank setup.

### Phase 1 — “Banks already exist” (create 3 banks)
Go to **Bank CRUD → Add bank** and create:

1) `001` SBI
2) `002` HDFC
3) `003` AXIS

Suggested inputs:
- Bank name: `SBI`, code: `001`
- Bank name: `HDFC`, code: `002`
- Bank name: `AXIS`, code: `003`

Then **Bank CRUD → List banks** and note the generated `id` values.

### Phase 2 — Create branches inside each bank
We will use 2 “base branch names”:
- `BTA`
- `AMY`

Now create branches for each bank using **Branch CRUD → Add branch (needs bankId)**.

Recommended naming rule:
- Branch name: `<BASE>_<BANK>`
- IFSC: `<BASE><BANKCODE>` (just keep it unique)

Examples (you can copy-paste):
- For SBI (`bankId = ...`):
	- name `BTA_SBI`, IFSC `BTA001`
	- name `AMY_SBI`, IFSC `AMY001`
- For HDFC:
	- name `BTA_HDFC`, IFSC `BTA002`
	- name `AMY_HDFC`, IFSC `AMY002`
- For AXIS:
	- name `BTA_AXIS`, IFSC `BTA003`
	- name `AMY_AXIS`, IFSC `AMY003`

Then **Branch CRUD → List branches**.

Tip: write down the branch `id` values (you will need them for customers, accounts, and loans).

### Phase 3 — Create customers
Use **Customer CRUD → Add customer**.

It will ask: `Branch id (customer belongs to)` — pick one of the branch ids you created above.

Example customers:

- Aman → `aman@demo.com` (choose branch `BTA_SBI`)
- Riya → `riya@demo.com` (choose branch `AMY_HDFC`)
- Mohit → `mohit@demo.com` (choose branch `BTA_AXIS`)

Then **Customer CRUD → List customers** and note customer `id`s.

### Phase 4 — Create accounts (customers can share / have multiple)
Use **Account CRUD → Add account**.

It will ask: `Branch id (account opened at)` — choose the branch where the account exists.

Important: it asks for `Customer ids (comma separated)`.
This is how you attach customers to the account.

Try these scenarios:

- Salary account for Aman only (opened at Aman’s branch):
	- account number `SBIN0001`
	- balance `5000`
	- customer ids: `1` (use Aman’s real id)

- Joint account for Aman + Riya (pick ONE branch for the account):
	- account number `HDFC0002`
	- balance `15000`
	- customer ids: `1,2`

Then **Account CRUD → List accounts**.

Tip: write down the account `id` values (you will need them for loans).

### Phase 5 — Create loans (loan can be taken by many customers)
Use **Loan CRUD → Add loan**.

It will ask:
- `Branch id (loan taken from)`
- `Account id (loan linked to)`

Rule enforced by code: the selected account must belong to the selected branch.

Try these scenarios:

- Home loan taken by Aman + Riya together:
	- type `HOME`
	- amount `2500000`
	- branch id: choose a branch that matches the account you selected
	- account id: choose the account id created in Phase 4
	- customer ids: `1,2`

- Education loan for Mohit:
	- type `EDU`
	- amount `300000`
	- branch id: choose a branch that matches the account you selected
	- account id: choose the account id created in Phase 4
	- customer ids: `3`

Then **Loan CRUD → List loans**.


## Packages
- `com.lpu.entity` -> `Bank`, `Branch`, `Customer`, `Account`, `Loan`
- `com.lpu.repository` -> `BankRepository`, `BranchRepository`, `CustomerRepository`, `AccountRepository`, `LoanRepository`
- `com.lpu.mainmenu` -> `MainMenu` (switch-based console UI)
- `com.lpu.database` -> `HibernateConnection` (EntityManagerFactory + EntityManager)

## Future implementations (easy upgrades)

These are good “next steps” you can add later to make it feel more like a real system.

### Menu improvements
- Add “attach existing customer to existing account”
- Add “attach existing customer to existing loan”
- Add “list branches by bank” (choose bank id → show its branches)
- Add “list customers by branch”, “list accounts by customer”, “list loans by account”

### Validation & safety
- Validate inputs (no negative balances/loan amounts, valid IFSC format, email format)
- Better error messages for duplicate values (unique constraints)
- Prevent deleting a bank if it still has branches (or ask for confirmation)

### Banking features
- Deposit / withdraw
- Transfer money between two accounts (single transaction)
- Account status: `ACTIVE`, `BLOCKED`, `CLOSED`

### Loan features
- Loan status: `PENDING`, `APPROVED`, `REJECTED`, `CLOSED`
- EMI schedule, interest rate, tenure
- Repay loan from an account (reduce outstanding amount)

### Data model upgrades
- Add audit fields: `createdAt`, `updatedAt`
- Add soft delete (e.g., `isActive` flag) instead of hard deletes
- Add enums for loan type / account type instead of raw strings

### Repository improvements
- Add search methods (find by code, find by IFSC, find by email, find by account number)
- Add pagination for list screens when data grows
