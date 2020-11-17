package de.coiaf.footballprediction.backend.importresults.common;

import de.coiaf.footballprediction.backend.importresults.entity.Division;
import de.coiaf.footballprediction.backend.persistence.ServiceQueryExecution;

import javax.inject.Inject;
import java.util.List;

public class DaoDivisions {

    private static final String QUERY_LOAD_ALL_DIVISIONS = "select new " + DivisionDisplayable.class.getName()
            + "(d) from Division d order by d.countryName, d.divisionLevel, d.divisionName";

    @Inject
    private ServiceQueryExecution queryExecutor;

    public List<DivisionDisplayable> loadAllDivisions() {
        return this.queryExecutor.loadMultipleResultsByQuery(QUERY_LOAD_ALL_DIVISIONS, null, DivisionDisplayable.class);
    }

    public Division reload(Division division) {
        return division == null || division.getId() == null ? null : this.queryExecutor.loadEntity(division.getId(), Division.class);
    }

}
