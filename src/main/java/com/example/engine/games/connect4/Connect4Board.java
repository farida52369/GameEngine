package com.example.engine.games.connect4;

import java.nio.charset.StandardCharsets;

// Human -> 1, CPU -> 2
public class Connect4Board implements IConnect4Board {

    // Initialization :)
    private final Connect4Piece[][] board;
    private final int rows, cols;
    private final int[][] MOVES = {
            {1, 0}, // Horizontal Check
            {0, 1}, // Vertical Check
            {1, 1}, // Positive Slope Diagonal
            {1, -1}, // Negative Slope Diagonal
    };

    public Connect4Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Connect4Piece[rows][cols];
    }

    private boolean inBoundCol(int col) {
        return col >= 0 && col < board[0].length;
    }

    @Override
    public int getCols() {
        return cols;
    }

    @Override
    public int nextRowToAddIn(int c) {
        for (int r = rows - 1; r >= 0; r--) {
            if (board[r][c] == null)
                return r;
        }
        return -1;
    }

    @Override
    public boolean pieceCouldBeAdded(int col) {
        return nextRowToAddIn(col) != -1;
    }

    private boolean validMove(int col) {
        return inBoundCol(col) && pieceCouldBeAdded(col);
    }

    @Override
    public boolean addPiece(int col, Connect4Piece piece) {
        if (validMove(col)) {
            board[nextRowToAddIn(col)][col] = piece;
            return true;
        }
        return false;
    }

    @Override
    public void removePiece(int col) {
        if (inBoundCol(col)) {
            int r = nextRowToAddIn(col);
            board[r == -1 ? 0 : r + 1][col] = null;
        }
    }

    @Override
    public Connect4Piece getPiece(int r, int c) {
        return board[r][c];
    }

    @Override
    public boolean isTie() {
        for (Connect4Piece[] r : board) {
            for (int c = 0; c < cols; c++) {
                if (r[c] == null) return false;
            }
        }
        return true;
    }

    private boolean notInRange(int i, int j) {
        return i < 0 || i >= rows || j < 0 || j >= cols;
    }

    @Override
    public boolean isWinner(Connect4PieceType player) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null || board[i][j].getPieceType() != player) continue;
                // The Four Directions Check
                for (int[] move : MOVES) {
                    boolean fourMatch = true;
                    for (int k = 0; k < 4; k++) {
                        int r = i + move[1] * k;
                        int c = j + move[0] * k;
                        if (notInRange(r, c) || board[r][c] == null || board[r][c].getPieceType() != player) {
                            fourMatch = false;
                            break;
                        }
                    }
                    if (fourMatch) return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getBoardScore(int col, Connect4PieceType player) {
        int val = nextRowToAddIn(col);
        int row = val == -1 ? 0 : val + 1, score = 0;
        for (int[] move : MOVES) {
            int dx = move[1];
            int dy = move[0];
            // Current, and Future 4 in a line.
            for (int i = 0; i < 4; i++) {
                score += windowScore(row - dx * i, col - dy * i, dx, dy, player);
            }
        }
        // for Human player, minus the score
        return player == Connect4PieceType.YELLOW ? score : -1 * score;
    }

    private int windowScore(int row, int col, int dx, int dy, Connect4PieceType curPlayer) {
        int player = 0, oppPlayer = 0;
        for (int i = 0; i < 4; i++) {
            if (notInRange(row, col)) return 0;
            if (board[row][col] != null) {
                if (board[row][col].getPieceType() == curPlayer) player++;
                else oppPlayer++;
            }
            row += dx;
            col += dy;
        }
        if (oppPlayer > 0)
            return player > 1 ? 0 : 10 * oppPlayer;
        return player == 4 ? 40 : 3 * player;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        byte[] red = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x94, (byte) 0xB4};
        byte[] yellow = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x9F, (byte) 0xA1};
        byte[] empty = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x94, (byte) 0xB5};
        String redBall = new String(red, StandardCharsets.UTF_8);
        String yellowBall = new String(yellow, StandardCharsets.UTF_8);
        String emptyBall = new String(empty, StandardCharsets.UTF_8);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == null) s.append(emptyBall).append("  ");
                else {
                    if (board[i][j].getPieceType() == Connect4PieceType.YELLOW)
                        s.append(yellowBall).append("  ");
                    else s.append(redBall).append("  ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }

}
