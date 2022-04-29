package de.felix.library.exception;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: Aufgabe5bis12.exception</p>
 * <p>Datei: DuplicateEntryException.java</p>
 * <p>Datum: 29.12.2021</p>
 * <p>Version: 1</p>
 */

public class MyWebException extends Exception {

    /**
     * <p>Define custom exception without message.</p>
     */

    public MyWebException() {}

    /**
     * <p>Define custom exception with message.</p>
     * @param message
     */

    public MyWebException(String message) {
        super(message);
    }
}
