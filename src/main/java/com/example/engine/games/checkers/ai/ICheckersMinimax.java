package com.example.engine.games.checkers.ai;

import com.example.engine.games.Move;
import com.example.engine.games.Point;
import com.example.engine.games.checkers.ICheckersBoard;

import java.util.List;

public interface ICheckersMinimax {
    Move<List<Point>> getCPUMoveCapture(ICheckersBoard board);

    Move<Point> getCPUMove(ICheckersBoard board);
}
