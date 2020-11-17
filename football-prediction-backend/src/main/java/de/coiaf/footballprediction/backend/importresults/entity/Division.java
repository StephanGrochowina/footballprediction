package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "view_divisions")
public class Division {

    private Long id;
    private String countryCode;
    private String countryName;
    private String divisionName;
    private int divisionLevel;
    private Set<Season> seasons = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "division_id", nullable = false, unique = true)
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Column(name = "country_code", nullable = false, length = 3)
    @Size(min = 3, max = 3)
    @NotNull
    public String getCountryCode() {
        return this.countryCode;
    }

    protected void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "country_name", nullable = false, length = AbstractEntity.LENGTH_NAME)
    @Size(max = AbstractEntity.LENGTH_NAME)
    @NotNull
    public String getCountryName() {
        return this.countryName;
    }

    protected void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Column(name = "division_name", nullable = false, length = AbstractEntity.LENGTH_NAME)
    @Size(max = AbstractEntity.LENGTH_NAME)
    @NotNull
    public String getDivisionName() {
        return this.divisionName;
    }

    protected void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    @Column(name = "division_level", nullable = false)
    @NotNull
    public int getDivisionLevel() {
        return this.divisionLevel;
    }

    protected void setDivisionLevel(int divisionLevel) {
        this.divisionLevel = divisionLevel;
    }

    @OneToMany(mappedBy = "division", cascade = CascadeType.ALL)
    public Set<Season> getSeasons() {
        return this.seasons;
    }

    public void setSeasons(Set<Season> seasons) {
        this.seasons = seasons == null ? new HashSet<>() : seasons;
    }
    public void addSeason(Season season) {
        if (season != null) {
            this.seasons.add(season);
            season.setDivision(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Division division = (Division) o;
        return divisionLevel == division.divisionLevel &&
                Objects.equals(countryCode, division.countryCode) &&
                Objects.equals(divisionName, division.divisionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryCode, divisionName, divisionLevel);
    }
}
