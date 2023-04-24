package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;
import de.coiaf.footballprediction.backend.persistence.entity.AbstractUnversionedEntity;
import de.coiaf.footballprediction.backend.persistence.entity.NumericSizes;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NamedQueries({
        @NamedQuery(name = OutcomeOddGroupMapping.QUERY_LOAD_ODD_GROUP_FOR_SCORE,
                query = "SELECT new de.coiaf.footballprediction.backend.model.sharedcontext.OddGroupOutcome("
                        + "m.oddHomeWin.impliedProbability, m.oddDraw.impliedProbability, m.oddAwayWin.impliedProbability"
                        +") from OutcomeOddGroupMapping m "
                        + "WHERE m.expectedHomeGoals = :expectedHomeGoals "
                        + "and m.expectedAwayGoals = :expectedAwayGoals "),
        @NamedQuery(name = OutcomeOddGroupMapping.QUERY_LOAD_SCORE_FOR_ODD_GROUP,
                query = "SELECT new de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore("
                        + "avg(m.expectedHomeGoals), avg(m.expectedAwayGoals) "
                        + ") from OutcomeOddGroupMapping m "
                        + "WHERE "
                        + "m.oddHomeWin.oddValue between :minHomeWinOddValue and :maxHomeWinOddValue "
                        + "and m.oddDraw.oddValue between :minDrawOddValue and :maxDrawOddValue "
                        + "and m.oddAwayWin.oddValue between :minAwayWinOddValue and :maxAwayWinOddValue "
                        + "group by sqrt("
                        + "(m.oddHomeWin.oddValue - :oddValueHomeWin) * (m.oddHomeWin.oddValue - :oddValueHomeWin) + "
                        + "(m.oddDraw.oddValue - :oddDraw) * (m.oddDraw.oddValue - :oddDraw) + "
                        + "(m.oddAwayWin.oddValue - :oddValueAwayWin) * (m.oddAwayWin.oddValue - :oddValueAwayWin) "
                        + ") "
                        + "order by sqrt("
                        + "(m.oddHomeWin.oddValue - :oddValueHomeWin) * (m.oddHomeWin.oddValue - :oddValueHomeWin) + "
                        + "(m.oddDraw.oddValue - :oddDraw) * (m.oddDraw.oddValue - :oddDraw) + "
                        + "(m.oddAwayWin.oddValue - :oddValueAwayWin) * (m.oddAwayWin.oddValue - :oddValueAwayWin) "
                        + ") "
        ),
        @NamedQuery(name = OutcomeOddGroupMapping.QUERY_LOAD_SCORE_FOR_ODD_GROUP_AND_TOTAL_GOALS,
                query = "SELECT new de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore("
                        + "avg(m.expectedHomeGoals), avg(m.expectedAwayGoals) "
                        + ") from OutcomeOddGroupMapping m "
                        + "WHERE m.expectedTotalGoals = :expectedTotalGoals "
                        + "and m.oddHomeWin.oddValue between :minHomeWinOddValue and :maxHomeWinOddValue "
                        + "and m.oddDraw.oddValue between :minDrawOddValue and :maxDrawOddValue "
                        + "and m.oddAwayWin.oddValue between :minAwayWinOddValue and :maxAwayWinOddValue "
                        + "group by sqrt("
                        + "(m.oddHomeWin.oddValue - :oddValueHomeWin) * (m.oddHomeWin.oddValue - :oddValueHomeWin) + "
                        + "(m.oddDraw.oddValue - :oddDraw) * (m.oddDraw.oddValue - :oddDraw) + "
                        + "(m.oddAwayWin.oddValue - :oddValueAwayWin) * (m.oddAwayWin.oddValue - :oddValueAwayWin) "
                        + ") "
                        + "order by sqrt("
                        + "(m.oddHomeWin.oddValue - :oddValueHomeWin) * (m.oddHomeWin.oddValue - :oddValueHomeWin) + "
                        + "(m.oddDraw.oddValue - :oddDraw) * (m.oddDraw.oddValue - :oddDraw) + "
                        + "(m.oddAwayWin.oddValue - :oddValueAwayWin) * (m.oddAwayWin.oddValue - :oddValueAwayWin) "
                        + ") "
        ),
})
@Entity
@Table(name = "matching_goals_home_away", uniqueConstraints = {@UniqueConstraint(columnNames = {"goals_home", "goals_away"})})
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "matching_home_away_id"))
})
public class OutcomeOddGroupMapping extends AbstractUnversionedEntity {

    static final String QUERY_LOAD_ODD_GROUP_FOR_SCORE = "OutcomeOddGroupMapping.loadOddGroupForScore";
    static final String QUERY_LOAD_SCORE_FOR_ODD_GROUP = "OutcomeOddGroupMapping.loadScoreForOddGroup";
    static final String QUERY_LOAD_SCORE_FOR_ODD_GROUP_AND_TOTAL_GOALS = "OutcomeOddGroupMapping.loadScoreForOddGroupAndTotalGoals";

    private EstimatedGoals expectedHomeGoals;
    private EstimatedGoals expectedAwayGoals;
    private EstimatedGoals expectedTotalGoals;
    private EmbeddableOdd oddHomeWin;
    private EmbeddableOdd oddDraw;
    private EmbeddableOdd oddAwayWin;

    @Column(name= "goals_home", precision = NumericSizes.EXPECTED_GOALS_PRECISION, scale = NumericSizes.EXPECTED_GOALS_SCALE)
    @NotNull
    public EstimatedGoals getExpectedHomeGoals() {
        return this.expectedHomeGoals;
    }

    public void setExpectedHomeGoals(EstimatedGoals expectedHomeGoals) {
        this.expectedHomeGoals = expectedHomeGoals;
    }

    @Column(name= "goals_away", precision = NumericSizes.EXPECTED_GOALS_PRECISION, scale = NumericSizes.EXPECTED_GOALS_SCALE)
    @NotNull
    public EstimatedGoals getExpectedAwayGoals() {
        return this.expectedAwayGoals;
    }

    public void setExpectedAwayGoals(EstimatedGoals expectedAwayGoals) {
        this.expectedAwayGoals = expectedAwayGoals;
    }

    @Column(name= "total_goals", precision = NumericSizes.EXPECTED_GOALS_PRECISION, scale = NumericSizes.EXPECTED_GOALS_SCALE)
    @NotNull
    public EstimatedGoals getExpectedTotalGoals() {
        return this.expectedTotalGoals;
    }

    public void setExpectedTotalGoals(EstimatedGoals expectedTotalGoals) {
        this.expectedTotalGoals = expectedTotalGoals;
    }

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odd_home_win", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_home_win", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE))
    })
    public EmbeddableOdd getOddHomeWin() {
        return this.oddHomeWin;
    }

    public void setOddHomeWin(EmbeddableOdd oddHomeWin) {
        this.oddHomeWin = oddHomeWin;
    }

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odd_draw", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_draw", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE))
    })
    public EmbeddableOdd getOddDraw() {
        return this.oddDraw;
    }

    public void setOddDraw(EmbeddableOdd oddDraw) {
        this.oddDraw = oddDraw;
    }

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odd_away_win", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_away_win", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE))
    })
    public EmbeddableOdd getOddAwayWin() {
        return this.oddAwayWin;
    }

    public void setOddAwayWin(EmbeddableOdd oddAwayWin) {
        this.oddAwayWin = oddAwayWin;
    }
}
