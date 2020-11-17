package de.coiaf.footballprediction.backend.importresults.entity;

import de.coiaf.footballprediction.backend.persistence.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@MappedSuperclass
public abstract class AbstractOdds extends AbstractEntity {

    @SuppressWarnings("WeakerAccess")
    protected static final String NAME_MATCH = "match";
    @SuppressWarnings("WeakerAccess")
    protected static final String NAME_BOOKMAKER_NAME = "bookmakerName";
    @SuppressWarnings("WeakerAccess")
    protected static final String NAME_MARGIN = "margin";

    private MatchResult match;
    private String bookmakerName;
    private BigDecimal margin;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @NotNull
    public MatchResult getMatch() {
        return this.match;
    }

    public void setMatch(MatchResult match) {
        this.match = match;
    }

    @NotNull
    public String getBookmakerName() {
        return this.bookmakerName;
    }

    public void setBookmakerName(String bookmakerName) {
        this.bookmakerName = bookmakerName;
    }

    @NotNull
    public BigDecimal getMargin() {
        return this.margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }
}
