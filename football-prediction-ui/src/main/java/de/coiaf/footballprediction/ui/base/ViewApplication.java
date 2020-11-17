package de.coiaf.footballprediction.ui.base;

import java.io.Serializable;

public interface ViewApplication extends Serializable {

    String VIEW_NAME_IMPORT_RESULTS_FOOTBALL_DATA_CO_UK_FILE_PER_SEASON = "VIEW_NAME_IMPORT_RESULTS_FOOTBALL_DATA_CO_UK_FILE_PER_SEASON";

    String getTitle();

    interface ObserverViewApplication<V extends ViewApplication> {
        void setView(V view);
    }
}
