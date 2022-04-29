package de.felix.library.medium;

import java.io.Serializable;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.medium</p>
 * <p>Datei: Medium.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.3</p>
 */

public abstract class Medium implements Comparable<Medium>, Serializable {

    private String _title;

    public Medium() {}

    /**
     * <p>Define constructor.</p>
     */

    public Medium(String _title) {
        setTitle(_title);
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        try {
            if (title.length() > 0) {
                this._title = title;
            }
            else {
                throw new IllegalArgumentException("The title-value is to short!");
            }
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("The title is null!");
        }
    }

    /**
     * <p>Define abstract method for calculating representation that need to be present in each subclass.</p>
     */

    public StringBuilder calculateRepresentation() {
        try {
            StringBuilder objectString = new StringBuilder();
            objectString.append("Titel: " + getTitle() + System.lineSeparator());
            return objectString;
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("Unable to create representation!");
        }
    }

    /**
     * <p>Define abstract method to encode a Medium into a BibTex-string.</p>
     */

    public StringBuilder toBibTex() {
        try {
            StringBuilder objectString = new StringBuilder();
            objectString.append("@");
            return objectString;
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("Unable to create BibTex-Sting!");
        }
    }

    /**
     * <p>Compare titles of two Medium's.</p>
     * @param medium Medium that should be compared with
     */

    @Override
    public int compareTo(Medium medium) {
        return this.getTitle().compareToIgnoreCase(medium.getTitle());
    }

//    public boolean isValid() {
//        throw new UnsupportedOperationException();
//    }
}