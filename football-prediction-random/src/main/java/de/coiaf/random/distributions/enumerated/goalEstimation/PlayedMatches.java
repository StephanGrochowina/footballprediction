package de.coiaf.random.distributions.enumerated.goalEstimation;

import de.coiaf.footballprediction.common.vo.numerical.AbstractDoubleBasedValueObject;

import java.util.Objects;

class PlayedMatches extends AbstractDoubleBasedValueObject<PlayedMatches> {

    private static final double MIN_PLAYED_MATCHES_VALUE = 0.0;
    static final PlayedMatches NO_PLAYED_MATCHES = PlayedMatches.valueOf(MIN_PLAYED_MATCHES_VALUE);

    static <N extends Number> PlayedMatches valueOf(N matchesPlayed) {
        Objects.requireNonNull(matchesPlayed, "Parameter matchesPlayed must not be null.");

        return new PlayedMatches(matchesPlayed.doubleValue());
    }

    private static Double createInternalValue(double matchesPlayed) {
        validateLowerBound(matchesPlayed);

        return matchesPlayed;
    }

    private static void validateLowerBound(double matchesPlayed) {
        if (matchesPlayed < MIN_PLAYED_MATCHES_VALUE) {
            throw new IllegalArgumentException("Parameter matchesPlayed must be positive.");
        }
    }

    private PlayedMatches(double matchesPlayed) {
        super(() -> createInternalValue(matchesPlayed));
    }

//    double getInverseValue() {
//        double matchesPlayed = this.getInternalValue();
//
//        return this.hasNotPlayedMatches(matchesPlayed) ? Double.POSITIVE_INFINITY : 1.0 / matchesPlayed;
//    }

    boolean hasPlayedMatches() {
        return this.hasPlayedMatches(this.getInternalValue());
    }

    private boolean hasPlayedMatches(double matchesPlayed) {
        return MIN_PLAYED_MATCHES_VALUE < matchesPlayed;
    }
}
