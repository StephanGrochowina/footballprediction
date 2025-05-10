package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.model.probability.PredictionsServiceQueryExecution;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FactoryPoissonTotalGoalsRepository {

    public static PoissonTotalGoalsRepository createSpy(PredictionsServiceQueryExecution queryExecutor) {
        return spy(createInstance(queryExecutor));
    }

    public static PoissonTotalGoalsRepository createInstance(PredictionsServiceQueryExecution queryExecutor) {
        PoissonTotalGoalsRepository instance = new PoissonTotalGoalsRepository();

        instance.setQueryExecutor(queryExecutor);

        return instance;
    }

    public static PoissonTotalGoalsRepository createMock() {
        PoissonTotalGoalsRepository mock = mock(PoissonTotalGoalsRepository.class);

        return mock;
    }
}
