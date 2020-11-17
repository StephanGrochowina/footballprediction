package de.coiaf.random.betevaluation;

import de.coiaf.random.odds.Odd;
import de.coiaf.random.probability.Probability;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.Stream;

public class BetEvaluator {

    static final AggregatedEvaluationResult EMPTY_EVALUATION_RESULT = new AggregatedEvaluationResult();

    private final BudgetRateCalculator calculator;

    BetEvaluator(BudgetRateCalculator calculator) {
        Objects.requireNonNull(calculator);

        this.calculator = calculator;
    }

    public AggregatedEvaluationResult evaluate(Stream<EvaluationContext> contexts) {
        Objects.requireNonNull(contexts);

        return contexts.filter(Objects::nonNull)
                .map(this::createEvaluationResult)
                .filter(aggregatedEvaluationResult -> BigDecimal.ZERO.compareTo(aggregatedEvaluationResult.aggregatedInvestment) < 0)
                .reduce(
                        EMPTY_EVALUATION_RESULT,
                        AggregatedEvaluationResult::add,
                        AggregatedEvaluationResult::add);
    }

    public EvaluationContext createEvaluationContext(Odd<?> bookmakersOdd, Probability modelProbability, boolean hasWon) {
        return new EvaluationContext(bookmakersOdd, modelProbability, hasWon);
    }

    AggregatedEvaluationResult createEvaluationResult(EvaluationContext context) {
        Objects.requireNonNull(context);

        BigDecimal bet = this.calculator.calculateBet(context.getBookmakersOdd(), context.getModelProbability(), BigDecimal.ONE);
        BigDecimal oddValue = context.getBookmakersOdd().getDecimalOddValue();
        BigDecimal balance = context.isHasWon() ? oddValue.multiply(bet) : BigDecimal.ZERO;

        balance = balance.subtract(bet);

        return new AggregatedEvaluationResult(oddValue, balance, bet);
    }

    public static class EvaluationContext {

        private final Odd<?> bookmakersOdd;
        private final Probability modelProbability;
        private final boolean hasWon;

        private EvaluationContext(Odd<?> bookmakersOdd, Probability modelProbability, boolean hasWon) {
            Objects.requireNonNull(bookmakersOdd);
            Objects.requireNonNull(modelProbability);

            this.bookmakersOdd = bookmakersOdd;
            this.modelProbability = modelProbability;
            this.hasWon = hasWon;
        }

        Odd<?> getBookmakersOdd() {
            return this.bookmakersOdd;
        }

        Probability getModelProbability() {
            return this.modelProbability;
        }

        boolean isHasWon() {
            return this.hasWon;
        }
    }

    public static class AggregatedEvaluationResult implements Comparable<AggregatedEvaluationResult> {

        private final BigDecimal aggregatedBookmakersOdds;
        private final BigDecimal aggregatedBalance;
        private final BigDecimal aggregatedInvestment;
        private final long evaluatedBets;
        private final BigDecimal averageBalance;

        private AggregatedEvaluationResult() {
            this(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0L);
        }

        private AggregatedEvaluationResult(BigDecimal aggregatedBookmakersOdds, BigDecimal aggregatedBalance, BigDecimal aggregatedInvestment) {
            this(aggregatedBookmakersOdds, aggregatedBalance, aggregatedInvestment, 1L);
        }

        private AggregatedEvaluationResult(BigDecimal aggregatedBookmakersOdds, BigDecimal aggregatedBalance, BigDecimal aggregatedInvestment, long evaluatedBets) {
            this.validateProperties(aggregatedBookmakersOdds, aggregatedBalance, aggregatedInvestment, evaluatedBets);

            BigDecimal evaluatedBetsAsBigDecimal = BigDecimal.valueOf(evaluatedBets);

            this.aggregatedBookmakersOdds = evaluatedBets == 0L ? BigDecimal.ZERO : aggregatedBookmakersOdds;
            this.aggregatedBalance = evaluatedBets == 0L ? BigDecimal.ZERO : aggregatedBalance;
            this.aggregatedInvestment = evaluatedBets == 0L ? BigDecimal.ZERO : aggregatedInvestment;
            this.evaluatedBets = evaluatedBets;
            this.averageBalance = evaluatedBets == 0L ? BigDecimal.ZERO : this.aggregatedBalance.divide(evaluatedBetsAsBigDecimal, 2, BigDecimal.ROUND_HALF_UP);
        }

        public AggregatedEvaluationResult add(AggregatedEvaluationResult aggregatedEvaluationResult) {
            Objects.requireNonNull(aggregatedEvaluationResult);

            return this.add(
                    aggregatedEvaluationResult.aggregatedBookmakersOdds,
                    aggregatedEvaluationResult.aggregatedBalance,
                    aggregatedEvaluationResult.aggregatedInvestment,
                    aggregatedEvaluationResult.evaluatedBets);
        }
        public AggregatedEvaluationResult add(BigDecimal bookmakersOdds, BigDecimal balance, BigDecimal investment) {
            return this.add(bookmakersOdds, balance, investment, 1L);
        }
        private AggregatedEvaluationResult add(BigDecimal aggregatedBookmakersOdds, BigDecimal aggregatedBalance, BigDecimal aggregatedInvestment, long evaluatedBets) {
            this.validateProperties(aggregatedBookmakersOdds, aggregatedBalance, aggregatedInvestment, evaluatedBets);

            return new AggregatedEvaluationResult(
                    this.aggregatedBookmakersOdds.add(aggregatedBookmakersOdds),
                    this.aggregatedBalance.add(aggregatedBalance),
                    this.aggregatedInvestment.add(aggregatedInvestment),
                    this.evaluatedBets + evaluatedBets);
        }

        private void validateProperties(BigDecimal aggregatedBookmakersOdds, BigDecimal aggregatedBalance, BigDecimal aggregatedInvestment, long evaluatedBets) {
            this.validateAggregatedBookmakersOdds(aggregatedBookmakersOdds, evaluatedBets);
            this.validateAggregatedBalance(aggregatedBalance, evaluatedBets);
            this.validateAggregatedInvestment(aggregatedInvestment, evaluatedBets);
            this.validateEvaluatedBets(evaluatedBets);

        }

        private void validateAggregatedBalance(BigDecimal aggregatedBalance, long evaluatedBets) {
            Objects.requireNonNull(aggregatedBalance, "Parameter aggregatedBalance must not be null.");

            BigDecimal minAggregatedBookmakersOdds = BigDecimal.valueOf(evaluatedBets);

            if (BigDecimal.ZERO.compareTo(minAggregatedBookmakersOdds) == 0 && BigDecimal.ZERO.compareTo(aggregatedBalance) != 0) {
                throw new IllegalArgumentException("If evaluatedBets is zero aggregatedBalance must be zero, too.");
            }
        }

        private void validateAggregatedBookmakersOdds(BigDecimal aggregatedBookmakersOdds, long evaluatedBets) {
            Objects.requireNonNull(aggregatedBookmakersOdds, "Parameter aggregatedBookmakersOdds must not be null.");

            BigDecimal minAggregatedBookmakersOdds = BigDecimal.valueOf(evaluatedBets);

            if (BigDecimal.ZERO.compareTo(minAggregatedBookmakersOdds) == 0 && BigDecimal.ZERO.compareTo(aggregatedBookmakersOdds) != 0) {
                throw new IllegalArgumentException("If evaluatedBets is zero aggregatedBookmakersOdds must be zero, too.");
            }
            else if (minAggregatedBookmakersOdds.compareTo(aggregatedBookmakersOdds) > 0) {
                throw new IllegalArgumentException("Parameter aggregatedBookmakersOdds must not be less than evaluatedBets!");
            }
        }

        private void validateAggregatedInvestment(BigDecimal aggregatedInvestment, long evaluatedBets) {
            Objects.requireNonNull(aggregatedInvestment, "Parameter aggregatedInvestment must not be null.");

            BigDecimal minAggregatedBookmakersOdds = BigDecimal.valueOf(evaluatedBets);

            if (BigDecimal.ZERO.compareTo(minAggregatedBookmakersOdds) == 0 && BigDecimal.ZERO.compareTo(aggregatedInvestment) != 0) {
                throw new IllegalArgumentException("If evaluatedBets is zero aggregatedInvestment must be zero, too.");
            }
            if (BigDecimal.ZERO.compareTo(aggregatedInvestment) > 0) {
                throw new IllegalArgumentException("Parameter aggregatedInvestment cannot be negative.");
            }
        }

        private void validateEvaluatedBets(long evaluatedBets) {
            if (evaluatedBets < 0L) {
                throw new IllegalArgumentException("Parameter evaluatedBets cannot be negative.");
            }
        }

        BigDecimal getAggregatedBookmakersOdds() {
            return this.aggregatedBookmakersOdds;
        }

        BigDecimal getAggregatedBalance() {
            return this.aggregatedBalance;
        }

        BigDecimal getAggregatedInvestment() {
            return this.aggregatedInvestment;
        }

        long getEvaluatedBets() {
            return this.evaluatedBets;
        }

        public BigDecimal getAverageBalance() {
            return this.averageBalance;
        }

        public boolean isCompetitiveModel() {
            return BigDecimal.ZERO.compareTo(this.aggregatedBalance) < 0;
        }

        @Override
        public int compareTo(AggregatedEvaluationResult other) {
            return this.getAverageBalance().compareTo(other.getAverageBalance());
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {return true;}
            if (other == null || this.getClass() != other.getClass()) {return false;}

            AggregatedEvaluationResult that = (AggregatedEvaluationResult) other;

            return Objects.equals(getAverageBalance(), that.getAverageBalance());
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getAverageBalance());
        }
    }
}
