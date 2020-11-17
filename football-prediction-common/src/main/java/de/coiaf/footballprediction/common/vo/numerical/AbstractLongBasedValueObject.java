package de.coiaf.footballprediction.common.vo.numerical;

import java.util.function.Supplier;

public abstract class AbstractLongBasedValueObject<VO extends AbstractLongBasedValueObject> extends AbstractNumberBasedValueObject<Long, VO> {

    protected AbstractLongBasedValueObject(Supplier<Long> internalValueSupplier) {
        super(internalValueSupplier);
    }

    @Override
    public int compareTo(VO other) {
        return this.getInternalValue().compareTo((Long) other.getInternalValue());
    }
}
