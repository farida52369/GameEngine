package com.example.engine.games.connect4;

import com.example.engine.Main;
import com.example.engine.games.CommonUX;
import com.example.engine.games.connect4.ai.Connect4Minimax;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;

import static com.example.engine.Constants.*;

public class Connect4Game {

    private final DropShadow shadow;
    private final Connect4Minimax minimax;
    private final GridPane gridPane;
    private ImageView humanImageView;
    private ImageView cpuImageView;
    private IConnect4Board board;

    public Connect4Game() {
        // GUI
        this.shadow = new DropShadow();
        this.gridPane = new GridPane();
        // BackEnd
        this.board = new Connect4Board(CONNECT4_ROWS, CONNECT4_COLS);
        this.minimax = new Connect4Minimax(7);
    }

    public void startPlaying(Stage stage) {
        drawMainWindowForConnect4(stage);
    }

    private void drawMainWindowForConnect4(Stage stage) {
        AnchorPane pane = new AnchorPane();
        int h = SQUARE_SIZE_CONNECT4 * CONNECT4_ROWS + PADDING_CONNECT4 * 2 + HEADER_FOOTER_HEIGHT * 2;
        int w = SQUARE_SIZE_CONNECT4 * CONNECT4_COLS + PADDING_CONNECT4 * 2;
        // ********* Header
        // UX for Human Turn
        Label human = CommonUX.getLabel("You", PADDING_CONNECT4, PADDING_CONNECT4);
        humanImageView = CommonUX.getImageView(
                PATH_CONNECT4 + "red.png",
                PADDING_CONNECT4 + LABEL_WIDTH,
                PADDING_CONNECT4 - 5,
                RECOGNIZE_PLAYER_WIDTH_CONNECT4,
                RECOGNIZE_PLAYER_HEIGHT_CONNECT4
        );
        // UX for CPU Turn
        int xPosForLabel = PADDING_CONNECT4 + SQUARE_SIZE_CONNECT4 * (CONNECT4_COLS - 2) + SPIRIT_SIZE_CONNECT4 / 2;
        Label cpu = CommonUX.getLabel("CPU", xPosForLabel, PADDING_CONNECT4);
        cpuImageView = CommonUX.getImageView(
                PATH_CONNECT4 + "yellow.png",
                xPosForLabel + LABEL_WIDTH,
                PADDING_CONNECT4 - 5,
                RECOGNIZE_PLAYER_WIDTH_CONNECT4,
                RECOGNIZE_PLAYER_HEIGHT_CONNECT4
        );
        // ******** GridPane Content
        drawGridPane();
        // ******** Footer
        int yPosFooter = h - HEADER_FOOTER_HEIGHT;
        Button startNewGame = CommonUX.getButton("Start", w - SQUARE_SIZE_CONNECT4, yPosFooter);
        startNewGame.setOnMouseClicked(e -> startNew());
        Button returnToMain = CommonUX.getButton("Menu", PADDING_CONNECT4, yPosFooter);
        returnToMain.setOnMouseClicked(e -> Main.begin(stage));
        // add Nodes to the pane
        pane.getChildren().addAll(gridPane, returnToMain, startNewGame, human, humanImageView, cpu, cpuImageView);
        // Creating a scene to the stage
        Scene scene = new Scene(pane, w, h);
        stage.setScene(scene);
        stage.show();
    }

    private void startNew() {
        drawGridPane();
        board = new Connect4Board(CONNECT4_ROWS, CONNECT4_COLS);
    }

    private void drawGridPane() {
        // Clear all
        gridPane.getChildren().clear();
        gridPane.setDisable(false);
        gridPane.setLayoutY(HEADER_FOOTER_HEIGHT);
        humanImageView.setEffect(shadow);
        // Properties for the GridPane
        gridPane.setPadding(new Insets(PADDING_CONNECT4, PADDING_CONNECT4, PADDING_CONNECT4, PADDING_CONNECT4));

        for (int i = 0; i < CONNECT4_COLS; i++) {
            for (int j = 0; j < CONNECT4_ROWS; j++) {
                // For the Background Colors
                StackPane field = new StackPane();
                field.setMinWidth(SQUARE_SIZE_CONNECT4);
                field.setMinHeight(SQUARE_SIZE_CONNECT4);
                field.setBackground(SKY_BLUE);
                gridPane.add(field, i, j);

                // Circle for UX aligned in the center
                Circle circle = new Circle(SPIRIT_SIZE_CONNECT4 / 2.0);
                circle.setStyle("-fx-fill: #B1AEA7;");
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
                gridPane.add(circle, i, j);
            }
        }
        gridPane.setOnMouseClicked(this::doAction);
    }

    public void doAction(MouseEvent e) {
        int col = (int) ((e.getX() - PADDING_CONNECT4) / SQUARE_SIZE_CONNECT4);
        // Notify The BackEnd That we have new Mouse Click
        // If human turn finished, begin CPU
        Connect4Piece piece;
        piece = new Connect4Piece(Connect4PieceType.RED);
        if (board.addPiece(col, piece)) {
            updateUX(col, piece);
            System.out.println("After Human Move, The board!");
            System.out.println(board);
            if (notGameOver(Connect4PieceType.RED)) {
                // CPU turn
                humanImageView.setEffect(null);
                cpuImageView.setEffect(shadow);
                col = minimax.playCPUMove(board);
                piece = new Connect4Piece(Connect4PieceType.YELLOW);
                board.addPiece(col, piece);
                updateUX(col, piece);
                System.out.println("After CPU Move, The board!");
                System.out.println(board);
                if (notGameOver(Connect4PieceType.YELLOW)) {
                    // Human Turn
                    humanImageView.setEffect(shadow);
                    cpuImageView.setEffect(null);
                }
            }
        }
    }

    private void updateUX(int col, Connect4Piece piece) {
        ImageView imageView = piece.getSprite();
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);
        gridPane.add(imageView, col, board.nextRowToAddIn(col) + 1);
    }

    private boolean notGameOver(Connect4PieceType player) {
        if (board.isWinner(player)) {
            byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x84};
            String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);
            // alert that player won
            if (player == Connect4PieceType.RED) {
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

}
