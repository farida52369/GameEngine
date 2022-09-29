package com.example.engine.games.chess;

import com.example.engine.games.Point;
import com.example.engine.games.chess.pieces.ChessPiece;
import com.example.engine.games.chess.pieces.ChessPieceColor;

import java.util.List;

public interface IChessBoard {

    ChessPiece getPiece(int r, int c);

    void setPiece(int r, int c, ChessPiece piece);

    void setKingPos(int r, int c, boolean color);

    boolean pieceAtCoordination(int row, int col);

    boolean enemyAtCoordination(int row, int col, boolean color);

    boolean validMove(int row, int col, boolean color);

    List<Point> pieceAllPossibleMoves(int row, int col);

    int makeMove(Point from, Point to, ChessPieceColor curColor);

    int gameOver(boolean color);

}
