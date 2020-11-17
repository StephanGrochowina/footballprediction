package de.coiaf.footballprediction.backend.persistence;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FactoryServiceQueryExecution {

    public static ServiceQueryExecution createSpy(EntityManager entityManager) {
        return spy(createInstance(entityManager));
    }
    public static ServiceQueryExecution createInstance(EntityManager entityManager) {
        ServiceQueryExecution instance = new ServiceQueryExecution();

        instance.setEntityManager(entityManager);

        return instance;
    }

    public static ServiceQueryExecution createMock() {
        ServiceQueryExecution mock = mock(ServiceQueryExecution.class);

        return mock;
    }
}
