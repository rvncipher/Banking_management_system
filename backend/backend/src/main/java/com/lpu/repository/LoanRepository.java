package com.lpu.repository;

import com.lpu.database.HibernateConnection;
import com.lpu.entity.Account;
import com.lpu.entity.Branch;
import com.lpu.entity.Customer;
import com.lpu.entity.Loan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LoanRepository {

    public Loan create(Long accountId, Long branchId, Loan loan, Set<Long> customerIds) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Account account = em.find(Account.class, accountId);
            if (account == null) {
                throw new IllegalArgumentException("Account not found: " + accountId);
            }
            Branch branch = em.find(Branch.class, branchId);
            if (branch == null) {
                throw new IllegalArgumentException("Branch not found: " + branchId);
            }
            if (account.getBranch() == null || account.getBranch().getId() == null || !account.getBranch().getId().equals(branchId)) {
                throw new IllegalArgumentException("Account " + accountId + " does not belong to branch " + branchId);
            }
            loan.setAccount(account);
            loan.setBranch(branch);

            Set<Customer> customers = new HashSet<>();
            for (Long customerId : customerIds) {
                Customer customer = em.find(Customer.class, customerId);
                if (customer == null) {
                    throw new IllegalArgumentException("Customer not found: " + customerId);
                }
                customers.add(customer);
            }

            loan.getCustomers().addAll(customers);
            for (Customer customer : customers) {
                customer.getLoans().add(loan);
            }

            em.persist(loan);
            tx.commit();
            return loan;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Loan findById(Long id) {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.find(Loan.class, id);
        } finally {
            em.close();
        }
    }

    public List<Loan> findAll() {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.createQuery("SELECT l FROM Loan l", Loan.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Loan update(Long id, String loanType, String amount) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Loan loan = em.find(Loan.class, id);
            if (loan == null) {
                throw new IllegalArgumentException("Loan not found: " + id);
            }
            if (loanType != null && !loanType.isBlank()) loan.setLoanType(loanType);
            if (amount != null && !amount.isBlank()) loan.setAmount(new java.math.BigDecimal(amount));
            tx.commit();
            return loan;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public boolean deleteById(Long id) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Loan loan = em.find(Loan.class, id);
            if (loan == null) {
                tx.rollback();
                return false;
            }
            em.remove(loan);
            tx.commit();
            return true;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }
}
