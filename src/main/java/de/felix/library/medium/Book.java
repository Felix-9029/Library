package de.felix.library.medium;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.medium</p>
 * <p>Datei: Book.java</p>
 * <p>Datum: 27.11.2021</p>
 * <p>Version: 1.3</p>
 */

public class Book extends Medium {

    private int _year;
    private String _publisher;
    private String _isbn;
    private String _author;

    /**
     * <p>Define constructor.</p>
     */

    public Book(String _title, int _year, String _publisher, String _isbn, String _author) {
        super(_title);
        setYear(_year);
        setPublisher(_publisher);
        setISBN(_isbn);
        setAuthor(_author);
    }

    public int getYear() {
        return this._year;
    }

    public String getPublisher() {
        return this._publisher;
    }

    public String getISBN() {
        return this._isbn;
    }

    public String getAuthor() {
        return this._author;
    }

    public void setYear(int _year) {
        this._year = _year;
    }

    public void setPublisher(String _publisher) {
        if (!_publisher.isEmpty()) {
            this._publisher = _publisher;
        }
        else {
            throw new IllegalArgumentException("The publisher-value is empty!");
        }
    }

    public void setISBN(String _isbn) {

        String _numIsbn = _isbn.replace("-", "");
        int[] isbnArr = new int[_numIsbn.length()];
        // Split string into an array of characters
        char[] numberStrArray = _numIsbn.toCharArray();
        // Convert array of characters to an array of integers.
        for (int i = 0; i < _numIsbn.length(); i++) {
            isbnArr[i] = Character.getNumericValue(numberStrArray[i]);
        }
        boolean check = false;
        if (isbnArr.length == 13 ) {
            check = checkISBN13(isbnArr);
            if (check) {
                this._isbn = _isbn;
            }
            else {
                throw new IllegalArgumentException("The ISBN-number is incorrect!");
            }
        }
        else if (isbnArr.length == 10) {
            check = checkISBN10(isbnArr);
            if (check) {
                this._isbn = _isbn;
            }
            else {
                throw new IllegalArgumentException("The ISBN-number is incorrect!");
            }
        }
        else {
            throw new IllegalArgumentException("Wrong length of the ISBN-number!");
        }
    }

    public void setAuthor(String _author) {
        if (!_author.isEmpty()) {
            this._author = _author;
        }
        else {
            throw new IllegalArgumentException("The author-value is empty!");
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
     * <p>Method to check ISBN with a length of 13 characters for correctness.</p>
     */

    public static boolean checkISBN10(int[] isbn) {
        int sum = 0;
        for (int i = 1; i <= isbn.length; i++) {
            sum += i * isbn[i - 1];
        }
        return sum % 11 == 0;
    }

    /**
     * <p>Method to check ISBN with a length of 13 characters for correctness.</p>
     */

    public static boolean checkISBN13(int[] isbn) {
        int sum = 0;
        for (int i = 1; i < isbn.length; i++) {
            if (i % 2 == 0) {
                sum += isbn[i - 1] * 3;
            } else {
                sum += isbn[i - 1];
            }
        }
        int lastDigit = sum % 10;
        int check = (10 - lastDigit) % 10;
        return isbn[isbn.length - 1] == check;
    }

    /**
     * <p>Method to generate representation.</p>
     */

    @Override
    public StringBuilder calculateRepresentation() {
        try {
            StringBuilder objectString = super.calculateRepresentation();
            objectString.append("Erscheinungsjahr: " + getYear() + System.lineSeparator());
            objectString.append("Verlag: " + getPublisher() + System.lineSeparator());
            objectString.append("ISBN: " + getISBN() + System.lineSeparator());
            objectString.append("Verfasser: " + getAuthor() + System.lineSeparator());
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
            objectString.append("book{");
            objectString.append("title = {" + getTitle() + "}, ");
            objectString.append("year = " + getYear() + ", ");
            objectString.append("publisher = {" + getPublisher() + "}, ");
            objectString.append("isbn = {" + getISBN() + "}, ");
            objectString.append("author = {" + getAuthor() + "}}");
            return objectString;
        }
        catch (NullPointerException e) {
            System.err.println("Unable to create BibTex-Sting!");
        }
        return null;
    }
}