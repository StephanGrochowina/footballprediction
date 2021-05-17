package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.sharedkernal.domain.model.score.EstimatedGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;
import de.coiaf.footballprediction.backend.persistence.entity.AbstractUnversionedEntity;
import de.coiaf.footballprediction.backend.persistence.entity.NumericSizes;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NamedQueries({
        @NamedQuery(name = TotalGoalsOddGroupMapping.QUERY_LOAD_ODD_GROUP_FOR_TOTAL_GOALS_AND_THRESHOLD,
                query = "SELECT new de.coiaf.footballprediction.backend.model.sharedcontext.OddGroupTotalGoals("
                        + "m.oddBelowThreshold.impliedProbability, m.oddAboveThreshold.impliedProbability"
                        +") from TotalGoalsOddGroupMapping m "
                        + "WHERE m.expectedTotalGoals = :expectedTotalGoals "
                        + "and m.threshold = :threshold "),
        @NamedQuery(name = TotalGoalsOddGroupMapping.QUERY_LOAD_TOTAL_GOALS_FOR_ODD_GROUP_AND_THRESHOLD,
                query = "SELECT avg(m.expectedTotalGoals) "
                        + "from TotalGoalsOddGroupMapping m "
                        + "WHERE m.threshold = :threshold "
                        + "group by sqrt("
                        + "(m.oddBelowThreshold.oddValue - :oddValueBelowThreshold) * (m.oddBelowThreshold.oddValue - :oddValueBelowThreshold) + "
                        + "(m.oddAboveThreshold.oddValue - :oddValueAboveThreshold) * (m.oddAboveThreshold.oddValue - :oddValueAboveThreshold) "
                        + ") "
                        + "order by sqrt("
                        + "(m.oddBelowThreshold.oddValue - :oddValueBelowThreshold) * (m.oddBelowThreshold.oddValue - :oddValueBelowThreshold) + "
                        + "(m.oddAboveThreshold.oddValue - :oddValueAboveThreshold) * (m.oddAboveThreshold.oddValue - :oddValueAboveThreshold) "
                        + ") "
        ),
})
@Entity
@Table(name = "matching_total_goals_p_threshold", uniqueConstraints = {@UniqueConstraint(columnNames = {"threshold", "expected_total_goals"})})
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "matching_total_goals_id"))
})
public class TotalGoalsOddGroupMapping extends AbstractUnversionedEntity {

    static final String QUERY_LOAD_ODD_GROUP_FOR_TOTAL_GOALS_AND_THRESHOLD = "TotalGoalsOddGroupMapping.loadOddGroupForTotalGoalsAndThreshold";
    static final String QUERY_LOAD_TOTAL_GOALS_FOR_ODD_GROUP_AND_THRESHOLD = "TotalGoalsOddGroupMapping.loadTotalGoalsForOddGroupAndThreshold";

    private ThresholdTotalGoals threshold;
    private EstimatedGoals expectedTotalGoals;
    private EmbeddableOdd oddBelowThreshold;
    private EmbeddableOdd oddAboveThreshold;

    @Column(name= "threshold", precision = NumericSizes.THRESHOLD_PRECISION, scale = NumericSizes.THRESHOLD_SCALE)
    @NotNull
    protected ThresholdTotalGoals getThreshold() {
        return this.threshold;
    }

    public void setThreshold(ThresholdTotalGoals threshold) {
        this.threshold = threshold;
    }

    @Column(name= "expected_total_goals", precision = NumericSizes.EXPECTED_GOALS_PRECISION, scale = NumericSizes.EXPECTED_GOALS_SCALE)
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
            @AttributeOverride(name = "oddValue", column = @Column(name = "odd_less_than_threshold", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_less_than_threshold", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE))
    })
    public EmbeddableOdd getOddBelowThreshold() {
        return this.oddBelowThreshold;
    }

    public void setOddBelowThreshold(EmbeddableOdd oddBelowThreshold) {
        this.oddBelowThreshold = oddBelowThreshold;
    }

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odd_greater_than_threshold", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_greater_than_threshold", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE))
    })
    public EmbeddableOdd getOddAboveThreshold() {
        return this.oddAboveThreshold;
    }

    public void setOddAboveThreshold(EmbeddableOdd oddAboveThreshold) {
        this.oddAboveThreshold = oddAboveThreshold;
    }
}
