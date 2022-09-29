package com.example.engine;

import com.example.engine.games.checkers.CheckersGame;
import com.example.engine.games.chess.ChessGame;
import com.example.engine.games.connect4.Connect4Game;
import com.example.engine.games.tic_tac_toe.XOGame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.engine.Constants.PATH_AUDIO;

public class MainController implements Initializable {

    private Stage stage;
    private AudioClip start_game;
    private DropShadow shadow;

    @FXML
    private Button chessButton;

    @FXML
    private Button xoButton;

    @FXML
    private Button checkersButton;

    @FXML
    private Button connect4Button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.start_game = new AudioClip(PATH_AUDIO + "start_game.wav");
        this.shadow = new DropShadow();

        // Hover for games buttons mouse move
        List<Button> buttons = new ArrayList<>(4);
        buttons.addAll(List.of(chessButton, xoButton, checkersButton, connect4Button));
        buttons.forEach(e -> {
            e.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
                shadow.setSpread(.1);
                e.setEffect(shadow);
            });

            e.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
                e.setEffect(null);
            });
        });

    }

    public void playChess() {
        start_game.play();
        new ChessGame().startPlaying(stage);
    }

    public void playCheckers() {
        start_game.play();
        new CheckersGame().startPlaying(stage);
    }

    public void playXO() {
        start_game.play();
        XOGame drawer = new XOGame();
        drawer.startPlaying(stage);
    }

    public void playConnect4() {
        start_game.play();
        Connect4Game drawer = new Connect4Game();
        drawer.startPlaying(stage);

    }

    public void getMainStage(Stage stage) {
        this.stage = stage;
    }
}
