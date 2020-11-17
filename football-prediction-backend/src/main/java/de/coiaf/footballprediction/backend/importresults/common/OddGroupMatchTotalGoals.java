package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.EmbeddableOdd;
import de.coiaf.footballprediction.backend.importresults.entity.OddsMatchTotalGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.odds.Odd;
import de.coiaf.random.odds.OddGroup;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OddGroupMatchTotalGoals extends OddGroup<String, BigDecimal> {

    private static final String KEY_LESS_GOALS_THAN_THRESHOLD = "less";
    private static final String KEY_MORE_GOALS_THAN_THRESHOLD = "more";

    private static Map<String, Odd<BigDecimal>> createOdds(BigDecimal oddValueLessGoalsThanThreshold, BigDecimal oddValueMoreGoalsThanThreshold) {
        Map<String, Odd<BigDecimal>> odds = new HashMap<>();

        odds.put(KEY_LESS_GOALS_THAN_THRESHOLD, DecimalOdd.from(oddValueLessGoalsThanThreshold));
        odds.put(KEY_MORE_GOALS_THAN_THRESHOLD, DecimalOdd.from(oddValueMoreGoalsThanThreshold));

        return odds;
    }

    public OddGroupMatchTotalGoals(BigDecimal oddValueLessGoalsThanThreshold, BigDecimal oddValueMoreGoalsThanThreshold) {
        super(createOdds(oddValueLessGoalsThanThreshold, oddValueMoreGoalsThanThreshold));
    }

    public OddsMatchTotalGoals createMatchOutcomeOdds(String bookmaker, ThresholdTotalGoals threshold) {
        OddsMatchTotalGoals oddsMatchTotalGoals = new OddsMatchTotalGoals();

        Objects.requireNonNull(bookmaker);
        Objects.requireNonNull(threshold);

        oddsMatchTotalGoals.setBookmakerName(bookmaker);
        oddsMatchTotalGoals.setThreshold(threshold);
        oddsMatchTotalGoals.setMargin(this.getMarginBookmaker());
        oddsMatchTotalGoals.setOddGoalsBelowThreshold(new EmbeddableOdd(
                this.getOddValue(KEY_LESS_GOALS_THAN_THRESHOLD),
                this.getImpliedProbability(KEY_LESS_GOALS_THAN_THRESHOLD),
                this.getNormalizedProbability(KEY_LESS_GOALS_THAN_THRESHOLD),
                this.getNormalizedOddValue(KEY_LESS_GOALS_THAN_THRESHOLD)));
        oddsMatchTotalGoals.setOddGoalsAboveThreshold(new EmbeddableOdd(
                this.getOddValue(KEY_MORE_GOALS_THAN_THRESHOLD),
                this.getImpliedProbability(KEY_MORE_GOALS_THAN_THRESHOLD),
                this.getNormalizedProbability(KEY_MORE_GOALS_THAN_THRESHOLD),
                this.getNormalizedOddValue(KEY_MORE_GOALS_THAN_THRESHOLD)));

        return oddsMatchTotalGoals;
    }
}
