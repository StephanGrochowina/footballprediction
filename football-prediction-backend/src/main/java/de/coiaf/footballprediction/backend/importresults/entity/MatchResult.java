package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "match_results")
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "match_result_id")),
        @AttributeOverride(name = AbstractEntity.NAME_VERSION, column = @Column(name = "version"))
})
public class MatchResult extends AbstractEntity {

    private Season season;
    private Team home;
    private Team away;
    private LocalDate matchDay;
    private EmbeddableScore halftime;
    private EmbeddableScore fulltime;
    private EmbeddableMatchStat shots;
    private EmbeddableMatchStat shotsOnTarget;
    private EmbeddableMatchStat corners;
    private EmbeddableMatchStat foulsCommitted;
    private EmbeddableMatchStat freeKicksConceded;
    private EmbeddableMatchStat offsides;
    private EmbeddableMatchStat yellowCards;
    private EmbeddableMatchStat redCards;
    private Set<OddsMatchOutcome> oddsMatchOutcome = new HashSet<>();
    private Set<OddsMatchTotalGoals> oddsMatchTotalGoals = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "season_id", nullable=false)
    @NotNull
    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "home_team_id", nullable=false)
    @NotNull
    public Team getHome() {
        return this.home;
    }

    public void setHome(Team home) {
        this.home = home;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "away_team_id", nullable=false)
    @NotNull
    public Team getAway() {
        return this.away;
    }

    public void setAway(Team away) {
        this.away = away;
    }

    @Column(name= "match_day")
    @NotNull
    public LocalDate getMatchDay() {
        return this.matchDay;
    }

    public void setMatchDay(LocalDate matchDay) {
        this.matchDay = matchDay;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "goalsHome", column = @Column(name = "half_time_goals_home_team")),
            @AttributeOverride(name = "goalsAway", column = @Column(name = "half_time_goals_away_team")),
            @AttributeOverride(name = "outcome", column = @Column(name = "half_time_outcome"))
    })
    public EmbeddableScore getHalftime() {
        return this.halftime;
    }

    public void setHalftime(EmbeddableScore halftime) {
        this.halftime = halftime;
    }

    @NotNull
    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "goalsHome", column = @Column(name = "full_time_goals_home_team")),
            @AttributeOverride(name = "goalsAway", column = @Column(name = "full_time_goals_away_team")),
            @AttributeOverride(name = "outcome", column = @Column(name = "full_time_outcome"))
    })
    public EmbeddableScore getFulltime() {
        return this.fulltime;
    }

    public void setFulltime(EmbeddableScore fulltime) {
        this.fulltime = fulltime;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_shots")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_shots"))
    })
    public EmbeddableMatchStat getShots() {
        return this.shots;
    }

    public void setShots(EmbeddableMatchStat shots) {
        this.shots = shots;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_shots_on_target")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_shots_on_target"))
    })
    public EmbeddableMatchStat getShotsOnTarget() {
        return this.shotsOnTarget;
    }

    public void setShotsOnTarget(EmbeddableMatchStat shotsOnTarget) {
        this.shotsOnTarget = shotsOnTarget;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_corners")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_corners"))
    })
    public EmbeddableMatchStat getCorners() {
        return this.corners;
    }

    public void setCorners(EmbeddableMatchStat corners) {
        this.corners = corners;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_fouls_committed")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_fouls_committed"))
    })
    public EmbeddableMatchStat getFoulsCommitted() {
        return this.foulsCommitted;
    }

    public void setFoulsCommitted(EmbeddableMatchStat foulsCommitted) {
        this.foulsCommitted = foulsCommitted;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_free_kicks_conceded")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_free_kicks_conceded"))
    })
    public EmbeddableMatchStat getFreeKicksConceded() {
        return this.freeKicksConceded;
    }

    public void setFreeKicksConceded(EmbeddableMatchStat freeKicksConceded) {
        this.freeKicksConceded = freeKicksConceded;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_offsides")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_offsides"))
    })
    public EmbeddableMatchStat getOffsides() {
        return this.offsides;
    }

    public void setOffsides(EmbeddableMatchStat offsides) {
        this.offsides = offsides;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_yellow_cards")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_yellow_cards"))
    })
    public EmbeddableMatchStat getYellowCards() {
        return this.yellowCards;
    }

    public void setYellowCards(EmbeddableMatchStat yellowCards) {
        this.yellowCards = yellowCards;
    }

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "matchStatHome", column = @Column(name = "home_team_red_cards")),
            @AttributeOverride(name = "matchStatAway", column = @Column(name = "away_team_red_cards"))
    })
    public EmbeddableMatchStat getRedCards() {
        return this.redCards;
    }

    public void setRedCards(EmbeddableMatchStat redCards) {
        this.redCards = redCards;
    }

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    public Set<OddsMatchOutcome> getOddsMatchOutcome() {
        return this.oddsMatchOutcome;
    }

    public void setOddsMatchOutcome(Set<OddsMatchOutcome> oddsMatchOutcome) {
        this.oddsMatchOutcome = oddsMatchOutcome == null ? new HashSet<>() : oddsMatchOutcome;
    }

    public void addOdds(OddsMatchOutcome odds) {
        if (odds != null) {
            this.oddsMatchOutcome.add(odds);
            odds.setMatch(this);
        }
    }

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    public Set<OddsMatchTotalGoals> getOddsMatchTotalGoals() {
        return this.oddsMatchTotalGoals;
    }

    public void setOddsMatchTotalGoals(Set<OddsMatchTotalGoals> oddsMatchTotalGoals) {
        this.oddsMatchTotalGoals = oddsMatchTotalGoals;
    }

    public void addOdds(OddsMatchTotalGoals odds) {
        if (odds != null) {
            this.oddsMatchTotalGoals.add(odds);
            odds.setMatch(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchResult that = (MatchResult) o;
        return Objects.equals(home, that.home) &&
                Objects.equals(away, that.away) &&
                Objects.equals(matchDay, that.matchDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(home, away, matchDay);
    }
}
