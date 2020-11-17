package de.coiaf.footballprediction.backend.persistence.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class AbstractUnversionedEntity implements Serializable {

    public static final String NAME_ID = "id";

    private Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return this.id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
}
