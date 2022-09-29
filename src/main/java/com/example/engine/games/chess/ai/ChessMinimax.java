package com.example.engine.games.chess.ai;

import com.example.engine.games.Move;
import com.example.engine.games.Point;
import com.example.engine.games.chess.IChessBoard;
import com.example.engine.games.chess.pieces.ChessPiece;
import com.example.engine.games.chess.pieces.ChessPieceColor;
import com.example.engine.games.chess.pieces.ChessPieceType;
import com.example.engine.games.chess.pieces.Rook;

import java.util.List;

import static com.example.engine.Constants.COLS;
import static com.example.engine.Constants.ROWS;

public class ChessMinimax implements IChessMinimax {

    private final int maxDepth;
    private final int INF = 1000;
    private Move<Point> bestMove;

    public ChessMinimax(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public Move<Point> getCpuMove(IChessBoard board) {
        long start = System.currentTimeMillis();
        minimax(board, 0, -INF, INF, true);
        System.out.println();
        System.out.println("Time Taken in(ms): " + (System.currentTimeMillis() - start));
        return bestMove;
    }

    private int minimax(IChessBoard board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == maxDepth) // stalemate | reach max depth
            return 0;

        ChessPieceColor curColor = isMaximizing ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
        int bestScore = isMaximizing ? -INF : INF;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board.pieceAtCoordination(r, c) && !board.enemyAtCoordination(r, c, curColor.type)) {
                    ChessPiece curPiece = board.getPiece(r, c).clone();
                    boolean hasMovedBefore = curPiece.hasMoved(); // for castling
                    curPiece.setHasMoved(true);
                    // moves for curPiece
                    List<Point> moves = board.pieceAllPossibleMoves(r, c);
                    for (Point to : moves) {
                        int score = 0;
                        int toX = to.getRow(), toY = to.getCol();
                        ChessPiece destPiece = board.getPiece(toX, toY);
                        // Make the move
                        boolean promotion = false, castling = false;
                        int moveNews = board.makeMove(new Point(r, c), to, curColor);
                        if (moveNews == 1) {
                            promotion = true;
                            score += (isMaximizing ? 8 : -8);
                        } else if (moveNews == 2) {
                            castling = true;
                            score += (isMaximizing ? 12 : -12);
                        }

                        if (destPiece != null) score += destPiece.getPieceValue();
                        score += minimax(board, depth + 1, alpha, beta, !isMaximizing);
                        // Update Scores
                        if (depth == 0 && bestScore <= score && board.gameOver(curColor.type) == 0)
                            bestMove = new Move<>(new Point(r, c), to);
                        if (isMaximizing) {
                            bestScore = Math.max(bestScore, score);
                            alpha = Math.max(alpha, score);
                        } else {
                            bestScore = Math.min(bestScore, score);
                            beta = Math.min(beta, score);
                        }

                        // Unmake The move
                        curPiece.setHasMoved(hasMovedBefore);
                        if (promotion) {
                            board.setPiece(r, c, curPiece);
                            board.setPiece(toX, toY, destPiece);
                        } else if (castling) {
                            board.setPiece(r, toY == 2 ? 0 : 7, new Rook(r, toY == 2 ? 0 : 7, curColor));
                            board.setPiece(r, toY == 2 ? 2 : 5, null);
                            board.setPiece(r, toY == 2 ? 3 : 6, null);
                            board.setPiece(r, c, curPiece);
                            board.setKingPos(r, c, curColor.type);
                        } else {
                            board.setPiece(r, c, curPiece);
                            board.setPiece(toX, toY, destPiece);
                            if (curPiece.getClass().getSimpleName().equals(ChessPieceType.KING.name))
                                board.setKingPos(r, c, curColor.type);
                        }

                        if (beta <= alpha) break;
                    }
                }
            }
        }
        return bestScore;
    }

}
