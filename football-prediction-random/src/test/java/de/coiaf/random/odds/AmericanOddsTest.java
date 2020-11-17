package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AmericanOddsTest {

    private static final Odd<?> MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY;
    private static final Odd<?> MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY;
    private static final Odd<?> MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY = OddsTest.MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY;

    private static final BigDecimal DECIMAL_ODD_VALUE_IMPOSSIBLE = MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY.getDecimalOddValue();
    private static final BigDecimal DECIMAL_ODD_VALUE_UNCERTAIN = MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue();
    private static final BigDecimal DECIMAL_ODD_VALUE_CERTAIN = MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY.getDecimalOddValue();

    private static final int POSITIVE_AMERICAN_VALUE_IMPOSSIBLE = Integer.MAX_VALUE;
    private static final int POSITIVE_AMERICAN_VALUE_UNCERTAIN = 100;
    private static final int POSITIVE_AMERICAN_VALUE_CERTAIN = 0;

    private static final int NEGATIVE_AMERICAN_VALUE_IMPOSSIBLE = -Integer.MAX_VALUE;
    private static final int NEGATIVE_AMERICAN_VALUE_UNCERTAIN = -100;

    private static final Predicate<AmericanOdd> PREDICATE_POSITIVE_AMERICAN_ODD = americanOdd -> americanOdd instanceof AmericanOddPositive;
    private static final Predicate<AmericanOdd> PREDICATE_NEGATIVE_AMERICAN_ODD = americanOdd -> americanOdd instanceof AmericanOddNegative;

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void from_nullOdd() {
        AmericanOdds.from((Odd<?>) null);
    }
    @Test
    public void from_oddImpossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_oddUncertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_oddCertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void from_nullOdd_falseNegativeAmericanOddIfPossible() {
        AmericanOdds.from((Odd<?>) null, false);
    }
    @Test
    public void from_oddImpossible_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_oddUncertain_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_oddCertain_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }
    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void from_nullOdd_trueNegativeAmericanOddIfPossible() {
        AmericanOdds.from((Odd<?>) null, true);
    }
    @Test
    public void from_oddImpossible_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_IMPOSSIBLE_IMPLIED_PROBABILITY, true),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                NEGATIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_oddUncertain_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_UNCERTAIN_IMPLIED_PROBABILITY, true),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                NEGATIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_oddCertain_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(MOCK_ODD_CERTAIN_IMPLIED_PROBABILITY, true),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void from_nullProbability() {
        AmericanOdds.from((Probability) null);
    }
    @Test
    public void from_probabilityImpossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.IMPOSSIBLE),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_probabilityUncertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.UNCERTAIN),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_probabilityCertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.CERTAIN),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void from_nullProbability_falseNegativeAmericanOddIfPossible() {
        AmericanOdds.from((Probability) null, false);
    }
    @Test
    public void from_probabilityImpossible_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.IMPOSSIBLE, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_probabilityUncertain_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.UNCERTAIN, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_probabilityCertain_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.CERTAIN, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }
    @Test(expected = NullPointerException.class)
    public void from_nullProbability_trueNegativeAmericanOddIfPossible() {
        AmericanOdds.from((Probability) null, true);
    }
    @Test
    public void from_probabilityImpossible_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.IMPOSSIBLE, true),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                NEGATIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_probabilityUncertain_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.UNCERTAIN, true),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                NEGATIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_probabilityCertain_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(Probability.CERTAIN, true),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }

    @Test
    public void from_negativeOddValueImpossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(NEGATIVE_AMERICAN_VALUE_IMPOSSIBLE),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                DECIMAL_ODD_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_negativeOddValueUncertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(NEGATIVE_AMERICAN_VALUE_UNCERTAIN),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                DECIMAL_ODD_VALUE_UNCERTAIN);
    }
    @Test
    public void from_positiveOddValueImpossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(POSITIVE_AMERICAN_VALUE_IMPOSSIBLE),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                DECIMAL_ODD_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_positiveOddValueUncertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(POSITIVE_AMERICAN_VALUE_UNCERTAIN),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                DECIMAL_ODD_VALUE_UNCERTAIN);
    }
    @Test
    public void from_positiveOddValueCertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(POSITIVE_AMERICAN_VALUE_CERTAIN),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                DECIMAL_ODD_VALUE_CERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void from_nullDecimalOddValue() {
        AmericanOdds.from((BigDecimal) null);
    }
    @Test
    public void from_decimalOddValueImpossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_IMPOSSIBLE),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_decimalOddValueUncertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_UNCERTAIN),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_decimalOddValueCertain() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_CERTAIN),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }

    @Test(expected = NullPointerException.class)
    public void from_nullDecimalOddValue_falseNegativeAmericanOddIfPossible() {
        AmericanOdds.from((BigDecimal) null, false);
    }
    @Test
    public void from_decimalOddValueImpossible_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_IMPOSSIBLE, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_decimalOddValueUncertain_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_UNCERTAIN, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_decimalOddValueCertain_falseNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_CERTAIN, false),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }
    @Test(expected = NullPointerException.class)
    public void from_nullDecimalOddValue_trueNegativeAmericanOddIfPossible() {
        AmericanOdds.from((BigDecimal) null, true);
    }
    @Test
    public void from_decimalOddValueImpossible_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_IMPOSSIBLE, true),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                NEGATIVE_AMERICAN_VALUE_IMPOSSIBLE);
    }
    @Test
    public void from_decimalOddValueUncertain_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_UNCERTAIN, true),
                PREDICATE_NEGATIVE_AMERICAN_ODD,
                NEGATIVE_AMERICAN_VALUE_UNCERTAIN);
    }
    @Test
    public void from_decimalOddValueCertain_trueNegativeAmericanOddIfPossible() {
        this.verifyFrom(
                () -> AmericanOdds.from(DECIMAL_ODD_VALUE_CERTAIN, true),
                PREDICATE_POSITIVE_AMERICAN_ODD,
                POSITIVE_AMERICAN_VALUE_CERTAIN);
    }

    private void verifyFrom(Supplier<AmericanOdd> americanOddSupplier, Predicate<AmericanOdd> classValidator, int expectedOddValue) {
        Objects.requireNonNull(americanOddSupplier);
        Objects.requireNonNull(classValidator);

        AmericanOdd result = americanOddSupplier.get();

        assertTrue(classValidator.test(result));
        assertEquals((Integer) expectedOddValue, result.getOddValue());
    }

    private void verifyFrom(Supplier<AmericanOdd> americanOddSupplier, Predicate<AmericanOdd> classValidator, BigDecimal expectedDecimalOddValue) {
        Objects.requireNonNull(americanOddSupplier);
        Objects.requireNonNull(classValidator);
        Objects.requireNonNull(expectedDecimalOddValue);

        AmericanOdd result = americanOddSupplier.get();

        assertTrue(classValidator.test(result));
        assertEquals(0, expectedDecimalOddValue.compareTo(result.getDecimalOddValue()));
    }
}