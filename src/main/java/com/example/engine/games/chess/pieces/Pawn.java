package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.PATH_CHESS;
import static com.example.engine.Constants.SPIRIT_SIZE;

public class Pawn extends ChessPiece {

    private ImageView imageView;

    public Pawn(int row, int col, ChessPieceColor pieceColor) {
        super(row, col, pieceColor);
        setPieceSprite();
    }

    @Override
    public void setPieceSprite() {
        String location = PATH_CHESS + String.format("%s_pawn.png", super.getPieceColor().type ? "white" : "black");
        Image image = new Image(location);
        imageView = new ImageView(image);
        imageView.setFitWidth(SPIRIT_SIZE);
        imageView.setFitHeight(SPIRIT_SIZE);
    }

    @Override
    public ImageView getPieceSprite() {
        return imageView;
    }

    @Override
    public List<Point> validMoves(IChessBoard board) {
        List<Point> moves = new LinkedList<>();
        int change = super.getPieceColor().type ? -1 : 1;
        int row = super.getRow(), col = super.getCol();
        boolean color = super.getPieceColor().type;
        // check a forward move 1, and 2 cells
        if (board.validMove(row + change, col, color) && !board.pieceAtCoordination(row + change, col)) {
            moves.add(new Point(row + change, col));
            if (board.validMove(row + 2 * change, col, color) && !super.hasMoved() && !board.pieceAtCoordination(row + 2 * change, col))
                moves.add(new Point(row + 2 * change, col));
        }
        // Capture diagonally LEFT
        if (board.validMove(row + change, col - 1, color) && board.enemyAtCoordination(row + change, col - 1, color))
            moves.add(new Point(row + change, col - 1));
        // Capture diagonally RIGHT
        if (board.validMove(row + change, col + 1, color) && board.enemyAtCoordination(row + change, col + 1, color))
            moves.add(new Point(row + change, col + 1));
        return moves;
    }
}
