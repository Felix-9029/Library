package de.felix.library.exception;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: Aufgabe5bis12.exception</p>
 * <p>Datei: DuplicateEntryException.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1</p>
 */

public class DuplicateEntryException extends Exception {

    /**
     * <p>Define custom exception without message.</p>
     */

    public DuplicateEntryException() {}

    /**
     * <p>Define custom exception with message.</p>
     * @param message
     */

    public DuplicateEntryException(String message) {
        super(message);
    }
}
