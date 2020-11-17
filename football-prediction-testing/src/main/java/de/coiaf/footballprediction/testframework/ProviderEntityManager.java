package de.coiaf.footballprediction.testframework;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.sql.SQLException;

class ProviderEntityManager {

    private final HandlerEntityManager handlerEntityManager = new HandlerEntityManager();
    private final EntityManagerFactory entityManagerFactory;
    private Connection connection;

    ProviderEntityManager(String persistenceUnitName) {
        this.entityManagerFactory = this.handlerEntityManager.createEntityManagerFactory(persistenceUnitName);
        this.connection = this.createConnection();
    }

    void invalidate(boolean closeConnection) throws SQLException {
        if (closeConnection) {
            this.closeConnection();
        }
        this.closeEntityManagerFactory();
    }

    Connection recreateConnection() throws SQLException {
        this.closeConnection();

        return this.createConnection();
    }

    private void closeConnection() throws SQLException {
        if (this.connection != null) {
            // hsqldb was supposed to shutdown on close, but it isn't
            this.connection.prepareStatement("SHUTDOWN").execute();
            this.connection.close();
            this.connection = null;
        }
    }

    private void closeEntityManagerFactory() {
        this.handlerEntityManager.closeEntityManagerFactory(this.entityManagerFactory);
    }

    Connection getConnection() {
        if (this.connection == null) {
            this.connection = this.createConnection();
        }

        return this.connection;
    }

    Connection createConnection() {
        return this.handlerEntityManager.createConnection(this.entityManagerFactory);
    }

    Connection createConnection(EntityManager entityManager) {
        return this.handlerEntityManager.createConnection(entityManager);
    }

    EntityManager createEntityManager() {
        return this.handlerEntityManager.createEntityManager(this.entityManagerFactory);
    }

    void closeEntityManager(EntityManager entityManager) {
        this.handlerEntityManager.closeEntityManager(entityManager);
    }

    void beginTransaction(EntityManager entityManager) {
        this.handlerEntityManager.beginTransaction(entityManager);
    }

    void commitTransaction(EntityManager entityManager) {
        this.handlerEntityManager.commitTransaction(entityManager);
    }

    void rollbackTransaction(EntityManager entityManager) {
        this.handlerEntityManager.rollbackTransaction(entityManager);
    }
}
