package com.example.engine.games.chess.pieces;

// white -> Human | black -> CPU
public enum ChessPieceColor {
    WHITE(true), BLACK(false);

    public final boolean type;

    ChessPieceColor(boolean type) {
        this.type = type;
    }
}
