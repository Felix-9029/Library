package de.felix.library.medium;

import de.felix.library.WikiBookContentHandler;
import de.felix.library.exception.BookNotFoundException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.medium</p>
 * <p>Datei: WikiBook.java</p>
 * <p>Datum: 23.12.2021</p>
 * <p>Version: 1.1</p>
 */

public class WikiBook extends Medium {

    private String _url;
    private Date _timestamp;
    private String _contributor;
    private ArrayList<String> _shelves = new ArrayList<>();
    private ArrayList<String> _chapters = new ArrayList<>();

    /**
     * <p>Define constructor.</p>
     */

    public WikiBook(String _bookName) {
        super();
        WikiBook wikiBook = wikiBookConnection(_bookName.replace(" ", "_"));
        setTitle(wikiBook.getTitle());
        setURL(wikiBook.getURL());
        setTimestamp(wikiBook.getTimestamp());
        setContributor(wikiBook.getContributor());
        setShelves(wikiBook.getShelves());
        setChapters(wikiBook.getChapters());
    }

    public WikiBook(String _title, String _url, Date _timestamp, String _contributor, ArrayList<String> _shelves, ArrayList<String> _chapters) {
        super(_title);
        setURL(_url);
        setTimestamp(_timestamp);
        setContributor(_contributor);
        setShelves(_shelves);
        setChapters(_chapters);
    }

    public Date getTimestamp() {
        return _timestamp;
    }

    public void setTimestamp(Date _timestamp) {
        this._timestamp = _timestamp;
    }

    public String getContributor() {
        return _contributor;
    }

    public void setContributor(String _contributor) {
        this._contributor = _contributor;
    }

    public String getURL() {
        return _url;
    }

    public void setURL(String _url) {
        if (ElectronicMedium.checkURL(_url)) {
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
     * <p>Method to get the whole page-tag only as a String for further processing.</p>
     * @param inputString
     * @return
     * @throws BookNotFoundException
     */

    public static String getPage(String inputString) throws BookNotFoundException {
        if (inputString.contains("<page>") && inputString.contains("</page>")) {
            int personStart = inputString.indexOf("<page>") + 8;
            int personEnd = inputString.indexOf("</page>");
            return inputString.substring(personStart, personEnd);
        }
        else {
            throw new BookNotFoundException("Unable to find Book!");
        }
    }

    public ArrayList<String> getShelves() {
        return _shelves;
    }

    public void setShelves(ArrayList<String> _shelves) {
        this._shelves = _shelves;
    }

    public ArrayList<String> getChapters() {
        return _chapters;
    }

    public void setChapters(ArrayList<String> _chapters) {
        this._chapters = _chapters;
    }

    /**
     * <p>Method to generate representation.</p>
     */

    @Override
    public StringBuilder calculateRepresentation() {
        try {
            StringBuilder objectString = super.calculateRepresentation();
            if(getShelves().size() > 1) {
                for (int i = 0; i < getShelves().size(); i++) {
                    objectString.append(getShelves().get(i));
                    if (getShelves().size() == i + 1) {
                        objectString.append(System.lineSeparator());
                    }
                    else {
                        objectString.append(", ");
                    }
                }
            }
            else {
                objectString.append("Regal: " + getShelves().get(0) + System.lineSeparator());
            }
            objectString.append("Kapitel:" + System.lineSeparator());
            for(int i = 0; i < getChapters().size(); i++){
                objectString.append((i + 1)+ " " + getChapters().get(i) + System.lineSeparator());
            }
            try {
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat simpleLocalDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                simpleLocalDate.setTimeZone(TimeZone.getTimeZone("utc"));
                Date localDate = simpleLocalDate.parse(simpleDate.format(getTimestamp()));
                SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                objectString.append("Letzte Änderung: " + formattedDate.format(localDate) + " um " + time.format(localDate) + " Uhr" + System.lineSeparator());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            objectString.append("Urheber: " + getContributor() + System.lineSeparator());
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
            objectString.append("wikiBook{");
            objectString.append("title = {" + getTitle() + "}, ");
            objectString.append("URL = " + getURL() + ", ");
            objectString.append("timestamp = {" + getTimestamp() + "}, ");
            objectString.append("contributor = {" + getContributor() + "}}");
            return objectString;
        }
        catch (NullPointerException e) {
            System.err.println("Unable to create BibTex-Sting!");
        }
        return null;
    }

    /**
     * <p>Method to connect to a xml from the internet and parse the data.</p>
     * @param bookName Name of the book in the database.
     * @return
     */

    public WikiBook wikiBookConnection(String bookName) {
        WikiBookContentHandler wikiBookContentHandler = new WikiBookContentHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        StringBuilder xml = new StringBuilder();

        try {
            URL url = new URL("https://de.wikibooks.org/wiki/Spezial:Exportieren/" + bookName);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setDoInput(true);

            // check for redirect
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream input = new BufferedInputStream(inputStream);
            byte[] rssContent = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(rssContent)) != -1) {
                xml.append(new String(rssContent, 0, bytesRead));
            }
            String xmlString = xml.toString();
            Pattern redirectPattern = Pattern.compile("<redirect title=\"(.+?)\"");
            Matcher redirectMatcher = redirectPattern.matcher(getPage(xmlString));
            if (redirectMatcher.find()) {
                url = new URL("https://de.wikibooks.org/wiki/Spezial:Exportieren/" + redirectMatcher.group(1));
                input.close();
            }
            inputStream.close();
            input.close();

            SAXParser parser = factory.newSAXParser();
            parser.parse(String.valueOf(url), wikiBookContentHandler);

            String[] values = wikiBookContentHandler.getWikiBook();

            ArrayList<String> shelves = new ArrayList<>();
            ArrayList<String> chapters = new ArrayList<>();

            Pattern patternChapters = Pattern.compile("== (.+?) ==");
            Pattern patternShelves = Pattern.compile("\\{\\{Regal[ ]*\\|[ ]*(?:ort=)?([^}]+)}}");
            for (String string : String.join("", values[4]).split("\n")) {
                Matcher matcherShelves = patternShelves.matcher(string);
                if (matcherShelves.find()) {
                    shelves.addAll(List.of(matcherShelves.group(1).split("\\|")));
                    continue;
                }
                Matcher matcherChapters = patternChapters.matcher(string);
                if (matcherChapters.find()) {
                    chapters.add(matcherChapters.group(1));
                }
            }

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return new WikiBook(values[0], String.valueOf(url), simpleDate.parse(values[2]), values[3], shelves, chapters);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Something went wrong!");
    }
}
