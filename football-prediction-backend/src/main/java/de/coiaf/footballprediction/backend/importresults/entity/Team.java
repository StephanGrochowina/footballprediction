package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teams")
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "team_id")),
        @AttributeOverride(name = AbstractEntity.NAME_VERSION, column = @Column(name = "version"))
})
public class Team extends AbstractEntity {

    private Season season;
    private String name;
    private List<MatchResult> homeMatches = new ArrayList<>();
    private List<MatchResult> awayMatches = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "season_id", nullable=false)
    @NotNull
    public Season getSeason() {
        return this.season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    @Column(name = "team_name", nullable = false, length = AbstractEntity.LENGTH_NAME)
    @Size(max = AbstractEntity.LENGTH_NAME)
    @NotNull
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL)
    @OrderBy("matchDay ASC")
    public List<MatchResult> getHomeMatches() {
        return homeMatches;
    }

    public void setHomeMatches(List<MatchResult> homeMatches) {
        this.homeMatches = homeMatches == null ? new ArrayList<>() : homeMatches;
    }
    public void addHomeMatch(MatchResult homeMatch) {
        if (homeMatch != null) {
            this.homeMatches.add(homeMatch);
            homeMatch.setHome(this);
        }
    }

    @OneToMany(mappedBy = "away", cascade = CascadeType.ALL)
    @OrderBy("matchDay ASC")
    public List<MatchResult> getAwayMatches() {
        return awayMatches;
    }

    public void setAwayMatches(List<MatchResult> awayMatches) {
        this.awayMatches = awayMatches == null ? new ArrayList<>() : awayMatches;
    }
    public void addAwayMatch(MatchResult awayMatch) {
        if (awayMatch != null) {
            this.awayMatches.add(awayMatch);
            awayMatch.setAway(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(season, team.season) &&
                Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(season, name);
    }
}
