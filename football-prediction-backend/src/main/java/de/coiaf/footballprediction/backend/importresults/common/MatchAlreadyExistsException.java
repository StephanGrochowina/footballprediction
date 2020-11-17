package de.coiaf.footballprediction.backend.importresults.common;

public class MatchAlreadyExistsException extends IllegalArgumentException {
    MatchAlreadyExistsException(String s) {
        super(s);
    }
}
