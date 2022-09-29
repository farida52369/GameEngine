package com.example.engine.games.chess.pieces;

public enum ChessPieceType {
    PAWN("Pawn"),
    BISHOP("Bishop"),
    ROOK("Rook"),
    KING("King"),
    KNIGHT("Knight"),
    QUEEN("Queen");

    public final String name;

    ChessPieceType(String name) {
        this.name = name;
    }
}
