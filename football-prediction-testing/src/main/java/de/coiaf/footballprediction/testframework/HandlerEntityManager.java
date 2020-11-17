package de.coiaf.footballprediction.testframework;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.util.Objects;

class HandlerEntityManager {

    EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
        Objects.requireNonNull(persistenceUnitName);

        return Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    void closeEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }

    EntityManager createEntityManager(EntityManagerFactory entityManagerFactory) {
        Objects.requireNonNull(entityManagerFactory);

        return entityManagerFactory.createEntityManager();
    }

    void closeEntityManager(EntityManager entityManager) {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    void beginTransaction(EntityManager entityManager) {
        if (entityManager != null && !entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
    }

    void commitTransaction(EntityManager entityManager) {
        if (entityManager != null && entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().commit();
        }
    }

    void rollbackTransaction(EntityManager entityManager) {
        if (entityManager != null && entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }

    Connection createConnection(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = this.createEntityManager(entityManagerFactory);

        return this.createConnection(entityManager);
    }
    Connection createConnection(EntityManager entityManager) {
        Session session = this.determineSession(entityManager);
        SessionImpl sessionImpl = (SessionImpl) session;

        return sessionImpl.connection();
    }

    private Session determineSession(EntityManager entityManager) {
        Objects.requireNonNull(entityManager);

        return entityManager.unwrap(Session.class);
    }

}
