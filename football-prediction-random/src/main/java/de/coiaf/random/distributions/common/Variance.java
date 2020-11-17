package de.coiaf.random.distributions.common;

public class Variance extends AbstractDistributionElement<Variance> {

    public static final Variance NO_VARIANCE = new Variance(0.0, true);
    public static final Variance MINIMUM_NON_ZERO_VARIANCE = new Variance(Double.MIN_VALUE, false);
    public static final Variance INFINITE_VARIANCE = new Variance(Double.POSITIVE_INFINITY, false);

    private static final String DISTRIBUTION_ELEMENT_NAME = "variance";

    Variance(double variance, boolean lowerBoundInclusive) {
        super(variance, lowerBoundInclusive, DISTRIBUTION_ELEMENT_NAME);
    }

}
