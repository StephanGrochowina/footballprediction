package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;
import de.coiaf.footballprediction.backend.persistence.entity.NumericSizes;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "bookmaker_odds_total_goals")
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "odd_id")),
        @AttributeOverride(name = AbstractEntity.NAME_VERSION, column = @Column(name = "version")),
        @AttributeOverride(name = AbstractOdds.NAME_BOOKMAKER_NAME, column = @Column(name = "bookmaker_name")),
        @AttributeOverride(name = AbstractOdds.NAME_MARGIN, column = @Column(name = "margin"))
})
@AssociationOverrides({
        @AssociationOverride(name = AbstractOdds.NAME_MATCH, joinColumns = {@JoinColumn(name = "match_result_id", nullable = false)})
})
public class OddsMatchTotalGoals extends AbstractOdds {

    private EmbeddableOdd oddGoalsBelowThreshold;
    private EmbeddableOdd oddGoalsAboveThreshold;
    private ThresholdTotalGoals threshold;

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odds_goals_below_threshold", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_implied_goals_below_threshold", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedProbability", column = @Column(name = "probability_normalized_goals_below_threshold", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedOddValue", column = @Column(name = "odd_normalized_goals_below_threshold", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE))
    })
    @NotNull
    public EmbeddableOdd getOddGoalsBelowThreshold() {
        return this.oddGoalsBelowThreshold;
    }

    public void setOddGoalsBelowThreshold(EmbeddableOdd oddGoalsBelowThreshold) {
        this.oddGoalsBelowThreshold = oddGoalsBelowThreshold;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odds_goals_above_threshold", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_implied_goals_above_threshold", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedProbability", column = @Column(name = "probability_normalized_goals_above_threshold", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedOddValue", column = @Column(name = "odd_normalized_goals_above_threshold", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE))
    })
    @NotNull
    public EmbeddableOdd getOddGoalsAboveThreshold() {
        return this.oddGoalsAboveThreshold;
    }

    public void setOddGoalsAboveThreshold(EmbeddableOdd oddGoalsAboveThreshold) {
        this.oddGoalsAboveThreshold = oddGoalsAboveThreshold;
    }

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @Column(name = "threshold", precision = NumericSizes.THRESHOLD_PRECISION, scale = NumericSizes.THRESHOLD_SCALE)
    public ThresholdTotalGoals getThreshold() {
        return this.threshold;
    }

    public void setThreshold(ThresholdTotalGoals threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OddsMatchTotalGoals that = (OddsMatchTotalGoals) o;
        return Objects.equals(this.getMatch(), that.getMatch()) &&
                Objects.equals(this.getBookmakerName(), that.getBookmakerName()) &&
                Objects.equals(this.getThreshold(), that.getThreshold());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMatch(), this.getBookmakerName(), this.getThreshold());
    }

}
