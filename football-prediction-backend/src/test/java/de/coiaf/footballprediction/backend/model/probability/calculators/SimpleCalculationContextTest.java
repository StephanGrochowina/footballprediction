package de.coiaf.footballprediction.backend.model.probability.calculators;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleCalculationContextTest {
    private static final Function<Integer, Probability> TOTAL_GOALS_FUNCTION_SYMMETRIC = totalGoals -> {
        if (totalGoals == null || totalGoals < 0)  {
            return null;
        }
        else if (totalGoals == 0 || totalGoals == 1) {
            return Probability.UNCERTAIN;
        }
        return Probability.IMPOSSIBLE;
    };

    @Test(expected = NullPointerException.class)
    public void constructor_nullTotalGoalsProbabilityFunction() {
        new SimpleCalculationContext(null);
    }

    @Test
    public void apply_nullTotalGoals() {
        this.verifyApply(null, Probability.IMPOSSIBLE);
    }
    @Test
    public void apply_negativeTotalGoals() {
        this.verifyApply(-1, Probability.IMPOSSIBLE);
    }
    @Test
    public void apply_zeroTotalGoals() {
        this.verifyApply(0, Probability.UNCERTAIN);
    }
    @Test
    public void apply_oneTotalGoals() {
        this.verifyApply(1, Probability.UNCERTAIN);
    }
    @Test
    public void apply_twoTotalGoals() {
        this.verifyApply(2, Probability.IMPOSSIBLE);
    }

    private void verifyApply(Integer totalGoals, Probability expectedProbability) {
        SimpleCalculationContext context = new SimpleCalculationContext(TOTAL_GOALS_FUNCTION_SYMMETRIC);
        Probability result = context.apply(totalGoals);

        assertNotNull(result);
        assertEquals(expectedProbability, result);
    }
}