package de.coiaf.random;

import de.coiaf.random.distributions.discrete.DiscreteDistribution;
import de.coiaf.random.distributions.discrete.DiscreteDistributions;
import de.coiaf.random.probability.Probability;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataCreatorTotalGoals {
    private static final String OUTRPUT_FILE = "D:\\Projekte\\Workspaces\\Intellij\\FootballPrediction\\football-prediction-random\\out\\total_goals.txt";
    private static final BigDecimal THRESHOLD = new BigDecimal("2.5");
    static final BigDecimal STEP = new BigDecimal("0.01");
    static final BigDecimal MAXIMUM = new BigDecimal("20.01");

    public static void main(String[] args) {
        Path path = Paths.get(OUTRPUT_FILE);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("threshold,goals,probabilityLess,probabilityGreater");
            writer.newLine();
            BigDecimal counter = new BigDecimal("0.01");
            while(counter.compareTo(MAXIMUM) <= 0) {
                DiscreteDistribution poisson = DiscreteDistributions.createPoissonDistribution(counter.doubleValue());
                Probability probLess25 = poisson.getDistribution(2);
                Probability probGreater25 = probLess25.negate();

                writer.write(THRESHOLD.toPlainString());
                writer.write(',');
                writer.write(counter.toPlainString());
                writer.write(',');
                writer.write(probLess25.toBigDecimal().setScale(13, BigDecimal.ROUND_HALF_UP).toPlainString());
                writer.write(',');
                writer.write(probGreater25.toBigDecimal().setScale(13, BigDecimal.ROUND_HALF_UP).toPlainString());
                writer.newLine();

                counter = counter.add(STEP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
