package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.PATH_CHESS;
import static com.example.engine.Constants.SPIRIT_SIZE;

public class King extends ChessPiece {

    private ImageView imageView;

    public King(int row, int col, ChessPieceColor pieceColor) {
        super(row, col, pieceColor);
        setPieceSprite();
    }

    @Override
    public void setPieceSprite() {
        String location = PATH_CHESS + String.format("%s_king.png", super.getPieceColor().type ? "white" : "black");
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
        int row = super.getRow(), col = super.getCol();
        boolean color = super.getPieceColor().type;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i == row && j == col) continue;
                if (board.validMove(i, j, color))
                    moves.add(new Point(i, j));
            }
        }
        return moves;
    }
}
