package de.felix.library.GUI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.GUI</p>
 * <p>Datei: WikiBooksUtility.java</p>
 * <p>Datum: 29.12.2021</p>
 * <p>Version: 1</p>
 */

public class WikiBooksUtility {

    /**
     * <p>Method to create a URL-String from a book name.</p>
     * @param book
     * @return
     */

    public static String getURL(String book) {
        book = book.replace(" ", "_");
        String url = "https://de.wikibooks.org/wiki/" + book;
        return url;
    }

    /**
     * <p>Method to get synonyms for a word.</p>
     * @param word
     * @return
     * @throws ParseException
     * @throws IOException
     */

    public static ArrayList<String> getSynonyms(String word) throws ParseException, IOException {
        String BasicUrl = "http://api.corpora.uni-leipzig.de/ws/similarity/";
        String Corpus = "deu_news_2012_1M";
        String requesttype = "/coocsim/";
        String Parameter = "?minSim=0.1&limit=50";
        URL myURL;
        ArrayList<String> synonymsList = new ArrayList<>();
        myURL = new URL(BasicUrl + Corpus + requesttype + word + Parameter);
        JSONParser jsonParser = new JSONParser();
        String jsonResponse = streamToString(myURL.openStream());
// den gelieferten String in ein Array parsen
        JSONArray jsonResponseArr = (JSONArray) jsonParser.parse(jsonResponse);

        if (!jsonResponseArr.isEmpty()) {
// das erste Element in diesem Array
            JSONObject firstEntry = (JSONObject) jsonResponseArr.get(0);
// aus dem Element den Container für das Synonym beschaffen
            JSONObject wordContainer = (JSONObject) firstEntry.get("word");
// aus dem Container das Synonym beschaffen
            String synonym = (String) wordContainer.get("word");
// alle abgefragten Synonyme
            for (Object el : jsonResponseArr) {
                wordContainer = (JSONObject) ((JSONObject) el).get("word");
                synonym = (String) wordContainer.get("word");
                synonymsList.add(synonym);
            }
            Collections.sort(synonymsList);
        }
        else {
            synonymsList.add("<keine>");
        }
        return synonymsList;
    }

    /**
     * <p>Reads InputStream's contents into String.</p>
     *
     * @param is - the InputStream
     * @return String representing is' contents
     * @throws IOException
     */
    public static String streamToString(InputStream is) throws IOException {
        String result = "";
// other options: https://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
        try (Scanner s = new Scanner(is)) {
            s.useDelimiter("\\A");
            result = s.hasNext() ? s.next() : "";
            is.close();
        }
        return result;
    }
}
