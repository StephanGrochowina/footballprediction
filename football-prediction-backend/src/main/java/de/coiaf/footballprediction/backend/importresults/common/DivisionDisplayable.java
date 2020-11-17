package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.Division;

import java.io.Serializable;
import java.util.Objects;

public class DivisionDisplayable implements Serializable {

    private final Division division;
    private final String divisionName;

    public DivisionDisplayable(Division division) {
        Objects.requireNonNull(division);

        this.division = division;
        this.divisionName = this.createDivisionName(division);
    }

    private String createDivisionName(Division division) {

        return division.getDivisionName() +
                " (" +
                division.getCountryCode() +
                '-' +
                division.getDivisionLevel() +
                ')';
    }

    public Division getDivision() {
        return this.division;
    }

    public String getDivisionName() {
        return this.divisionName;
    }
}
