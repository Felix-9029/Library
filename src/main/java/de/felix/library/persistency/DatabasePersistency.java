package de.felix.library.persistency;

import de.felix.library.MediaStorage;
import de.felix.library.medium.*;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.persistency</p>
 * <p>Datei: DatabasePersistency.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.1</p>
 */

public class DatabasePersistency implements Persistency {

    /**
     * <p>Method for saving data into a sqlite database file.</p>
     *
     * @param mediaStorage        Object that contains the data
     * @param filename name for the file on the disk
     */

    @Override
    public void save(MediaStorage mediaStorage, String filename) {
        String database = "jdbc:sqlite:" + filename;
        try (Connection connection = DriverManager.getConnection(database)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE IF EXISTS Medium");
            statement.executeUpdate("CREATE TABLE Medium (Type CHAR(20), Title VARCHAR(200), argument1 VARCHAR(200), argument2 VARCHAR(200), argument3 VARCHAR(200), argument4 VARCHAR(200))");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Medium VALUES (?, ?, ?, ?, ?, ?)");
            for (Medium medium : mediaStorage) {
                if (medium instanceof Book book) {
                    preparedStatement.setString(1, "book");
                    preparedStatement.setString(2, book.getTitle());
                    preparedStatement.setString(3, String.valueOf(book.getYear()));
                    preparedStatement.setString(4, book.getPublisher());
                    preparedStatement.setString(5, book.getISBN());
                    preparedStatement.setString(6, book.getAuthor());
                }
                else if (medium instanceof CD cd) {
                    preparedStatement.setString(1, "cd");
                    preparedStatement.setString(2, cd.getTitle());
                    preparedStatement.setString(3, cd.getLabel());
                    preparedStatement.setString(4, cd.getArtist());
                    preparedStatement.setString(5, null);
                    preparedStatement.setString(6, null);
                }
                else if (medium instanceof ElectronicMedium electronicMedium) {
                    preparedStatement.setString(1, "elMed");
                    preparedStatement.setString(2, electronicMedium.getTitle());
                    preparedStatement.setString(3, electronicMedium.getURL());
                    preparedStatement.setString(4, null);
                    preparedStatement.setString(5, null);
                    preparedStatement.setString(6, null);
                }
                else if (medium instanceof Journal journal) {
                    preparedStatement.setString(1, "journal");
                    preparedStatement.setString(2, journal.getTitle());
                    preparedStatement.setString(3, journal.getISSN());
                    preparedStatement.setString(4, String.valueOf(journal.getVolume()));
                    preparedStatement.setString(5, String.valueOf(journal.getNumber()));
                    preparedStatement.setString(6, null);
                }
                // TODO
                else if (medium instanceof WikiBook wikiBook) {
                    preparedStatement.setString(1, "wikiBook");
                    preparedStatement.setString(2, wikiBook.getTitle());
                    preparedStatement.setString(3, wikiBook.getURL());
                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    preparedStatement.setString(4, simpleDate.format(wikiBook.getTimestamp()));
                    preparedStatement.setString(5, wikiBook.getContributor());
                    preparedStatement.setString(6, null);
                }
                else {
                    throw new IllegalArgumentException("Medium type " + medium.getClass().getSimpleName() + " does not exist!");
                }
                preparedStatement.execute();
            }
            preparedStatement.close();
            statement.close();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException("Error while writing database!");
        }
    }

    /**
     * <p>Method for loading data from a sqlite database file.</p>
     *
     * @param filename name for the file on the disk
     * @return Object that contains the data
     */

    @Override
    public MediaStorage load(String filename) {
        String database = "jdbc:sqlite:" + filename;
        MediaStorage mediaStorage = new MediaStorage();
        try (Connection connection = DriverManager.getConnection(database)) {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Medium");
            while (result.next()) {
                String[] values = new String[6];
                values[0] = result.getString(1);
                values[1] = result.getString(2);
                values[2] = result.getString(3);
                values[3] = result.getString(4);
                values[4] = result.getString(5);
                values[5] = result.getString(6);
                switch (values[0]) {
                    case "cd" -> {
                        mediaStorage.addMedium(new CD(values[1], values[2], values[3]));
                    }
                    case "journal" -> {
                        mediaStorage.addMedium(new Journal(values[1], values[2], Integer.parseInt(values[3]), Integer.parseInt(values[4])));
                    }
                    case "book" -> {
                        mediaStorage.addMedium(new Book(values[1], Integer.parseInt(values[2]), values[3], values[4], values[5]));
                    }
                    case "elMed" -> {
                        mediaStorage.addMedium(new ElectronicMedium(values[1], values[2]));
                    }
                    case "wikiBook" -> {
                        mediaStorage.addMedium(new WikiBook(values[1]));
                    }
                    default -> throw new IllegalArgumentException("Medium-Type does not exist!");
                }
            }
            result.close();
            statement.close();
        }
        catch (SQLException e) {
            throw new IllegalArgumentException("Error while reading database!");
        }
        return mediaStorage;
    }
}