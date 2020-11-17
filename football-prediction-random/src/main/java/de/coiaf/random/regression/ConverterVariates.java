package de.coiaf.random.regression;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

class ConverterVariates {

    private static final double NULL_VARIATE_VALUE = -1.0;

    static RealVector convert(int expectedDimensions, Double[] variates) {
        ValidatorRegression.validateDimensions(expectedDimensions);
        ValidatorRegression.validateDimensions(expectedDimensions, variates);

        Double[] normalizedVariates = new Double[expectedDimensions];
        int variatesLength = variates.length;

        for (int i = 0; i < expectedDimensions; i++) {
            normalizedVariates[i] = i < variatesLength ? variates[i] : NULL_VARIATE_VALUE;
        }

        return new ArrayRealVector(normalizedVariates);
    }

    static void copy(RealVector source, double[] target) {
        for(int i=0; i < target.length; i++) {
            target[i] = i < source.getDimension() ? source.getEntry(i) : ConverterVariates.NULL_VARIATE_VALUE;
        }
    }

    static void copy(double[] source, RealVector target) {
        for(int i=0; i < source.length; i++) {
            if (i < target.getDimension()) {
                target.setEntry(i, source[i]);
            }
        }
    }
}
