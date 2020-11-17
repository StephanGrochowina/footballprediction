package de.coiaf.footballprediction.ui.importResults;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import de.coiaf.footballprediction.backend.importresults.common.DivisionDisplayable;
import de.coiaf.footballprediction.ui.base.AbstractViewApplication;

import javax.inject.Inject;
import java.util.List;

@CDIView(ViewImportResultsFootballDataCoUkFilePerSeason.VIEW_NAME_IMPORT_RESULTS_FOOTBALL_DATA_CO_UK_FILE_PER_SEASON)
@UIScoped
public class ViewImportResultsFootballDataCoUkFilePerSeasonImpl
        extends AbstractViewApplication<ViewImportResultsFootballDataCoUkFilePerSeason, ViewImportResultsFootballDataCoUkFilePerSeason.ObserverViewImportResults>
        implements ViewImportResultsFootballDataCoUkFilePerSeason {

    private ComboBox<DivisionDisplayable> comboBoxDivisions;
    private TextField textFieldSeason;
    private TextField textFieldSourceUrl;
    private TextArea textAreaOutput;
    private Button buttonImportSeason;

    @Override
    @Inject
    protected void setObserver(ObserverViewImportResults observer) {
        super.setObserver(observer);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        this.comboBoxDivisions.setSelectedItem(null);
        this.textFieldSeason.clear();
        this.textFieldSourceUrl.clear();
        this.textAreaOutput.clear();
        this.buttonImportSeason.setEnabled(true);
    }

    @Override
    protected Component createViewContent() {
        List<DivisionDisplayable> divisions = this.getObserver().loadSupportedDivisions();
        VerticalLayout content = new VerticalLayout();

        this.comboBoxDivisions = new ComboBox<>("Division");
        this.comboBoxDivisions.setItemCaptionGenerator(DivisionDisplayable::getDivisionName);
        this.comboBoxDivisions.setEmptySelectionAllowed(false);
//        this.comboBoxDivisions.setEmptySelectionCaption("-- Select a division --");
        this.comboBoxDivisions.setPlaceholder("-- Select a division --");
        this.comboBoxDivisions.setTextInputAllowed(true);
        this.comboBoxDivisions.setWidth("100%");
        this.comboBoxDivisions.setItems(divisions);
        this.comboBoxDivisions.setPageLength(10);
        content.addComponent(this.comboBoxDivisions);

        this.textFieldSeason = new TextField("Season");
        this.textFieldSeason.setWidth("100%");
        this.textFieldSeason.setRequiredIndicatorVisible(true);
        content.addComponent(this.textFieldSeason);

        this.textFieldSourceUrl = new TextField("URL");
        this.textFieldSourceUrl.setWidth("100%");
        this.textFieldSourceUrl.setRequiredIndicatorVisible(true);
        content.addComponent(this.textFieldSourceUrl);

        this.buttonImportSeason = new Button("Import season");
        this.buttonImportSeason.addClickListener(event -> {
            this.buttonImportSeason.setEnabled(false);
            this.getObserver().startImport(
                    this.comboBoxDivisions.getValue().getDivision(), this.textFieldSeason.getValue(), this.textFieldSourceUrl.getValue());
        });
        content.addComponent(this.buttonImportSeason);

        this.textAreaOutput = new TextArea("Output");
        this.textAreaOutput.setWidth("100%");
        this.textAreaOutput.setRows(20);
        this.textAreaOutput.setWordWrap(false);
        this.textAreaOutput.setReadOnly(true);
        content.addComponent(this.textAreaOutput);

        return content;
    }

    @Override
    public String getTitle() {
        return "Import football results (season per file) from football-data.co.uk";
    }

    @Override
    public void notifyInfo(String message) {
        this.appendOutput("INFO> " + message);
    }

    @Override
    public void notifyError(String message) {
        this.appendOutput("ERROR> " + message);
    }

    @Override
    public void notifyWarning(String message) {
        this.appendOutput("WARN> " + message);
    }

    @Override
    public void onSucceeded(String message) {
        this.appendOutput("\r\nImport successfully finished.");
        this.appendOutput(message);
        this.buttonImportSeason.setEnabled(true);
    }

    @Override
    public void onFailed(String message, Exception e) {
        this.appendOutput("\r\nImport failed: " + message);
        this.appendOutput("Exception: " + e.getClass().getName());
        this.buttonImportSeason.setEnabled(true);
    }

    private void appendOutput(String message) {
        StringBuilder output = new StringBuilder();

        if (!this.textAreaOutput.isEmpty()) {
            output.append(this.textAreaOutput.getValue());
        }

        if (message != null && !message.trim().isEmpty()) {
            if (output.length() > 0) {
                output.append("\r\n");
            }

            output.append(message.trim());

            this.textAreaOutput.setValue(output.toString());
        }
    }

    @Override
    public void clearOutput() {
        this.textAreaOutput.clear();
    }
}
