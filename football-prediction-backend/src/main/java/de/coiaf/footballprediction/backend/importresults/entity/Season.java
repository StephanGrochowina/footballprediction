package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "seasons")
@AttributeOverrides({
        @AttributeOverride(name = AbstractEntity.NAME_ID, column = @Column(name = "season_id")),
        @AttributeOverride(name = AbstractEntity.NAME_VERSION, column = @Column(name = "version"))
})
public class Season extends AbstractEntity {

    private Division division;
    private String description;
    private Set<Team> teams = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "division_id", nullable=false)
    @NotNull
    public Division getDivision() {
        return this.division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    @Column(name = "description", nullable = false, length = AbstractEntity.LENGTH_DESCRIPTION)
    @Size(max = AbstractEntity.LENGTH_DESCRIPTION)
    @NotNull
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    public Set<Team> getTeams() {
        return this.teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams == null ? new HashSet<>() : teams;
    }
    public void addTeam(Team team) {
        if (team != null) {
            this.teams.add(team);
            team.setSeason(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return Objects.equals(division, season.division) &&
                Objects.equals(description, season.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(division, description);
    }
}
