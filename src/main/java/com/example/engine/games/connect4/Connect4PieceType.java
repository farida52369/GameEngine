package com.example.engine.games.connect4;

public enum Connect4PieceType {
    YELLOW("yellow"), // CPU
    RED("red"); // Human

    public final String type;

    Connect4PieceType(String type) {
        this.type = type;
    }
}
