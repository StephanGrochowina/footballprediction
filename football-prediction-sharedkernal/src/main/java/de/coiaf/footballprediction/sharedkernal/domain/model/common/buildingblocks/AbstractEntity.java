package de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks;

import java.io.Serializable;
import java.util.Objects;

/**
 * abstract parent class for DDD entity classes
 * @param <ID> the type of the ID used by the identifier
 * @param <I> the type of the identifier
 * @param <T> the type of this instance
 */
public abstract class AbstractEntity<ID, I extends Identifier<ID>, T extends AbstractEntity<ID, I, T>> implements Entity<ID, I>, Serializable {

    private final I identifier;

    /**
     * constructor
     * @param identifier the entity identifier
     * @throws NullPointerException if {@param identifier} is null
     */
    protected AbstractEntity(I identifier) {
        Objects.requireNonNull(identifier, "Parameter entityIdentifier must not be null");

        this.identifier = identifier;
    }

    @Override
    public I getIdentifier() {
        return this.identifier;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || !this.getClass().isAssignableFrom(other.getClass())) {
            return false;
        }
        T that = (T) other;

        return this.hasSameIdentity(that);
    }

    /**
     *
     * @param otherEntity the entity to be compared
     * @return <code>true</code> if <code>otherEntity</code> is not null and has the same identity as this instance.
     */
    protected boolean hasSameIdentity(T otherEntity) {
        return otherEntity != null && this.getIdentifier().equals(otherEntity.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getIdentifier());
    }
}
