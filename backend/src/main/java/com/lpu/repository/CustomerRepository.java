package com.lpu.repository;

import com.lpu.database.HibernateConnection;
import com.lpu.entity.Branch;
import com.lpu.entity.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class CustomerRepository {

    public Customer create(Long branchId, Customer customer) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Branch branch = em.find(Branch.class, branchId);
            if (branch == null) {
                throw new IllegalArgumentException("Branch not found: " + branchId);
            }
            customer.setBranch(branch);
            em.persist(customer);
            tx.commit();
            return customer;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Customer findById(Long id) {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public List<Customer> findAll() {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Customer c", Customer.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Customer update(Long id, String name, String email, Long branchId) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Customer customer = em.find(Customer.class, id);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found: " + id);
            }
            if (name != null && !name.isBlank()) customer.setName(name);
            if (email != null && !email.isBlank()) customer.setEmail(email);
            if (branchId != null) {
                Branch branch = em.find(Branch.class, branchId);
                if (branch == null) {
                    throw new IllegalArgumentException("Branch not found: " + branchId);
                }
                customer.setBranch(branch);
            }
            tx.commit();
            return customer;
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
            Customer customer = em.find(Customer.class, id);
            if (customer == null) {
                tx.rollback();
                return false;
            }
            em.remove(customer);
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
