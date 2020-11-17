package de.coiaf.footballprediction.backend.importresults.common;

public interface ObserverResultImport {

    void notifyInfo(String message);

    void notifyError(String message);

    void notifyWarning(String message);

    void onSucceeded(String message);

    void onFailed(String message, Exception e);
}
