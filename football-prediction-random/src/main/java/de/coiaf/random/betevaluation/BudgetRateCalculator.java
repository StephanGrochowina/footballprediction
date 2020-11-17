package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;

@FunctionalInterface
public interface BudgetRateCalculator {

    /**
     * Calculates the budget rate for a given {@code bookmakersOdd} and {@code modelProbability}. The budget rate
     * is the fraction of the pay role which is proposed to be invested depending on those parameters. If betting
     * on {@code bookmakersOdd} with {@code modelProbability} has no value {@link Probability#IMPOSSIBLE} is returned.
     * If {@code modelProbability} equals {@link Probability#CERTAIN} then {@link Probability#CERTAIN} is returned.
     * Otherwise a {@link Probability instance} representing a value p with
     * {@code {@link Probability#IMPOSSIBLE} &lt; p &lt; {@link Probability#CERTAIN} } is returned.
     * The returned budget rate is never null.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @return the budget rate
     * @throws NullPointerException if {@code bookmakersOdd} or {@code modelProbability} is null
     */
    Probability calculateBudgetRate(Odd<?> bookmakersOdd, Probability modelProbability);

    /**
     * Calculates the amount to be betted for  a given {@code bookmakersOdd} and {@code modelProbability}. The
     * result is a value b with {@code 0 &lt; b &lt;= {@code payRole}}.
     * If betting on {@code bookmakersOdd} with {@code modelProbability} has no value {@link BigDecimal#ZERO} is
     * returned.
     * @param bookmakersOdd the odd provided by a bookmaker
     * @param modelProbability the probability of a personal model
     * @param payRole the current pay role for betting
     * @return the amount to bet
     * @throws NullPointerException if {@code bookmakersOdd}, {@code modelProbability} or {@code payRole} is null
     */
    default BigDecimal calculateBet(Odd<?> bookmakersOdd, Probability modelProbability, BigDecimal payRole) {
        Objects.requireNonNull(payRole);

        Probability budgetRate = this.calculateBudgetRate(bookmakersOdd, modelProbability);

        return Probability.isImpossible(budgetRate) ? BigDecimal.ZERO
                : payRole.multiply(budgetRate.toBigDecimal()).setScale(2, BigDecimal.ROUND_DOWN);
    }
}
