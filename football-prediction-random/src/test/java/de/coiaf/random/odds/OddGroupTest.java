package de.coiaf.random.odds;

import de.coiaf.random.probability.Probability;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class OddGroupTest {

    private enum OddSample {
        ODD_PROBABILITY_NULL((Probability) null),
        ODD_PROBABILITY_0_PERCENT(Probability.IMPOSSIBLE),
        ODD_PROBABILITY_0_PERCENT2(Probability.IMPOSSIBLE),
        ODD_PROBABILITY_0_PERCENT3(Probability.IMPOSSIBLE),
        ODD_PROBABILITY_0_PERCENT4(Probability.IMPOSSIBLE),
        ODD_PROBABILITY_20_PERCENT(Probability.valueOf(new BigDecimal("0.2"))),
        ODD_PROBABILITY_30_PERCENT(Probability.valueOf(new BigDecimal("0.3"))),
        ODD_PROBABILITY_50_PERCENT(Probability.UNCERTAIN),
        ODD_PROBABILITY_75_PERCENT(Probability.valueOf(new BigDecimal("0.75"))),
        ODD_PROBABILITY_80_PERCENT(Probability.valueOf(new BigDecimal("0.8"))),
        ODD_PROBABILITY_100_PERCENT(Probability.CERTAIN)
        ;

        private final DecimalOdd odd;

        OddSample(BigDecimal probabilityValue) {
            this(probabilityValue == null ? null : new Probability(probabilityValue));
        }
        OddSample(Probability probability) {
            this.odd = probability == null ? null : new DecimalOdd(probability);
        }

        public Odd<BigDecimal> getOdd() {
            return this.odd;
        }
    }

    private static final BigDecimal BOOKMAKER_MARGIN_MINIMUM = new BigDecimal("-1");
    private static final BigDecimal BOOKMAKER_MARGIN_MAXIMUM = BigDecimal.ONE;
    private static final BiPredicate<Probability, Probability> VERIFIER_NORMALIZED_PROBABILITY_IS_IMPOSSIBLE =
            (impliedProbability, normalizedProbability) -> Probability.IMPOSSIBLE.compareTo(normalizedProbability) == 0;
    private static final BiPredicate<Probability, Probability> VERIFIER_NORMALIZED_PROBABILITY_IS_CERTAIN =
            (impliedProbability, normalizedProbability) -> Probability.CERTAIN.compareTo(normalizedProbability) == 0;
    private static final BiPredicate<Probability, Probability> VERIFIER_NORMALIZED_PROBABILITY_IS_LESS_THAN_IMPLIED_PROBABILITY =
            (impliedProbability, normalizedProbability) -> impliedProbability.compareTo(normalizedProbability) > 0;
    private static final BiPredicate<Probability, Probability> VERIFIER_NORMALIZED_PROBABILITY_IS_GREATER_THAN_IMPLIED_PROBABILITY =
            (impliedProbability, normalizedProbability) -> impliedProbability.compareTo(normalizedProbability) < 0;
    private static final BiPredicate<Probability, Probability> VERIFIER_NORMALIZED_PROBABILITY_EQUALS_IMPLIED_PROBABILITY =
            (impliedProbability, normalizedProbability) -> impliedProbability.compareTo(normalizedProbability) == 0;

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullOdds() {
        new OddGroup<>(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_emptyOdds() {
        new OddGroup<>(Collections.EMPTY_MAP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_singleOddsEntry_nullKey_givenOdd() {
        Map<OddSample, Odd<BigDecimal>> oddMap = new HashMap<>();

        oddMap.put(null, OddSample.ODD_PROBABILITY_100_PERCENT.getOdd());

        new OddGroup<OddSample, BigDecimal>(oddMap);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_singleOddsEntry_givenKey_nullOdd() {
        this.createOddGroup(OddSample.ODD_PROBABILITY_NULL);
    }

    @Test
    public void testConstructor_singleOddsEntry_givenKey_oddImpliedProbabilityValue0() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_0_PERCENT};
        BigDecimal expectedBookmakerMargin = BOOKMAKER_MARGIN_MINIMUM;
        BiPredicate<Probability, Probability> verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_CERTAIN;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_singleOddsEntry_givenKey_oddImpliedProbabilityValue50() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_50_PERCENT};
        BigDecimal expectedBookmakerMargin = BOOKMAKER_MARGIN_MINIMUM;
        BiPredicate<Probability, Probability>  verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_CERTAIN;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_singleOddsEntry_givenKey_oddImpliedProbabilityValue80() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_80_PERCENT};
        BigDecimal expectedBookmakerMargin = new BigDecimal("-0.25");
        BiPredicate<Probability, Probability>  verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_CERTAIN;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_singleOddsEntry_givenKey_oddImpliedProbabilityValue100() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_100_PERCENT};
        BigDecimal expectedBookmakerMargin = BigDecimal.ZERO;
        BiPredicate<Probability, Probability>  verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_CERTAIN;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_twoOddsEntries_givenKey_oddImpliedProbabilityValueSum0() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_0_PERCENT, OddSample.ODD_PROBABILITY_0_PERCENT2};
        BigDecimal expectedBookmakerMargin = BOOKMAKER_MARGIN_MINIMUM;
        BiPredicate<Probability, Probability> verifierNormalizedProbability =
                (impliedProbability, normalizedProbability) -> Probability.UNCERTAIN.compareTo(normalizedProbability) == 0;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_fourOddsEntries_givenKey_oddImpliedProbabilityValueSum0() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_0_PERCENT, OddSample.ODD_PROBABILITY_0_PERCENT2,
                OddSample.ODD_PROBABILITY_0_PERCENT3, OddSample.ODD_PROBABILITY_0_PERCENT4};
        BigDecimal expectedBookmakerMargin = BOOKMAKER_MARGIN_MINIMUM;
        BiPredicate<Probability, Probability> verifierNormalizedProbability =
                (impliedProbability, normalizedProbability) -> {
                    Probability expectedProbability = new Probability(new BigDecimal("0.25"));

                    return expectedProbability.compareTo(normalizedProbability) == 0;
                };

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_givenKey_oddImpliedProbabilityValueSum50() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_20_PERCENT, OddSample.ODD_PROBABILITY_30_PERCENT};
        BigDecimal expectedBookmakerMargin = BOOKMAKER_MARGIN_MINIMUM;
        BiPredicate<Probability, Probability> verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_GREATER_THAN_IMPLIED_PROBABILITY;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_givenKey_oddImpliedProbabilityValueSum100() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_20_PERCENT, OddSample.ODD_PROBABILITY_30_PERCENT, OddSample.ODD_PROBABILITY_50_PERCENT};
        BigDecimal expectedBookmakerMargin = BigDecimal.ZERO;
        BiPredicate<Probability, Probability> verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_EQUALS_IMPLIED_PROBABILITY;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_givenKey_oddImpliedProbabilityValueSum125() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_50_PERCENT, OddSample.ODD_PROBABILITY_75_PERCENT};
        BigDecimal expectedBookmakerMargin = new BigDecimal("0.2");
        BiPredicate<Probability, Probability> verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_LESS_THAN_IMPLIED_PROBABILITY;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_givenKey_oddImpliedProbabilityValueSum200() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_20_PERCENT, OddSample.ODD_PROBABILITY_30_PERCENT, OddSample.ODD_PROBABILITY_50_PERCENT, OddSample.ODD_PROBABILITY_100_PERCENT};
        BigDecimal expectedBookmakerMargin = new BigDecimal("0.5");
        BiPredicate<Probability, Probability> verifierNormalizedProbability = VERIFIER_NORMALIZED_PROBABILITY_IS_LESS_THAN_IMPLIED_PROBABILITY;

        this.verifyOddGroup(expectedBookmakerMargin, verifierNormalizedProbability, samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_impossibleOddPresent_oddImpliedProbabilityValueSum50() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_0_PERCENT, OddSample.ODD_PROBABILITY_20_PERCENT, OddSample.ODD_PROBABILITY_30_PERCENT};

        this.verifyImpossibleElement(samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_impossibleOddPresent_oddImpliedProbabilityValueSum100() {
        OddSample[] samples = {
                OddSample.ODD_PROBABILITY_0_PERCENT, OddSample.ODD_PROBABILITY_20_PERCENT, OddSample.ODD_PROBABILITY_30_PERCENT, OddSample.ODD_PROBABILITY_50_PERCENT};

        this.verifyImpossibleElement(samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_impossibleOddPresent_oddImpliedProbabilityValueSum125() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_0_PERCENT, OddSample.ODD_PROBABILITY_50_PERCENT, OddSample.ODD_PROBABILITY_75_PERCENT};

        this.verifyImpossibleElement(samples);
    }

    @Test
    public void testConstructor_multipleOddsEntries_impossibleOddPresent_oddImpliedProbabilityValueSum200() {
        OddSample[] samples = {OddSample.ODD_PROBABILITY_0_PERCENT, OddSample.ODD_PROBABILITY_20_PERCENT, OddSample.ODD_PROBABILITY_30_PERCENT,
                OddSample.ODD_PROBABILITY_50_PERCENT, OddSample.ODD_PROBABILITY_100_PERCENT};

        this.verifyImpossibleElement(samples);
    }

    private void verifyImpossibleElement(OddSample... samples) {
        OddGroup<OddSample, BigDecimal> oddGroup = this.createOddGroup(samples);

        assertNotNull(oddGroup);
        this.verifyOddGroupElement(oddGroup, OddSample.ODD_PROBABILITY_0_PERCENT, VERIFIER_NORMALIZED_PROBABILITY_IS_IMPOSSIBLE);
    }

    private void verifyOddGroup(BigDecimal expectedBookmakerMargin, BiPredicate<Probability, Probability> verifierNormalizedProbability, OddSample... samples) {
        OddGroup<OddSample, BigDecimal> oddGroup = this.createOddGroup(samples);

        assertNotNull(oddGroup);
        this.verifyBookmakerMargin(expectedBookmakerMargin, oddGroup.getMarginBookmaker());
        this.verifyOddGroupElements(oddGroup, verifierNormalizedProbability, samples);
    }

    private void verifyOddGroupElements(OddGroup<OddSample, BigDecimal> oddGroup, BiPredicate<Probability, Probability> verifierNormalizedProbability, OddSample... samples) {
        Objects.requireNonNull(samples);

        if (samples.length > 0) {
            for (OddSample sample : samples) {
                if (sample != null) {
                    this.verifyOddGroupElement(oddGroup, sample, verifierNormalizedProbability);
                }
            }
        }

    }

    private void verifyOddGroupElement(OddGroup<OddSample, BigDecimal> oddGroup, OddSample key, BiPredicate<Probability, Probability> verifierNormalizedProbability) {
        Objects.requireNonNull(oddGroup);
        Objects.requireNonNull(key);
        Objects.requireNonNull(key.getOdd());

        Odd<BigDecimal> expectedOdd = key.getOdd();
        BigDecimal expectedOddValue = expectedOdd.getOddValue();
        BigDecimal expectedDecimalOddValue = expectedOdd.getDecimalOddValue();
        Probability expectedImpliedProbability = expectedOdd.getImpliedProbability();

        BigDecimal resultOddValue = oddGroup.getOddValue(key);
        assertNotNull(resultOddValue);
        assertEquals(expectedOddValue, resultOddValue);

        BigDecimal resultDecimalOddValue = oddGroup.getDecimalOddValue(key);
        assertNotNull(resultDecimalOddValue);
        assertEquals(expectedDecimalOddValue, resultDecimalOddValue);

        Probability resultImpliedProbability = oddGroup.getImpliedProbability(key);
        assertNotNull(resultImpliedProbability);
        assertEquals(expectedImpliedProbability, resultImpliedProbability);

        Probability resultNormalizedProbability = oddGroup.getNormalizedProbability(key);
        assertNotNull(resultNormalizedProbability);
        if (verifierNormalizedProbability != null) {
            assertTrue(verifierNormalizedProbability.test(expectedImpliedProbability, resultNormalizedProbability));
        }
    }

    private void verifyBookmakerMargin(BigDecimal expectedMargin, BigDecimal resultMargin) {

        assertNotNull(resultMargin);
        assertTrue(BOOKMAKER_MARGIN_MINIMUM.compareTo(resultMargin) <= 0);
        assertTrue(BOOKMAKER_MARGIN_MAXIMUM.compareTo(resultMargin) >= 0);
        if (expectedMargin != null) {
            assertTrue(expectedMargin.compareTo(resultMargin) == 0);
        }
    }

    private OddGroup<OddSample, BigDecimal> createOddGroup(OddSample... samples) {
        Map<OddSample, Odd<BigDecimal>> oddMap = new HashMap<>();

        if (samples.length > 0) {
            for (OddSample sample : samples) {
                if (sample != null) {
                    oddMap.put(sample, sample.getOdd());
                }
            }
        }

        return new OddGroup<>(oddMap);
    }

}