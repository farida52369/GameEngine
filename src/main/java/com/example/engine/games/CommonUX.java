package com.example.engine.games;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import static com.example.engine.Constants.*;

public class CommonUX {

    public static DropShadow shadow = new DropShadow();

    public static Button getButton(String title, int xPos, int yPos) {

        Button button = new Button(title);
        button.setLayoutX(xPos);
        button.setLayoutY(yPos);
        button.setCursor(Cursor.HAND);
        button.setFont(new Font(FONT_FAMILY, FONT_SIZE_BUTTONS));
        button.setStyle(BUTTON_ATTRIBUTES);
        button.setOnMouseEntered(e -> button.setEffect(shadow));
        button.setOnMouseExited(e -> button.setEffect(null));

        return button;
    }

    public static ImageView getImageView(String path, int xPos, int yPos, int w, int h) {

        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setLayoutX(xPos);
        imageView.setLayoutY(yPos);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);

        return imageView;
    }

    public static Label getLabel(String title, int xPos, int yPos) {

        Label label = new Label(title);
        label.setLayoutX(xPos);
        label.setLayoutY(yPos);
        label.setFont(new Font(FONT_FAMILY, FONT_SIZE_LABEL));

        return label;
    }

    public static void alert(String message, char whoWins) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("The Game is Over!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        /*
        // l7d el pics tghaz
        ImageView imageView = new ImageView(new Image());
        imageView.setFitWidth(80);
        imageView.setFitHeight(120);
        alert.setGraphic(imageView);
        */

        // Setting Attributes __ CSS for text in alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-size: 22px; -fx-font-family: sans-serif;");
        dialogPane.lookupButton(ButtonType.OK).setStyle("-fx-font-size: 12px;");

        alert.showAndWait();
    }

    // For the Numbers in Chess, Checkers Board
    public static Label newRowLabel(int i) {
        Label l = new Label(COLS - i + "");
        l.setMinSize(LABEL, SPIRIT_SIZE);
        l.setAlignment(Pos.CENTER);
        return l;
    }

    // For the Letters in Chess, Checkers Board
    public static Label newColLabel(int i) {
        Label l = new Label((char) (i + 'A') + "");
        l.setMinSize(SPIRIT_SIZE, LABEL);
        l.setAlignment(Pos.CENTER);
        return l;
    }
}
