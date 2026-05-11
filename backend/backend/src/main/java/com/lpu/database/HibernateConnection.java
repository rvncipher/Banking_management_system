package com.lpu.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public final class HibernateConnection {

    private static final EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory("Tom");
    }

    private HibernateConnection() {
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
