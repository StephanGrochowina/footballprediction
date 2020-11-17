package de.coiaf.random.regression;

import java.util.OptionalInt;
import java.util.stream.Stream;

@FunctionalInterface
public interface Source {

    int DEFAULT_DIMENSIONS = 1;

    Stream<RegressionDataRow> createStream();

    default int getDimensions() {
        OptionalInt firstFoundDimension = this.createStream()
                .filter(dataRow -> dataRow != null && dataRow.getVariates() != null && dataRow.getVariates().getDimension() > 0)
                .mapToInt(dataRow -> dataRow.getVariates().getDimension())
                .findFirst();

        return firstFoundDimension.isPresent() ? firstFoundDimension.getAsInt() : DEFAULT_DIMENSIONS;
    }
}
