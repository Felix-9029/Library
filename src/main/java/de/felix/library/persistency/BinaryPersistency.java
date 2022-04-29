package de.felix.library.persistency;

import de.felix.library.MediaStorage;

import java.io.*;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.persistency</p>
 * <p>Datei: BinaryPersistency.java</p>
 * <p>Datum: 02.11.2021</p>
 * <p>Version: 1</p>
 */

public class BinaryPersistency implements Persistency {

    /**
     * <p>Method for saving data into a binary file.</p>
     * @param mediaStorage Object that contains the data
     * @param filename name for the file on the disk
     */

    @Override
    public void save(MediaStorage mediaStorage, String filename) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filename)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(mediaStorage);
            fileOutputStream.close();
            objectOutputStream.close();
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File or path does not exist!");
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Unable to serialize!");
        }
    }

    /**
     * <p>Method for loading data from a binary file.</p>
     * @param filename name for the file on the disk
     * @return Object that contains the data
     */

    @Override
    public MediaStorage load(String filename) {
        try (FileInputStream fileInputStream = new FileInputStream(filename)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            MediaStorage mediaStorage = (MediaStorage) objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return mediaStorage;
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File or path does not exist!");
        }
        catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Error while reading file!");
        }
    }
}