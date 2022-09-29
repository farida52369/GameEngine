package com.example.engine.games.tic_tac_toe;

import com.example.engine.games.Point;

import java.util.List;

public interface IXOBoard {

    void addPiece(int i, int j, char player);

    void removePiece(int i, int j);

    boolean validMoves(int i, int j);

    boolean update(int i, int j, char player);

    List<Point> emptySpots();

    boolean isTie();

    boolean isWinner(int player);

}
