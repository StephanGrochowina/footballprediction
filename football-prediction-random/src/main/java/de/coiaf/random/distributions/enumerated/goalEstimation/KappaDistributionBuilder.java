package de.coiaf.random.distributions.enumerated.goalEstimation;

import de.coiaf.random.distributions.common.DistributionElementFactory;
import de.coiaf.random.distributions.continuous.MeanBasedContinuousDistributionFunction;
import de.coiaf.random.distributions.common.StandardDeviation;
import de.coiaf.random.distributions.common.Variance;
import de.coiaf.random.distributions.continuous.ContinuousDistribution;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class KappaDistributionBuilder {

    private static final Variance DEFAULT_VARIANCE = Variance.NO_VARIANCE;
    private static final int DEFAULT_MATCHES = 0;

    private final KappaMeanEstimationBuilder meanBuilder;
    private Variance homeVariance = DEFAULT_VARIANCE;
    private int homeMatches = DEFAULT_MATCHES;
    private Variance awayVariance = DEFAULT_VARIANCE;
    private int awayMatches = DEFAULT_MATCHES;

    private final Consumer<Variance> homeVarianceConsumer = variance -> this.homeVariance = variance;
    private final Consumer<Integer> homeMatchesConsumer = matches -> this.homeMatches = matches;
    private final Consumer<Variance> awayVarianceConsumer = variance -> this.awayVariance = variance;
    private final Consumer<Integer> awayMatchesConsumer = matches -> this.awayMatches = matches;

    public static KappaDistributionBuilder createBuilder() {
        return createBuilder(KappaMeanEstimationBuilder.createBuilder());
    }

    private static KappaDistributionBuilder createBuilder(KappaMeanEstimationBuilder meanBuilder) {
        return new KappaDistributionBuilder(meanBuilder);
    }

    private KappaDistributionBuilder(KappaMeanEstimationBuilder meanBuilder) {
        Objects.requireNonNull(meanBuilder,"Parameter meanBuilder must not be null.");

        this.meanBuilder = meanBuilder;
    }

    public KappaDistributionBuilder applyHomeScore(Number scored, Number conceded) {
        this.meanBuilder.applyHomeScore(scored, conceded);

        return this;
    }

    public KappaDistributionBuilder applyAwayScore(Number scored, Number conceded) {
        this.meanBuilder.applyAwayScore(scored, conceded);

        return this;
    }

    public KappaDistributionBuilder applyLowerBoundGoals(Double lowerBoundInclusiveGoals) {
        this.meanBuilder.applyLowerBoundGoals(lowerBoundInclusiveGoals);

        return this;
    }

    public KappaDistributionBuilder applyHomeVariance(double varianceValue, int matches) {
        return this.applyVariance(
                () -> DistributionElementFactory.createVarianceZeroInclusive(varianceValue),
                matches,
                this.homeVarianceConsumer,
                this.homeMatchesConsumer);
    }

    public KappaDistributionBuilder applyHomeVariance(Variance variance, int matches) {
        Objects.requireNonNull(variance,"Parameter variance must not be null.");

        return this.applyVariance(
                () -> variance,
                matches,
                this.homeVarianceConsumer,
                this.homeMatchesConsumer);
    }

    public KappaDistributionBuilder applyHomeStandardDeviation(double deviationValue, int matches) {
        return this.applyVariance(
                () -> DistributionElementFactory.createVarianceZeroInclusive(Math.pow(deviationValue, 2)),
                matches,
                this.homeVarianceConsumer,
                this.homeMatchesConsumer);
    }

    public KappaDistributionBuilder applyHomeStandardDeviation(StandardDeviation deviation, int matches) {
        Objects.requireNonNull(deviation,"Parameter deviation must not be null.");

        return this.applyVariance(
                () -> DistributionElementFactory.createVariance(deviation),
                matches,
                this.homeVarianceConsumer,
                this.homeMatchesConsumer);
    }

    public KappaDistributionBuilder applyAwayVariance(double varianceValue, int matches) {
        return this.applyVariance(
                () -> DistributionElementFactory.createVarianceZeroInclusive(varianceValue),
                matches,
                this.awayVarianceConsumer,
                this.awayMatchesConsumer);
    }

    public KappaDistributionBuilder applyAwayVariance(Variance variance, int matches) {
        Objects.requireNonNull(variance,"Parameter variance must not be null.");

        return this.applyVariance(
                () -> variance,
                matches,
                this.awayVarianceConsumer,
                this.awayMatchesConsumer);
    }

    public KappaDistributionBuilder applyAwayStandardDeviation(double deviationValue, int matches) {
        return this.applyVariance(
                () -> DistributionElementFactory.createVarianceZeroInclusive(Math.pow(deviationValue, 2)),
                matches,
                this.awayVarianceConsumer,
                this.awayMatchesConsumer);
    }

    public KappaDistributionBuilder applyAwayStandardDeviation(StandardDeviation deviation, int matches) {
        Objects.requireNonNull(deviation,"Parameter deviation must not be null.");

        return this.applyVariance(
                () -> DistributionElementFactory.createVariance(deviation),
                matches,
                this.awayVarianceConsumer,
                this.awayMatchesConsumer);
    }

    private KappaDistributionBuilder applyVariance(Supplier<Variance> varianceSupplier, int matches, Consumer<Variance> varianceConsumer, Consumer<Integer> matchesConsumer) {
        Objects.requireNonNull(varianceSupplier,"Parameter varianceSupplier must not be null.");
        Objects.requireNonNull(varianceConsumer,"Parameter varianceConsumer must not be null.");
        Objects.requireNonNull(matchesConsumer,"Parameter matchesConsumer must not be null.");

        Variance variance = varianceSupplier.get();
        Objects.requireNonNull(variance,"The supplied value of parameter varianceSupplier must not be null.");

        varianceConsumer.accept(variance);
        matchesConsumer.accept(matches);

        return this;
    }

    public ContinuousDistribution buildHomeDistribution(MeanBasedContinuousDistributionFunction continuousDistributionFunction) {
        return this.buildDistribution(
                continuousDistributionFunction,
                this.meanBuilder::buildHomeMeanEstimation,
                () -> KappaVarianceEstimationFunction.calculateVariance(this.homeVariance, this.homeMatches));
    }

    public ContinuousDistribution buildAwayDistribution(MeanBasedContinuousDistributionFunction continuousDistributionFunction) {
        return this.buildDistribution(
                continuousDistributionFunction,
                this.meanBuilder::buildAwayMeanEstimation,
                () -> KappaVarianceEstimationFunction.calculateVariance(this.awayVariance, this.awayMatches));
    }

    private ContinuousDistribution buildDistribution(
            MeanBasedContinuousDistributionFunction continuousDistributionFunction,
            Supplier<Double> meanSupplier, Supplier<Variance> varianceSuppler) {
        Objects.requireNonNull(continuousDistributionFunction,"Parameter continuousDistributionFunction must not be null.");
        Objects.requireNonNull(meanSupplier,"Parameter meanSupplier must not be null.");
        Objects.requireNonNull(varianceSuppler,"Parameter varianceSuppler must not be null.");

        Double mean = meanSupplier.get();
        Variance variance = varianceSuppler.get();
        Objects.requireNonNull(mean,"The supplied value of parameter meanSupplier must not be null.");
        Objects.requireNonNull(variance,"The supplied value of parameter varianceSuppler must not be null.");

        ContinuousDistribution result = continuousDistributionFunction.createDistribution(mean, variance);

        Objects.requireNonNull(result, "Parameter continuousDistributionFunction must return a ContinuousDistribution instance.");
        return result;
    }
}
