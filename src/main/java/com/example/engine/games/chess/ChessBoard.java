package com.example.engine.games.chess;

import com.example.engine.games.Point;
import com.example.engine.games.chess.pieces.*;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.COLS;
import static com.example.engine.Constants.ROWS;

public class ChessBoard implements IChessBoard {

    private final ChessPiece[][] board;
    private Point whiteKing;
    private Point blackKing;

    public ChessBoard() {
        this.board = new ChessPiece[ROWS][COLS];
        this.whiteKing = new Point(7, 4);
        this.blackKing = new Point(0, 4);
        init();
    }

    private void init() {
        for (int j = 0; j < COLS; j++) {
            board[1][j] = new Pawn(1, j, ChessPieceColor.BLACK); // Pawn Black
            board[6][j] = new Pawn(6, j, ChessPieceColor.WHITE); // Pawn White
        }

        // Rook
        board[0][0] = new Rook(0, 0, ChessPieceColor.BLACK);
        board[0][7] = new Rook(0, 7, ChessPieceColor.BLACK);
        board[7][0] = new Rook(7, 0, ChessPieceColor.WHITE);
        board[7][7] = new Rook(7, 7, ChessPieceColor.WHITE);

        // Knight
        board[0][1] = new Knight(0, 1, ChessPieceColor.BLACK);
        board[0][6] = new Knight(0, 6, ChessPieceColor.BLACK);
        board[7][1] = new Knight(7, 1, ChessPieceColor.WHITE);
        board[7][6] = new Knight(7, 6, ChessPieceColor.WHITE);

        // Bishop
        board[0][2] = new Bishop(0, 2, ChessPieceColor.BLACK);
        board[0][5] = new Bishop(0, 5, ChessPieceColor.BLACK);
        board[7][2] = new Bishop(7, 2, ChessPieceColor.WHITE);
        board[7][5] = new Bishop(7, 5, ChessPieceColor.WHITE);

        // Queen
        board[0][3] = new Queen(0, 3, ChessPieceColor.BLACK);
        board[7][3] = new Queen(7, 3, ChessPieceColor.WHITE);

        // King
        board[0][4] = new King(0, 4, ChessPieceColor.BLACK);
        board[7][4] = new King(7, 4, ChessPieceColor.WHITE);
    }

    @Override
    public ChessPiece getPiece(int r, int c) {
        return board[r][c];
    }

    @Override
    public void setPiece(int r, int c, ChessPiece piece) {
        board[r][c] = piece;
        if (piece != null) piece.setPosition(r, c);
    }

    @Override
    public void setKingPos(int r, int c, boolean color) {
        if (color) this.whiteKing = new Point(r, c);
        else this.blackKing = new Point(r, c);
    }

    private boolean inBound(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLS;
    }

    @Override
    public boolean pieceAtCoordination(int row, int col) {
        return inBound(row, col) && getPiece(row, col) != null;
    }

    @Override
    public boolean enemyAtCoordination(int row, int col, boolean color) {
        if (pieceAtCoordination(row, col))
            return getPiece(row, col).getPieceColor().type != color;
        return false;
    }

    @Override
    public boolean validMove(int row, int col, boolean color) { // dest -> (row, col)
        return inBound(row, col) && (!pieceAtCoordination(row, col) || enemyAtCoordination(row, col, color));
    }

    private boolean kingInCheck(int row, int col, boolean color) {
        // QUEEN __ ROOK __ BISHOP
        // UP _ DOWN _ LEFT _ RIGHT
        int[][] straight = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        for (int[] d : straight) {
            for (int i = 1; i < ROWS; i++) {
                int newR = row + d[0] * i, newC = col + d[1] * i;
                if (inBound(newR, newC)) {
                    ChessPiece piece = getPiece(newR, newC);
                    if (piece != null) {
                        if (color == piece.getPieceColor().type) break;
                        if (piece.getClass().getSimpleName().equals(ChessPieceType.QUEEN.name) ||
                                piece.getClass().getSimpleName().equals(ChessPieceType.ROOK.name))
                            return true;
                        break;
                    }
                } else break;
            }
        }
        // LEFT UP _ RIGHT UP _ LEFT DOWN _ LEFT UP
        int[][] diagonal = {{1, 1}, {-1, -1}, {-1, 1}, {1, -1}};
        for (int[] d : diagonal) {
            for (int i = 1; i < ROWS; i++) {
                int newR = row + d[0] * i, newC = col + d[1] * i;
                if (inBound(newR, newC)) {
                    ChessPiece piece = getPiece(newR, newC);
                    if (piece != null) {
                        if (color == piece.getPieceColor().type) break;
                        if (piece.getClass().getSimpleName().equals(ChessPieceType.QUEEN.name) ||
                                piece.getClass().getSimpleName().equals(ChessPieceType.BISHOP.name))
                            return true;
                        break;
                    }
                } else break;
            }
        }
        // KNIGHT CHECK
        for (int i = row - 2; i <= row + 2; i++) {
            for (int j = col - 2; j <= col + 2; j++) {
                if (inBound(i, j)) {
                    if ((Math.abs(row - i) == 2 && Math.abs(col - j) == 1) || (Math.abs(row - i) == 1 && Math.abs(col - j) == 2)) {
                        ChessPiece piece = getPiece(i, j);
                        if (piece != null) {
                            if (color == piece.getPieceColor().type) continue;
                            if (piece.getClass().getSimpleName().equals(ChessPieceType.KNIGHT.name))
                                return true;
                        }
                    }
                }
            }
        }
        // PAWN CHECK DIAGONALLY LEFT _ RIGHT
        int change = color ? -1 : 1;
        int newR = row + change, newC = col + change;
        if (enemyAtCoordination(newR, newC, color))
            if (getPiece(newR, newC).getClass().getSimpleName().equals(ChessPieceType.PAWN.name))
                return true;
        newR = row + change;
        newC = col - change;
        if (enemyAtCoordination(newR, newC, color))
            if (getPiece(newR, newC).getClass().getSimpleName().equals(ChessPieceType.PAWN.name))
                return true;
        // KING CHECK __ SQUARE
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                if (inBound(r, c)) {
                    if (r == row && c == col) continue;
                    if (enemyAtCoordination(r, c, color))
                        if (getPiece(r, c).getClass().getSimpleName().equals(ChessPieceType.KING.name))
                            return true;
                }
            }
        }
        // PASS ALL THE CHECKS? THE KING NOT UNDER ATTACK
        return false;
    }

    private boolean kingNotInCheckAfterMove(int fromX, int fromY, int toX, int toY, int xKing, int yKing) {
        ChessPiece srcPiece = board[fromX][fromY].clone();
        ChessPiece destPiece = board[toX][toY];
        boolean inCheck = false;
        // save king coordination in case of play around
        if (srcPiece.getClass().getSimpleName().equals(ChessPieceType.KING.name)) {
            xKing = toX;
            yKing = toY;
        }
        board[toX][toY] = srcPiece;
        board[toX][toY].setPosition(toX, toY);
        board[fromX][fromY] = null;
        // if the king will be under attack, then we can't take this move
        if (kingInCheck(xKing, yKing, srcPiece.getPieceColor().type)) inCheck = true;
        // restore pieces
        board[fromX][fromY] = srcPiece;
        board[fromX][fromY].setPosition(fromX, fromY);
        board[toX][toY] = destPiece;
        if (destPiece != null) board[toX][toY].setPosition(toX, toY);
        return !inCheck;
    }

    @Override
    public List<Point> pieceAllPossibleMoves(int row, int col) {
        ChessPiece piece = getPiece(row, col);
        // Set king co-ordinations
        int xKing, yKing;
        if (piece.getPieceColor().type) {
            xKing = whiteKing.getRow();
            yKing = whiteKing.getCol();
        } else {
            xKing = blackKing.getRow();
            yKing = blackKing.getCol();
        }
        // Get legal moves
        List<Point> moves = new LinkedList<>();
        List<Point> pieceMoves = piece.validMoves(this);
        for (Point p : pieceMoves) {
            if (kingNotInCheckAfterMove(row, col, p.getRow(), p.getCol(), xKing, yKing))
                moves.add(p);
        }
        // Castling
        if (row == xKing && col == yKing) { // means we are dealing with king
            boolean color = piece.getPieceColor().type;
            // left and right Castling
            if (!kingInCheck(row, col, color)) { // Condition 0: king basically not in check
                for (int i = -1; i <= 1; i += 2) {
                    if (inBound(row, col + (i == -1 ? -4 : 3))) {
                        ChessPiece rook = getPiece(row, col + (i == -1 ? -4 : 3));
                        // Condition 1: king and rook never moved before
                        if (!piece.hasMoved() && pieceAtCoordination(row, col + (i == -1 ? -4 : 3)) &&
                                rook.getClass().getSimpleName().equals(ChessPieceType.ROOK.name) && !rook.hasMoved()) {
                            // Condition 2: between king and rook nothing at all
                            if (!pieceAtCoordination(row, col + i) && !pieceAtCoordination(row, col + 2 * i)) {
                                if (i == -1) if (pieceAtCoordination(row, col + 3 * i)) continue;
                                // Condition 3: the king can't pass through check
                                if (!kingInCheck(row, col + i, color) && !kingInCheck(row, col + 2 * i, color)) {
                                    moves.add(new Point(row, col + 2 * i));
                                }
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    // 0 -> regular move
    // 1 -> promotion move
    // 2 -> castling move
    @Override
    public int makeMove(Point from, Point to, ChessPieceColor curColor) {
        int r = from.getRow(), c = from.getCol();
        int toX = to.getRow(), toY = to.getCol();
        ChessPiece curPiece = getPiece(r, c);
        // Promotion
        if ((toX == 0 || toX == 7) && curPiece.getClass().getSimpleName().equals(ChessPieceType.PAWN.name)) {
            this.setPiece(toX, toY, new Queen(toX, toY, curColor));
            this.setPiece(r, c, null);
            return 1;
        }
        // Castling
        else if ((toY == 6 || toY == 2) && (toX == 0 || toX == 7) && (c == 4 && (r == 0 || r == 7)) &&
                curPiece.getClass().getSimpleName().equals(ChessPieceType.KING.name)) {
            ChessPiece rook = this.getPiece(r, c + (toY == 6 ? 3 : -4)).clone();
            rook.setHasMoved(true);
            this.setPiece(toX, toY, curPiece);
            this.setPiece(toX, toY + (toY == 6 ? -1 : 1), rook);
            this.setPiece(r, c, null);
            this.setPiece(r, c + (toY == 6 ? 3 : -4), null);
            this.setKingPos(toX, toY, curColor.type);
            return 2;
        }
        // Regular Move
        this.setPiece(toX, toY, curPiece);
        this.setPiece(r, c, null);
        // if the moved was king, then we need to update
        if (curPiece.getClass().getSimpleName().equals(ChessPieceType.KING.name))
            this.setKingPos(toX, toY, curColor.type);
        return 0;
    }

    // 0 -> no checkmate | stalemate | draw
    // 1 -> checkmate
    // 2 -> stalemate (tie)
    @Override
    public int gameOver(boolean color) {
        int xKing, yKing;
        if (color) {
            xKing = whiteKing.getRow();
            yKing = whiteKing.getCol();
        } else {
            xKing = blackKing.getRow();
            yKing = blackKing.getCol();
        }
        int legalMoves = 0, same = 0, oppo = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                ChessPiece piece = getPiece(r, c);
                if (pieceAtCoordination(r, c)) {
                    if (color == piece.getPieceColor().type) {
                        legalMoves += pieceAllPossibleMoves(r, c).size();
                        same++;
                    } else oppo++;
                }
            }
        }
        // Stalemate | Draw
        if ((legalMoves == 0 && !kingInCheck(xKing, yKing, color)) || (same == oppo && same == 1)) return 2;
        if (legalMoves == 0) return 1; // Checkmate
        return 0; // not game over
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("The Board:\n");
        for (int r = 0; r < ROWS; r++) {
            s.append("| ");
            for (int c = 0; c < COLS; c++) {
                ChessPiece piece = getPiece(r, c);
                if (piece == null) s.append('\u005F').append(" | ");
                else {
                    String pieceName = piece.getClass().getSimpleName();
                    if (pieceName.equals(ChessPieceType.PAWN.name))
                        s.append(!piece.getPieceColor().type ? '\u2659' : '\u265F').append(" | ");
                    else if (pieceName.equals(ChessPieceType.BISHOP.name))
                        s.append(!piece.getPieceColor().type ? '\u2657' : '\u265D').append(" | ");
                    else if (pieceName.equals(ChessPieceType.KNIGHT.name))
                        s.append(!piece.getPieceColor().type ? '\u2658' : '\u265E').append(" | ");
                    else if (pieceName.equals(ChessPieceType.ROOK.name))
                        s.append(!piece.getPieceColor().type ? '\u2656' : '\u265C').append(" | ");
                    else if (pieceName.equals(ChessPieceType.QUEEN.name))
                        s.append(!piece.getPieceColor().type ? '\u2655' : '\u265B').append(" | ");
                    else if (pieceName.equals(ChessPieceType.KING.name))
                        s.append(!piece.getPieceColor().type ? '\u2654' : '\u265A').append(" | ");
                }
            }
            s.append("\n");
        }
        s.append("\n");
        System.out.println("white king: " + whiteKing.getRow() + " " + whiteKing.getCol());
        System.out.println("black king: " + blackKing.getRow() + " " + blackKing.getCol());
        return s.toString();
    }
}
