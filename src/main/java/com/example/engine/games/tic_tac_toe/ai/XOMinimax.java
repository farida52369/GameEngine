package com.example.engine.games.tic_tac_toe.ai;

import com.example.engine.games.Point;
import com.example.engine.games.tic_tac_toe.IXOBoard;

import java.util.List;

import static com.example.engine.Constants.CPU_PLAYER;
import static com.example.engine.Constants.HUMAN_PLAYER;

public class XOMinimax implements IXOMinimax {

    private final Point index;
    private final int INF = 100;

    public XOMinimax() {
        this.index = new Point();
    }

    @Override
    public Point playCPUMove(IXOBoard board) {
        long start = System.nanoTime();
        minimax(board, 0, true, -INF, INF);
        System.out.println("Time Taken in (ns): " + (System.nanoTime() - start));
        return index;
    }

    /**
     * minimax Algorithm
     * To check whether the current move is better than the best move
     * we take the help of minimax() function which will consider all the possible ways
     * the game can go and returns the best value for that move,
     * assuming the opponent also plays optimally
     *
     * @param board        the game Board
     * @param depth        for XO game, its only importance to count for X(10 - depth) O(-10 + depth)
     * @param isMaximizing maximize the score for O player or minimize for X player
     * @param alpha        initialize to -infinity help to cut some unnecessary recursions
     * @param beta         initialize to infinity help to cut some unnecessary recursions
     * @return best score
     */
    private int minimax(IXOBoard board, int depth, boolean isMaximizing, int alpha, int beta) {

        // Base Cases
        List<Point> availSpots = board.emptySpots();
        if (board.isWinner(CPU_PLAYER)) // CPU wins _ Maximizer
            return 10;
        else if (board.isWinner(HUMAN_PLAYER)) // Human wins __ Minimizer
            return -10;
        else if (availSpots.isEmpty()) // TIE
            return 0;

        int bestScore = (isMaximizing ? -INF : INF);
        for (Point spot : availSpots) {
            int r = spot.getRow();
            int c = spot.getCol();
            board.addPiece(r, c, isMaximizing ? CPU_PLAYER : HUMAN_PLAYER);
            int res = minimax(board, depth + 1, !isMaximizing, alpha, beta);
            board.removePiece(r, c);
            if (depth == 0 && bestScore < res)
                index.setPos(r, c);
            if (isMaximizing) {
                bestScore = Math.max(bestScore, res);
                alpha = Math.max(alpha, res);
            } else {
                bestScore = Math.min(bestScore, res);
                beta = Math.min(beta, res);
            }
            if (beta <= alpha) break;
        }
        return bestScore;
    }

}
