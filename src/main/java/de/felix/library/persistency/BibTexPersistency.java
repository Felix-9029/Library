package de.felix.library.persistency;

import de.felix.library.MediaStorage;
import de.felix.library.medium.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.persistency</p>
 * <p>Datei: BibTexPersistency.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 2.1</p>
 */

public class BibTexPersistency implements Persistency {

    /**
     * <p>Method for saving data into a file with the BibTex-format.</p>
     * @param mediaStorage Object that contains the data
     * @param filename name for the file on the disk
     */

    @Override
    public void save(MediaStorage mediaStorage, String filename) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Medium medium : mediaStorage) {
            stringBuilder.append(medium.toBibTex().toString() + System.lineSeparator());
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename, StandardCharsets.UTF_8))) {
            bufferedWriter.write(stringBuilder.toString());
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File or path does not exist!");
        }
        catch (Exception e) {
            throw new IllegalArgumentException("An error appeared while creating the representation!");
        }
    }

    /**
     * <p>Method for loading data from a BibTex-encoded file.</p>
     * @param filename name for the file on the disk
     * @return Object that contains the data
     */

    @Override
    public MediaStorage load(String filename) {
        ArrayList<String> bibTexRawArr = new ArrayList<>();
        // Read File
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bibTexRawArr.add(line);
            }
        }
        catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File or path does not exist!");
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Error while reading file!");
        }
        MediaStorage mediaStorage = new MediaStorage();
        for (String bibTexRaw : bibTexRawArr) {
            mediaStorage.addMedium(bibTexDecode(bibTexRaw));
        }
        return mediaStorage;
    }

    public String getRegexType() {
        return "@(.[a-zA-Z]*)\\{";
    }

    public String getRegexValue() {
        return "([a-zA-Z]*) = (\\{(.+?)}|[0-9]+)";
    }

    /**
     * <p>Method to decode a BibTex-string into a Medium.</p>
     * @param encodedString String with the BibTex-string
     * @return Medium with values from encodedString
     */

    public Medium bibTexDecode(String encodedString) {

        String[] bibTex = new String[6];
        boolean[] check = {false, false, false, false, false};

        try {
            // Extraction of media-type
            Pattern patternType = Pattern.compile(getRegexType(), Pattern.CASE_INSENSITIVE);
            Matcher matcherType = patternType.matcher(encodedString);
            while (matcherType.find()) {
                bibTex[0] = matcherType.group(1);
            }

            // Extraction of media-values
            Pattern patternValue = Pattern.compile(getRegexValue(), Pattern.CASE_INSENSITIVE);
            Matcher matcherValue = patternValue.matcher(encodedString);
            while (matcherValue.find()) {
                String value = matcherValue.group(2).replace("{", "").replace("}", "");
                switch (matcherValue.group(1)) {
                    case "title" -> {
                        bibTex[1] = value;
                        check[0] = true;
                    }
                    case "label", "issn", "year", "URL" -> {
                        bibTex[2] = value;
                        check[1] = true;
                    }
                    case "artist", "volume", "publisher", "timestamp" -> {
                        bibTex[3] = value;
                        check[2] = true;
                    }
                    case "number", "isbn", "contributor" -> {
                        bibTex[4] = value;
                        check[3] = true;
                    }
                    case "author" -> {
                        bibTex[5] = value;
                        check[4] = true;
                    }
                    default -> throw new IllegalArgumentException(matcherValue.group(1) + " does not exist as an argument!");
                }
            }
        }
        catch (NullPointerException e) {
            throw new IllegalArgumentException("Bad String was overhanded and is unable to be parsed!");
        }

        // check if Exception should be thrown
        exceptionsHandling(bibTex, check);

        try {
            switch (bibTex[0]) {
                case "cd" -> {
                    return new CD(bibTex[1], bibTex[2], bibTex[3]);
                }
                case "journal" -> {
                    return new Journal(bibTex[1], bibTex[2], Integer.parseInt(bibTex[3]), Integer.parseInt(bibTex[4]));
                }
                case "book" -> {
                    return new Book(bibTex[1], Integer.parseInt(bibTex[2]), bibTex[3], bibTex[4], bibTex[5]);
                }
                case "elMed" -> {
                    return new ElectronicMedium(bibTex[1], bibTex[2]);
                }
                case "wikiBook" -> {
                    return new WikiBook(bibTex[1]);
                }
                default -> throw new IllegalArgumentException("Medium-Type does not exist!");
            }
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error while adding media to arraylist!");
        }
    }

    /**
     * <p>Method for Exception handling.</p>
     * @param bibTex Media-Type
     * @param check Array with variable-check
     */

    public void exceptionsHandling(String[] bibTex, boolean[] check) {
        // TODO better Exception handling
        if (bibTex[0] == null) {
            throw new IllegalArgumentException("Unable to parse medium with empty type!");
        }
        if (!check[0]) {
            throw new IllegalArgumentException("Medium " + bibTex[0] + " does not exist!");
        }
        switch (bibTex[0]) {
            case "cd" -> {
                if (!check[1]) {
                    throw new IllegalArgumentException("Label of cd does not exist!");
                }
                if (!check[2]) {
                    throw new IllegalArgumentException("Artist of cd does not exist!");
                }
            }
            case "journal" -> {
                if (!check[1]) {
                    throw new IllegalArgumentException("ISSN of journal does not exist!");
                }
                if (!check[2]) {
                    throw new IllegalArgumentException("Volume of journal does not exist!");
                }
                if (!check[3]) {
                    throw new IllegalArgumentException("Number of journal does not exist!");
                }
            }
            case "book" -> {
                if (!check[1]) {
                    throw new IllegalArgumentException("Year of book does not exist!");
                }
                if (!check[2]) {
                    throw new IllegalArgumentException("Publisher of book does not exist!");
                }
                if (!check[3]) {
                    throw new IllegalArgumentException("ISBN of book does not exist!");
                }
                if (!check[4]) {
                    throw new IllegalArgumentException("author of book does not exist!");
                }
            }
            case "elMed" -> {
                if (!check[1]) {
                    throw new IllegalArgumentException("URL of elMed does not exist!");
                }
            }
            case "wikiBook" -> {
                if (!check[1]) {
                    throw new IllegalArgumentException("URL of wikiBook does not exist!");
                }
                if (!check[2]) {
                    throw new IllegalArgumentException("Timestamp of wikiBook does not exist!");
                }
                if (!check[3]) {
                    throw new IllegalArgumentException("Contributor of wikiBook does not exist!");
                }
            }
            default -> throw new IllegalArgumentException("You somehow wrote a wrong value into the first index of the array. Congrats on that!");
        }
    }
}