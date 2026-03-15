package com.lpu.mainmenu;

import com.lpu.database.HibernateConnection;
import com.lpu.entity.Account;
import com.lpu.entity.Bank;
import com.lpu.entity.Branch;
import com.lpu.entity.Customer;
import com.lpu.entity.Loan;
import com.lpu.repository.AccountRepository;
import com.lpu.repository.BankRepository;
import com.lpu.repository.BranchRepository;
import com.lpu.repository.CustomerRepository;
import com.lpu.repository.LoanRepository;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in);

    private final BankRepository bankRepository = new BankRepository();
    private final BranchRepository branchRepository = new BranchRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final AccountRepository accountRepository = new AccountRepository();
    private final LoanRepository loanRepository = new LoanRepository();

    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        try {
            menu.run();
        } finally {
            HibernateConnection.close();
        }
    }

    public void run() {
        while (true) {
            System.out.println("\n===== BANK MANAGEMENT (Hibernate) =====");
            System.out.println("1. Bank CRUD");
            System.out.println("2. Branch CRUD");
            System.out.println("3. Customer CRUD");
            System.out.println("4. Account CRUD");
            System.out.println("5. Loan CRUD");
            System.out.println("0. Exit");

            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> bankMenu();
                case 2 -> branchMenu();
                case 3 -> customerMenu();
                case 4 -> accountMenu();
                case 5 -> loanMenu();
                case 0 -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void bankMenu() {
        while (true) {
            System.out.println("\n--- Bank CRUD ---");
            System.out.println("1. Add bank");
            System.out.println("2. List banks");
            System.out.println("3. Update bank");
            System.out.println("4. Delete bank");
            System.out.println("5. Find bank by id");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> {
                    String name = readLine("Bank name: ");
                    String code = readLine("Bank code (unique): ");
                    Bank bank = bankRepository.create(new Bank(name, code));
                    System.out.println("Created: " + bank);
                }
                case 2 -> {
                    List<Bank> banks = bankRepository.findAll();
                    banks.forEach(System.out::println);
                }
                case 3 -> {
                    long id = readLong("Bank id: ");
                    Bank bank = bankRepository.findById(id);
                    if (bank == null) {
                        System.out.println("Bank not found.");
                        break;
                    }
                    String name = readLine("New name (blank = keep): ");
                    String code = readLine("New code (blank = keep): ");
                    if (!name.isBlank()) bank.setName(name);
                    if (!code.isBlank()) bank.setCode(code);
                    Bank updated = bankRepository.update(bank);
                    System.out.println("Updated: " + updated);
                }
                case 4 -> {
                    long id = readLong("Bank id: ");
                    boolean ok = bankRepository.deleteById(id);
                    System.out.println(ok ? "Deleted." : "Bank not found.");
                }
                case 5 -> {
                    long id = readLong("Bank id: ");
                    Bank bank = bankRepository.findById(id);
                    System.out.println(bank != null ? bank : "Bank not found.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void branchMenu() {
        while (true) {
            System.out.println("\n--- Branch CRUD ---");
            System.out.println("1. Add branch (needs bankId)");
            System.out.println("2. List branches");
            System.out.println("3. Update branch");
            System.out.println("4. Delete branch");
            System.out.println("5. Find branch by id");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> {
                    long bankId = readLong("Bank id: ");
                    String name = readLine("Branch name: ");
                    String ifsc = readLine("IFSC (unique): ");
                    Branch branch = branchRepository.create(bankId, new Branch(name, ifsc));
                    System.out.println("Created: " + branch);
                }
                case 2 -> {
                    List<Branch> branches = branchRepository.findAll();
                    branches.forEach(System.out::println);
                }
                case 3 -> {
                    long id = readLong("Branch id: ");
                    String name = readLine("New name (blank = keep): ");
                    String ifsc = readLine("New IFSC (blank = keep): ");
                    String bankIdText = readLine("New bankId (blank = keep): ");
                    Long bankId = bankIdText.isBlank() ? null : Long.parseLong(bankIdText);
                    Branch updated = branchRepository.update(id, name, ifsc, bankId);
                    System.out.println("Updated: " + updated);
                }
                case 4 -> {
                    long id = readLong("Branch id: ");
                    boolean ok = branchRepository.deleteById(id);
                    System.out.println(ok ? "Deleted." : "Branch not found.");
                }
                case 5 -> {
                    long id = readLong("Branch id: ");
                    Branch branch = branchRepository.findById(id);
                    System.out.println(branch != null ? branch : "Branch not found.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void customerMenu() {
        while (true) {
            System.out.println("\n--- Customer CRUD ---");
            System.out.println("1. Add customer");
            System.out.println("2. List customers");
            System.out.println("3. Update customer");
            System.out.println("4. Delete customer");
            System.out.println("5. Find customer by id");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> {
                    long branchId = readLong("Branch id (customer belongs to): ");
                    String name = readLine("Customer name: ");
                    String email = readLine("Email (unique): ");
                    Customer customer = customerRepository.create(branchId, new Customer(name, email));
                    System.out.println("Created: " + customer);
                }
                case 2 -> {
                    List<Customer> customers = customerRepository.findAll();
                    customers.forEach(System.out::println);
                }
                case 3 -> {
                    long id = readLong("Customer id: ");
                    String name = readLine("New name (blank = keep): ");
                    String email = readLine("New email (blank = keep): ");
                    String branchIdText = readLine("New branchId (blank = keep): ");
                    Long branchId = branchIdText.isBlank() ? null : Long.parseLong(branchIdText);
                    Customer updated = customerRepository.update(id, name, email, branchId);
                    System.out.println("Updated: " + updated);
                }
                case 4 -> {
                    long id = readLong("Customer id: ");
                    boolean ok = customerRepository.deleteById(id);
                    System.out.println(ok ? "Deleted." : "Customer not found.");
                }
                case 5 -> {
                    long id = readLong("Customer id: ");
                    Customer customer = customerRepository.findById(id);
                    System.out.println(customer != null ? customer : "Customer not found.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void accountMenu() {
        while (true) {
            System.out.println("\n--- Account CRUD ---");
            System.out.println("1. Add account (optional customerIds)");
            System.out.println("2. List accounts");
            System.out.println("3. Update account balance");
            System.out.println("4. Delete account");
            System.out.println("5. Find account by id");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> {
                    long branchId = readLong("Branch id (account opened at): ");
                    String accountNumber = readLine("Account number (unique): ");
                    String balanceText = readLine("Balance: ");
                    Set<Long> customerIds = readIdSet("Customer ids (comma separated, blank = none): ");
                    Account created = accountRepository.create(branchId, new Account(accountNumber, new BigDecimal(balanceText)), customerIds);
                    System.out.println("Created: " + created);
                }
                case 2 -> {
                    List<Account> accounts = accountRepository.findAll();
                    accounts.forEach(System.out::println);
                }
                case 3 -> {
                    long id = readLong("Account id: ");
                    String balanceText = readLine("New balance: ");
                    Account updated = accountRepository.updateBalance(id, balanceText);
                    System.out.println("Updated: " + updated);
                }
                case 4 -> {
                    long id = readLong("Account id: ");
                    boolean ok = accountRepository.deleteById(id);
                    System.out.println(ok ? "Deleted." : "Account not found.");
                }
                case 5 -> {
                    long id = readLong("Account id: ");
                    Account account = accountRepository.findById(id);
                    System.out.println(account != null ? account : "Account not found.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void loanMenu() {
        while (true) {
            System.out.println("\n--- Loan CRUD ---");
            System.out.println("1. Add loan (optional customerIds)");
            System.out.println("2. List loans");
            System.out.println("3. Update loan");
            System.out.println("4. Delete loan");
            System.out.println("5. Find loan by id");
            System.out.println("0. Back");

            int choice = readInt("Choose: ");
            switch (choice) {
                case 1 -> {
                    long branchId = readLong("Branch id (loan taken from): ");
                    long accountId = readLong("Account id (loan linked to): ");
                    String type = readLine("Loan type: ");
                    String amountText = readLine("Amount: ");
                    Set<Long> customerIds = readIdSet("Customer ids (comma separated, blank = none): ");
                    Loan created = loanRepository.create(accountId, branchId, new Loan(type, new BigDecimal(amountText)), customerIds);
                    System.out.println("Created: " + created);
                }
                case 2 -> {
                    List<Loan> loans = loanRepository.findAll();
                    loans.forEach(System.out::println);
                }
                case 3 -> {
                    long id = readLong("Loan id: ");
                    String type = readLine("New type (blank = keep): ");
                    String amountText = readLine("New amount (blank = keep): ");
                    Loan updated = loanRepository.update(id, type, amountText);
                    System.out.println("Updated: " + updated);
                }
                case 4 -> {
                    long id = readLong("Loan id: ");
                    boolean ok = loanRepository.deleteById(id);
                    System.out.println(ok ? "Deleted." : "Loan not found.");
                }
                case 5 -> {
                    long id = readLong("Loan id: ");
                    Loan loan = loanRepository.findById(id);
                    System.out.println(loan != null ? loan : "Loan not found.");
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Enter a number.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                return Long.parseLong(line.trim());
            } catch (NumberFormatException ex) {
                System.out.println("Enter a number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private Set<Long> readIdSet(String prompt) {
        String line = readLine(prompt).trim();
        Set<Long> ids = new HashSet<>();
        if (line.isBlank()) {
            return ids;
        }
        String[] parts = line.split(",");
        for (String part : parts) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                ids.add(Long.parseLong(trimmed));
            }
        }
        return ids;
    }
}
