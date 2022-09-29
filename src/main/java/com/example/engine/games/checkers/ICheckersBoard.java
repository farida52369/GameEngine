package com.example.engine.games.checkers;

import com.example.engine.games.Point;

import java.util.List;

public interface ICheckersBoard {

    boolean isEmptyCell(int r, int c);

    boolean samePlayer(int r, int c, CheckersPieceType player);

    CheckersPiece getPiece(int r, int c);

    CheckersPiece[][] getBoard();

    boolean getShouldCapture();

    List<Point> getValidPieces(CheckersPieceType player);

    List<List<Point>> captureMove(CheckersPiece piece);

    List<Point> regularMove(CheckersPiece piece);

    void setMove(Point from, Point to);

    void setMove(Point from, List<Point> to);

    boolean isGameOver();

    int getBoardScore();
}
