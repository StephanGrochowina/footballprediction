package de.coiaf.random.regression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Regularized Poisson regression
 * This class is inspired by https://github.com/bnyeggen/Regularized-Poisson-Regression .
 */
public class PoissonRegression {

    private static final double DEFAULT_WEIGHT = 1.0;
    private static final double DEFAULT_REGULARIZATION = 0.0;

    public static RegressionModel createModel(Source source) {
        return createModel(source, DEFAULT_REGULARIZATION);
    }
    public static RegressionModel createModel(Source source, double regularization) {
        Objects.requireNonNull(source);
        ValidatorRegression.validateRegularization(regularization);

        RegressionModel model;
        RegressionModelCreator modelCreator = new RegressionModelCreator(regularization, source);

        modelCreator.fit();
        model = new RegressionModel(modelCreator.getBetas());

        return model;
    }

    private final int variateDimensions;
    private final List<RegressionDataRow> entries = new ArrayList<>();

    public PoissonRegression(int variateDimensions) {
        ValidatorRegression.validateDimensions(variateDimensions);

        this.variateDimensions = variateDimensions;
    }

    public void addEntry(double outcome, Double... variates) {
        this.addEntry(outcome, DEFAULT_WEIGHT, variates);
    }
    public void addEntry(double outcome, double weight, Double... variates) {
        RegressionDataRow entry = new RegressionDataRow(this.variateDimensions, outcome, weight, variates);

        this.entries.add(entry);
    }

    public RegressionModel createModel() {
        return createModel(new SourceCollection(this.variateDimensions, this.entries));
    }
    public RegressionModel createModel(double regularization) {
        return createModel(new SourceCollection(this.variateDimensions, this.entries), regularization);
    }

    private static class SourceCollection implements Source {

        private final int dimensions;
        private final Collection<RegressionDataRow> entries;

        public SourceCollection(int dimensions, Collection<RegressionDataRow> entries) {
            Objects.requireNonNull(entries);
            ValidatorRegression.validateDimensions(dimensions);

            this.dimensions = dimensions;
            this.entries = entries;
        }

        @Override
        public Stream<RegressionDataRow> createStream() {
            return this.entries.stream();
        }

        @Override
        public int getDimensions() {
            return this.dimensions;
        }
    }
}
