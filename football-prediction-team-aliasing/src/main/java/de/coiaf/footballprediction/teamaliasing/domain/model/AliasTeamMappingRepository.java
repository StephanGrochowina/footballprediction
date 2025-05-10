package de.coiaf.footballprediction.teamaliasing.domain.model;

import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;

/**
 * Repository for {@link AliasTeamMapping} instances
 */
public interface AliasTeamMappingRepository {

    /**
     * Saves or updates the mapping.
     * @param mapping the mapping to be persisted
     * @throws NullPointerException if {@code mapping} is null
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    void saveOrUpdate(AliasTeamMapping mapping);

    /**
     * Loads an {@link AliasTeamMapping} instance for the given alias team mapping identifier.
     * If the identifier is null or no mapping exists for the identifier null will be returned.
     * @param identifier the alias team mapping identifier
     * @return an {@link AliasTeamMapping} instance or null if no mapping
     * exists for the alias team mapping identifier
     */
    AliasTeamMapping findMapping(AliasTeamMappingIdentifier identifier);

    /**
     * Loads an {@link AliasTeamMapping} instance for the given alias. If the
     * alias is null or no mapping exists for the alias null will be returned.
     * @param alias the alias
     * @return an {@link AliasTeamMapping} instance or null if no mapping
     * exists for the alias
     */
    default AliasTeamMapping findMapping(Alias alias) {
        if (alias == null) {
            return null;
        }

        return this.findMapping(alias.getIdentifier());
    }

    /**
     * Loads an {@link AliasTeamMapping} instance for the given alias identifier. If the
     * identifier is null or no mapping exists for the identifier null will be returned.
     * @param identifier the alias identifier
     * @return an {@link AliasTeamMapping} instance or null if no mapping
     * exists for the alias identifier
     */
    AliasTeamMapping findMapping(AliasIdentifier identifier);
}
