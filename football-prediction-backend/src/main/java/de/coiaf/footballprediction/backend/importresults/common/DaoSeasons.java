package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.Division;
import de.coiaf.footballprediction.backend.importresults.entity.MatchResult;
import de.coiaf.footballprediction.backend.importresults.entity.Season;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import java.util.Objects;
import java.util.function.Consumer;

public class DaoSeasons {

    private static final String QUERY_FIND_SEASON_BY_DIVISON_AND_DESCRIPTION =
            "select s from Season s inner join s.division d where d.id = :divisionId and s.description = :seasonDescription";

    @Inject
    @ResultImportsServiceQueryExecution
    private ServiceQueryExecution queryExecutor;

    public ContextSeasonImport createContext(Division division, String seasonDescription) {
        Season determinedSeason = determineSeason(division, seasonDescription);

        return new ContextSeasonImport(determinedSeason);
    }

    private Season determineSeason(Division division, String seasonDescription) {
        Objects.requireNonNull(division);
        Objects.requireNonNull(seasonDescription);

        Consumer<TypedQuery<Season>> providerQueryParameters = seasonTypedQuery ->
                seasonTypedQuery.setParameter("divisionId", division.getId())
                .setParameter("seasonDescription", seasonDescription);
        Season foundSeason = this.queryExecutor.loadSingleResultByQuery(QUERY_FIND_SEASON_BY_DIVISON_AND_DESCRIPTION, providerQueryParameters, Season.class, false);
        Season result;

        if (foundSeason != null) {
            result = foundSeason;
        }
        else {
            result = new Season();
            result.setDescription(seasonDescription);
            division.addSeason(result);
        }

        return result;
    }

    public Season saveOrUpdate(ContextSeasonImport context) {
        Objects.requireNonNull(context);

        Season contextSeason = context.getCurrentSeason();
        Season result;

        if (contextSeason != null) {
            result = this.queryExecutor.saveOrUpdate(contextSeason, !context.isNewSeason());
        }
        else {
            result = null;
        }

        return result;
    }

    public MatchResult persist(MatchResult match) {
        Objects.requireNonNull(match);

        return this.queryExecutor.saveOrUpdate(match, false);
    }
}
