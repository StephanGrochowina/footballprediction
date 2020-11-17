package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.MatchResult;
import de.coiaf.footballprediction.backend.importresults.entity.Season;
import de.coiaf.footballprediction.backend.importresults.entity.Team;

import java.time.LocalDate;
import java.util.*;

public class ContextSeasonImport {

    private final boolean newSeason;
    private final Season currentSeason;
    private final Map<String, Team> teams = new HashMap<>();
    private final Set<MatchResult> matchResults = new HashSet<>();
    private int addedMatches = 0;
    private int skippedMatches = 0;

    private static Season initializeSeason(Season season) {
        return season == null ? new Season() : season;
    }

    private static boolean isNewSeason(Season season) {
        return season == null || season.getId() == null;
    }

    public ContextSeasonImport(Season currentSeason) {
        Season usedSeason = initializeSeason(currentSeason);

        this.newSeason = isNewSeason(currentSeason);
        this.currentSeason = usedSeason;

         this.buildUpContext(usedSeason);
    }

    private void buildUpContext(Season season) {
        if (season != null) {
            season.getTeams().forEach(team -> {
                this.teams.put(team.getName(), team);
                this.matchResults.addAll(team.getHomeMatches());
            });
        }
    }

    /**
     *
     * @param teamName name of the team
     * @return the Team instance represented by teamName. If no such team exists, it will be created.
     * @throws NullPointerException if teamName is null
     */
    public Team determineTeam(String teamName) {
        Team createdTeam;

        Objects.requireNonNull(teamName);

        if (this.teams.containsKey(teamName)) {
            return this.teams.get(teamName);
        }

        createdTeam = new Team();
        createdTeam.setName(teamName);
        this.currentSeason.addTeam(createdTeam);
        this.teams.put(teamName, createdTeam);

        return createdTeam;
    }

    /**
     *
     * @param homeTeam home team name
     * @param awayTeam away team name
     * @param matchDay day the match took place
     * @return true if match represented by these parameters is already present in this context.
     * @throws NullPointerException if one or more of these parameters is null
     */
    public boolean hasMatch(Team homeTeam, Team awayTeam, LocalDate matchDay) {
        Objects.requireNonNull(homeTeam);
        Objects.requireNonNull(awayTeam);
        Objects.requireNonNull(matchDay);

        MatchResult match = new MatchResult();
        match.setHome(homeTeam);
        match.setAway(awayTeam);
        match.setMatchDay(matchDay);

        return this.hasMatch(match);
    }

    /**
     *
     * @param match day the match took place
     * @return true if match is already part of this context.
     * @throws NullPointerException if match is null
     */
    public boolean hasMatch(MatchResult match) {
        Objects.requireNonNull(match);

        return this.matchResults.contains(match);
    }

    /**
     * Adds match to this instance if it is not already present.
     * @param match day the match took place
     * @return whether match has been added to this context
     * @throws NullPointerException if match is null
     */
    public boolean addMatch(MatchResult match) {
        boolean result = false;

        if (!this.hasMatch(match)) {
            result = this.matchResults.add(match);
            this.addedMatches += result ? 1 : 0;
            this.skippedMatches += result ? 0 : 1;
        }
        else {
            this.skippedMatches++;
        }

        return result;
    }

    public void skipMatch() {
        this.skippedMatches++;
    }

    public boolean isNewSeason() {
        return newSeason;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public int getAddedMatches() {
        return this.addedMatches;
    }

    public int getSkippedMatches() {
        return this.skippedMatches;
    }
}
