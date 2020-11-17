package de.coiaf.random.regression;

import org.apache.commons.math3.linear.RealVector;

public class RegressionDataRow {

    private final double outcome;
    private final double weight;
    private final RealVector variates;

    RegressionDataRow(int variateDimensions, double outcome, double weight, Double... variates) {
        this.outcome = outcome;
        this.weight = weight;
        this.variates = ConverterVariates.convert(variateDimensions, variates);
    }

    double getOutcome() {
        return this.outcome;
    }

    double getWeight() {
        return this.weight;
    }

    RealVector getVariates() {
        return this.variates;
    }
}
