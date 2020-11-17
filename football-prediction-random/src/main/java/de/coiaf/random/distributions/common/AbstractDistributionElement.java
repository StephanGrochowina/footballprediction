package de.coiaf.random.distributions.common;

import de.coiaf.footballprediction.common.vo.numerical.AbstractDoubleBasedValueObject;

import java.util.Objects;

class AbstractDistributionElement<DE extends AbstractDistributionElement> extends AbstractDoubleBasedValueObject<DE> {

    private static final double LOWER_BOUND_VALUE = 0.0;
    private static final double UPPER_BOUND_VALUE = Double.POSITIVE_INFINITY;

    private static Double createInternalValue(double distributionElementValue, boolean lowerBoundInclusive, String distributionElementName) {
        validateLowerBound(distributionElementValue, lowerBoundInclusive, distributionElementName);

        return distributionElementValue < UPPER_BOUND_VALUE ? distributionElementValue : UPPER_BOUND_VALUE;
    }

    private static void validateLowerBound(double distributionElementValue, boolean lowerBoundInclusive, String distributionElementName) {
        Objects.requireNonNull(distributionElementName, "Parameter distributionElementName must not be null.");

        if (!lowerBoundInclusive && distributionElementValue <= LOWER_BOUND_VALUE) {
            throw new IllegalArgumentException("Parameter " + distributionElementName + " must be greater than 0.");
        }
        else if (distributionElementValue < LOWER_BOUND_VALUE) {
            throw new IllegalArgumentException("Parameter " + distributionElementName + " must be positive or zero.");
        }
    }

    AbstractDistributionElement(double distributionElementValue, String distributionElementName) {
        this(distributionElementValue, false, distributionElementName);
    }

    AbstractDistributionElement(double distributionElementValue, boolean lowerBoundInclusive, String distributionElementName) {
        super(() -> createInternalValue(distributionElementValue, lowerBoundInclusive, distributionElementName));
    }

    public boolean isLowerBound() {
        return LOWER_BOUND_VALUE == this.getInternalValue();
    }

    public boolean isUpperBound() {
        return UPPER_BOUND_VALUE == this.getInternalValue();
    }
}
