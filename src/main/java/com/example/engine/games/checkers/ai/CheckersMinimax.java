package com.example.engine.games.checkers.ai;

import com.example.engine.games.Move;
import com.example.engine.games.Point;
import com.example.engine.games.checkers.CheckersBoard;
import com.example.engine.games.checkers.CheckersPiece;
import com.example.engine.games.checkers.CheckersPieceType;
import com.example.engine.games.checkers.ICheckersBoard;

import java.util.LinkedList;
import java.util.List;

public class CheckersMinimax implements ICheckersMinimax {

    private final int maxDepth;
    private final int INF = 1000;
    private Move<Point> bestColMove;
    private Move<List<Point>> bestColCapture;

    public CheckersMinimax(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private void helper(ICheckersBoard board) {
        long start = System.currentTimeMillis();
        maximizer(board, 0, -INF, INF);
        System.out.println("Time Taken For CPU to Generate Move in (ms): " + (System.currentTimeMillis() - start));
    }

    @Override
    public Move<List<Point>> getCPUMoveCapture(ICheckersBoard board) {
        helper(board);
        return bestColCapture;
    }

    @Override
    public Move<Point> getCPUMove(ICheckersBoard board) {
        helper(board);
        return bestColMove;
    }

    private int maximizer(ICheckersBoard board, int depth, int alpha, int beta) {
        List<Point> validPieces = board.getValidPieces(CheckersPieceType.RED);
        if (board.isGameOver()) return -INF;
        if (depth == maxDepth) return board.getBoardScore();

        int bestScore = -INF;
        for (Point from : validPieces) {
            if (board.getShouldCapture()) {
                List<List<Point>> captureMove = board.captureMove(board.getPiece(from.getRow(), from.getCol()));
                for (List<Point> to : captureMove) {
                    CheckersBoard newBoard = new CheckersBoard(board.getBoard());
                    newBoard.setMove(from, to);
                    int totalScore = minimizer(newBoard, depth + 1, alpha, beta);
                    if (depth == 0 && totalScore >= bestScore) // update the bestCol in the first depth
                        bestColCapture = new Move<>(from, new LinkedList<>(to));
                    bestScore = Math.max(bestScore, totalScore);
                    if (alpha >= beta) break;
                }
            } else {
                List<Point> regularMove = board.regularMove(board.getPiece(from.getRow(), from.getCol()));
                for (Point to : regularMove) {
                    CheckersBoard newBoard = new CheckersBoard(board.getBoard());
                    newBoard.setMove(from, to);
                    int totalScore = minimizer(newBoard, depth + 1, alpha, beta);
                    if (depth == 0 && totalScore >= bestScore) // update the bestCol in the first depth
                        bestColMove = new Move<>(from, to);
                    bestScore = Math.max(bestScore, totalScore);
                    if (alpha >= beta) break;
                }
            }
        }
        return bestScore;
    }

    private int minimizer(ICheckersBoard board, int depth, int alpha, int beta) {
        List<Point> validPieces = board.getValidPieces(CheckersPieceType.BLACK);
        if (board.isGameOver()) return INF;
        if (depth == maxDepth) return board.getBoardScore();

        int bestScore = INF;
        for (Point from : validPieces) {
            if (board.getShouldCapture()) {
                List<List<Point>> captureMove = board.captureMove(board.getPiece(from.getRow(), from.getCol()));
                for (List<Point> to : captureMove) {
                    CheckersBoard newBoard = new CheckersBoard(board.getBoard());
                    newBoard.setMove(from, to);
                    int totalScore = -1 * maximizer(newBoard, depth + 1, alpha, beta);
                    bestScore = Math.min(bestScore, totalScore);
                    if (alpha >= beta) break;
                }
            } else {
                List<Point> regularMove = board.regularMove(board.getPiece(from.getRow(), from.getCol()));
                for (Point to : regularMove) {
                    CheckersBoard newBoard = new CheckersBoard(board.getBoard());
                    newBoard.setMove(from, to);
                    int totalScore = -1 * maximizer(newBoard, depth + 1, alpha, beta);
                    bestScore = Math.min(bestScore, totalScore);
                    if (alpha >= beta) break;
                }
            }
        }
        return bestScore;
    }
}
