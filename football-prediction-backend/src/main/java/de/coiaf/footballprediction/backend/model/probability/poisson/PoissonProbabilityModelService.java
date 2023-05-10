package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore;
import de.coiaf.footballprediction.backend.model.sharedcontext.OddGroupTotalGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PoissonProbabilityModelService {

    @Inject
    private PoissonOddGroupCalculator calculator;
    @Inject
    private PoissonTotalGoalsRepository totalGoalsRepository;

    /**
     * Determines the total goals odd group for over/under bets depending on the {@code prediction}
     * to be expected in a match and the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * defining what number of goals are considered to be part of the under and the over bet.
     * @param score the estimated prediction
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code prediction} is null
     */
    public OddGroupTotalGoals determineTotalGoalsOdds(EstimatedScore score) {
        Objects.requireNonNull(score, "Parameter prediction must not be null");

        return this.determineTotalGoalsOdds(
                () -> this.totalGoalsRepository.findOddGroup(score),
                () -> this.calculator.createTotalGoalsOdds(score),
                oddGroup -> this.totalGoalsRepository.persist(score.getTotalGoals(), oddGroup)
        );
    }

    /**
     * Determines the total goals odd group for over/under bets depending on the {@code prediction}
     * to be expected in a match and the {@code threshold} defining what number of goals are considered to be
     * part of the under and the over bet.
     * @param score the estimated prediction
     * @param threshold the threshold for over/under probabilities
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code prediction} or {@code threshold} is null
     */
    public OddGroupTotalGoals determineTotalGoalsOdds(EstimatedScore score, ThresholdTotalGoals threshold) {
        Objects.requireNonNull(score, "Parameter prediction must not be null");
        Objects.requireNonNull(threshold, "Parameter threshold must not be null");

        return this.determineTotalGoalsOdds(
                () -> this.totalGoalsRepository.findOddGroup(score, threshold),
                () -> this.calculator.createTotalGoalsOdds(score, threshold),
                oddGroup -> this.totalGoalsRepository.persist(score.getTotalGoals(), threshold, oddGroup)
        );
    }

    /**
     * Determines the total goals odd group for over/under bets depending on the {@code totalGoals}
     * to be expected in a match and the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * defining what number of goals are considered to be part of the under and the over bet.
     * @param goals the total goals to be expected in a match
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code goals} is null
     */
    public OddGroupTotalGoals determineTotalGoalsOdds(EstimatedGoals goals) {
        Objects.requireNonNull(goals, "Parameter goals must not be null");

        return this.determineTotalGoalsOdds(
                () -> this.totalGoalsRepository.findOddGroup(goals),
                () -> this.calculator.createTotalGoalsOdds(goals),
                oddGroup -> this.totalGoalsRepository.persist(goals, oddGroup)
        );
    }

    /**
     * Determines the total goals odd group for over/under bets depending on the {@code totalGoals}
     * to be expected in a match and the {@code threshold} defining what number of goals are considered to be
     * part of the under and the over bet.
     * @param goals the total goals to be expected in a match
     * @param threshold the threshold for over/under probabilities
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code goals} or {@code threshold} is null
     */
    public OddGroupTotalGoals determineTotalGoalsOdds(EstimatedGoals goals, ThresholdTotalGoals threshold) {
        Objects.requireNonNull(goals, "Parameter goals must not be null");
        Objects.requireNonNull(threshold, "Parameter threshold must not be null");

        return this.determineTotalGoalsOdds(
                () -> this.totalGoalsRepository.findOddGroup(goals, threshold),
                () -> this.calculator.createTotalGoalsOdds(goals, threshold),
                oddGroup -> this.totalGoalsRepository.persist(goals, threshold, oddGroup)
        );
    }

    private OddGroupTotalGoals determineTotalGoalsOdds(
            Supplier<OddGroupTotalGoals> supplierFromRepository,
            Supplier<OddGroupTotalGoals> supplierFromCalculator,
            Consumer<OddGroupTotalGoals> persisterToRepository
            ) {
        Objects.requireNonNull(supplierFromRepository, "Parameter supplierFromRepository must not be null");
        Objects.requireNonNull(supplierFromCalculator, "Parameter supplierFromCalculator must not be null");
        Objects.requireNonNull(persisterToRepository, "Parameter persisterToRepository must not be null");

        OddGroupTotalGoals result = supplierFromRepository.get();

        if (result == null) {
            result = supplierFromCalculator.get();

            try {
                persisterToRepository.accept(result);
            }
            catch (PersistenceException e) {
                // If odd group has been created concurrently take that one.
                OddGroupTotalGoals itemFromRepository = supplierFromRepository.get();
                if (itemFromRepository != null) {
                    result = itemFromRepository;
                }
            }
        }

        return result;
    }

    /**
     * Finds a {@link EstimatedGoals} instance for the odd group {@code odds} and the default threshold
     * {@link ThresholdTotalGoals#getDefaultInstance()}. If no such instance exists null will be returned.
     * @param odds the odd group providing odds and probabilities for total goals below/above
     *        the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * @return an {@link EstimatedGoals} instance or null
     * @throws NullPointerException if {@code oddGroup} is null
     */
    public EstimatedGoals determineTotalGoals(OddGroupTotalGoals odds) {
        Objects.requireNonNull(odds, "Parameter odds must not be null");

        return this.totalGoalsRepository.findGoals(odds);
    }

    /**
     * Finds a {@link EstimatedGoals} instance for the odd group {@code odds} and the threshold
     * {@code threshold}. If no such instance exists null will be returned.
     * @param odds the odd group providing odds and probabilities for total goals below/above
     *        the threshold {@code threshold}
     * @param threshold the threshold for over/under probabilities
     * @return an {@link EstimatedGoals} instance or null
     * @throws NullPointerException if {@code oddGroup} or {@code threshold} is null
     */
    public EstimatedGoals determineTotalGoals(OddGroupTotalGoals odds, ThresholdTotalGoals threshold) {
        Objects.requireNonNull(odds, "Parameter odds must not be null");
        Objects.requireNonNull(threshold, "Parameter threshold must not be null");

        return this.totalGoalsRepository.findGoals(odds, threshold);
    }

    void setCalculator(PoissonOddGroupCalculator calculator) {
        this.calculator = calculator;
    }

    void setTotalGoalsRepository(PoissonTotalGoalsRepository totalGoalsRepository) {
        this.totalGoalsRepository = totalGoalsRepository;
    }
}
