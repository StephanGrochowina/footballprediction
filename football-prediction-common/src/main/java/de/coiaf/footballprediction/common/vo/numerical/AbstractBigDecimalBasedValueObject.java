package de.coiaf.footballprediction.common.vo.numerical;

import java.math.BigDecimal;
import java.util.function.Supplier;

public abstract class AbstractBigDecimalBasedValueObject<VO extends AbstractBigDecimalBasedValueObject> extends AbstractNumberBasedValueObject<BigDecimal, VO> {

    protected AbstractBigDecimalBasedValueObject(Supplier<BigDecimal> internalValueSupplier) {
        super(internalValueSupplier);
    }

    /**
     *
     * @return the {@link BigDecimal} value representing this instance
     */
    public BigDecimal toBigDecimal() {
        return this.getInternalValue();
    }

    @Override
    public int compareTo(VO other) {
        return this.getInternalValue().compareTo((BigDecimal) other.getInternalValue());
    }
}
