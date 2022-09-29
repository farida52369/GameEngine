package com.example.engine.games.chess.pieces;

public enum ChessPieceValue {
    PAWN(1),
    KNIGHT(3),
    BISHOP(3),
    ROOK(5),
    QUEEN(9),
    KING(100);

    public final int pieceValue;

    ChessPieceValue(int pieceValue) {
        this.pieceValue = pieceValue;
    }
}
