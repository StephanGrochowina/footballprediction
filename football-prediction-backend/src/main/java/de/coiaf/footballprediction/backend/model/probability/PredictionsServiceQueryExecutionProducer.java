package de.coiaf.footballprediction.backend.model.probability;

import de.coiaf.footballprediction.backend.BackendConstants;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PredictionsServiceQueryExecutionProducer {

    @PersistenceContext(unitName = BackendConstants.PERSISTENCE_CONTEXT_UNIT_NAME_PREDICTIONS)
    private EntityManager entityManager;

    @Produces
    @PredictionsServiceQueryExecution
    ServiceQueryExecution createServiceQueryExecution() {
        return new ServiceQueryExecution(this.entityManager);
    }
}
