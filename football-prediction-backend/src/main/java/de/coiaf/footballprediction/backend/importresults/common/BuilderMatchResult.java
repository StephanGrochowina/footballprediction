package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.*;
import de.coiaf.footballprediction.backend.model.sharedcontext.ThresholdTotalGoals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BuilderMatchResult {

    private final ContextSeasonImport context;
    private final Team homeTeam;
    private final Team awayTeam;
    private final MatchResult match;

    /**
     * Creates a BuilderMatchResult instance
     * @param context the context for importing the season
     * @param supplierHomeTeamName a supplier for providing the home team name
     * @param supplierAwayTeamName a supplier for providing the away team name
     * @param supplierMatchDay a supplier for providing the day the match is played
     * @return the BuilderMatchResultInstance
     * @throws NullPointerException if one or more of the parameters are null or any of the suppliers returns null
     * if its get method is called
     * @throws IllegalArgumentException if context already contains a MatchResult instance which is represented by
     * homeTeamName, awayTeamName and matchDay
     */
    public static BuilderMatchResult createInstance(ContextSeasonImport context, Supplier<String> supplierHomeTeamName
            , Supplier<String> supplierAwayTeamName, Supplier<LocalDate> supplierMatchDay) {

        Objects.requireNonNull(supplierHomeTeamName, "Parameter supplierHomeTeamName must not be null.");
        Objects.requireNonNull(supplierAwayTeamName, "Parameter supplierAwayTeamName must not be null.");
        Objects.requireNonNull(supplierMatchDay, "Parameter supplierMatchDay must not be null.");

        return createInstance(context, supplierHomeTeamName.get(), supplierAwayTeamName.get(), supplierMatchDay.get());
    }

    /**
     * Creates a BuilderMatchResult instance
     * @param context the context for importing the season
     * @param homeTeamName the name of the home team
     * @param awayTeamName the name of the away team
     * @param matchDay the day the match is played
     * @return the BuilderMatchResultInstance
     * @throws NullPointerException if one or more of the parameters are null
     * @throws MatchAlreadyExistsException if context already contains a MatchResult instance which is represented by
     * homeTeamName, awayTeamName and matchDay
     */
    public static BuilderMatchResult createInstance(ContextSeasonImport context, String homeTeamName, String awayTeamName, LocalDate matchDay) {
        Team homeTeam;
        Team awayTeam;

        Objects.requireNonNull(context, "Parameter context must not be null.");
        Objects.requireNonNull(homeTeamName, "Parameter homeTeamName must not be null.");
        Objects.requireNonNull(awayTeamName, "Parameter awayTeamName must not be null.");
        Objects.requireNonNull(matchDay, "Parameter matchDay must not be null.");

        homeTeam = context.determineTeam(homeTeamName);
        awayTeam = context.determineTeam(awayTeamName);

        if (context.hasMatch(homeTeam, awayTeam, matchDay)) {
            throw new MatchAlreadyExistsException("Match " + homeTeamName + " vs. " + awayTeamName + " on " + matchDay.toString() + " already exists.");
        }

        return new BuilderMatchResult(context, homeTeam, awayTeam, matchDay);
    }

    private BuilderMatchResult(ContextSeasonImport context, Team homeTeam, Team awayTeam, LocalDate matchDay) {
        this.context = context;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.match = new MatchResult();

        this.match.setMatchDay(matchDay);
    }

    public MatchResult build() {
        this.assignMatch();

        return this.match;
    }

    private void assignMatch() {
        this.match.setSeason(this.context.getCurrentSeason());
        this.homeTeam.addHomeMatch(this.match);
        this.awayTeam.addAwayMatch(this.match);
        this.context.addMatch(this.match);
    }

    public BuilderMatchResult addScoreHalfTime(Supplier<Integer> supplierGoalsHome, Supplier<Integer> supplierGoalsAway) {
        return this.addScore(supplierGoalsHome, supplierGoalsAway, this::addScoreHalfTime);
    }
    private BuilderMatchResult addScoreHalfTime(int goalsHome, int goalsAway) {
        EmbeddableScore scoreHalfTime = new EmbeddableScore(goalsHome, goalsAway);

        this.validateScores(scoreHalfTime, this.match.getFulltime());

        this.match.setHalftime(scoreHalfTime);

        return this;
    }

    public BuilderMatchResult addScoreFullTime(Supplier<Integer> supplierGoalsHome, Supplier<Integer> supplierGoalsAway) {
        return this.addScore(supplierGoalsHome, supplierGoalsAway, this::addScoreFullTime);
    }
    private BuilderMatchResult addScoreFullTime(int goalsHome, int goalsAway) {
        EmbeddableScore scoreFullTime = new EmbeddableScore(goalsHome, goalsAway);

        this.validateScores(this.match.getHalftime(), scoreFullTime);

        this.match.setFulltime(scoreFullTime);

        return this;
    }

    private BuilderMatchResult addScore(Supplier<Integer> supplierGoalsHome, Supplier<Integer> supplierGoalsAway, BiFunction<Integer, Integer, BuilderMatchResult> handlerScore) {
        Objects.requireNonNull(handlerScore);

        Integer goalsHome = supplierGoalsHome == null ? null : supplierGoalsHome.get();
        Integer goalsAway = supplierGoalsAway == null ? null : supplierGoalsAway.get();

        if (goalsHome != null && goalsAway != null) {
            handlerScore.apply(goalsHome, goalsAway);
        }

        return this;
    }

    private void validateScores(EmbeddableScore halfTime, EmbeddableScore fullTime) {
        if (halfTime != null && fullTime != null) {
            if (halfTime.getGoalsHome() > fullTime.getGoalsHome()) {
                throw new IllegalArgumentException("More home goals have been scored at half time than at full time.");
            }
            else if (halfTime.getGoalsAway() > fullTime.getGoalsAway()) {
                throw new IllegalArgumentException("More away goals have been scored at half time than at full time.");
            }
        }
    }

    public BuilderMatchResult addMatchStatShots(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatShots);
    }
    private BuilderMatchResult addMatchStatShots(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setShots(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatShotsOnTarget(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatShotsOnTarget);
    }
    private BuilderMatchResult addMatchStatShotsOnTarget(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setShotsOnTarget(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatCorners(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatCorners);
    }
    private BuilderMatchResult addMatchStatCorners(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setCorners(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatFoulsCommitted(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatFoulsCommitted);
    }
    private BuilderMatchResult addMatchStatFoulsCommitted(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setFoulsCommitted(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatFreeKicksConceded(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatFreeKicksConceded);
    }
    private BuilderMatchResult addMatchStatFreeKicksConceded(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setFreeKicksConceded(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatOffsides(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatOffsides);
    }
    private BuilderMatchResult addMatchStatOffsides(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setOffsides(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatYellowCards(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatYellowCards);
    }
    private BuilderMatchResult addMatchStatYellowCards(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setYellowCards(matchStatShots);

        return this;
    }

    public BuilderMatchResult addMatchStatRedCards(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway) {
        return this.addMatchStat(supplierMatchStatHome, supplierMatchStatAway, this::addMatchStatRedCards);
    }
    private BuilderMatchResult addMatchStatRedCards(int matchStatHome, int matchStatAway) {
        EmbeddableMatchStat matchStatShots = new EmbeddableMatchStat(matchStatHome, matchStatAway);

        this.match.setRedCards(matchStatShots);

        return this;
    }

    private BuilderMatchResult addMatchStat(Supplier<Integer> supplierMatchStatHome, Supplier<Integer> supplierMatchStatAway, BiFunction<Integer, Integer, BuilderMatchResult> handlerMatchStat) {
        Objects.requireNonNull(handlerMatchStat);

        Integer matchStatHome = supplierMatchStatHome == null ? null : supplierMatchStatHome.get();
        Integer matchStatAway = supplierMatchStatAway == null ? null : supplierMatchStatAway.get();

        if (matchStatHome != null && matchStatAway != null) {
            handlerMatchStat.apply(matchStatHome, matchStatAway);
        }

        return this;
    }

    public BuilderMatchResult addOddsMatchOutcome(Supplier<String> supplierBookmaker, Supplier<BigDecimal> supplierDecimalOddHomeWin
            , Supplier<BigDecimal> supplierDecimalOddDraw, Supplier<BigDecimal> supplierDecimalOddAwayWin) {
        String bookmaker = supplierBookmaker == null ? null : supplierBookmaker.get();
        BigDecimal decimalOddHomeWin = supplierDecimalOddHomeWin == null ? null : supplierDecimalOddHomeWin.get();
        BigDecimal decimalOddDraw = supplierDecimalOddDraw == null ? null : supplierDecimalOddDraw.get();
        BigDecimal decimalOddAwayWin = supplierDecimalOddAwayWin == null ? null : supplierDecimalOddAwayWin.get();

        if (bookmaker != null && decimalOddHomeWin != null && decimalOddDraw != null && decimalOddAwayWin != null) {
            this.addOddsMatchOutcome(bookmaker, decimalOddHomeWin, decimalOddDraw, decimalOddAwayWin);
        }

        return this;
    }
    private BuilderMatchResult addOddsMatchOutcome(String bookmaker, BigDecimal decimalOddHomeWin, BigDecimal decimalOddDraw, BigDecimal decimalOddAwayWin) {
        OddGroupMatchOutcome oddGroup = new OddGroupMatchOutcome(decimalOddHomeWin, decimalOddDraw, decimalOddAwayWin);
        OddsMatchOutcome oddsMatchOutcome = oddGroup.createMatchOutcomeOdds(bookmaker);

        this.match.addOdds(oddsMatchOutcome);

        return this;
    }

    public BuilderMatchResult addOddsMatchTotalGoals(Supplier<String> supplierBookmaker, Supplier<ThresholdTotalGoals> supplierThreshold
            , Supplier<BigDecimal> supplierDecimalOddGoalsBelowThreshold, Supplier<BigDecimal> supplierDecimalOddGoalsAboveThreshold) {
        String bookmaker = supplierBookmaker == null ? null : supplierBookmaker.get();
        ThresholdTotalGoals threshold = supplierThreshold == null ? null : supplierThreshold.get();
        BigDecimal decimalOddGoalsBelowThreshold = supplierDecimalOddGoalsBelowThreshold == null ? null : supplierDecimalOddGoalsBelowThreshold.get();
        BigDecimal decimalOddGoalsAboveThreshold = supplierDecimalOddGoalsAboveThreshold == null ? null : supplierDecimalOddGoalsAboveThreshold.get();

        if (bookmaker != null && threshold != null && decimalOddGoalsBelowThreshold != null && decimalOddGoalsAboveThreshold != null) {
            this.addOddsMatchTotalGoals(bookmaker, threshold, decimalOddGoalsBelowThreshold, decimalOddGoalsAboveThreshold);
        }

        return this;
    }
    private BuilderMatchResult addOddsMatchTotalGoals(String bookmaker, ThresholdTotalGoals threshold, BigDecimal decimalOddGoalsBelowThreshold, BigDecimal decimalOddGoalsAboveThreshold) {
        OddGroupMatchTotalGoals oddGroup = new OddGroupMatchTotalGoals(decimalOddGoalsBelowThreshold, decimalOddGoalsAboveThreshold);
        OddsMatchTotalGoals oddsMatchTotalGoals = oddGroup.createMatchOutcomeOdds(bookmaker, threshold);

        this.match.addOdds(oddsMatchTotalGoals);

        return this;
    }
}
