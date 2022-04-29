package de.felix.library;

import de.felix.library.medium.WikiBook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library</p>
 * <p>Datei: WikipediaContributorRequest.java</p>
 * <p>Datum: 28.11.2021</p>
 * <p>Version: 1</p>
 */

public class WikipediaContributorRequest {

    /**
     * <p>Method that parses the book name, the latest edit and the contributor from a wikibook xml.</p>
     *
     * @param args
     */

    public static void main(String[] args) {
        if (args.length != 0 && args[0] != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(args[0]);
            for (int x = 1; x < args.length; x++) {
                stringBuilder.append("_");
                stringBuilder.append(args[x]);
            }
            WikiBook wikiBook = new WikiBook(stringBuilder.toString());
            try {
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat simpleLocalDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                simpleLocalDate.setTimeZone(TimeZone.getTimeZone("utc"));
                Date localDate = simpleLocalDate.parse(simpleDate.format(wikiBook.getTimestamp()));
                SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                System.out.println("Suche nach: " + wikiBook.getTitle());
                System.out.println("Letzte Änderung: " + formattedDate.format(localDate) + " um " + time.format(localDate) + " Uhr");
                System.out.println("Urheber: " + wikiBook.getContributor());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            throw new IllegalArgumentException("No arguments used!");
        }
    }
}
