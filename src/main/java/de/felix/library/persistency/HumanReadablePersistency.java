package de.felix.library.persistency;

import de.felix.library.MediaStorage;
import de.felix.library.medium.Medium;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.persistency</p>
 * <p>Datei: HumanReadablePersistency.java</p>
 * <p>Datum: 02.11.2021</p>
 * <p>Version: 1</p>
 */

public class HumanReadablePersistency implements Persistency {

    /**
     * <p>Method for saving data into a readable file.</p>
     * @param mediaStorage Object that contains the data
     * @param filename name for the file on the disk
     */

    @Override
    public void save(MediaStorage mediaStorage, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Medium medium : mediaStorage) {
            stringBuilder.append(medium.calculateRepresentation() + System.lineSeparator());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
            writer.write(stringBuilder.toString());
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File or path does not exist!");
        }
        catch (Exception e) {
            throw new IllegalArgumentException("An error appeared while creating the representation!");
        }
    }

    /**
     * <p>Method for loading data from a readable file.</p>
     * @param filename name for the file on the disk
     * @return Object that contains the data
     */

    @Override
    public MediaStorage load(String filename) {
        throw new UnsupportedOperationException();
    }
}