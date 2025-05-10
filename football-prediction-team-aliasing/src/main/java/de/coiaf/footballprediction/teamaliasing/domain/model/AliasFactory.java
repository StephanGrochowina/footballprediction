package de.coiaf.footballprediction.teamaliasing.domain.model;

import java.util.UUID;

/**
 * Factory to create source team entities
 */
public class AliasFactory {

    /**
     * Creates a {@link Alias} entity with a random identifier, given type and
     * given alias name.
     * @param aliasType the type of the alias
     * @param aliasName the alias name of the team
     * @return the {@link Alias} instance to be created
     * @throws NullPointerException if aliasType or aliasName is null
     */
    public static Alias createInstance(AliasTypes aliasType, String aliasName) {
        AliasIdentifier identifierValue = AliasIdentifier.createRandomIdentifier();
        ClubName aliasNameValue = ClubName.valueOf(aliasName);

        return createInstance(identifierValue, aliasType, aliasNameValue);
    }

    /**
     * Creates a {@link Alias} entity with the given identifier, type and alias
     * name.
     * @param identifier the entity identifier, if null a randomly created
     *                   identifier will be used internally.
     * @param aliasType the type of the alias
     * @param aliasName the alias name of the team
     * @return the {@link Alias} instance to be created
     * @throws NullPointerException if aliasType or aliasName is null
     */
    public static Alias createInstance(UUID identifier, AliasTypes aliasType, String aliasName) {
        AliasIdentifier identifierValue = AliasIdentifier.of(identifier);
        ClubName aliasNameValue = ClubName.valueOf(aliasName);

        return createInstance(identifierValue, aliasType, aliasNameValue);
    }

    /**
     * Creates a {@link Alias} entity with a random identifier, given type and
     * the given alias name.
     * @param aliasType the type of the alias
     * @param aliasName the alias name of the team
     * @return the {@link Alias} instance to be created
     * @throws NullPointerException if any of the parameters is null
     */
    public static Alias createInstance(AliasTypes aliasType, ClubName aliasName) {
        AliasIdentifier identifier = AliasIdentifier.createRandomIdentifier();

        return createInstance(identifier, aliasType, aliasName);
    }

    /**
     * Creates a {@link Alias} entity with the given identifier, type and alias
     * name.
     * @param identifier the entity identifier
     * @param aliasType the type of the alias
     * @param aliasName the alias name of the team
     * @return the {@link Alias} instance to be created
     * @throws NullPointerException if any of the parameters is null
     */
    public static Alias createInstance(AliasIdentifier identifier, AliasTypes aliasType, ClubName aliasName) {
        return new Alias(identifier, aliasType, aliasName);
    }
}
