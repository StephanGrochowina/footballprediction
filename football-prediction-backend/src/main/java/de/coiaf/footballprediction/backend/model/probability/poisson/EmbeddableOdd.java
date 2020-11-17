package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.persistence.entity.DecimalOddValue;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.probability.Probability;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
public class EmbeddableOdd implements Serializable {

    static EmbeddableOdd from (DecimalOdd odd) {
        return new EmbeddableOdd(odd);
    }

    private BigDecimal oddValue;
    private Probability impliedProbability;

    protected EmbeddableOdd() {
    }

    /**
     * Creates EmbeddableOdd instance for {@code odd}.
     * @param odd the decimal odd to initialize this instance
     * @throws NullPointerException if {@code odd} is null
     */
    public EmbeddableOdd(DecimalOdd odd) {
        this.oddValue = odd.getOddValue();
        this.impliedProbability = odd.getImpliedProbability();
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

    public DecimalOdd createDecimalOdd() {
        return DecimalOdd.from(this.impliedProbability);
    }
}
