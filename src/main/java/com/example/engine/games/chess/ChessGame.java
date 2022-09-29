package com.example.engine.games.chess;

import com.example.engine.Main;
import com.example.engine.games.CommonUX;
import com.example.engine.games.Move;
import com.example.engine.games.Point;
import com.example.engine.games.chess.ai.ChessMinimax;
import com.example.engine.games.chess.ai.IChessMinimax;
import com.example.engine.games.chess.pieces.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static com.example.engine.Constants.*;

public class ChessGame {

    private final DropShadow shadow;
    private final GridPane gridPane;
    private final IChessMinimax minimax;
    // Some Logic vars
    private IChessBoard logic;
    private ImageView humanImageView;
    private ImageView cpuImageView;
    private Stack<List<Point>> lastClickMoves;
    private Stack<Point> lastClick;

    public ChessGame() {
        // GUI stuff
        this.gridPane = new GridPane();
        this.shadow = new DropShadow();

        // Logic Stuff
        this.logic = new ChessBoard();
        this.minimax = new ChessMinimax(3);
        this.lastClickMoves = new Stack<>();
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
                PATH_CHESS + "white_king.png",
                PADDING + LABEL + LABEL_WIDTH,
                LABEL - 5,
                RECOGNIZE_PLAYER,
                RECOGNIZE_PLAYER
        );

        // UX for CPU Turn
        int xPosForLabel = SPIRIT_SIZE * (COLS - 1);
        Label cpu = CommonUX.getLabel("CPU", xPosForLabel, LABEL);
        cpuImageView = CommonUX.getImageView(
                PATH_CHESS + "black_king.png",
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
                ChessPiece piece = logic.getPiece(r, c);
                if (piece != null)
                    gridPane.add(piece.getPieceSprite(), c + 1, r + 1);
            }
        }
        gridPane.setOnMouseClicked(this::doAction);
    }

    private void startNew() {
        // Logic
        this.logic = new ChessBoard();
        // Stacks help interact with the user (GUI)
        this.lastClickMoves = new Stack<>();
        this.lastClick = new Stack<>();
        drawGridPane();
    }

    private void doAction(MouseEvent e) {
        int space_1 = PADDING + LABEL;
        int space_2 = PADDING + LABEL + SPIRIT_SIZE * COLS + GAP * COLS;
        int X = (int) e.getX(), Y = (int) e.getY();
        if (inBound(X, Y, space_1, space_2)) { // Valid Mouse Click
            space_1 += GAP * (ROWS + 1);
            int r = (Y - space_1) / SPIRIT_SIZE;
            int c = (X - space_1) / SPIRIT_SIZE;
            if (!lastClick.empty()) { // we have painted things already
                boolean moved = false;
                Point from = lastClick.pop();
                List<Point> lastMoves = lastClickMoves.pop();
                unHighLight(lastMoves);
                for (Point to : lastMoves) {
                    if (to.getRow() == r && to.getCol() == c) {
                        // move
                        moveLogic(from, to, false);
                        moved = true;
                        // CPU turn
                        if (notGameOver()) {
                            humanImageView.setEffect(null);
                            cpuImageView.setEffect(shadow);
                            Move<Point> cpuMove = minimax.getCpuMove(logic);
                            moveLogic(cpuMove.from, cpuMove.to, true);
                            if (notGameOver()) {
                                humanImageView.setEffect(shadow);
                                cpuImageView.setEffect(null);
                            }
                        }
                        break;
                    }
                }
                if (!moved) helper(r, c);
            } else helper(r, c);
        }
    }

    private boolean inBound(int x, int y, int space_1, int space_2) {
        return x >= space_1 && y >= space_1 && x <= space_2 && y <= space_2;
    }

    private void moveLogic(Point from, Point to, boolean AI) {
        int fromX = from.getRow(), fromY = from.getCol();
        int toX = to.getRow(), toY = to.getCol();
        if (logic.getPiece(fromX, fromY) == null) return;
        ChessPiece srcPiece = logic.getPiece(fromX, fromY).clone();
        ChessPiece destPiece = logic.getPiece(toX, toY);
        srcPiece.setHasMoved(true); // to notice it has move
        // Promotion
        if ((toX == 0 || toX == 7) && srcPiece.getClass().getSimpleName().equals(ChessPieceType.PAWN.name)) {
            if (logic.pieceAtCoordination(toX, toY)) {
                gridPane.getChildren().removeAll(srcPiece.getPieceSprite(), destPiece.getPieceSprite());
            } else gridPane.getChildren().remove(srcPiece.getPieceSprite());
            ChessPiece promoted = AI ? new Queen(toX, toY, srcPiece.getPieceColor()) : choosePromotePiece(toX, toY, srcPiece.getPieceColor());
            logic.setPiece(toX, toY, promoted);
            logic.setPiece(fromX, fromY, null);
            gridPane.add(logic.getPiece(toX, toY).getPieceSprite(), toY + 1, toX + 1);
        }
        // Castling
        else if ((toY == 6 || toY == 2) && (toX == 0 || toX == 7) && (fromY == 4 && (fromX == 0 || fromX == 7)) &&
                srcPiece.getClass().getSimpleName().equals(ChessPieceType.KING.name)) {
            ChessPiece rook = logic.getPiece(fromX, fromY + (toY == 6 ? 3 : -4)).clone();
            rook.setHasMoved(true);
            System.out.println(rook);
            // remove king and rook from their start place
            gridPane.getChildren().removeAll(srcPiece.getPieceSprite(), rook.getPieceSprite());
            highLightBackGround(fromX, fromY + (toY == 6 ? 3 : -4), ((fromX + fromY + (toY == 6 ? 3 : -4)) & 1) == 0 ? WHITE : GREY);
            // add king and rook in the castling positions
            gridPane.add(srcPiece.getPieceSprite(), toY + 1, toX + 1);
            gridPane.add(rook.getPieceSprite(), fromY + (toY == 6 ? 2 : 0), fromX + 1);
            // dealing with backend
            logic.setPiece(toX, toY, srcPiece);
            logic.setPiece(toX, toY + (toY == 6 ? -1 : 1), rook);
            logic.setPiece(fromX, fromY, null);
            logic.setPiece(fromX, fromY + (toY == 6 ? 3 : -4), null);
            // update king position
            logic.setKingPos(toX, toY, srcPiece.getPieceColor().type);
        }
        // Regular Move
        else {
            if (logic.pieceAtCoordination(toX, toY)) {
                gridPane.getChildren().removeAll(srcPiece.getPieceSprite(), destPiece.getPieceSprite());
            } else gridPane.getChildren().remove(srcPiece.getPieceSprite());
            gridPane.add(srcPiece.getPieceSprite(), toY + 1, toX + 1);
            // Move in backend
            logic.setPiece(toX, toY, srcPiece);
            logic.setPiece(fromX, fromY, null);
            // if the moved was king, then we need to update
            if (srcPiece.getClass().getSimpleName().equals(ChessPieceType.KING.name)) {
                // update king position
                logic.setKingPos(toX, toY, srcPiece.getPieceColor().type);
            }
        }
        System.out.println(logic);
    }

    private void helper(int r, int c) {
        boolean type = ChessPieceColor.WHITE.type;
        if (logic.pieceAtCoordination(r, c) && !logic.enemyAtCoordination(r, c, type)) {
            List<Point> validMoves = logic.pieceAllPossibleMoves(r, c);
            lastClick.push(new Point(r, c));
            lastClickMoves.push(validMoves);
            highLight(validMoves);
        }
    }

    private void highLightBackGround(int x, int y, Background background) {
        StackPane field = new StackPane();
        field.setBackground(background);
        gridPane.add(field, y + 1, x + 1);
    }

    private void highLight(List<Point> moves) {
        for (Point i : moves) {
            int iX = i.getRow(), iY = i.getCol();
            if (!logic.pieceAtCoordination(iX, iY)) {
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE_BLUE : BLACK_BLUE);
            } else {
                // Saving the piece before any work
                ChessPiece piece = logic.getPiece(iX, iY);
                // remove the NODE from the gridPane
                gridPane.getChildren().remove(piece.getPieceSprite());
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE_RED : BLACK_RED);
                // add The NODE again
                gridPane.add(piece.getPieceSprite(), iY + 1, iX + 1);
            }
        }
    }

    private void unHighLight(List<Point> moves) {
        for (Point i : moves) {
            int iX = i.getRow(), iY = i.getCol();
            if (!logic.pieceAtCoordination(iX, iY)) {
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE : GREY);
            } else {
                // Saving the piece before any work
                ChessPiece piece = logic.getPiece(iX, iY);
                // remove the NODE from the gridPane
                gridPane.getChildren().remove(piece.getPieceSprite());
                highLightBackGround(iX, iY, ((iX + iY) & 1) == 0 ? WHITE : GREY);
                // add The NODE again
                gridPane.add(piece.getPieceSprite(), iY + 1, iX + 1);
            }
        }
    }

    public ChessPiece choosePromotePiece(int x, int y, ChessPieceColor color) {
        ChessPiece promotedPiece = null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Promotion");
        alert.setHeaderText("You can promote the brave pawn!");
        alert.setContentText("Choose one of the following pieces: ");

        ButtonType buttonRook = new ButtonType(ChessPieceType.ROOK.name);
        ButtonType buttonKnight = new ButtonType(ChessPieceType.KNIGHT.name);
        ButtonType buttonBishop = new ButtonType(ChessPieceType.BISHOP.name);
        ButtonType buttonQueen = new ButtonType(ChessPieceType.QUEEN.name);
        alert.getButtonTypes().setAll(buttonRook, buttonKnight, buttonBishop, buttonQueen);

        // Who's promoting
        String location = PATH_CHESS + (color.type ? "white" : "black") + "_king.png";
        ImageView imageView = CommonUX.getImageView(location, 0, 0, SPIRIT_SIZE, SPIRIT_SIZE);
        alert.setGraphic(imageView);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == buttonRook) {
                promotedPiece = new Rook(x, y, color);
            } else if (result.get() == buttonKnight) {
                promotedPiece = new Knight(x, y, color);
            } else if (result.get() == buttonBishop) {
                promotedPiece = new Bishop(x, y, color);
            } else if (result.get() == buttonQueen) {
                promotedPiece = new Queen(x, y, color);
            }
        }

        return promotedPiece;
    }

    private boolean notGameOver() {
        int w_gameOver = logic.gameOver(ChessPieceColor.WHITE.type);
        int b_gameOver = logic.gameOver(ChessPieceColor.BLACK.type);
        System.out.println("game over:  w= " + w_gameOver + " b= " + b_gameOver);
        if (w_gameOver != 0 || b_gameOver != 0) {
            if (b_gameOver == 1) { // Checkmate
                CommonUX.alert("You've Won The Game", HUMAN_PLAYER);
            } else if (w_gameOver == 1) {
                CommonUX.alert("You've Lost The Game :)", CPU_PLAYER);
            } else { // Stalemate
                CommonUX.alert("It's a Draw!", '0');
            }
            gridPane.setDisable(true);
            return false;
        }
        return true;
    }
}
