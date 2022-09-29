package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.*;

public class Rook extends ChessPiece {

    private ImageView imageView;

    public Rook(int row, int col, ChessPieceColor pieceColor) {
        super(row, col, pieceColor);
        setPieceSprite();
    }

    @Override
    public void setPieceSprite() {
        String location = PATH_CHESS + String.format("%s_rook.png", super.getPieceColor().type ? "white" : "black");
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
        int[][] dim = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] d : dim) {
            for (int j = 0; j < ROWS; j++) {
                int newR = row + d[0] * j, newC = col + d[1] * j;
                if (newR != row || newC != col) {
                    if (board.validMove(newR, newC, color)) {
                        moves.add(new Point(newR, newC));
                        if (board.pieceAtCoordination(newR, newC)) break;
                    } else break;
                }
            }
        }
        return moves;
    }
}
