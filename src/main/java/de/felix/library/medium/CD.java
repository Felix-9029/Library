package de.felix.library.medium;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.medium</p>
 * <p>Datei: CD.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.3</p>
 */

public class CD extends Medium {

    private String _label;
    private String _artist;

    /**
     * <p>Define constructor.</p>
     */

    public CD(String _title, String _label, String _artist) {
        super(_title);
        setLabel(_label);
        setArtist(_artist);
    }

    public String getLabel() {
        return this._label;
    }

    public String getArtist() {
        return this._artist;
    }

    public void setLabel(String _label) {
        if (!_label.isEmpty()) {
            this._label = _label;
        }
        else {
            throw new IllegalArgumentException("The label-value is empty!");
        }
    }

    public void setArtist(String _artist) {
        if (!_artist.isEmpty()) {
            this._artist = _artist;
        }
        else {
            throw new IllegalArgumentException("The artist-value is empty!");
        }
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * <p>Method to generate representation.</p>
     */

    @Override
    public StringBuilder calculateRepresentation() {
        try {
            StringBuilder objectString = super.calculateRepresentation();
            objectString.append("Label: " + getLabel() + System.lineSeparator());
            objectString.append("Künstler: " + getArtist() + System.lineSeparator());
            return objectString;
        }
        catch (NullPointerException e) {
            System.err.println("Unable to create representation!");
        }
        return null;
    }

    /**
     * <p>Method to generate BibTex-String.</p>
     */

    @Override
    public StringBuilder toBibTex() {
        try {
            StringBuilder objectString = super.toBibTex();
            objectString.append("cd{");
            objectString.append("title = {" + getTitle() + "}, ");
            objectString.append("label = {" + getLabel() + "}, ");
            objectString.append("artist = {" + getArtist() + "}}");
            return objectString;
        }
        catch (NullPointerException e) {
            System.err.println("Unable to create BibTex-Sting!");
        }
        return null;
    }
}