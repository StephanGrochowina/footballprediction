package de.coiaf.random;

import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.distributions.enumerated.matchOutcome.MatchOutcomeDistributionFactory;
import de.coiaf.random.distributions.enumerated.matchOutcome.Outcomes;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.probability.Probability;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataCreatorHomeAwayGoals {
    private static final String OUTRPUT_FILE = "C:\\Users\\stoff\\IdeaProjects\\footballprediction\\football-prediction-random\\out\\home_away_goals.csv";

    public static void main(String[] args) {
        Path path = Paths.get(OUTRPUT_FILE);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("goals_home,goals_away,goals_total,probabilityDraw,probabilityHomeWin,probabilityAwayWin,oddValueDraw,oddValueHomeWin,oddValueAwayWin");
            writer.newLine();
            BigDecimal counterHome = new BigDecimal("0.01");
            while(counterHome.compareTo(DataCreatorTotalGoals.MAXIMUM) <= 0) {
                BigDecimal counterAway = new BigDecimal("0.01");
                BigDecimal maximumAway = DataCreatorTotalGoals.MAXIMUM.subtract(counterHome);
                System.out.println("Creating data for " + counterHome + ":" + counterAway + "-" + maximumAway + "...");

                while(counterAway.compareTo(maximumAway) <= 0) {
                    BigDecimal totalGoals = counterHome.add(counterAway);
                    EnumDistribution<Outcomes, Probability> distribution =
                            MatchOutcomeDistributionFactory.createDistribution(counterHome.doubleValue(), counterAway.doubleValue());
                    Probability probDraw = distribution.getDensity(Outcomes.DRAW);
                    Probability probHomeWin = distribution.getDensity(Outcomes.HOME_WIN);
                    Probability probAwayWin = distribution.getDensity(Outcomes.AWAY_WIN);
                    BigDecimal oddValueDraw = DecimalOdd.from(probDraw).getDecimalOddValue().setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal oddValueHomeWin = DecimalOdd.from(probHomeWin).getDecimalOddValue().setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal oddValueAwayWin = DecimalOdd.from(probAwayWin).getDecimalOddValue().setScale(2, BigDecimal.ROUND_HALF_UP);

                    writer.write(counterHome.toPlainString());
                    writer.write(',');
                    writer.write(counterAway.toPlainString());
                    writer.write(',');
                    writer.write(totalGoals.toPlainString());
                    writer.write(',');
                    writer.write(probDraw.toBigDecimal().setScale(13, BigDecimal.ROUND_HALF_UP).toPlainString());
                    writer.write(',');
                    writer.write(probHomeWin.toBigDecimal().setScale(13, BigDecimal.ROUND_HALF_UP).toPlainString());
                    writer.write(',');
                    writer.write(probAwayWin.toBigDecimal().setScale(13, BigDecimal.ROUND_HALF_UP).toPlainString());
                    writer.write(',');
                    writer.write(oddValueDraw.toPlainString());
                    writer.write(',');
                    writer.write(oddValueHomeWin.toPlainString());
                    writer.write(',');
                    writer.write(oddValueAwayWin.toPlainString());
                    writer.newLine();

                    counterAway = counterAway.add(DataCreatorTotalGoals.STEP);
                }

                counterHome = counterHome.add(DataCreatorTotalGoals.STEP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
