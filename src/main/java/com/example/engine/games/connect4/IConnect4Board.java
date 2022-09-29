package com.example.engine.games.connect4;

public interface IConnect4Board {

    int getCols();

    int nextRowToAddIn(int c);

    boolean pieceCouldBeAdded(int col);

    boolean addPiece(int col, Connect4Piece piece);

    void removePiece(int col);

    Connect4Piece getPiece(int r, int c);

    boolean isTie();

    boolean isWinner(Connect4PieceType player);

    int getBoardScore(int col, Connect4PieceType player);
}
