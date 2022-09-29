package com.example.engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Main.begin(stage);
    }

    public static void main(String[] args) {
        // Launch? 🙂️
        launch();
    }

    public static void begin(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml_files/main_window.fxml"));
            Parent root = loader.load();

            // To pass Stage to the Controller :)
            MainController controller = loader.getController();
            controller.getMainStage(stage);

            // For Image as an icon
            Image icon = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/images_games/logo.png")));
            stage.getIcons().add(icon);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
