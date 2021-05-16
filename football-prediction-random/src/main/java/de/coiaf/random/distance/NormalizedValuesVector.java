package de.coiaf.random.distance;

import de.coiaf.random.probability.Probability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class NormalizedValuesVector {
    private final List<Probability> vector = new ArrayList<>();
    private final double maximumRelativeDistance;

    public NormalizedValuesVector(Probability... probabilities) {
        this(Arrays.asList(probabilities));
    }

    public NormalizedValuesVector(List<Probability> vector) {
        validateVector(vector);

        this.vector.addAll(vector);
        this.maximumRelativeDistance = Math.sqrt(
                vector.stream()
                    .filter(probability -> probability != null)
                    .map(probability -> Probability.UNCERTAIN.compareTo(probability) <= 0 ? probability : probability.negate())
                    .mapToDouble(probability -> Math.pow(probability.doubleValue(), 2))
                    .sum()
        );
    }

    public List<Probability> getVector() {
        return new ArrayList<>(this.vector);
    }

    public int getDimensions() {
        return this.vector.size();
    }

    public Probability determineDistance(NormalizedValuesVector other) {
        validateVector(other, this.getDimensions());

        if (this.vector.isEmpty()) {
            return Probability.IMPOSSIBLE;
        }

        double euclideanDistance = Math.sqrt(
                IntStream.range(0, this.getDimensions())
                    .mapToDouble(index -> Math.pow(this.vector.get(index).doubleValue() - other.vector.get(index).doubleValue(), 2))
                    .sum()
        );

        return Probability.valueOf(euclideanDistance / this.maximumRelativeDistance);
    }

    private static void validateVector(NormalizedValuesVector vector, int expectedDimensions) {
        Objects.requireNonNull(vector, "Parameter vector should not be null.");

        if (expectedDimensions != vector.getDimensions()) {
            throw new IllegalArgumentException("Parameter vector should have " + expectedDimensions + " dimensions. Instead, it has " + vector.getDimensions() + "dimensions.");
        }
    }

    private static void validateVector(List<Probability> vector) {
        Objects.requireNonNull(vector, "Parameter vector should not be null.");

        validateVector(vector.stream(), vector.size());
    }

    private static void validateVector(Stream<Probability> stream, long expectedDimensions) {
        Objects.requireNonNull(stream, "Parameter stream should not be null.");

        long foundDimensions = stream.filter(probability -> probability == null).count();
        if (foundDimensions != expectedDimensions) {
            throw new IllegalArgumentException("Vector should not contain null values");
        }
    }
}
