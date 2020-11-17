package de.coiaf.footballprediction.backend.model.probability.poisson;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class FactoryPoissonProbabilityModelService {

    public static PoissonProbabilityModelService createSpy(
            PoissonOddGroupCalculator calculator,
            PoissonTotalGoalsRepository totalGoalsRepository
    ) {
        return spy(createInstance(calculator, totalGoalsRepository));
    }

    public static PoissonProbabilityModelService createInstance(
            PoissonOddGroupCalculator calculator,
            PoissonTotalGoalsRepository totalGoalsRepository
    ) {
        PoissonProbabilityModelService instance = new PoissonProbabilityModelService();

        instance.setCalculator(calculator);
        instance.setTotalGoalsRepository(totalGoalsRepository);

        return instance;
    }

    public static PoissonProbabilityModelService createMock() {
        return mock(PoissonProbabilityModelService.class);
    }
}
