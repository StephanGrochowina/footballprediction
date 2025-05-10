package de.coiaf.footballprediction.backend.persistence;

import javax.persistence.EntityManager;

import java.util.Objects;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FactoryServiceQueryExecution {

    public static AbstractServiceQueryExecution createSpy(EntityManager entityManager) {
        return spy(createInstance(entityManager));
    }

    public static <QE extends AbstractServiceQueryExecution> QE createSpy(EntityManager entityManager, Function<EntityManager, QE> serviceQueryExecutionGenerator) {

        return spy(createInstance(entityManager, serviceQueryExecutionGenerator));
    }

    public static AbstractServiceQueryExecution createInstance(EntityManager entityManager) {
        AbstractServiceQueryExecution instance = new ServiceQueryExecutionTestImpl(entityManager);

        return instance;
    }

    public static <QE extends AbstractServiceQueryExecution> QE createInstance(EntityManager entityManager, Function<EntityManager, QE> serviceQueryExecutionGenerator) {
        Objects.requireNonNull(serviceQueryExecutionGenerator);

        QE instance = serviceQueryExecutionGenerator.apply(entityManager);

        return instance;
    }

    public static AbstractServiceQueryExecution createMock() {
        AbstractServiceQueryExecution mock = mock(AbstractServiceQueryExecution.class);

        return mock;
    }

    public static <QE extends AbstractServiceQueryExecution> QE createMock(Class<QE> serviceQueryExecutionType) {
        Objects.requireNonNull(serviceQueryExecutionType);

        QE mock = mock(serviceQueryExecutionType);

        return mock;
    }
}
