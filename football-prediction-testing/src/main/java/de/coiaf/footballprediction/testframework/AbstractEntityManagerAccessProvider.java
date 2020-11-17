package de.coiaf.footballprediction.testframework;

import javax.persistence.EntityManager;
import java.sql.Connection;

abstract class AbstractEntityManagerAccessProvider {

    public static final String PERSISTENCE_UNIT_NAME_UNIT_TEST = "Persistence_Unit_Test";
    private static ProviderEntityManager providerEntityManager;

    private EntityManager entityManager;

    static void setupDatabase() {
        providerEntityManager = new ProviderEntityManager(PERSISTENCE_UNIT_NAME_UNIT_TEST);
    }

    static void closeDatabase() throws Exception {
        providerEntityManager.invalidate(true);
    }

    void setEntityManager() {
        this.entityManager = createEntityManager();
    }

    void closeEntityManager() {
        closeEntityManager(this.entityManager);
    }

    static void closeEntityManager(EntityManager entityManager) {
        providerEntityManager.closeEntityManager(entityManager);
    }

    static EntityManager createEntityManager() {
        return providerEntityManager.createEntityManager();
    }

    protected static Connection createConnection() {
        return providerEntityManager.createConnection();
    }
    protected static Connection createConnection(EntityManager entityManager) {
        return providerEntityManager.createConnection(entityManager);
    }

    protected static Connection getConnection() {
        return providerEntityManager.getConnection();
    }

    protected void beginTransaction() {
        beginTransaction(this.entityManager);
    }
    static void beginTransaction(EntityManager entityManager) {
        providerEntityManager.beginTransaction(entityManager);
    }

    protected void commitTransaction() {
        commitTransaction(this.entityManager);
    }
    static void commitTransaction(EntityManager entityManager) {
        providerEntityManager.commitTransaction(entityManager);
    }

    protected void commitTransaction(boolean clearContext) {
        this.commitTransaction();
        if ( clearContext ) {
            this.entityManager.clear();
        }
    }

    protected void rollbackTransaction() {
        rollbackTransaction(this.entityManager);
    }
    static void rollbackTransaction(EntityManager entityManager) {
        providerEntityManager.rollbackTransaction(entityManager);
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
