package com.example.engine.games.tic_tac_toe.ai;

import com.example.engine.games.Point;
import com.example.engine.games.tic_tac_toe.IXOBoard;

public interface IXOMinimax {

    Point playCPUMove(IXOBoard board);
}
