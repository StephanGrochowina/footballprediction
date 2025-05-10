package de.coiaf.footballprediction.backend.model.probability;

import de.coiaf.footballprediction.backend.BackendConstants;
import de.coiaf.footballprediction.backend.persistence.AbstractServiceQueryExecution;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PredictionsServiceQueryExecution extends AbstractServiceQueryExecution {

    @PersistenceContext(unitName = BackendConstants.PERSISTENCE_CONTEXT_UNIT_NAME_PREDICTIONS)
    private EntityManager entityManager;

    /**
     * the constructor to be used for dependency injection
     */
    @Inject public PredictionsServiceQueryExecution() {
    }

    /**
     * the constructor to be used for unit tests without CDI
     * @param entityManager the entity manager to be added
     */
    public PredictionsServiceQueryExecution(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}
