package com.example.engine.games.chess.pieces;

import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import javafx.scene.image.ImageView;

import java.util.List;

public abstract class ChessPiece implements Cloneable {

    private final ChessPieceColor pieceColor;
    private int row, col;
    private boolean hasMoved;

    public ChessPiece(int row, int col, ChessPieceColor pieceColor) {
        this.row = row;
        this.col = col;
        this.pieceColor = pieceColor;
        this.hasMoved = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public ChessPieceColor getPieceColor() {
        return pieceColor;
    }

    public int getPieceValue() {
        String pieceName = this.getClass().getSimpleName();
        int val;
        if (pieceName.equals(ChessPieceType.PAWN.name))
            val = ChessPieceValue.PAWN.pieceValue;
        else if (pieceName.equals(ChessPieceType.KNIGHT.name))
            val = ChessPieceValue.KNIGHT.pieceValue;
        else if (pieceName.equals(ChessPieceType.BISHOP.name))
            val = ChessPieceValue.BISHOP.pieceValue;
        else if (pieceName.equals(ChessPieceType.ROOK.name))
            val = ChessPieceValue.ROOK.pieceValue;
        else if (pieceName.equals(ChessPieceType.QUEEN.name))
            val = ChessPieceValue.QUEEN.pieceValue;
        else val = ChessPieceValue.KING.pieceValue;
        return pieceColor.type ? val : -1 * val;
    }

    public abstract void setPieceSprite();

    public abstract ImageView getPieceSprite();

    public abstract List<Point> validMoves(IChessBoard board);

    @Override
    public String toString() {
        return "Piece\n" + "row: " + row + " col: " + col + "\n" +
                "Piece Type: " + (pieceColor.type ? "White" : "Black") + "\n" +
                "The piece " + (hasMoved ? "has" : "hasn't") + " moved before!\n";
    }

    @Override
    public ChessPiece clone() {
        try {
            return (ChessPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
