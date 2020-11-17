package de.coiaf.footballprediction.common.vo.numerical;

import java.util.function.Supplier;

public abstract class AbstractFloatBasedValueObject<VO extends AbstractFloatBasedValueObject> extends AbstractNumberBasedValueObject<Float, VO> {

    protected AbstractFloatBasedValueObject(Supplier<Float> internalValueSupplier) {
        super(internalValueSupplier);
    }

    @Override
    public int compareTo(VO other) {
        return this.getInternalValue().compareTo((Float) other.getInternalValue());
    }
}
