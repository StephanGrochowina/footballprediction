package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.ValueObject;

import java.io.Serializable;
import java.util.Objects;

public class ClubName implements ValueObject<ClubName>, Serializable {

    private final String name;

    public static ClubName valueOf(String name) {
        return new ClubName(name);
    }

    private ClubName(String name) {
        Objects.requireNonNull(name, "Parameter name must not be null.");

        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClubName that = (ClubName) o;
        return this.name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }
}
