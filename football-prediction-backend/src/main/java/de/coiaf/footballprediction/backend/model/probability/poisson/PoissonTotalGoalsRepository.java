package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.EstimatedScore;
import de.coiaf.footballprediction.backend.model.sharedcontext.OddGroupTotalGoals;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import java.util.Objects;

public class PoissonTotalGoalsRepository {

    @Inject
    private ServiceQueryExecution queryExecutor;

    /**
     * Finds a {@link OddGroupTotalGoals} instance for the estimated score {@code score} and the
     * default threshold {@link ThresholdTotalGoals#getDefaultInstance()}.
     * @param score the estimated score
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code score} is null
     */
    public OddGroupTotalGoals findOddGroup(EstimatedScore score) {
        return this.findOddGroup(score.getTotalGoals());
    }

    /**
     * Finds a {@link OddGroupTotalGoals} instance for the estimated score {@code score} and the threshold
     * {@code threshold}.
     * @param score the estimated score
     * @param threshold the threshold for over/under probabilities
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code score} or {@code threshold} is null
     */
    public OddGroupTotalGoals findOddGroup(EstimatedScore score, ThresholdTotalGoals threshold) {
        return this.findOddGroup(score.getTotalGoals(), threshold);
    }

    /**
     * Finds a {@link OddGroupTotalGoals} instance for the total goals {@code goals} and the
     * default threshold {@link ThresholdTotalGoals#getDefaultInstance()}.
     * @param goals the total goals
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code goals} is null
     */
    public OddGroupTotalGoals findOddGroup(EstimatedGoals goals) {
        return this.findOddGroup(goals, ThresholdTotalGoals.getDefaultInstance());
    }

    /**
     * Finds a {@link OddGroupTotalGoals} instance for the total goals {@code goals} and the threshold
     * {@code threshold}.
     * @param goals the total goals
     * @param threshold the threshold for over/under probabilities
     * @return an {@link OddGroupTotalGoals} instance
     * @throws NullPointerException if {@code goals} or {@code threshold} is null
     */
    public OddGroupTotalGoals findOddGroup(EstimatedGoals goals, ThresholdTotalGoals threshold) {
        Objects.requireNonNull(goals, "Parameter goals must not be null.");
        Objects.requireNonNull(threshold, "Parameter threshold must not be null.");

        @SuppressWarnings("UnnecessaryLocalVariable")
        OddGroupTotalGoals result = this.queryExecutor.loadSingleResultByNamedQuery(
                TotalGoalsOddGroupMapping.QUERY_LOAD_ODD_GROUP_FOR_TOTAL_GOALS_AND_THRESHOLD,
                query -> query
                        .setParameter("expectedTotalGoals", goals)
                        .setParameter("threshold", threshold),
                OddGroupTotalGoals.class, false);

        return result;
    }

    /**
     * Persists the {@code oddGroup} for given total goals {@code goals} and the default threshold
     * {@link ThresholdTotalGoals#getDefaultInstance()}.
     * @param goals the total goals
     * @param oddGroup the odd group providing odds and probabilities for total goals below/above
     *        the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * @throws NullPointerException if {@code goals} or {@code oddGroup} is null
     * @throws PersistenceException if an entity with {@code goals} total goals and default threshold
     * {@link ThresholdTotalGoals#getDefaultInstance()} already exists
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public void persist(EstimatedGoals goals, OddGroupTotalGoals oddGroup) {
        this.persist(goals, ThresholdTotalGoals.getDefaultInstance(), oddGroup);
    }

    /**
     * Persists the {@code oddGroup} for given total goals {@code goals} and the threshold
     * {@code threshold}.
     * @param goals the total goals
     * @param threshold the threshold for over/under probabilities
     * @param oddGroup the odd group providing odds and probabilities for total goals below/above
     *        the threshold {@code threshold}
     * @throws NullPointerException if {@code goals}, {@code threshold} or {@code oddGroup} is null
     * @throws PersistenceException if an entity with {@code goals} total goals and threshold
     * {@code threshold} already exists
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public void persist(EstimatedGoals goals, ThresholdTotalGoals threshold, OddGroupTotalGoals oddGroup) {
        Objects.requireNonNull(goals, "Parameter goals must not be null.");
        Objects.requireNonNull(threshold, "Parameter threshold must not be null.");
        Objects.requireNonNull(oddGroup, "Parameter oddGroup must not be null.");

        TotalGoalsOddGroupMapping entity = new TotalGoalsOddGroupMapping();

        entity.setExpectedTotalGoals(goals);
        entity.setThreshold(threshold);
        entity.setOddBelowThreshold(EmbeddableOdd.from(oddGroup.getOddBelowThreshold()));
        entity.setOddAboveThreshold(EmbeddableOdd.from(oddGroup.getOddAboveThreshold()));

        this.queryExecutor.saveOrUpdate(entity, false);
    }

    /**
     * Finds a {@link EstimatedGoals} instance for the odd group {@code oddGroup} and the default threshold
     * {@link ThresholdTotalGoals#getDefaultInstance()}.
     * @param oddGroup the odd group providing odds and probabilities for total goals below/above
     *        the default threshold {@link ThresholdTotalGoals#getDefaultInstance()}
     * @return an {@link EstimatedGoals} instance
     * @throws NullPointerException if {@code oddGroup} is null
     */
    public EstimatedGoals findGoals(OddGroupTotalGoals oddGroup) {
        return this.findGoals(oddGroup, ThresholdTotalGoals.getDefaultInstance());
    }

    /**
     * Finds a {@link EstimatedGoals} instance for the odd group {@code oddGroup} and the threshold
     * {@code threshold}.
     * @param oddGroup the odd group providing odds and probabilities for total goals below/above
     *        the threshold {@code threshold}
     * @param threshold the threshold for over/under probabilities
     * @return an {@link EstimatedGoals} instance
     * @throws NullPointerException if {@code oddGroup} or {@code threshold} is null
     */
    public EstimatedGoals findGoals(OddGroupTotalGoals oddGroup, ThresholdTotalGoals threshold) {
        Objects.requireNonNull(threshold, "Parameter threshold must not be null.");
        Objects.requireNonNull(oddGroup, "Parameter oddGroup must not be null.");

        Double result = this.queryExecutor.loadSingleResultByNamedQuery(
                TotalGoalsOddGroupMapping.QUERY_LOAD_TOTAL_GOALS_FOR_ODD_GROUP_AND_THRESHOLD,
                query -> query
                        .setParameter("threshold", threshold)
                        .setParameter("oddValueBelowThreshold", oddGroup.getOddBelowThreshold().getOddValue())
                        .setParameter("oddValueAboveThreshold", oddGroup.getOddAboveThreshold().getOddValue())
                        .setMaxResults(1),
                Double.class, false
        );

        return EstimatedGoals.valueOf(result, true);
    }

    void setQueryExecutor(ServiceQueryExecution queryExecutor) {
        this.queryExecutor = queryExecutor;
    }
}
