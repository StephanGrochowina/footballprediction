package de.coiaf.random.regression;

import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable;
import cc.mallet.optimize.Optimizer;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Objects;

class RegressionModelCreator implements Optimizable.ByGradientValue, Optimizable.ByGradient {

    private final double regularization;
    private final Source source;
    private final int columns;
    private final RealVector betas;
    private final Optimizer optimizer;

    public RegressionModelCreator(double regularization, Source source) {
        ValidatorRegression.validateRegularization(regularization);
        Objects.requireNonNull(source);

        this.columns = source.getDimensions();

        this.regularization = regularization;
        this.source = source;

        this.betas = new ArrayRealVector(this.columns);
        this.optimizer = new LimitedMemoryBFGS(this);
    }

    /**Fit the model with the given regularization parameter.  Higher
     * regularization => smaller-in-magnitude parameters.*/
    void fit() {
        this.optimizer.optimize();
    }

    RealVector getBetas() {
        return this.betas;
    }

    @Override
    public void getValueGradient(double[] buffer) {
        RealVector accumulator = this.betas.mapMultiply(-this.regularization);//negative sign on regularization to subtract
        RealVector sum = this.source.createStream()
                .filter(entry -> entry != null)
                .map(entry -> this.determineValueGradientSummand(entry))
                .reduce(new ArrayRealVector(this.columns, 0.0), (partialSum, summand) -> partialSum.add(summand));

        accumulator = accumulator.add(sum);

        ConverterVariates.copy(accumulator, buffer);
    }

    private RealVector determineValueGradientSummand(RegressionDataRow entry) {
        RealVector summand = new ArrayRealVector(this.columns, 0.0);

        if (entry != null) {
            RealVector x = entry.getVariates();
            double y = entry.getOutcome();
            double w = entry.getWeight();

            // if these represent population slices, need to multiply by w
            summand = x.mapMultiply(w * (y - Math.exp(betas.dotProduct(x))));
        }

        return summand;
    }

    @Override
    public double getValue() {
        double accumulator = this.source.createStream()
                .filter(entry -> entry != null)
                .mapToDouble(entry -> this.determineValueSummand(entry))
                .reduce(0.0, (sum, summand) -> sum + summand);

        return accumulator - this.regularization / 2 * this.betas.dotProduct(this.betas);
    }

    private double determineValueSummand(RegressionDataRow entry) {
        double summand = 0;

        if (entry != null) {
            RealVector x = entry.getVariates();
            double y = entry.getOutcome();
            double w = entry.getWeight();
            double mu = this.betas.dotProduct(x);

            summand = w * (-Math.exp(mu) + y * mu); //omits "- Gamma.logGamma(1+y)" as it's constant
        }

        return summand;
    }

    @Override
    public int getNumParameters() {
        return this.columns;
    }

    @Override
    public void getParameters(double[] buffer) {
        ConverterVariates.copy(this.betas, buffer);
    }

    @Override
    public double getParameter(int index) {
        return this.betas.getEntry(index);
    }

    @Override
    public void setParameters(double[] params) {
        ConverterVariates.copy(params, this.betas);
    }

    @Override
    public void setParameter(int index, double value) {
        if (index < this.betas.getDimension()) {
            this.betas.setEntry(index, value);
        }
    }
}
