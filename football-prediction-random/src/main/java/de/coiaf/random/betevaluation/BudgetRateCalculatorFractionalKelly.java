package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Objects;
import java.util.function.Supplier;

public class BudgetRateCalculatorFractionalKelly implements BudgetRateCalculator {

    static final Probability ADVISED_KELLY_FRACTION = new Probability(new BigDecimal("0.1", MathContext.UNLIMITED));

    private final Probability fraction;
    private final BudgetRateCalculatorKellyComplete delegate;

    BudgetRateCalculatorFractionalKelly() {
        this(ADVISED_KELLY_FRACTION);
    }
    BudgetRateCalculatorFractionalKelly(BigDecimal valueThreshold) {
        this(ADVISED_KELLY_FRACTION, valueThreshold);
    }
    BudgetRateCalculatorFractionalKelly(Probability fraction) {
        this(fraction, BudgetRateCalculatorKellyComplete::new);
    }
    BudgetRateCalculatorFractionalKelly(Probability fraction, BigDecimal valueThreshold) {
        this(fraction, () -> new BudgetRateCalculatorKellyComplete(valueThreshold));
    }
    private BudgetRateCalculatorFractionalKelly(Probability fraction, Supplier<BudgetRateCalculatorKellyComplete> supplier) {
        Objects.requireNonNull(fraction);
        if (Probability.isImpossible(fraction)) {
            throw new IllegalArgumentException("Parameter fraction should not represent an impossible value.");
        }
        Objects.requireNonNull(supplier);

        this.fraction = fraction;
        this.delegate = supplier.get();
    }

    @Override
    public Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability) {
        return this.fraction.multiply(this.delegate.calculateBudgetRate(bookmakersOdd, modelProbability));
    }

    protected Probability getFraction() {
        return this.fraction;
    }
}
