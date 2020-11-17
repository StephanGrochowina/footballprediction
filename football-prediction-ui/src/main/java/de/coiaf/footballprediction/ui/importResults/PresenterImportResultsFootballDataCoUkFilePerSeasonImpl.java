package de.coiaf.footballprediction.ui.importResults;

import de.coiaf.footballprediction.backend.importresults.common.DivisionDisplayable;
import de.coiaf.footballprediction.backend.importresults.ServiceResultImport;
import de.coiaf.footballprediction.backend.importresults.entity.Division;
import de.coiaf.footballprediction.ui.base.AbstractPresenterApplication;

import javax.inject.Inject;
import java.util.List;

public class PresenterImportResultsFootballDataCoUkFilePerSeasonImpl extends AbstractPresenterApplication<ViewImportResultsFootballDataCoUkFilePerSeason> implements ViewImportResultsFootballDataCoUkFilePerSeason.ObserverViewImportResults, PresenterImportResultsFootballDataCoUkFilePerSeason {

    @Inject
    private ServiceResultImport importer;

    @Override
    public List<DivisionDisplayable> loadSupportedDivisions() {
        return this.importer.getSupportedDivisions();
    }

    @Override
    public void startImport(Division division, String season, String sourceUrl) {
        this.getView().clearOutput();
        this.importer.importSingleSeasonFromFootballDataCoUk(division, season, this.getView(), sourceUrl);
    }
}
