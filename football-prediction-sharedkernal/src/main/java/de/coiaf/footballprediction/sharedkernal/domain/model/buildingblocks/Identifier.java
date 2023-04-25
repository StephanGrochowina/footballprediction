package de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks;

/**
 * An instance representing a DDD identifier building block
 * @param <ID> the type of the ID used
 */
public interface Identifier<ID> extends ValueObject<Identifier<ID>> {

    /**
     * Gets the value of the ID
     * @return the value of the ID
     */
    ID getIdValue();
}
