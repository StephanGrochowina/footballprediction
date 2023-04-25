package de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks;

/**
 * An instance representing a DDD entity building block
 * @param <T> the entity type
 */
public interface Entity<T extends Entity<T>> {

    /**
     *
     * @param otherEntity the entity to be compared
     * @return <code>true</code> if <code>otherEntity</code> is not null and has the same identity as this instance.
     */
    boolean hasSameIdentity(T otherEntity);
}
