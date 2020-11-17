package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.DecimalOddValue;
import de.coiaf.random.probability.Probability;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class EmbeddableOdd implements Serializable {

    private BigDecimal oddValue;
    private Probability impliedProbability;
    private Probability normalizedProbability;
    private BigDecimal normalizedOddValue;

    protected EmbeddableOdd() {
    }

    public EmbeddableOdd(BigDecimal oddValue, Probability impliedProbability, Probability normalizedProbability, BigDecimal normalizedOddValue) {
        this.oddValue = oddValue;
        this.impliedProbability = impliedProbability;
        this.normalizedProbability = normalizedProbability;
        this.normalizedOddValue = normalizedOddValue;
    }

    @NotNull
    @DecimalOddValue
    public BigDecimal getOddValue() {
        return this.oddValue;
    }

    protected void setOddValue(BigDecimal oddValue) {
        this.oddValue = oddValue;
    }

    @NotNull
    public Probability getImpliedProbability() {
        return this.impliedProbability;
    }

    protected void setImpliedProbability(Probability impliedProbability) {
        this.impliedProbability = impliedProbability;
    }

    @NotNull
    public Probability getNormalizedProbability() {
        return this.normalizedProbability;
    }

    protected void setNormalizedProbability(Probability normalizedProbability) {
        this.normalizedProbability = normalizedProbability;
    }

    @NotNull
    @DecimalOddValue
    public BigDecimal getNormalizedOddValue() {
        return this.normalizedOddValue;
    }

    public void setNormalizedOddValue(BigDecimal normalizedOddValue) {
        this.normalizedOddValue = normalizedOddValue;
    }
}
