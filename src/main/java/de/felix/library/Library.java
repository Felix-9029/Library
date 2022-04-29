package de.felix.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author <p>Felix Reichert</p>
 * <p>Matrikelnummer: 19019</p>
 * <p>Package: library</p>
 * <p>Datei: Library.java</p>
 * <p>Datum: 29.12.2021</p>
 * <p>Version: 4</p>
 */

public class Library extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * <p>Method to start GUI and load layout.</p>
     * @param stage
     * @throws IOException
     */

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Library.class.getResource("gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bibliothek");
        stage.setMinHeight(800);
        stage.setMinWidth(1200);
        stage.setScene(scene);
        stage.show();
    }
}