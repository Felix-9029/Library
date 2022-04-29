package de.felix.library.medium;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.medium</p>
 * <p>Datei: Journal.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.3</p>
 */

public class Journal extends Medium {

    private String _issn;
    private int _volume;
    private int _number;

    /**
     * <p>Define constructor.</p>
     */

    public Journal(String _title, String _issn, int _volume, int _number) {
        super(_title);
        setISSN(_issn);
        setVolume(_volume);
        setNumber(_number);
    }

    public String getISSN() {
        return this._issn;
    }

    public int getVolume() {
        return this._volume;
    }

    public int getNumber() {
        return this._number;
    }

    public void setISSN(String _issn) {
        if (!_issn.isEmpty()) {
            this._issn = _issn;
        }
        else {
            throw new IllegalArgumentException("The issn-value is empty!");
        }
    }

    public void setVolume(int _volume) {
        this._volume = _volume;
    }

    public void setNumber(int _number) {
        this._number = _number;
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
            objectString.append("ISSN: " + getISSN() + System.lineSeparator());
            objectString.append("Volume: " + getVolume() + System.lineSeparator());
            objectString.append("Nummer: " + getNumber() + System.lineSeparator());
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
            objectString.append("journal{");
            objectString.append("title = {" + getTitle() + "}, ");
            objectString.append("issn = {" + getISSN() + "}, ");
            objectString.append("volume = " + getVolume() + ", ");
            objectString.append("number = " + getNumber() + "}");
            return objectString;
        }
        catch (NullPointerException e) {
            System.err.println("Unable to create BibTex-Sting!");
        }
        return null;
    }
}