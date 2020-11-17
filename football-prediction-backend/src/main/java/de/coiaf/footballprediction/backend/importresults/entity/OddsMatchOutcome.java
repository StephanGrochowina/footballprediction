package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;
import de.coiaf.footballprediction.backend.persistence.entity.NumericSizes;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "bookmaker_odds_1x2")
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "odd_id")),
        @AttributeOverride(name = AbstractEntity.NAME_VERSION, column = @Column(name = "version")),
        @AttributeOverride(name = AbstractOdds.NAME_BOOKMAKER_NAME, column = @Column(name = "bookmaker_name")),
        @AttributeOverride(name = AbstractOdds.NAME_MARGIN, column = @Column(name = "margin"))
})
@AssociationOverrides({
        @AssociationOverride(name = AbstractOdds.NAME_MATCH, joinColumns = {@JoinColumn(name = "match_result_id", nullable = false)})
})
public class OddsMatchOutcome extends AbstractOdds {

    private EmbeddableOdd oddHomeWin;
    private EmbeddableOdd oddDraw;
    private EmbeddableOdd oddAwayWin;

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odds_home_win", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_implied_home_win", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedProbability", column = @Column(name = "probability_normalized_home_win", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedOddValue", column = @Column(name = "odd_normalized_home_win", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE))
    })
    @NotNull
    public EmbeddableOdd getOddHomeWin() {
        return this.oddHomeWin;
    }

    public void setOddHomeWin(EmbeddableOdd oddHomeWin) {
        this.oddHomeWin = oddHomeWin;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odds_draw", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_implied_draw", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedProbability", column = @Column(name = "probability_normalized_draw", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedOddValue", column = @Column(name = "odd_normalized_draw", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE))
    })
    @NotNull
    public EmbeddableOdd getOddDraw() {
        return this.oddDraw;
    }

    public void setOddDraw(EmbeddableOdd oddDraw) {
        this.oddDraw = oddDraw;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "oddValue", column = @Column(name = "odds_away_win", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE)),
            @AttributeOverride(name = "impliedProbability", column = @Column(name = "probability_implied_away_win", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedProbability", column = @Column(name = "probability_normalized_away_win", precision = NumericSizes.PROBABILITY_PRECISION, scale = NumericSizes.PROBABILITY_SCALE)),
            @AttributeOverride(name = "normalizedOddValue", column = @Column(name = "odd_normalized_away_win", precision = NumericSizes.DECIMAL_ODD_VALUE_PRECISION, scale = NumericSizes.DECIMAL_ODD_VALUE_SCALE))
    })
    @NotNull
    public EmbeddableOdd getOddAwayWin() {
        return this.oddAwayWin;
    }

    public void setOddAwayWin(EmbeddableOdd oddAwayWin) {
        this.oddAwayWin = oddAwayWin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OddsMatchOutcome that = (OddsMatchOutcome) o;
        return Objects.equals(this.getMatch(), that.getMatch()) &&
                Objects.equals(this.getBookmakerName(), that.getBookmakerName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMatch(), this.getBookmakerName());
    }

}

