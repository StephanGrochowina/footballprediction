package de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks;

/**
 * An instance representing a DDD entity building block
 * @param <ID> the type of the ID used by the identifier
 * @param <I> the type of the identifier
 */
public interface Entity<ID, I extends Identifier<ID>> {

    /**
     * the entity identifier
     * @return the identifier of this entity
     */
    I getIdentifier();
}
