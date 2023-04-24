package de.coiaf.footballprediction.probabilitymodel.domain.model.results;

import de.coiaf.footballprediction.sharedkernal.domain.model.bets.BetTypes;
import de.coiaf.random.odds.DecimalOdd;
import de.coiaf.random.probability.Probability;

import java.util.Map;

/**
 * Description of the result calculated by a probability model
 */
public interface ProbabilityModelResults {

    /**
     * Gets the probability for the given bet type.
     * @param betType the bet type
     * @return the probability
     * @throws NullPointerException if betType is null
     * @throws IllegalArgumentException if there is no result for bet type, i.e. the probability model which has generated
     *                                  this result does not support this bet type.
     */
    Probability getProbability(BetTypes betType);

    /**
     * Gets the decimal odd for the given bet type.
     * @param betType the bet type
     * @return a decimal odd
     * @throws NullPointerException if betType is null
     * @throws IllegalArgumentException if there is no result for bet type, i.e. the probability model which has generated
     *                                  this result does not support this bet type.
     */
    default DecimalOdd getOdd(BetTypes betType) {
        Probability probability = this.getProbability(betType);

        return DecimalOdd.from(probability);
    }

    /**
     * Gets the results for all supported bet types
     * @return a Map of (supported bet type, probability)
     */
    Map<BetTypes, Probability> All();
}
