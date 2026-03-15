package com.lpu.repository;

import com.lpu.database.HibernateConnection;
import com.lpu.entity.Bank;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class BankRepository {

    public Bank create(Bank bank) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(bank);
            tx.commit();
            return bank;
        } catch (RuntimeException ex) {
            if (tx.isActive()) tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Bank findById(Long id) {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.find(Bank.class, id);
        } finally {
            em.close();
        }
    }

    public List<Bank> findAll() {
        EntityManager em = HibernateConnection.getEntityManager();
        try {
            return em.createQuery("SELECT b FROM Bank b", Bank.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Bank update(Bank bank) {
        EntityManager em = HibernateConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Bank merged = em.merge(bank);
            tx.commit();
            return merged;
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
            Bank bank = em.find(Bank.class, id);
            if (bank == null) {
                tx.rollback();
                return false;
            }
            em.remove(bank);
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
