module de.felix {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;
    requires java.xml;
    requires java.sql;
    requires json.simple;

    opens de.felix.library to javafx.fxml;
    exports de.felix.library;
    opens de.felix.library.medium to javafx.fxml;
    exports de.felix.library.medium;
    opens de.felix.library.GUI to javafx.fxml;
    exports de.felix.library.GUI;
    opens de.felix.library.persistency to javafx.fxml;
    exports de.felix.library.persistency;
    opens de.felix.library.exception to javafx.fxml;
    exports de.felix.library.exception;
}