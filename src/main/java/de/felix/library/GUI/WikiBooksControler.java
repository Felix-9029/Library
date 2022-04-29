package de.felix.library.GUI;

import de.felix.library.MediaStorage;
import de.felix.library.exception.MyWebException;
import de.felix.library.medium.Medium;
import de.felix.library.medium.WikiBook;
import de.felix.library.persistency.BibTexPersistency;
import de.felix.library.persistency.BinaryPersistency;
import de.felix.library.persistency.DatabasePersistency;
import de.felix.library.persistency.HumanReadablePersistency;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library.GUI</p>
 * <p>Datei: WikiBooksController.java</p>
 * <p>Datum: 29.12.2021</p>
 * <p>Version: 1</p>
 */

public class WikiBooksControler implements Initializable {
    private MediaStorage mediaStorage = new MediaStorage();
    private WikiBook currentWikiBook;
    private int counter;
    private boolean navButtonUsed;
    private ArrayList<String> history = new ArrayList<>();

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnSearchSynonym;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnAdd;

    @FXML
    private MenuItem btnSortUp;

    @FXML
    private MenuItem btnSortDown;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnLoad;

    @FXML
    private MenuItem btnExport;

    @FXML
    private MenuItem btnImport;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnForward;

    @FXML
    private ComboBox<String> boxHistory;

    @FXML
    private TextField tfSearchTermf;

    @FXML
    private WebView browser;

    @FXML
    private Label contributor;

    @FXML
    private Label date;

    @FXML
    private Label shelve;

    @FXML
    private ListView<String> listSynonyms;

    @FXML
    private ListView<String> listMedia;

    /**
     * <p>initialize GUI and disable buttons.</p>
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine engine = browser.getEngine();
        engine.load("https://de.wikibooks.org");
        listSynonyms.setEditable(false);
        listMedia.setEditable(false);
        btnAdd.setDisable(true);
        listSynonyms.setDisable(true);
        btnSearchSynonym.setDisable(true);

        btnBack.setDisable(true);
        btnForward.setDisable(true);
        boxHistory.setDisable(true);

        btnSortUp.setDisable(true);
        btnSortDown.setDisable(true);
        btnSave.setDisable(true);
        btnExport.setDisable(true);

        btnDelete.setDisable(true);

        counter = -1;
        navButtonUsed = false;
    }

    /**
     * <p>Listener for search-Button to search a book entered in the TextField.</p>
     */

    @FXML
    public void onSearchButtonClick() {
        if (tfSearchTermf.getText().isEmpty()) {
            throwAlert("Bitte einen Buchname in das Textfeld eingeben!");
        }
        else {
            navigateBrowser(tfSearchTermf.getText());
        }
    }

    /**
     * <p>Listener for add-Button to add medium to media-storage.</p>
     */

    @FXML
    public void onAddButtonClick() {
        mediaStorage.addMedium(currentWikiBook);
        setMediaNames();
        btnSortUp.setDisable(false);
        btnSortDown.setDisable(false);
        btnSave.setDisable(false);
        btnExport.setDisable(false);
    }

    /**
     * <p>Listener for search-Button to sort media.</p>
     */

    @FXML
    public void onSortUpButtonClick() {
        mediaStorage.sort(MediaStorage.SortOrder.UP);
        setMediaNames();
        btnSortUp.setDisable(true);
        btnSortDown.setDisable(false);
    }

    /**
     * <p>Listener for sortDown-Button to sort media backwards.</p>
     */

    @FXML
    public void onSortDownButtonClick() {
        mediaStorage.sort(MediaStorage.SortOrder.DOWN);
        setMediaNames();
        btnSortUp.setDisable(false);
        btnSortDown.setDisable(true);
    }

    /**
     * <p>Listener for delete-Button.</p>
     */

    @FXML
    public void onDeleteButtonClick() {
        //TODO
        setMediaNames();
    }

    /**
     * <p>Listener for search-Button to load media from a file.</p>
     */

    @FXML
    public void onLoadButtonClick() {
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Human Readable", "*.txt"),
                new FileChooser.ExtensionFilter("Binary", "*.bin"),
                new FileChooser.ExtensionFilter("Database", "*.db"));
        File selectedFile = fileChooser.showOpenDialog(secondStage);
        if (selectedFile != null) {
            String fileName = selectedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, selectedFile.getName().length());
            switch (fileExtension) {
                case "txt" -> {
                    HumanReadablePersistency humanReadablePersistency = new HumanReadablePersistency();
                    mediaStorage = humanReadablePersistency.load(selectedFile.getAbsolutePath());
                    btnSortUp.setDisable(false);
                    btnSortDown.setDisable(false);
                    btnSave.setDisable(false);
                    btnExport.setDisable(false);
                }
                case "bin" -> {
                    BinaryPersistency binaryPersistency = new BinaryPersistency();
                    mediaStorage = binaryPersistency.load(selectedFile.getAbsolutePath());
                    btnSortUp.setDisable(false);
                    btnSortDown.setDisable(false);
                    btnSave.setDisable(false);
                    btnExport.setDisable(false);
                }
                case "db" -> {
                    DatabasePersistency databasePersistency = new DatabasePersistency();
                    mediaStorage = databasePersistency.load(selectedFile.getAbsolutePath());
                    btnSortUp.setDisable(false);
                    btnSortDown.setDisable(false);
                    btnSave.setDisable(false);
                    btnExport.setDisable(false);
                }
                default -> throwAlert(fileExtension);
            }
        }
        setMediaNames();
    }

    /**
     * <p>Listener for save-Button to save media to a file.</p>
     */

    @FXML
    public void onSaveButtonClick() {
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Human Readable", "*.txt"),
                new FileChooser.ExtensionFilter("Binary", "*.bin"),
                new FileChooser.ExtensionFilter("Database", "*.db"));
        File selectedFile = fileChooser.showSaveDialog(secondStage);
        if (selectedFile != null) {
            String fileName = selectedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, selectedFile.getName().length());
            switch (fileExtension) {
                case "txt" -> {
                    HumanReadablePersistency humanReadablePersistency = new HumanReadablePersistency();
                    humanReadablePersistency.save(mediaStorage, selectedFile.getAbsolutePath());
                }
                case "bin" -> {
                    BinaryPersistency binaryPersistency = new BinaryPersistency();
                    binaryPersistency.save(mediaStorage, selectedFile.getAbsolutePath());
                }
                case "db" -> {
                    DatabasePersistency databasePersistency = new DatabasePersistency();
                    databasePersistency.save(mediaStorage, selectedFile.getAbsolutePath());
                }
                default -> throwAlert(fileExtension);
            }
        }
    }

    /**
     * <p>Listener for search-Button to save a bibtex-file.</p>
     */

    @FXML
    public void onExportButtonClick() {
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Bibtex");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("BibTex", "*.tex"));
        File selectedFile = fileChooser.showSaveDialog(secondStage);
        if (selectedFile != null) {
            BibTexPersistency bibTexPersistency = new BibTexPersistency();
            bibTexPersistency.save(mediaStorage, selectedFile.getAbsolutePath());
        }
    }

    /**
     * <p>Listener for import-Button to load a bibtex-file.</p>
     */

    @FXML
    public void onImportButtonClick() {
        Stage secondStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Bibtex");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("BibTex", "*.tex"));
        File selectedFile = fileChooser.showOpenDialog(secondStage);
        if (selectedFile != null) {
            BibTexPersistency bibTexPersistency = new BibTexPersistency();
            mediaStorage = bibTexPersistency.load(selectedFile.getAbsolutePath());
            btnSortUp.setDisable(false);
            btnSortDown.setDisable(false);
            btnSave.setDisable(false);
            btnExport.setDisable(false);
        }
        setMediaNames();
    }

    /**
     * <p>Listener for searchSynonym-Button to search a book from a selected synonym.</p>
     */

    @FXML
    public void onSearchSynonymButtonClick() {
        tfSearchTermf.setText(listSynonyms.getSelectionModel().getSelectedItem());
        onSearchButtonClick();
    }

    /**
     * <p>Listener for back-Button to navigate backwards in history.</p>
     */

    @FXML
    public void onBackButtonClick() {
        counter--;
        if (counter == 0) {
            btnBack.setDisable(true);
        }
        boxHistory.setValue(history.get(counter));
        tfSearchTermf.setText(history.get(counter));
        navButtonUsed = true;
        onSearchButtonClick();
    }

    /**
     * <p>Listener for forward-Button to navigate forwards in history.</p>
     */

    @FXML
    public void onForwardButtonClick() {
        counter++;
        if (counter+1 == history.size()) {
            btnForward.setDisable(true);
        }
        boxHistory.setValue(history.get(counter));
        tfSearchTermf.setText(history.get(counter));
        navButtonUsed = true;
        onSearchButtonClick();
    }

    /**
     * <p>Listener for boxHistoryValue-Button to do navigation-tasks if clicked.</p>
     */

    @FXML
    public void onBoxHistoryValueClick() {
        if (!boxHistory.getValue().equals(tfSearchTermf.getText())) {
            counter = history.indexOf(boxHistory.getValue());
            if (counter == 0) {
                btnBack.setDisable(true);
            }
            if (counter+1 == history.size()) {
                btnForward.setDisable(true);
            }
            boxHistory.setValue(history.get(counter));
            tfSearchTermf.setText(history.get(counter));
            navButtonUsed = true;
            onSearchButtonClick();
        }
    }

    /**
     * <p>Method for browser navigation, filling GUI-information and button-management (enable/disable).</p>
     * @param search
     */

    @FXML
    public void navigateBrowser(String search) {
        WebEngine engine = browser.getEngine();
        engine.load(WikiBooksUtility.getURL(search));
        // higher counter when navButton not used
        if (!navButtonUsed) {
            counter++;
        }
        // overwrite history with new smaller one
        if (counter < history.size() && !navButtonUsed) {
            ArrayList<String> tempHist = new ArrayList<>();
            for (int x = 0; x < counter; x++) {
                tempHist.add(history.get(x));
            }
            history = tempHist;
            btnForward.setDisable(true);
        }
        // enable backButton
        if (counter > 0) {
            btnBack.setDisable(false);
        }
        // enable forwardButton (+1 because size gives higher number)
        if (counter+1 < history.size() && navButtonUsed) {
            btnForward.setDisable(false);
        }
        // ad search to history if it wasn't a navigation
        if (counter == history.size() && !navButtonUsed) {
            history.add(search);
            boxHistory.setDisable(false);
            setBoxHistory();
            boxHistory.setValue(search);
        }
        navButtonUsed = false;
        // get Values for GUI
        try {
            currentWikiBook = new WikiBook(search);
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            SimpleDateFormat simpleLocalDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            simpleLocalDate.setTimeZone(TimeZone.getTimeZone("utc"));
            Date localDate = simpleLocalDate.parse(simpleDate.format(currentWikiBook.getTimestamp()));
            SimpleDateFormat formattedDate = new SimpleDateFormat("dd.MM.yyyy");
            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
            if (currentWikiBook.getContributor().isEmpty() || currentWikiBook.getTimestamp() == null || currentWikiBook.getShelves().isEmpty()) {
                throw new MyWebException();
            }
            date.setText(formattedDate.format(localDate) + " um " + time.format(localDate));
            contributor.setText(currentWikiBook.getContributor());
            ArrayList<String> shelvesList = currentWikiBook.getShelves();
            StringBuilder shelves = new StringBuilder();
            for (int i = 0; i < shelvesList.size(); i++) {
                shelves.append(shelvesList.get(i));
                if (shelvesList.size() != i + 1) {
                    shelves.append(", ");
                }
            }
            shelve.setText(shelves.toString());
            btnAdd.setDisable(false);
        }
        catch (ParseException e) {
            throwAlert("Something went wrong while parsing Date!");
        }
        catch (Exception e) {
            date.setText("");
            contributor.setText("");
            shelve.setText("");
            btnAdd.setDisable(true);
            throwAlert("Unable to find book!");
        }
        try {
            ArrayList<String> synonymsList = WikiBooksUtility.getSynonyms(search);
            if (synonymsList.get(0).equals("<keine>")) {
                listSynonyms.setItems(FXCollections.observableArrayList(synonymsList));
                listSynonyms.setDisable(true);
                btnSearchSynonym.setDisable(true);
            }
            else {
                listSynonyms.setItems(FXCollections.observableArrayList(synonymsList));
                listSynonyms.setDisable(false);
                btnSearchSynonym.setDisable(false);
            }
        }
        catch (IOException | org.json.simple.parser.ParseException e) {
            throwAlert("Something went wrong while looking for synonyms!");
        }
        setMediaNames();
    }

    /**
     * <p>Listener for MenuAbout-Button.</p>
     */

    @FXML
    public void onMenuAboutClick() {
        throwInformation("Alle redaktionellen Inhalte stammen von den Internetseiten der Projekte Wikibooks und Wortschatz.\n" +
                "Die von Wikibooks bezogenen Inhalte unterliegen seit dem 22. Juni 2009 unter der Lizenz CC-BY-SA 3.0\n" +
                "Unported zur Verfügung. Eine deutschsprachige Dokumentation für Weiternutzer findet man in den\n" +
                "Nutzungsbedingungen der Wikimedia Foundation. Für alle Inhalte von Wikibooks galt bis zum 22. Juni\n" +
                "2009 standardmäßig die GNU FDL (GNU Free Documentation License, engl. für GNU-Lizenz für freie\n" +
                "Dokumentation). Der Text der GNU FDL ist unter\n" +
                "http://de.wikipedia.org/wiki/Wikipedia:GNU_Free_Documentation_License verfügbar.\n" +
                "Die von Wortschatz (http://wortschatz.uni-leipzig.de/) bezogenen Inhalte sind urheberrechtlich geschützt.\n" +
                "Sie werden hier für wissenschaftliche Zwecke eingesetzt und dürfen darüber hinaus in keiner Weise\n" +
                "genutzt werden.\n" +
                "Dieses Programm ist nur zur Nutzung durch den Programmierer selbst gedacht. Dieses Programm dient\n" +
                "der Demonstration und dem Erlernen von Prinzipien der Programmierung mit Java. Eine Verwendung\n" +
                "des Programms für andere Zwecke verletzt möglicherweise die Urheberrechte der Originalautoren der\n" +
                "redaktionellen Inhalte und ist daher untersagt.");
    }

    /**
     * <p>Method to listen for ENTER-Button and F1 input.</p>
     * @param keyEvent
     */

    @FXML
    public void tfSearchTermfKeyEventHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onSearchButtonClick();
        }
        if (keyEvent.getCode().equals(KeyCode.F1)) {
            onMenuAboutClick();
        }
        keyEvent.consume();
    }

    /**
     * <p>Method to listen for ENTER-Button and F1 input.</p>
     * @param keyEvent
     */

    @FXML
    public void listSynonymsKeyEventHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onSearchSynonymButtonClick();
        }
        if (keyEvent.getCode().equals(KeyCode.F1)) {
            onMenuAboutClick();
        }
        keyEvent.consume();
    }

    /**
     * <p>Method to listen for EventKey-input like F1 for "about".</p>
     * @param keyEvent
     */

    @FXML
    public void keyEventHandler(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.F1)) {
            onMenuAboutClick();
        }
        keyEvent.consume();
    }

    /**
     * <p>Method to listen for mouse double click.</p>
     * @param mouseEvent
     */

    @FXML
    public void listSynonymsMouseDoubleClickEventHandler(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            onSearchSynonymButtonClick();
        }
    }

    /**
     * <p>Method to throw an ERROR-alert with custom message.</p>
     */

    public void throwAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * <p>Method to throw an INFORMATION-alert with custom message.</p>
     */

    public void throwInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

    /**
     * <p>Method to set Names of contained media in ListView.</p>
     */

    public void setMediaNames() {
        ArrayList<String> mediaNames = new ArrayList<>();
        for (Medium medium : mediaStorage) {
            mediaNames.add(medium.getTitle());
        }
        listMedia.setItems(FXCollections.observableArrayList(mediaNames));
    }

    /**
     * <p>Method to set history in ComboBox.</p>
     */

    public void setBoxHistory() {
        ArrayList<String> tmp = new ArrayList<>();
        for (int x = history.size()-1; x >= 0; x--) {
            tmp.add(history.get(x));
        }
        boxHistory.setItems(FXCollections.observableArrayList(tmp));
    }
}
