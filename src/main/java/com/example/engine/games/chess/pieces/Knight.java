package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.PATH_CHESS;
import static com.example.engine.Constants.SPIRIT_SIZE;

public class Knight extends ChessPiece {

    private ImageView imageView;

    public Knight(int row, int col, ChessPieceColor pieceColor) {
        super(row, col, pieceColor);
        setPieceSprite();
    }

    @Override
    public void setPieceSprite() {
        String location = PATH_CHESS + String.format("%s_knight.png", super.getPieceColor().type ? "white" : "black");
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
        for (int i = row - 2; i <= row + 2; i++)
            for (int j = col - 2; j <= col + 2; j++)
                if ((Math.abs(row - i) == 2 && Math.abs(col - j) == 1) || (Math.abs(row - i) == 1 && Math.abs(col - j) == 2))
                    if (board.validMove(i, j, color)) moves.add(new Point(i, j));
        return moves;
    }
}
