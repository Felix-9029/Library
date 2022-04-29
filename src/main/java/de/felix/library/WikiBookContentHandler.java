package de.felix.library;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library</p>
 * <p>Datei: WikiBookContentHandler.java</p>
 * <p>Datum: 24.11.2021</p>
 * <p>Version: 1</p>
 */

public class WikiBookContentHandler extends DefaultHandler implements ContentHandler {

    StringBuilder currentText = new StringBuilder();
    private String currentValue;

    private String[] wikiBook = new String[5];

    public void characters(char[] ch, int start, int length) {
        currentValue = new String(ch, start, length);
        currentText.append(ch, start, length);
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) {

    }

    public void endElement(String uri, String localName, String qName) {

        if (localName.equals("title")) {
            wikiBook[0] = currentValue;
        }

        if (localName.equals("timestamp")) {
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            simpleDate.setTimeZone(TimeZone.getTimeZone("utc"));
            try {
                Date date = simpleDate.parse(currentValue);
                wikiBook[2] = simpleDate.format(date);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (localName.equals("username")) {
            wikiBook[3] = currentValue;
        }

        if (localName.equals("ip")) {
            wikiBook[3] = currentValue;
        }

        if (localName.equals("text")) {
            wikiBook[4] = currentText.toString().trim();
            currentText.setLength(0);
        }
    }

    public String[] getWikiBook() {
        return wikiBook;
    }
}