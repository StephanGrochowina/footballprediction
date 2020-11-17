package de.coiaf.random.regression;

class ValidatorRegression {

    static void validateDimensions(int expectedDimensions, Double[] variates) {
        int variatesDimensions = variates == null ? 0 : variates.length;

        if (expectedDimensions < variatesDimensions) {
            throw new IllegalArgumentException("Variates dimensions are greater than the dimensions of this instance.");
        }
    }

    static void validateDimensions(int dimensions) {
        if (dimensions < 1) {
            throw new IllegalArgumentException("The number of dimensions must be greater than zero.");
        }
    }

    static void validateRegularization(double regularization) {
        if (regularization < 0.0) {
            throw new IllegalArgumentException("Regularization is negative.");
        }
    }
}
