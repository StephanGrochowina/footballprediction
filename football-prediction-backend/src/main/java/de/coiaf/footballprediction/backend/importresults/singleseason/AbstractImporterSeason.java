package de.coiaf.footballprediction.backend.importresults.singleseason;

import de.coiaf.footballprediction.backend.csv.ImporterCsv;
import de.coiaf.footballprediction.backend.importresults.common.*;
import de.coiaf.footballprediction.backend.importresults.entity.Division;
import de.coiaf.footballprediction.backend.importresults.entity.MatchResult;
import de.coiaf.footballprediction.backend.importresults.entity.Season;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class AbstractImporterSeason {

    private static final CSVFormat SOURCE_IMPORT_FORMAT = CSVFormat.RFC4180.withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim()
            .withIgnoreEmptyLines()
            .withAllowMissingColumnNames();

    @Inject
    private DaoSeasons daoSeasons;
    @Inject
    private DaoDivisions daoDivisions;
    @Inject
    private ImporterCsv importerCsv;

    public void importSeason(Division division, String seasonDescription, ObserverResultImport observer, String urlSource) {
        ContextResultImport context;

        division = this.daoDivisions.reload(division);
        context = new ContextResultImport(division, seasonDescription, observer, urlSource);

        try {
            context.notifyInfo("Importing season '" + seasonDescription + "' from '" + urlSource + "' ...");
            this.determineSeason(context);
            this.parseFile(context);
            context.notifyInfo("Matches imported: " + context.getContextSeasonImport().getAddedMatches());
            context.notifyInfo("Matches skipped: " + context.getContextSeasonImport().getSkippedMatches());
            context.notifySucceeded("Season successfully imported.");
        }
        catch (Exception e) {
            context.notifyFailed("Season import failed.", e);
            e.printStackTrace();
        }
    }

    private void determineSeason(ContextResultImport context) {
        Objects.requireNonNull(context);

        ContextSeasonImport contextSeasonImport = this.daoSeasons.createContext(context.getDivision(), context.getSeasonDescription());
        Season currentSeason = contextSeasonImport.getCurrentSeason();
        context.setContextSeasonImport(contextSeasonImport);

        if (contextSeasonImport.isNewSeason()) {
            context.notifyInfo("Created new season " + currentSeason.getDivision().getDivisionName() + " " + currentSeason.getDescription() + ".");
        }
        else {
            context.notifyInfo("Loaded existing season " + currentSeason.getDivision().getDivisionName() + " " + currentSeason.getDescription() + ".");
        }
    }

    private void parseFile(ContextResultImport context) throws IOException {

        if (context.getUrlSource() != null) {
            this.importerCsv.importCsv(context.getUrlSource(), this.getSourceCharset(), SOURCE_IMPORT_FORMAT, record -> this.processRecord(context, record));
        }
        else {
            context.notifyWarning("No source URL specified.");
        }

    }

    protected CSVFormat getSourceImportFormat() {
        return SOURCE_IMPORT_FORMAT;
    }

    private void processRecord(ContextResultImport context, CSVRecord record) {
        String recordContent = record == null ? "" : record.toString();

        try {
            this.parseRecord(context, record);
        }
        catch (MatchAlreadyExistsException e) {
            context.notifyWarning(e.getMessage());
            context.notifyInfo("Skipping data row '" + recordContent + "'.");
            context.skipMatch();
        }
        catch (NullPointerException e) {
            context.notifyError("Error on importing data row: " + e.getMessage());
            context.notifyInfo("Skipping data row '" + recordContent + "'.");
            context.skipMatch();
        }
        catch (Exception e) {
            context.notifyError("Error on importing data row: " + e.getMessage());
            context.notifyInfo("Skipping data row '" + recordContent + "'.");
            e.printStackTrace();
            context.skipMatch();
        }
    }

    protected abstract Charset getSourceCharset();
    protected abstract void parseRecord(ContextResultImport context, CSVRecord record);

    protected void persistMatch(MatchResult match) {
        this.daoSeasons.persist(match);
    }

    protected ImporterCsv getImporterCsv() {
        return this.importerCsv;
    }

    protected static class ContextResultImport {
        private final Division division;
        private final String seasonDescription;
        private final ObserverResultImport observer;
        private final String urlSource;
        private ContextSeasonImport contextSeasonImport = null;

        public ContextResultImport(Division division, String seasonDescription, ObserverResultImport observer, String urlSource) {
            this.division = division;
            this.seasonDescription = seasonDescription;
            this.observer = observer;
            this.urlSource = urlSource;
        }

        public void notifyInfo(String message) {
            Objects.requireNonNull(message);

            this.notifyObserver(observerResultImport -> observerResultImport.notifyInfo(message));
        }

        public void notifyError(String message) {
            Objects.requireNonNull(message);

            this.notifyObserver(observerResultImport -> observerResultImport.notifyError(message));
        }

        public void notifyWarning(String message) {
            Objects.requireNonNull(message);

            this.notifyObserver(observerResultImport -> observerResultImport.notifyWarning(message));
        }

        public void notifySucceeded(String message) {
            this.notifyObserver(observerResultImport -> observerResultImport.onSucceeded(message));
        }

        public void notifyFailed(String message, Exception cause) {
            this.notifyObserver(observerResultImport -> observerResultImport.onFailed(message, cause));
        }

        private void notifyObserver(Consumer<ObserverResultImport> observerAction) {
            if (this.observer != null && observerAction != null) {
                observerAction.accept(this.observer);
            }
        }

        private void skipMatch() {
            if (this.contextSeasonImport != null) {
                this.contextSeasonImport.skipMatch();
            }
        }

        public Division getDivision() {
            return this.division;
        }

        public String getSeasonDescription() {
            return this.seasonDescription;
        }

        public ObserverResultImport getObserver() {
            return this.observer;
        }

        public String getUrlSource() {
            return this.urlSource;
        }

        public ContextSeasonImport getContextSeasonImport() {
            return this.contextSeasonImport;
        }

        public void setContextSeasonImport(ContextSeasonImport contextSeasonImport) {
            this.contextSeasonImport = contextSeasonImport;
        }
    }
}
