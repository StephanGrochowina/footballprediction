package de.coiaf.random.regression;

import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

public class RegressionModel {

    private final RealVector betas;

    RegressionModel(RealVector betas) {
        Objects.requireNonNull(betas);
        ValidatorRegression.validateDimensions(betas.getDimension());

        this.betas = betas;
    }

    public double predict(Double... variables) {
        RealVector convertedVariables = ConverterVariates.convert(this.betas.getDimension(), variables);

        return Math.exp(this.betas.dotProduct(convertedVariables));
    }
}
