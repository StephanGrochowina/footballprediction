package de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks;

/**
 * An aggregate root is a DDD building block which owns a group of entities and value objects which behave this way:
 * - The aggregate is created, retrieved and stored as a whole.
 * - The aggregate is always in a consistent state.
 * - The aggregate can be referenced from the outside through its root only. Objects outside of the aggregate may not
 *   reference any other entities inside the aggregate.
 * - The aggregate root is responsible for enforcing business invariants inside the aggregate, ensuring that the aggregate
 *   is in a consistent state at all times.
 *
 * The identifier of the aggregate root identifies the entire aggregate. An aggregate root should only reference other
 * aggregate roots by storing their identifier.
 *
 * @param <ID> the type of the ID used by the identifier
 * @param <I> the type of the identifier
 */
public interface AggregateRoot<ID, I extends Identifier<ID>> extends Entity<ID, I> {
}
