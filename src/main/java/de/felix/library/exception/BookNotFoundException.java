package de.felix.library.exception;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: Aufgabe5bis12.exception</p>
 * <p>Datei: BookNotFoundException.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1</p>
 */

public class BookNotFoundException extends Exception {

    /**
     * <p>Define custom exception without message.</p>
     */

    public BookNotFoundException() {}

    /**
     * <p>Define custom exception with message.</p>
     * @param message
     */

    public BookNotFoundException(String message) {
        super(message);
    }
}
