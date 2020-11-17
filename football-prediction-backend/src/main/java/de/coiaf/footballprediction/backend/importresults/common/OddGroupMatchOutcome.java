package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.EmbeddableOdd;
import de.coiaf.footballprediction.backend.importresults.entity.EmbeddableScore;
import de.coiaf.footballprediction.backend.importresults.entity.OddsMatchOutcome;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.odds.Odd;
import de.coiaf.random.odds.OddGroup;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OddGroupMatchOutcome extends OddGroup<EmbeddableScore.Outcome, BigDecimal> {

    private static Map<EmbeddableScore.Outcome, Odd<BigDecimal>> createOdds(BigDecimal oddValueHomeWin, BigDecimal oddValueDraw, BigDecimal oddValueAwayWin) {
        Map<EmbeddableScore.Outcome, Odd<BigDecimal>> odds = new HashMap<>();

        odds.put(EmbeddableScore.Outcome.HOME_WIN, DecimalOdd.from(oddValueHomeWin));
        odds.put(EmbeddableScore.Outcome.DRAW, DecimalOdd.from(oddValueDraw));
        odds.put(EmbeddableScore.Outcome.AWAY_WIN, DecimalOdd.from(oddValueAwayWin));

        return odds;
    }

    public OddGroupMatchOutcome(BigDecimal oddValueHomeWin, BigDecimal oddValueDraw, BigDecimal oddValueAwayWin) {
        super(createOdds(oddValueHomeWin, oddValueDraw, oddValueAwayWin));
    }

    public OddsMatchOutcome createMatchOutcomeOdds(String bookmaker) {
        OddsMatchOutcome oddsMatchOutcome = new OddsMatchOutcome();

        Objects.requireNonNull(bookmaker);

        oddsMatchOutcome.setBookmakerName(bookmaker);
        oddsMatchOutcome.setMargin(this.getMarginBookmaker());
        oddsMatchOutcome.setOddHomeWin(new EmbeddableOdd(
                this.getOddValue(EmbeddableScore.Outcome.HOME_WIN),
                this.getImpliedProbability(EmbeddableScore.Outcome.HOME_WIN),
                this.getNormalizedProbability(EmbeddableScore.Outcome.HOME_WIN),
                this.getNormalizedOddValue(EmbeddableScore.Outcome.HOME_WIN)));
        oddsMatchOutcome.setOddDraw(new EmbeddableOdd(
                this.getOddValue(EmbeddableScore.Outcome.DRAW),
                this.getImpliedProbability(EmbeddableScore.Outcome.DRAW),
                this.getNormalizedProbability(EmbeddableScore.Outcome.DRAW),
                this.getNormalizedOddValue(EmbeddableScore.Outcome.DRAW)));
        oddsMatchOutcome.setOddAwayWin(new EmbeddableOdd(
                this.getOddValue(EmbeddableScore.Outcome.AWAY_WIN),
                this.getImpliedProbability(EmbeddableScore.Outcome.AWAY_WIN),
                this.getNormalizedProbability(EmbeddableScore.Outcome.AWAY_WIN),
                this.getNormalizedOddValue(EmbeddableScore.Outcome.AWAY_WIN)));

        return oddsMatchOutcome;
    }
}
