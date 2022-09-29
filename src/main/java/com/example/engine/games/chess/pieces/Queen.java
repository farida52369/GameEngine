package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.PATH_CHESS;
import static com.example.engine.Constants.SPIRIT_SIZE;

public class Queen extends ChessPiece {

    private ImageView imageView;

    public Queen(int row, int col, ChessPieceColor pieceColor) {
        super(row, col, pieceColor);
        setPieceSprite();
    }

    @Override
    public void setPieceSprite() {
        String location = PATH_CHESS + String.format("%s_queen.png", super.getPieceColor().type ? "white" : "black");
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
        // Rook Moves + Bishop Moves = Queen Moves
        moves.addAll(new Rook(row, col, color ? ChessPieceColor.WHITE : ChessPieceColor.BLACK).validMoves(board));
        moves.addAll(new Bishop(row, col, color ? ChessPieceColor.WHITE : ChessPieceColor.BLACK).validMoves(board));
        return moves;
    }
}
