package de.felix.library;

import de.felix.library.exception.DuplicateEntryException;
import de.felix.library.medium.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library</p>
 * <p>Datei: MediaStorage.java</p>
 * <p>Datum: 20.12.2021</p>
 * <p>Version: 1.1</p>
 */

public class MediaStorage implements Iterable<Medium>, Serializable {

    private ArrayList<Medium> media = new ArrayList<>();
    private boolean _sorted;
    private boolean _orderUp;

    // TODO move Comparator to compareTo-method
    protected static final Comparator<Medium> compareByType = Comparator.comparing((Medium medium) -> medium.getClass().getName());
    protected static final Comparator<Medium> compareByNameAndType = Comparator.comparing(Medium::getTitle).thenComparing((Medium medium) -> medium.getClass().getName());

    /**
     * <p>Method for adding data to the ArrayList.</p>
     * @param medium Medium that will be added
     * @return True if successful
     */

    public boolean addMedium(Medium medium) {
        media.add(medium);
        System.out.println(getMedia().get(getMedia().size() - 1).calculateRepresentation());
        setSorted(false);
        return true;
    }

    /**
     * <p>Method for adding data to the ArrayList.</p>
     * @param title title of Medium that will be dropped
     * @return True if successful
     */

    public boolean dropMedium(String title) throws DuplicateEntryException {
        List<Medium> hits = findMedium(title);
        if (hits.size() > 1) {
            throw new DuplicateEntryException("Duplicate entries detected!");
        }
        else {
            media.remove(hits.get(0));
            return true;
        }
    }

    /**
     * <p>List of deletable types.</p>
     */

    public enum MediumType {
        ALL,
        BOOK,
        CD,
        ELECTRONICMEDIUM,
        JOURNAL,
        WIKIBOOK
    }

    /**
     * <p>Method for adding data to the ArrayList.</p>
     * @param title Title of Medium that will be dropped
     * @param mediumType Type of Medium that will be dropped
     * @return True if successful
     */

    public boolean dropMedium(String title, MediumType mediumType) {
        List<Medium> hits = findMedium(title);
        switch (mediumType) {
            case ALL -> {
                for (Medium hit : hits) {
                    media.remove(hit);
                }
                return true;
            }
            case CD -> {
                for (Medium hit : hits) {
                    if (hit instanceof CD) {
                        media.remove(hit);
                    }
                }
                return true;
            }
            case BOOK -> {
                for (Medium hit : hits) {
                    if (hit instanceof Book) {
                        media.remove(hit);
                    }
                }
                return true;
            }
            case JOURNAL -> {
                for (Medium hit : hits) {
                    if (hit instanceof Journal) {
                        media.remove(hit);
                    }
                }
                return true;
            }
            case ELECTRONICMEDIUM -> {
                for (Medium hit : hits) {
                    if (hit instanceof ElectronicMedium) {
                        media.remove(hit);
                    }
                }
                return true;
            }
            case WIKIBOOK -> {
                for (Medium hit : hits) {
                    if (hit instanceof WikiBook) {
                        media.remove(hit);
                    }
                }
                return true;
            }
            default -> {
                throw new IllegalArgumentException("Wrong Medium type!");
            }
        }
    }

    /**
     * <p>Check if Medium with a specific title exist.</p>
     * @param title Title of Medium that is searched
     * @return List with hits
     */

    public List<Medium> findMedium(String title) {
        List<Medium> hits = new ArrayList<>();
        for (Medium medium : media) {
            if (medium.getTitle().equals(title)) {
                hits.add(medium);
            }
        }
        if (hits.size() > 0) {
            return hits;
        }
        else {
            throw new IllegalArgumentException("Medium with title " + title + " does not exist!");
        }
    }

    /**
     * <p>List of sorting methods.</p>
     */

    public enum SortOrder {
        UP,
        DOWN
    }

    /**
     * <p>Method to sort all entries in MediaStorage with a defined order.</p>
     * @param order Order of sorting method
     * @return True if successful
     */

    public boolean sort(SortOrder order) {
        if (order.equals(SortOrder.UP) && (!isSorted() || !is_orderUp()) && media.size() > 1) {
            media.sort(compareByNameAndType);
            setSorted(true);
            setOrderUp(true);
            return true;
        }
        else if (order.equals(SortOrder.DOWN) && (!isSorted() || is_orderUp()) && media.size() > 1) {
            media.sort(compareByNameAndType.reversed());
            setSorted(true);
            setOrderUp(false);
            return true;
        }
        else if (!order.equals(SortOrder.UP) && !order.equals(SortOrder.DOWN)) {
            throw new IllegalArgumentException("This sort method does not exist!");
        }
        else {
            return false;
        }
    }

    public boolean isSorted() {
        return _sorted;
    }

    public void setSorted(boolean _sorted) {
        this._sorted = _sorted;
    }

    public boolean is_orderUp() {
        return _orderUp;
    }

    public void setOrderUp(boolean _orderUp) {
        this._orderUp = _orderUp;
    }

    public ArrayList<Medium> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Medium> media) {
        this.media = media;
    }

    /**
     * <p>Make MediaStorage iterable.</p>
     * @return List of type Medium
     */

    @Override
    public Iterator<Medium> iterator() {
        return media.iterator();
    }
}