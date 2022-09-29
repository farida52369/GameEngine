package com.example.engine.games.tic_tac_toe;

import com.example.engine.Main;
import com.example.engine.games.CommonUX;
import com.example.engine.games.Point;
import com.example.engine.games.tic_tac_toe.ai.IXOMinimax;
import com.example.engine.games.tic_tac_toe.ai.XOMinimax;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;

import static com.example.engine.Constants.*;

public class XOGame {

    private final GridPane gridPane;
    private final IXOMinimax minimax;
    private final DropShadow shadow;
    private IXOBoard board;
    private ImageView humanImageView;
    private ImageView cpuImageView;

    public XOGame() {
        // GUI
        this.gridPane = new GridPane();
        this.shadow = new DropShadow();
        // BackEnd
        this.board = new XOBoard();
        this.minimax = new XOMinimax();
    }

    public void startPlaying(Stage stage) {
        drawMainWindowForXO(stage);
    }

    private void drawMainWindowForXO(Stage stage) {
        AnchorPane pane = new AnchorPane();
        int h = HEADER_FOOTER_HEIGHT * 2 + SPIRIT_SIZE_XO * XO_WIDTH + PADDING_XO * 2;
        int w = SPIRIT_SIZE_XO * XO_WIDTH + PADDING_XO * 2;
        // ******** Header
        // UX for Human Turn
        Label human = CommonUX.getLabel("You", PADDING_XO, PADDING_XO - 15);
        humanImageView = CommonUX.getImageView(
                PATH_XO + "x.png",
                PADDING_XO + LABEL_WIDTH,
                PADDING_XO - 15,
                RECOGNIZE_PLAYER_SIZE_XO,
                RECOGNIZE_PLAYER_SIZE_XO
        );
        // UX for CPU Turn
        int xPosForLabel = PADDING_XO + SPIRIT_SIZE_XO * 2;
        Label cpu = CommonUX.getLabel("CPU", xPosForLabel, PADDING_XO - 15);
        cpuImageView = CommonUX.getImageView(
                PATH_XO + "o.png",
                xPosForLabel + LABEL_WIDTH + 10,
                PADDING_XO - 15,
                RECOGNIZE_PLAYER_SIZE_XO,
                RECOGNIZE_PLAYER_SIZE_XO
        );
        // ******** GridPane Content
        drawGridPane();
        // ******** Footer
        // Start new Game Button
        int yPosFooter = h - HEADER_FOOTER_HEIGHT;
        Button startNewGame = CommonUX.getButton("Start", w - SPIRIT_SIZE_XO, yPosFooter);
        startNewGame.setOnMouseClicked(e -> startNew());
        Button returnToMain = CommonUX.getButton("Menu", PADDING_XO, yPosFooter);
        returnToMain.setOnMouseClicked(e -> Main.begin(stage));
        // add attributes to the pane
        pane.getChildren().addAll(gridPane, returnToMain, startNewGame, human, humanImageView, cpu, cpuImageView);
        // Creating a scene to the stage
        Scene scene = new Scene(pane, w, h);
        stage.setScene(scene);
        stage.show();
    }

    private void startNew() {
        drawGridPane();
        // BackEnd need to be notified
        board = new XOBoard();
    }

    private void drawGridPane() {
        // Clear all
        gridPane.getChildren().clear();
        gridPane.setDisable(false);
        gridPane.setLayoutY(HEADER_FOOTER_HEIGHT);
        humanImageView.setEffect(shadow);
        // Properties for the GridPane
        gridPane.setPadding(new Insets(PADDING_XO, PADDING_XO, PADDING_XO, PADDING_XO));
        gridPane.setHgap(1);
        gridPane.setVgap(1);

        for (int i = 0; i < XO_WIDTH; i++) {
            for (int j = 0; j < XO_WIDTH; j++) {
                // For the Background Colors
                StackPane field = new StackPane();
                field.setMinWidth(SPIRIT_SIZE_XO);
                field.setMinHeight(SPIRIT_SIZE_XO);
                field.setBackground(SKY_BLUE);
                gridPane.add(field, i, j);
            }
        }

        gridPane.setOnMouseClicked(e -> {
            int i = (int) ((e.getY() - PADDING_XO) / SPIRIT_SIZE_XO);
            int j = (int) ((e.getX() - PADDING_XO) / SPIRIT_SIZE_XO);
            // Notify The BackEnd That we have new Mouse Click
            // If human turn finished, begin CPU
            if (board.update(i, j, HUMAN_PLAYER)) {
                updateUX(i, j, HUMAN_PLAYER);
                if (notGameOver(HUMAN_PLAYER)) {
                    // CPU Turn
                    humanImageView.setEffect(null);
                    cpuImageView.setEffect(shadow);
                    Point cpuMove = minimax.playCPUMove(board);
                    int r = cpuMove.getRow(), c = cpuMove.getCol();
                    if (board.update(r, c, CPU_PLAYER)) {
                        updateUX(r, c, CPU_PLAYER);
                        if (notGameOver(CPU_PLAYER)) {
                            // Human Turn
                            cpuImageView.setEffect(null);
                            humanImageView.setEffect(shadow);
                        }
                    }
                }
            }
        });
    }

    private void updateUX(int i, int j, int player) {
        ImageView imageView = getSpirit(player);
        gridPane.add(imageView, j, i);
    }

    private boolean notGameOver(int player) {
        if (board.isWinner(player)) {
            byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x84};
            String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);
            // alert that player who won
            if (player == HUMAN_PLAYER) {
                CommonUX.alert("You've Won The Game " + emoji + emoji + emoji, HUMAN_PLAYER);
            } else {
                CommonUX.alert("You've Lost The Game :)", CPU_PLAYER);
            }
            gridPane.setDisable(true);
            return false;
        } else if (board.isTie()) {
            // alert isTie
            CommonUX.alert("It's a TIE!", EMPTY);
            gridPane.setDisable(true);
            return false;
        }
        return true;
    }

    private ImageView getSpirit(int player) {
        String location = String.format(PATH_XO + "%s.png", (player == HUMAN_PLAYER) ? "x" : "o");
        Image image = new Image(location);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SPIRIT_SIZE_XO);
        imageView.setFitHeight(SPIRIT_SIZE_XO);
        return imageView;
    }
}
