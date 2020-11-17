package de.coiaf.random.distributions.common;

public class StandardDeviation extends AbstractDistributionElement<StandardDeviation> {

    public static final StandardDeviation NO_DEVIATION = new StandardDeviation(0.0, true);
    public static final StandardDeviation INFINITE_DEVIATION = new StandardDeviation(Double.POSITIVE_INFINITY, false);

    private static final String DISTRIBUTION_ELEMENT_NAME = "standardDeviation";

    StandardDeviation(double standardDeviation, boolean lowerBoundInclusive) {
        super(standardDeviation, lowerBoundInclusive, DISTRIBUTION_ELEMENT_NAME);
    }

}
