package de.coiaf.footballprediction.ui.importResults;

import de.coiaf.footballprediction.backend.importresults.common.DivisionDisplayable;
import de.coiaf.footballprediction.backend.importresults.common.ObserverResultImport;
import de.coiaf.footballprediction.backend.importresults.entity.Division;
import de.coiaf.footballprediction.ui.base.ViewApplication;

import java.util.List;

public interface ViewImportResultsFootballDataCoUkFilePerSeason extends ViewApplication, ObserverResultImport {

    void clearOutput();

    interface ObserverViewImportResults extends ObserverViewApplication<ViewImportResultsFootballDataCoUkFilePerSeason> {
        List<DivisionDisplayable> loadSupportedDivisions();

        void startImport(Division division, String season, String sourceUrl);
    }
}
