package com.example.engine.games.connect4.ai;

import com.example.engine.games.connect4.Connect4Piece;
import com.example.engine.games.connect4.Connect4PieceType;
import com.example.engine.games.connect4.IConnect4Board;

public class Connect4Minimax implements IConnect4Minimax {

    private final int INF = 1000;
    private final int maxDepth;
    private int bestCol;

    public Connect4Minimax(int maxDepth) {
        this.maxDepth = maxDepth;
        this.bestCol = -1;
    }

    @Override
    public int playCPUMove(IConnect4Board board) {
        long start = System.currentTimeMillis();
        minimax(board, 0, true, -INF, INF);
        System.out.println("Time Taken For CPU To Move in (ms): " + (System.currentTimeMillis() - start));
        return bestCol;
    }

    private int minimax(IConnect4Board board, int depth, boolean isMaximizing, int lastBoardScore, int lastBoardBestScore) {
        // Base Cases
        if (board.isWinner(Connect4PieceType.YELLOW))
            return INF;
        else if (board.isWinner(Connect4PieceType.RED))
            return -INF;
        else if (depth == maxDepth || board.isTie())
            return 0;

        // Start The Minimax Algorithm __ BackTracking
        int bestScore = (isMaximizing ? -INF : INF);
        for (int col = 0; col < board.getCols(); col++) {
            if (!board.pieceCouldBeAdded(col)) continue;
            Connect4PieceType curType = isMaximizing ? Connect4PieceType.YELLOW : Connect4PieceType.RED;
            Connect4Piece piece = new Connect4Piece(curType);
            board.addPiece(col, piece);
            int curBoardScore = board.getBoardScore(col, curType);
            int nextMoveScore = minimax(board, depth + 1, !isMaximizing, curBoardScore, bestScore);
            board.removePiece(col);

            int totalScore = nextMoveScore + curBoardScore;
            if (depth == 0 && totalScore > bestScore) // update the bestCol in the first depth
                bestCol = col;
            if (isMaximizing) {
                bestScore = Math.max(bestScore, totalScore);
                if (bestScore + lastBoardScore >= lastBoardBestScore)
                    break;
            } else {
                bestScore = Math.min(bestScore, totalScore);
                if (bestScore + lastBoardScore <= lastBoardBestScore)
                    break;
            }
        }
        return bestScore;
    }
}
