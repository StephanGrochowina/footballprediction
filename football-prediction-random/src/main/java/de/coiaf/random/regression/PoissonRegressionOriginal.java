package de.coiaf.random.regression;

import cc.mallet.optimize.LimitedMemoryBFGS;
import cc.mallet.optimize.Optimizable;
import cc.mallet.optimize.Optimizer;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

class PoissonRegressionOriginal {
    private ArrayList<Double> weights;
    private ArrayList<Double> outcomes;
    private ArrayList<RealVector> variates;
    private int columns;
    private int rows;

    public PoissonRegressionOriginal(){
        weights = new ArrayList<Double>();
        outcomes = new ArrayList<Double>();
        variates = new ArrayList<RealVector>();
        columns = 0;
        rows = 0;
    }

    /**Accumulates into the internal store, with a default weight of 1.0*/
    public void addEntry(double outcome, RealVector vars){
        addEntry(1.0,outcome,vars);
    }

    /**Accumulate into the internal store with a specified weight, for instance
     * for representing population chunks in a contingency table.  You must
     * manually represent things like bias terms, crosses of variables, etc.*/
    public void addEntry(double weight, double outcome, RealVector vars){
        if (columns!=0 && vars.getDimension()!=columns)
            throw new IllegalArgumentException("Dimension of new data differs from old");
        if (columns==0) columns = vars.getDimension();
        weights.add(weight);
        outcomes.add(outcome);
        variates.add(vars);
        rows++;
    }

    /**Returns a fittable model instance with a "frozen" snapshot of the current
     * dataset.*/
    public FittableModel makeModel() {
        return new FittableModel();
    }

    /**Actual, fittable model with a frozen snapshot of the data.*/
    private class FittableModel
            implements Optimizable.ByGradientValue, Optimizable.ByGradient{

        private final RealMatrix xMatrix;
        private final RealVector yVector;
        private final RealVector wVector;
        private final int localRows;
        private final RealVector betas;
        private double reg;
        private boolean isFitted;
        private final Optimizer opt;

        public FittableModel(){
            betas = new ArrayRealVector(columns);
            xMatrix = new BlockRealMatrix(rows, columns);
            yVector = new ArrayRealVector(outcomes.toArray(new Double[outcomes.size()]));
            wVector = new ArrayRealVector(weights.toArray(new Double[weights.size()]));
            opt = new LimitedMemoryBFGS(this);
            localRows = rows;
            isFitted = false;

            for(int row=0;row<localRows;row++){
                xMatrix.setRowVector(row, variates.get(row));
                yVector.setEntry(row, outcomes.get(row));
            }
        }

        /**Fit the model with the given regularization parameter.  Higher
         * regularization => smaller-in-magnitude parameters.*/
        public void fit(double regularization) {
            reg = regularization;
            opt.optimize();
            isFitted = true;
        }

        /**@return The (probably fitted) beta parameters.*/
        public RealVector getBetas() {
            return betas;
        }

        /**@return Whether the model has been fitted*/
        public boolean isFitted() {
            return isFitted;
        }
        /**Given a fitted model, take a parameter vector and return the predicted
         * value.*/
        public double predict(RealVector v) {
            return Math.exp(betas.dotProduct(v));
        }

        @Override
        public int getNumParameters() {
            return columns;
        }

        @Override
        public double getParameter(int index) {
            return betas.getEntry(index);
        }
        @Override
        public void getParameters(double[] buffer) {
            for (int i=0;i<buffer.length;i++) buffer[i]=betas.getEntry(i);
        }
        @Override
        public void setParameter(int index, double val) {
            betas.setEntry(index, val);
        }
        @Override
        public void setParameters(double[] buffer) {
            for (int i=0;i<buffer.length;i++) betas.setEntry(i, buffer[i]);
        }

        @Override
        public double getValue() {
            double accum = 0;
            for(int row=0;row<localRows;row++){
                RealVector x = xMatrix.getRowVector(row);
                double y = yVector.getEntry(row);
                double w = wVector.getEntry(row);
                double mu = betas.dotProduct(x);

                accum += w * (-Math.exp(mu) + y * mu); //omits "- Gamma.logGamma(1+y)" as it's constant
            }
            return accum - reg / 2 * betas.dotProduct(betas);
        }
        @Override
        public void getValueGradient(double[] buffer) {
            RealVector accum = betas.mapMultiply(-reg);//negative sign on regularization to subtract
            for (int row=0;row<localRows;row++){
                RealVector x = xMatrix.getRowVector(row);
                double y = yVector.getEntry(row);
                double w = wVector.getEntry(row);
                // if these represent population slices, need to multiply by w
                accum = accum.add(x.mapMultiply(w * (y - Math.exp(betas.dotProduct(x)))));
            }
            for(int i=0;i<buffer.length;i++) buffer[i] = accum.getEntry(i);
        }
    }

    //Example of usage
    public static void main(String[] args){
        final Random rng = new Random();
        final int nVariables = 90;
//        final int nVariables = 5;
        int iterations = 55200;
//        int iterations = 1000;
        final double regularization = 0.0;
        Date processStart, processEnd;

        processStart = new Date();
        System.out.println("Start: " + processStart);
        final PoissonRegressionOriginal regOriginal = new PoissonRegressionOriginal();
        PoissonRegression regCollection = new PoissonRegression(nVariables);

        for(int i=0; i<iterations; i++){
            final double outcome = rng.nextDouble();
            final double[] variablesOriginal = new double[nVariables];
            Double[] variablesCollection = new Double[nVariables];
            for(int j=0; j<nVariables; j++) {
                variablesOriginal[j] = rng.nextDouble();
                variablesCollection[j] = variablesOriginal[j];
            }
            regOriginal.addEntry(outcome, new ArrayRealVector(variablesOriginal));
            regCollection.addEntry(outcome, variablesCollection);
        }
        final FittableModel model = regOriginal.makeModel();
        model.fit(regularization);

        RegressionModel regressionModel = regCollection.createModel(regularization);

        final double[] variablesOriginal = new double[nVariables];
        Double[] variablesCollection = new Double[nVariables];
        for(int j=0; j<nVariables; j++) {
            variablesOriginal[j] = rng.nextDouble();
            variablesCollection[j] = variablesOriginal[j];
        }
        System.out.print("Prediction(");
//        for(int j=0; j<nVariables; j++) {
//            if (j > 0) {
//                System.out.print(", ");
//            }
//            System.out.print(variables[j]);
//        }
        System.out.print(") = ");
        System.out.print(model.predict(new ArrayRealVector(variablesOriginal)) + " (original), ");
        System.out.println(regressionModel.predict(variablesCollection) + " (refactored)");
        processEnd = new Date();
        System.out.println("Ende: " + processEnd);
        System.out.println("Dauer: " + (processEnd.getTime() - processStart.getTime()) + " Millisekunden");
    }
}
