package de.coiaf.footballprediction.sharedkernal.domain.model.buildingblocks;

/**
 * An instance representing a DDD dommain event building block
 * A domain event is something that is unique, but does not have a lifecycle.
 * The identity may be explicit, for example the sequence number of a payment,
 * or it could be derived from various aspects of the event such as where, when and what
 * has happened.
 * @param <T> the domain event type
 */
public interface DomainEvent<T extends DomainEvent<T>> {

    /**
     *
     * @param otherDomainEvent the domain event to be compared
     * @return <code>true</code> if <code>otheDomainEvent</code> is not null and represents the same event as this instance.
     */
    default boolean isSameEvent(T otherDomainEvent) {
        return this.equals(otherDomainEvent);
    }
}
