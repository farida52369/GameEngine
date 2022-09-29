package com.example.engine.games.checkers;

import com.example.engine.Main;
import com.example.engine.games.CommonUX;
import com.example.engine.games.Move;
import com.example.engine.games.Point;
import com.example.engine.games.checkers.ai.CheckersMinimax;
import com.example.engine.games.checkers.ai.ICheckersMinimax;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static com.example.engine.Constants.*;

public class CheckersGame {

    private final GridPane gridPane;
    private final ICheckersMinimax minimax;
    private ImageView humanImageView;
    private ImageView cpuImageView;
    private DropShadow shadow;
    private ICheckersBoard logic;
    private Stack<List<Point>> lastClickPieces;
    private Stack<List<List<Point>>> lastClickCapture;
    private Stack<Point> lastClick;

    public CheckersGame() {
        this.gridPane = new GridPane();
        this.shadow = new DropShadow();
        // Logic
        this.logic = new CheckersBoard();
        this.minimax = new CheckersMinimax(4);
        // Stacks help interact with the user (GUI)
        this.lastClickPieces = new Stack<>();
        this.lastClickCapture = new Stack<>();
        this.lastClick = new Stack<>();
    }

    public void startPlaying(Stage stage) {
        drawMainWindowForCheckers(stage);
    }

    private void drawMainWindowForCheckers(Stage stage) {
        AnchorPane pane = new AnchorPane();
        int h = SPIRIT_SIZE * ROWS + PADDING * 2 + HEADER_FOOTER_HEIGHT * 2 + LABEL * 2 + GAP * ROWS + LABEL;
        int w = SPIRIT_SIZE * COLS + PADDING * 2 + LABEL * 2 + GAP * ROWS;
        // ********* Header
        // UX for Human Turn
        Label human = CommonUX.getLabel("You", PADDING + LABEL, LABEL);
        humanImageView = CommonUX.getImageView(
                PATH_CHECKERS + "black.png",
                PADDING + LABEL + LABEL_WIDTH,
                LABEL - 5,
                RECOGNIZE_PLAYER,
                RECOGNIZE_PLAYER
        );
        // UX for CPU Turn
        int xPosForLabel = SPIRIT_SIZE * (COLS - 1);
        Label cpu = CommonUX.getLabel("CPU", xPosForLabel, LABEL);
        cpuImageView = CommonUX.getImageView(
                PATH_CHECKERS + "red.png",
                xPosForLabel + LABEL_WIDTH,
                LABEL - 5,
                RECOGNIZE_PLAYER,
                RECOGNIZE_PLAYER
        );
        // ******** GridPane Content
        drawGridPane();
        // ******** Footer
        int yPosFooter = h - HEADER_FOOTER_HEIGHT;
        Button startNewGame = CommonUX.getButton("Start", w - SPIRIT_SIZE - LABEL * 2, yPosFooter);
        startNewGame.setOnMouseClicked(e -> startNew());
        Button returnToMain = CommonUX.getButton("Menu", PADDING + LABEL, yPosFooter);
        returnToMain.setOnMouseClicked(e -> Main.begin(stage));
        // add Nodes to the pane
        pane.getChildren().addAll(gridPane, returnToMain, startNewGame, human, humanImageView, cpu, cpuImageView);
        // Creating a scene to the stage
        Scene scene = new Scene(pane, w, h);
        stage.setScene(scene);
        stage.show();
    }

    private void startNew() {
        this.shadow = new DropShadow();
        // Logic
        this.logic = new CheckersBoard();
        // Stacks help interact with the user (GUI)
        this.lastClickPieces = new Stack<>();
        this.lastClickCapture = new Stack<>();
        this.lastClick = new Stack<>();
        drawGridPane();
    }

    private void drawGridPane() {
        // Clear all
        gridPane.getChildren().clear();
        gridPane.setDisable(false);
        gridPane.setLayoutY(HEADER_FOOTER_HEIGHT);
        // Recognition
        humanImageView.setEffect(shadow);
        cpuImageView.setEffect(null);
        // Properties for the GridPane
        gridPane.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
        gridPane.setHgap(GAP);
        gridPane.setVgap(GAP);
        // Numbers and Letters in the Board
        for (int i = 0; i < COLS; i++) {
            gridPane.add(CommonUX.newRowLabel(i), 0, i + 1, 1, 1);
            gridPane.add(CommonUX.newRowLabel(i), 9, i + 1, 1, 1);
            gridPane.add(CommonUX.newColLabel(i), i + 1, 0, 1, 1);
            gridPane.add(CommonUX.newColLabel(i), i + 1, 9, 1, 1);
        }
        // For the Background Colors
        for (int r = 1; r <= ROWS; r++) {
            for (int c = 1; c <= COLS; c++) {
                StackPane field = new StackPane();
                field.setBackground(((r + c) & 1) == 0 ? WHITE : GREY);
                gridPane.add(field, r, c);
            }
        }
        // For the Pieces
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                CheckersPiece piece = logic.getPiece(r, c);
                if (piece != null)
                    gridPane.add(piece.getSprite(), c + 1, r + 1);
            }
        }
        gridPane.setOnMouseClicked(this::doAction);
    }

    private void doAction(MouseEvent e) {
        // Row, Col clicked
        int space_1 = PADDING + LABEL;
        int space_2 = PADDING + LABEL + SPIRIT_SIZE * COLS + GAP * COLS;
        int X = (int) e.getX(), Y = (int) e.getY();
        if (inBound(X, Y, space_1, space_2)) { // Valid Mouse Click
            space_1 += GAP * (ROWS + 1);
            int r = (Y - space_1) / SPIRIT_SIZE;
            int c = (X - space_1) / SPIRIT_SIZE;
            // if we have painted cells already
            if (!lastClick.isEmpty()) {
                Point from = lastClick.pop();
                boolean doneMove = false, samePlayer = true;
                // Capture or Regular Move
                if (logic.getShouldCapture()) {
                    List<List<Point>> lastValidMoves = lastClickCapture.pop();
                    for (List<Point> curPoint : lastValidMoves) {
                        Point to = curPoint.get(curPoint.size() - 1);
                        if (to.getRow() == r && to.getCol() == c) {
                            // UPDATE THE BOARD FOR HUMAN PLAYER
                            updateUX(from, to);
                            logic.setMove(from, curPoint);
                            highLightCapture(from.getRow(), from.getCol(), lastValidMoves, false);
                            doneMove = true;
                            samePlayer = false;
                            // DEBUGGING
                            System.out.println("HUMAN TURN RESULT BOARD");
                            System.out.println(logic);
                            // Time for CPU Move
                            aiMove();
                        }
                    }
                    if (!doneMove)
                        highLightCapture(from.getRow(), from.getCol(), lastValidMoves, false);
                } else {
                    List<Point> lastValidMoves = lastClickPieces.pop();
                    for (Point to : lastValidMoves) {
                        if (to.getRow() == r && to.getCol() == c) {
                            // Update backend and frontend
                            updateUX(from, to);
                            logic.setMove(from, to);
                            highLight(lastValidMoves, false);
                            doneMove = true;
                            samePlayer = false;
                            // DEBUGGING
                            System.out.println("HUMAN Turn Result Board");
                            System.out.println(logic);
                            // Time for CPU Move
                            aiMove();
                        }
                    }
                    if (!doneMove) highLight(lastValidMoves, false);
                }
                if (samePlayer) helper(r, c);
            } else helper(r, c);
        }
    }

    private boolean inBound(int x, int y, int space_1, int space_2) {
        return  x >= space_1 && y >= space_1 && x <= space_2 && y <= space_2;
    }

    private void aiMove() {
        // AI TURN
        logic.getValidPieces(CheckersPieceType.RED);
        if (notWinner(CheckersPieceType.BLACK)) {
            humanImageView.setEffect(null);
            cpuImageView.setEffect(shadow);
            if (logic.getShouldCapture()) {
                Move<List<Point>> T = minimax.getCPUMoveCapture(logic);
                updateUX(T.from, T.to.get(T.to.size() - 1));
                logic.setMove(T.from, T.to);
                List<List<Point>> unhighlight = new LinkedList<>(List.of(T.to));
                highLightCapture(T.from.getRow(), T.from.getCol(), unhighlight, false);
            } else {
                Move<Point> T = minimax.getCPUMove(logic);
                updateUX(T.from, T.to);
                logic.setMove(T.from, T.to);
            }
            // DEBUGGING
            System.out.println("AI Turn Result Board!");
            System.out.println(logic);
            // Game over or continue?
            logic.getValidPieces(CheckersPieceType.BLACK);
            if (notWinner(CheckersPieceType.RED)) {
                humanImageView.setEffect(shadow);
                cpuImageView.setEffect(null);
            }
        }
    }

    private void helper(int r, int c) {
        if (!logic.isEmptyCell(r, c) && logic.samePlayer(r, c, CheckersPieceType.BLACK)) {
            List<Point> validMoves = logic.getValidPieces(CheckersPieceType.BLACK);
            if (notWinner(CheckersPieceType.RED)) {
                CheckersPiece piece = logic.getPiece(r, c);
                for (Point p : validMoves) {
                    if (p.getRow() == r && p.getCol() == c) {
                        // Highlight its valid moves
                        if (logic.getShouldCapture()) {
                            List<List<Point>> movesForPiece = logic.captureMove(piece);
                            highLightCapture(r, c, movesForPiece, true);
                            lastClickCapture.push(movesForPiece);
                        } else {
                            List<Point> movesForPiece = logic.regularMove(piece);
                            highLight(movesForPiece, true);
                            lastClickPieces.push(movesForPiece);
                        }
                        lastClick.push(new Point(r, c));
                    }
                }
            }
        }
    }

    private void updateUX(Point from, Point to) {
        int r_1 = from.getRow(), c_1 = from.getCol();
        int r_2 = to.getRow(), c_2 = to.getCol();
        CheckersPiece toPiece = logic.getPiece(r_1, c_1);
        gridPane.getChildren().remove(toPiece.getSprite());
        highLightBackGround(r_1, c_1, ((r_1 + c_1) & 1) == 0 ? WHITE : GREY);
        if (r_2 == 0 || r_2 == 7) toPiece.makeKing();
        gridPane.add(toPiece.getSprite(), c_2 + 1, r_2 + 1);
    }

    private void highLightCapture(int r, int c, List<List<Point>> movesForPiece, boolean highLight) {
        for (List<Point> list : movesForPiece) {
            int r_1 = r, c_1 = c;
            int r_2 = -1, c_2 = -1;
            for (Point p : list) {
                r_2 = p.getRow();
                c_2 = p.getCol();
                int len = Math.abs(r_2 - r_1);
                int dx = (r_2 - r_1) / len;
                int dy = (c_2 - c_1) / len;
                for (int i = 1; i <= len; i++) {
                    int resR = r_1 + i * dx, resC = c_1 + i * dy;
                    if (!logic.isEmptyCell(resR, resC)) {
                        gridPane.getChildren().remove(logic.getPiece(resR, resC).getSprite());
                        highLightBackGround(resR, resC, highLight ? WHITE_RED : GREY);
                        // add The NODE again
                        gridPane.add(logic.getPiece(resR, resC).getSprite(), resC + 1, resR + 1);
                    } else {
                        highLightBackGround(resR, resC, highLight ? BLACK_BLUE : GREY);
                    }
                }
                r_1 = r_2;
                c_1 = c_2;
            }
            if (!logic.isEmptyCell(r_2, c_2)) {
                gridPane.getChildren().remove(logic.getPiece(r_2, c_2).getSprite());
                highLightBackGround(r_2, c_2, highLight ? WHITE_RED : GREY);
                // add The NODE again
                gridPane.add(logic.getPiece(r_2, c_2).getSprite(), c_2 + 1, r_2 + 1);
            } else {
                highLightBackGround(r_2, c_2, highLight ? GREEN : GREY);
            }
        }
    }

    private void highLight(List<Point> moves, boolean highLight) {
        for (Point p : moves) {
            int r = p.getRow(), c = p.getCol();
            if (logic.isEmptyCell(r, c)) {
                highLightBackGround(r, c, highLight ? GREEN : GREY);
            } else {
                CheckersPiece piece = logic.getPiece(r, c);
                gridPane.getChildren().remove(piece.getSprite());
                highLightBackGround(r, c, highLight ? GREEN : GREY);
                // Return the node again
                gridPane.add(piece.getSprite(), c + 1, r + 1);
            }
        }
    }

    private void highLightBackGround(int r, int c, Background background) {
        StackPane field = new StackPane();
        field.setBackground(background);
        gridPane.add(field, c + 1, r + 1);
    }

    private boolean notWinner(CheckersPieceType player) {
        if (logic.isGameOver()) {
            byte[] emojiByteCode = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x84};
            String emoji = new String(emojiByteCode, StandardCharsets.UTF_8);
            // alert that player won
            if (player == CheckersPieceType.BLACK) {
                CommonUX.alert("You've Won The Game " + emoji + emoji + emoji, HUMAN_PLAYER);
            } else {
                CommonUX.alert("You've Lost The Game :)", CPU_PLAYER);
            }
            gridPane.setDisable(true);
            return false;
        }
        return true;
    }
}
