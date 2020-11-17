package de.coiaf.footballprediction.common.vo.numerical;

import java.util.function.Supplier;

public abstract class AbstractDoubleBasedValueObject<VO extends AbstractDoubleBasedValueObject> extends AbstractNumberBasedValueObject<Double, VO> {

    protected AbstractDoubleBasedValueObject(Supplier<Double> internalValueSupplier) {
        super(internalValueSupplier);
    }

    @Override
    public int compareTo(VO other) {
        return this.getInternalValue().compareTo((Double) other.getInternalValue());
    }
}
