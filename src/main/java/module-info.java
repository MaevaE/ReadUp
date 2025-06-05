module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    opens com.example to javafx.fxml, javafx.graphics;
    opens com.example.controller to javafx.fxml;
    opens com.example.model to javafx.base;

    exports com.example;
}