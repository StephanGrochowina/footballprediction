package de.coiaf.footballprediction.backend.importresults;

import de.coiaf.footballprediction.backend.importresults.common.DaoDivisions;
import de.coiaf.footballprediction.backend.importresults.common.DivisionDisplayable;
import de.coiaf.footballprediction.backend.importresults.common.ObserverResultImport;
import de.coiaf.footballprediction.backend.importresults.entity.Division;
import de.coiaf.footballprediction.backend.importresults.singleseason.ImporterFootballDataCoUkFilePerSeason;

import javax.inject.Inject;
import java.util.List;

public class ServiceResultImport {

    @Inject
    private DaoDivisions daoDivisions;

    @Inject
    private ImporterFootballDataCoUkFilePerSeason importerFootballDataCoUkFilePerSeason;

    public List<DivisionDisplayable> getSupportedDivisions() {
        return this.daoDivisions.loadAllDivisions();
    }

    public void importSingleSeasonFromFootballDataCoUk(Division division, String seasonDescription, ObserverResultImport observer, String urlSource) {
        this.importerFootballDataCoUkFilePerSeason.importSeason(division, seasonDescription, observer, urlSource);
    }
}
