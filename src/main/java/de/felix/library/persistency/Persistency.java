package de.felix.library.persistency;

import de.felix.library.MediaStorage;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.persistency</p>
 * <p>Datei: Persistency.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.1</p>
 */

public interface Persistency {

    /**
     * <p>Implementation of necessary save method for inheriting classes.</p>
     * @param mediaStorage Object that contains the data
     * @param filename name for the file on the disk
     */

    void save(MediaStorage mediaStorage, String filename);

    /**
     * <p>Implementation of necessary load method for inheriting classes.</p>
     * @param filename name for the file on the disk
     * @return Object that contains the data
     */

    MediaStorage load(String filename);
}