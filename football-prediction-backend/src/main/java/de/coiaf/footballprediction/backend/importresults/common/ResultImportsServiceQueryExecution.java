package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.BackendConstants;
import de.coiaf.footballprediction.backend.persistence.AbstractServiceQueryExecution;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ResultImportsServiceQueryExecution extends AbstractServiceQueryExecution {

    @PersistenceContext(unitName = BackendConstants.PERSISTENCE_CONTEXT_UNIT_NAME_RESULTIMPORTS)
    private EntityManager entityManager;

    /**
     * the constructor to be used for dependency injection
     */
    @Inject
    public ResultImportsServiceQueryExecution() {
    }

    /**
     * the constructor to be used for unit tests without CDI
     * @param entityManager the entity manager to be added
     */
    public ResultImportsServiceQueryExecution(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}
