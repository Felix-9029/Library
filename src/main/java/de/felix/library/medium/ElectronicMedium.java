package de.felix.library.medium;

import java.net.URL;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.medium</p>
 * <p>Datei: ElectronicMedium.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.2</p>
 */

public class ElectronicMedium extends Medium {

    private String _url;

    /**
     * <p>Define constructor.</p>
     */

    public ElectronicMedium(String _title, String _url) {
        super(_title);
        setURL(_url);
    }

    public String getURL() {
        return this._url;
    }

    public void setURL(String _url) {
        if (checkURL(_url)) {
            this._url = _url;
        }
        else {
            throw new IllegalStateException("The URL is wrong or in a wrong format.");
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
     * <p>Method to check URL for correctness.</p>
     */

    public static boolean checkURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    /**
     * <p>Method to generate representation.</p>
     */

    @Override
    public StringBuilder calculateRepresentation() {
        try {
            StringBuilder objectString = super.calculateRepresentation();
            objectString.append("URL: " + getURL() + System.lineSeparator());
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
            objectString.append("elMed{");
            objectString.append("title = {" + getTitle() + "}, ");
            objectString.append("URL = {" + getURL() + "}}");
            return objectString;
        }
        catch (NullPointerException e) {
            System.err.println("Unable to create BibTex-Sting!");
        }
        return null;
    }
}