package com.lpu.repository;

import com.lpu.database.HibernateConnection;
import com.lpu.entity.Account;
import com.lpu.entity.Branch;
import com.lpu.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccountRepository {

    public Account create(Long branchId, Account account, Set<Long> customerIds) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            Branch branch = em.find(Branch.class, branchId);
            if (branch == null) {
                throw new IllegalArgumentException("Branch not found: " + branchId);
            }
            account.setBranch(branch);

            Set<Customer> customers = new HashSet<>();
            for (Long customerId : customerIds) {
                Customer customer = em.find(Customer.class, customerId);
                if (customer == null) {
                    throw new IllegalArgumentException("Customer not found: " + customerId);
                }
                customers.add(customer);
            }

            account.getCustomers().addAll(customers);
            for (Customer customer : customers) {
                customer.getAccounts().add(account);
            }

            em.persist(account);
            tx.commit();
            return account;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Account findById(Long id) {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }

    public List<Account> findAll() {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Account a", Account.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Account updateBalance(Long id, String balance) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Account account = em.find(Account.class, id);
            if (account == null) {
                throw new IllegalArgumentException("Account not found: " + id);
            }
            account.setBalance(new java.math.BigDecimal(balance));
            tx.commit();
            return account;
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
            Account account = em.find(Account.class, id);
            if (account == null) {
                tx.rollback();
                return false;
            }
            em.remove(account);
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
