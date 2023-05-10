package de.coiaf.footballprediction.backend.model.probability.poisson;

import de.coiaf.footballprediction.backend.model.probability.PredictionsServiceQueryExecution;
import de.coiaf.footballprediction.backend.model.sharedcontext.OddGroupOutcome;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedGoals;
import de.coiaf.footballprediction.sharedkernal.domain.model.prediction.EstimatedScore;
import de.coiaf.random.distributions.enumerated.EnumDistribution;
import de.coiaf.random.distributions.enumerated.matchOutcome.Outcomes;
import de.coiaf.random.probability.Probability;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import java.math.BigDecimal;
import java.util.Objects;

public class PoissonOutcomeRepository {

    private static final BigDecimal ODD_VALUE_RANGE = new BigDecimal("0.10");

    @Inject
    @PredictionsServiceQueryExecution
    private ServiceQueryExecution queryExecutor;

    /**
     * Finds a {@link EnumDistribution} instance for the estimated prediction {@code prediction}.
     * @param score the estimated prediction
     * @return an {@link EnumDistribution} instance
     * @throws NullPointerException if {@code prediction} is null
     */
    public EnumDistribution<Outcomes, Probability> findDistribution(EstimatedScore score) {
        OddGroupOutcome oddGroup = this.findOddGroup(score);

        return oddGroup == null ? null : oddGroup.toEnumDistribution();
    }

    /**
     * Finds a {@link OddGroupOutcome} instance for the estimated prediction {@code prediction}.
     * @param score the estimated prediction
     * @return an {@link OddGroupOutcome} instance
     * @throws NullPointerException if {@code prediction} is null
     */
    public OddGroupOutcome findOddGroup(EstimatedScore score) {
        Objects.requireNonNull(score, "Parameter prediction must not be null.");

        @SuppressWarnings("UnnecessaryLocalVariable")
        OddGroupOutcome result = this.queryExecutor.loadSingleResultByNamedQuery(
                OutcomeOddGroupMapping.QUERY_LOAD_ODD_GROUP_FOR_SCORE,
                query -> query
                        .setParameter("expectedHomeGoals", score.getHomeGoals())
                        .setParameter("expectedAwayGoals", score.getAwayGoals()),
                OddGroupOutcome.class, false);

        return result;
    }

    /**
     * Persists the {@code oddGroup} for a given prediction {@code prediction}.
     * @param score the estimated prediction
     * @param oddGroup the odd group providing odds and probabilities for the outcome
     * @throws NullPointerException if {@code prediction} or {@code oddGroup} is null
     * @throws PersistenceException if an entity with the prediction {@code prediction} already exists
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public void persist(EstimatedScore score, OddGroupOutcome oddGroup) {
        Objects.requireNonNull(score, "Parameter prediction must not be null.");
        Objects.requireNonNull(oddGroup, "Parameter oddGroup must not be null.");

        OutcomeOddGroupMapping entity = new OutcomeOddGroupMapping();

        entity.setExpectedHomeGoals(score.getHomeGoals());
        entity.setExpectedAwayGoals(score.getAwayGoals());
        entity.setExpectedTotalGoals(score.getTotalGoals());
        entity.setOddHomeWin(EmbeddableOdd.from(oddGroup.getOddHomeWin()));
        entity.setOddDraw(EmbeddableOdd.from(oddGroup.getOddDraw()));
        entity.setOddAwayWin(EmbeddableOdd.from(oddGroup.getOddAwayWin()));

        this.queryExecutor.saveOrUpdate(entity, false);
    }

    /**
     * Finds a {@link EstimatedScore} instance for the distribution {@code distribution}
     * @param distribution the distribution providing probabilities for the outcome
     * @return an {@link EstimatedScore} instance
     * @throws NullPointerException if {@code oddGroup} is null
     */
    public EstimatedScore findScore(EnumDistribution<Outcomes, Probability> distribution) {
        OddGroupOutcome oddGroup = OddGroupOutcome.createInstance(distribution);

        return this.findScore(oddGroup);
    }

    /**
     * Finds a {@link EstimatedScore} instance for the odd group {@code oddGroup}.
     * @param oddGroup the odd group providing odds and probabilities for the outcome
     * @return an {@link EstimatedScore} instance
     * @throws NullPointerException if {@code oddGroup} is null
     */
    public EstimatedScore findScore(OddGroupOutcome oddGroup) {
        Objects.requireNonNull(oddGroup, "Parameter oddGroup must not be null.");

        @SuppressWarnings("UnnecessaryLocalVariable")
        EstimatedScore result = this.queryExecutor.loadSingleResultByNamedQuery(
                TotalGoalsOddGroupMapping.QUERY_LOAD_TOTAL_GOALS_FOR_ODD_GROUP_AND_THRESHOLD,
                query -> query
                        .setParameter("minHomeWinOddValue", oddGroup.getOddHomeWin().getOddValue().subtract(ODD_VALUE_RANGE))
                        .setParameter("maxHomeWinOddValue", oddGroup.getOddHomeWin().getOddValue().add(ODD_VALUE_RANGE))
                        .setParameter("oddValueHomeWin", oddGroup.getOddHomeWin().getOddValue())
                        .setParameter("minDrawOddValue", oddGroup.getOddDraw().getOddValue().subtract(ODD_VALUE_RANGE))
                        .setParameter("maxDrawOddValue", oddGroup.getOddDraw().getOddValue().add(ODD_VALUE_RANGE))
                        .setParameter("oddDraw", oddGroup.getOddDraw().getOddValue())
                        .setParameter("minAwayWinOddValue", oddGroup.getOddAwayWin().getOddValue().subtract(ODD_VALUE_RANGE))
                        .setParameter("maxAwayWinOddValue", oddGroup.getOddAwayWin().getOddValue().add(ODD_VALUE_RANGE))
                        .setParameter("oddValueAwayWin", oddGroup.getOddAwayWin().getOddValue())
                        .setMaxResults(1),
                EstimatedScore.class, false
        );

        return result;
    }

    /**
     * Finds a {@link EstimatedScore} instance for the distribution {@code distribution} and the total goals {@code totalGoals}.
     * @param distribution the distribution providing probabilities for the outcome
     * @param totalGoals constraint on the total goals
     * @return an {@link EstimatedScore} instance
     * @throws NullPointerException if {@code distribution} or {@code totalGoals} is null
     */
    public EstimatedScore findScore(EnumDistribution<Outcomes, Probability> distribution, EstimatedGoals totalGoals) {
        OddGroupOutcome oddGroup = OddGroupOutcome.createInstance(distribution);

        return this.findScore(oddGroup, totalGoals);
    }

    /**
     * Finds a {@link EstimatedScore} instance for the odd group {@code oddGroup} and the total goals {@code totalGoals}.
     * @param oddGroup the odd group providing odds and probabilities for the outcome
     * @param totalGoals constraint on the total goals
     * @return an {@link EstimatedScore} instance
     * @throws NullPointerException if {@code oddGroup} or {@code totalGoals} is null
     */
    public EstimatedScore findScore(OddGroupOutcome oddGroup, EstimatedGoals totalGoals) {
        Objects.requireNonNull(oddGroup, "Parameter oddGroup must not be null.");
        Objects.requireNonNull(totalGoals, "Parameter totalGoals must not be null.");

        @SuppressWarnings("UnnecessaryLocalVariable")
        EstimatedScore result = this.queryExecutor.loadSingleResultByNamedQuery(
                TotalGoalsOddGroupMapping.QUERY_LOAD_TOTAL_GOALS_FOR_ODD_GROUP_AND_THRESHOLD,
                query -> query
                        .setParameter("expectedTotalGoals", totalGoals)
                        .setParameter("minHomeWinOddValue", oddGroup.getOddHomeWin().getOddValue().subtract(ODD_VALUE_RANGE))
                        .setParameter("maxHomeWinOddValue", oddGroup.getOddHomeWin().getOddValue().add(ODD_VALUE_RANGE))
                        .setParameter("oddValueHomeWin", oddGroup.getOddHomeWin().getOddValue())
                        .setParameter("minDrawOddValue", oddGroup.getOddDraw().getOddValue().subtract(ODD_VALUE_RANGE))
                        .setParameter("maxDrawOddValue", oddGroup.getOddDraw().getOddValue().add(ODD_VALUE_RANGE))
                        .setParameter("oddDraw", oddGroup.getOddDraw().getOddValue())
                        .setParameter("minAwayWinOddValue", oddGroup.getOddAwayWin().getOddValue().subtract(ODD_VALUE_RANGE))
                        .setParameter("maxAwayWinOddValue", oddGroup.getOddAwayWin().getOddValue().add(ODD_VALUE_RANGE))
                        .setParameter("oddValueAwayWin", oddGroup.getOddAwayWin().getOddValue())
                        .setMaxResults(1),
                EstimatedScore.class, false
        );

        return result;
    }

    void setQueryExecutor(ServiceQueryExecution queryExecutor) {
        this.queryExecutor = queryExecutor;
    }
}
