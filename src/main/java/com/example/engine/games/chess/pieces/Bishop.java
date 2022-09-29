package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.PATH_CHESS;
import static com.example.engine.Constants.SPIRIT_SIZE;

public class Bishop extends ChessPiece {

    private ImageView imageView;

    public Bishop(int row, int col, ChessPieceColor pieceColor) {
        super(row, col, pieceColor);
        setPieceSprite();
    }

    @Override
    public void setPieceSprite() {
        String location = PATH_CHESS + String.format("%s_bishop.png", super.getPieceColor().type ? "white" : "black");
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
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                int newR = row + i, newC = col + j;
                while (board.validMove(newR, newC, color)) {
                    moves.add(new Point(newR, newC));
                    if (board.pieceAtCoordination(newR, newC)) break;
                    newR += i;
                    newC += j;
                }
            }
        }
        return moves;
    }
}
