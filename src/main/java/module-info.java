module com.example.engine_game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;

    opens com.example.engine to javafx.fxml;
    exports com.example.engine;
}