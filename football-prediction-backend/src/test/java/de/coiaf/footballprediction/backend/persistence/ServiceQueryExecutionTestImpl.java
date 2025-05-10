package de.coiaf.footballprediction.backend.persistence;

import javax.persistence.EntityManager;

class ServiceQueryExecutionTestImpl extends AbstractServiceQueryExecution {

    private final EntityManager entityManager;

    ServiceQueryExecutionTestImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}
