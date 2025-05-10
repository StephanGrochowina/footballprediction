package de.coiaf.footballprediction.teamaliasing.domain.model;

import de.coiaf.footballprediction.sharedkernal.domain.model.common.buildingblocks.ValueObject;

public enum AliasTypes implements ValueObject<AliasTypes> {
    MATCH_RESULTS,
    ELO_SCORES
}
