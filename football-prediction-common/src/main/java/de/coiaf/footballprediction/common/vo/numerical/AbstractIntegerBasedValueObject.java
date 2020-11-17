package de.coiaf.footballprediction.common.vo.numerical;

import java.util.function.Supplier;

public abstract class AbstractIntegerBasedValueObject<VO extends AbstractIntegerBasedValueObject> extends AbstractNumberBasedValueObject<Integer, VO> {

    protected AbstractIntegerBasedValueObject(Supplier<Integer> internalValueSupplier) {
        super(internalValueSupplier);
    }

    @Override
    public int compareTo(VO other) {
        return this.getInternalValue().compareTo((Integer) other.getInternalValue());
    }
}
