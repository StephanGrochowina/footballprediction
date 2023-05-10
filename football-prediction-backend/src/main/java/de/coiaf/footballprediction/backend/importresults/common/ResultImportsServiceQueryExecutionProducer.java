package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.BackendConstants;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ResultImportsServiceQueryExecutionProducer {

    @PersistenceContext(unitName = BackendConstants.PERSISTENCE_CONTEXT_UNIT_NAME_RESULTIMPORTS)
    private EntityManager entityManager;

    @Produces
    @ResultImportsServiceQueryExecution
    ServiceQueryExecution createServiceQueryExecution() {
        return new ServiceQueryExecution(this.entityManager);
    }
}
