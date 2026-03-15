package com.lpu.repository;

import com.lpu.database.HibernateConnection;
import com.lpu.entity.Bank;
import com.lpu.entity.Branch;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class BranchRepository {

    public Branch create(Long bankId, Branch branch) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Bank bank = em.find(Bank.class, bankId);
            if (bank == null) {
                throw new IllegalArgumentException("Bank not found: " + bankId);
            }
            branch.setBank(bank);
            em.persist(branch);
            tx.commit();
            return branch;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Branch findById(Long id) {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.find(Branch.class, id);
        } finally {
            em.close();
        }
    }

    public List<Branch> findAll() {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.createQuery("SELECT br FROM Branch br", Branch.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Branch update(Long id, String name, String ifsc, Long bankId) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Branch branch = em.find(Branch.class, id);
            if (branch == null) {
                throw new IllegalArgumentException("Branch not found: " + id);
            }
            if (name != null && !name.isBlank()) branch.setName(name);
            if (ifsc != null && !ifsc.isBlank()) branch.setIfsc(ifsc);
            if (bankId != null) {
                Bank bank = em.find(Bank.class, bankId);
                if (bank == null) {
                    throw new IllegalArgumentException("Bank not found: " + bankId);
                }
                branch.setBank(bank);
            }
            tx.commit();
            return branch;
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
            Branch branch = em.find(Branch.class, id);
            if (branch == null) {
                tx.rollback();
                return false;
            }
            em.remove(branch);
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
