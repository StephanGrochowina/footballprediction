package de.coiaf.footballprediction.backend.persistence.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class AbstractEntity extends AbstractUnversionedEntity {

    public static final int LENGTH_DESCRIPTION = 200;
    public static final int LENGTH_NAME = 200;

    public static final String NAME_VERSION = "version";

    private Integer version;

    @Version
    public Integer getVersion() {
        return this.version;
    }

    protected void setVersion(Integer version) {
        this.version = version;
    }
}
