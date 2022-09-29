package com.example.engine.games.chess.ai;

import com.example.engine.games.Move;
import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;

public interface IChessMinimax {
    Move<Point> getCpuMove(IChessBoard board);
}
