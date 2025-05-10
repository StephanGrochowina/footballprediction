package de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks;

/**
 * abstract parent class for DDD aggregate root classes
 *
 * @param <ID> the type of the ID used by the identifier
 * @param <I> the type of the identifier
 * @param <T> the type of this instance
 */
public class AbstractAggregateRoot<ID, I extends Identifier<ID>, T extends AbstractAggregateRoot<ID, I, T>> extends AbstractEntity<ID, I, T> implements AggregateRoot<ID, I> {

    /**
     * constructor
     *
     * @param identifier the entity identifier
     * @throws NullPointerException if {@param identifier} is null
     */
    protected AbstractAggregateRoot(I identifier) {
        super(identifier);
    }
}
