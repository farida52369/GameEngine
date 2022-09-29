package com.example.engine.games.checkers;

import com.example.engine.games.Point;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.COLS;
import static com.example.engine.Constants.ROWS;

// Red   -> AI
// Black -> Human
public class CheckersBoard implements ICheckersBoard {

    public final int[][] MOVES = {
            {1, 1}, {1, -1}, // Standard Red Moves
            {-1, 1}, {-1, -1} // Standard Black Moves
    };
    private final CheckersPiece[][] board;
    // Weights
    private final double[] WEIGHTS = {5, 10, 2, 3, -3, 6, 7};
    private boolean shouldCapture;
    private boolean gameOver;

    public CheckersBoard() {
        this.board = new CheckersPiece[ROWS][COLS];
        this.shouldCapture = false;
        this.gameOver = false;
        init();
    }

    // For Testing and Clone the board
    public CheckersBoard(CheckersPiece[][] board) {
        this.board = new CheckersPiece[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] != null)
                    this.board[r][c] = board[r][c].clone();
            }
        }
    }

    private void init() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (((r + c) & 1) != 0) {
                    if (r < 3) { // Black Pieces
                        board[r][c] = new CheckersPiece(CheckersPieceType.RED, r, c);
                    } else if (r > 4) { // Red Pieces
                        board[r][c] = new CheckersPiece(CheckersPieceType.BLACK, r, c);
                    }
                }
            }
        } // Initialize Drawing Red _ Black Pieces

        /*
         // * TESTING
        board[0][3] = new CheckersPiece(CheckersPieceType.BLACK, 0, 3);
        board[0][3].makeKing();
        board[2][1] = new CheckersPiece(CheckersPieceType.RED, 2, 1);
        board[4][1] = new CheckersPiece(CheckersPieceType.RED, 4, 1);
        board[3][6] = new CheckersPiece(CheckersPieceType.RED, 3, 6);
        board[3][4] = new CheckersPiece(CheckersPieceType.RED, 3, 4);
        board[1][4] = new CheckersPiece(CheckersPieceType.RED, 1, 4);
        board[1][2] = new CheckersPiece(CheckersPieceType.RED, 1, 2);

        board[6][3] = new CheckersPiece(CheckersPieceType.BLACK, 6, 3);
        board[5][2] = new CheckersPiece(CheckersPieceType.RED, 5, 2);
        board[5][4] = new CheckersPiece(CheckersPieceType.RED, 5, 4);
        board[3][4] = new CheckersPiece(CheckersPieceType.RED, 3, 4);
        board[1][4] = new CheckersPiece(CheckersPieceType.RED, 1, 4);
        board[1][2] = new CheckersPiece(CheckersPieceType.RED, 1, 2);
        */
    }

    private boolean inBound(int r, int c) {
        return r >= 0 && r < ROWS && c >= 0 && c < COLS;
    }

    @Override
    public boolean isEmptyCell(int r, int c) {
        return getPiece(r, c) == null;
    }

    @Override
    public boolean samePlayer(int r, int c, CheckersPieceType player) {
        return getPiece(r, c).getPieceType() == player;
    }

    @Override
    public CheckersPiece getPiece(int r, int c) {
        return board[r][c];
    }

    @Override
    public CheckersPiece[][] getBoard() {
        return board;
    }

    private boolean isKing(int r, int c) {
        return board[r][c].isKing();
    }

    @Override
    public boolean getShouldCapture() {
        return shouldCapture;
    }

    @Override
    public List<Point> getValidPieces(CheckersPieceType player) {
        List<Point> res;
        res = canCapture(player) ? validPiecesCapture(player) : validPiecesMove(player);
        gameOver = res.isEmpty();
        return res;
    }

    private List<Point> validPiecesCapture(CheckersPieceType player) {
        List<Point> moves = new LinkedList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!isEmptyCell(r, c) && samePlayer(r, c, player)) {
                    // King -> capture from the 4 directions and more :)
                    // Pawn -> capture from the 4 directions
                    boolean canCapture = false;
                    for (int[] p : MOVES) {
                        for (int i = 0; i < (isKing(r, c) ? 8 : 1); i++) {
                            int newR = r + p[0] * i, newC = c + p[1] * i;
                            if (inBound(newR, newC)) {
                                if (canCapture(player, newR, newC, p[0], p[1])) {
                                    moves.add(new Point(r, c));
                                    canCapture = true;
                                    break;
                                }
                            } else break;
                        }
                        if (canCapture) break;
                    }
                }
            }
        }
        return moves;
    }

    private List<Point> validPiecesMove(CheckersPieceType player) {
        List<Point> res = new LinkedList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!isEmptyCell(r, c) && samePlayer(r, c, player)) {
                    // King       -> can move in four diagonal directions [0,4)
                    // Red Pawn   -> 2 Diagonal Down [0:2)
                    // Black Pawn -> 2 Diagonal Up [2,4)
                    int start = 0, end = MOVES.length;
                    if (!isKing(r, c)) {
                        switch (player) {
                            case RED:
                                end = 2;
                                break;
                            case BLACK:
                                start = 2;
                                break;
                        }
                    }
                    for (int i = start; i < end; i++) {
                        int dx = r + MOVES[i][0], dy = c + MOVES[i][1];
                        if (inBound(dx, dy) && isEmptyCell(dx, dy)) {
                            res.add(new Point(r, c));
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }

    private boolean canCapture(CheckersPieceType player) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!isEmptyCell(r, c) && samePlayer(r, c, player)) {
                    for (int[] p : MOVES) {
                        for (int i = 0; i < (isKing(r, c) ? 8 : 1); i++) {
                            int newR = r + p[0] * i, newC = c + p[1] * i;
                            if (inBound(newR, newC)) {
                                if (canCapture(player, newR, newC, p[0], p[1])) {
                                    return shouldCapture = true;
                                }
                            } else break;
                        }
                    }
                }
            }
        }
        return shouldCapture = false;
    }

    private boolean canCapture(CheckersPieceType curPlayer, int r, int c, int dx, int dy) {
        if (!isEmptyCell(r, c) && !samePlayer(r, c, curPlayer)) return false;
        int r_1 = r + dx, c_1 = c + dy; // Enemy Piece
        int r_2 = r + dx * 2, c_2 = c + dy * 2; // Empty Piece
        if (inBound(r_1, c_1) && inBound(r_2, c_2)) {
            return !isEmptyCell(r_1, c_1) &&
                    !samePlayer(r_1, c_1, curPlayer) &&
                    isEmptyCell(r_2, c_2);
        }
        return false;
    }

    // get valid Moves
    @Override
    public List<List<Point>> captureMove(CheckersPiece piece) {
        List<List<Point>> moves = new LinkedList<>();
        int r = piece.getRow(), c = piece.getCol();
        dfs(piece.getPieceType(), r, c, new boolean[ROWS][COLS], new LinkedList<>(), moves, isKing(r, c));
        return moves;
    }

    private void dfs(CheckersPieceType curPlayer, int r, int c, boolean[][] vis,
                     List<Point> t, List<List<Point>> res, boolean isKing) {
        vis[r][c] = true;
        boolean allFour = true;
        for (int[] p : MOVES) {
            for (int i = 0; i < (isKing ? 8 : 1); i++) {
                int newR = r + p[0] * i, newC = c + p[1] * i;
                int dx_2 = newR + p[0] * 2, dy_2 = newC + p[1] * 2;
                if (inBound(dx_2, dy_2)) {
                    vis[newR][newC] = true; // needed when the piece is king
                    if (!vis[dx_2][dy_2] && canCapture(curPlayer, newR, newC, p[0], p[1])) {
                        allFour = false;
                        t.add(new Point(dx_2, dy_2));
                        dfs(curPlayer, dx_2, dy_2, vis, t, res, isKing);
                        t.remove(t.size() - 1);
                    } else if (vis[dx_2][dy_2]) break;
                } else break;
            }
        }
        if (allFour) res.add(new LinkedList<>(t));
    }

    @Override
    public List<Point> regularMove(CheckersPiece piece) {
        List<Point> moves = new LinkedList<>();
        int r = piece.getRow(), c = piece.getCol();
        // King       -> can move in four diagonal directions [0,4)
        // Red Pawn   -> 2 Diagonal Down [0:2)
        // Black Pawn -> 2 Diagonal Up [2,4)
        int start = 0, end = MOVES.length;
        if (!isKing(r, c)) {
            switch (piece.getPieceType()) {
                case RED:
                    end = 2;
                    break;
                case BLACK:
                    start = 2;
                    break;
            }
        }
        for (int i = start; i < end; i++) {
            for (int j = 1; j < (isKing(r, c) ? 8 : 2); j++) {
                int dx = r + MOVES[i][0] * j, dy = c + MOVES[i][1] * j;
                if (inBound(dx, dy)) {
                    if (isEmptyCell(dx, dy)) {
                        moves.add(new Point(dx, dy));
                    } else break;
                } else break;
            }
        }
        return moves;
    }

    @Override
    public void setMove(Point from, Point to) {
        int r_1 = from.getRow(), c_1 = from.getCol();
        int r_2 = to.getRow(), c_2 = to.getCol();
        board[r_2][c_2] = board[r_1][c_1];
        board[r_2][c_2].setCoOrdinate(r_2, c_2);
        if (r_2 == 0 || r_2 == 7) {
            board[r_2][c_2].makeKing();
        }
        board[r_1][c_1] = null; // set from to null
    }

    @Override
    public void setMove(Point from, List<Point> to) {
        int r_1 = from.getRow(), c_1 = from.getCol();
        int r_2 = -1, c_2 = -1;
        for (Point p : to) {
            r_2 = p.getRow();
            c_2 = p.getCol();
            int len = Math.abs(r_2 - r_1);
            int dx = (r_2 - r_1) / len;
            int dy = (c_2 - c_1) / len;
            for (int i = 1; i <= len; i++) {
                int resR = r_1 + i * dx, resC = c_1 + i * dy;
                board[resR][resC] = null;
            }
            // Update
            r_1 = r_2;
            c_1 = c_2;
        }
        r_1 = from.getRow();
        c_1 = from.getCol();
        board[r_2][c_2] = board[r_1][c_1];
        board[r_2][c_2].setCoOrdinate(r_2, c_2);
        if (r_2 == 0 || r_2 == 7) {
            board[r_2][c_2].makeKing();
        }
        board[r_1][c_1] = null; // set from to null
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    private int[] heuristic() {
        int n = WEIGHTS.length;
        // 0: # of Pawns
        // 1: # of Kings
        // 2: # of centrally positioned checkers in middle box
        // 3: # of centrally positioned checkers in non-middle box
        // 4: # of checkers that can be captured this turn
        // 5: # of protected checkers (adjacent to the edge of the board)
        // 6: # of checkers positioned in last row
        int[] humanScore = new int[n];
        int[] cpuScore = new int[n];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (!isEmptyCell(r, c)) {
                    if (samePlayer(r, c, CheckersPieceType.BLACK)) { // Human Player
                        // Pawn, or King
                        if (isKing(r, c)) humanScore[1]++;
                        else humanScore[0]++;
                        if (r == 0 || r == 7) {
                            humanScore[5]++;
                            humanScore[6]++;
                        } else {
                            // Checking if this piece can be captured
                            for (int[] p : MOVES) {
                                if (canCapture(CheckersPieceType.BLACK, r, c, p[0], p[1]))
                                    humanScore[4]++;
                            }
                            // Checking for centrally positioned
                            if (r == 3 || r == 4) {
                                if (c >= 2 && c <= 5) humanScore[2]++;
                                else humanScore[3]++;
                            }
                            // Checking for protected checkers
                            if (c == 0 || c == 7) humanScore[5]++;
                            else {
                                boolean allOkay = true;
                                for (int[] move : MOVES) {
                                    int dx_1 = r + move[0], dy_1 = c + move[1];
                                    int dx_2 = r - move[0], dy_2 = c - move[1];
                                    if ((isEmptyCell(dx_1, dy_1) && !isEmptyCell(dx_2, dy_2) && samePlayer(dx_2, dy_2, CheckersPieceType.RED)) ||
                                            (isEmptyCell(dx_2, dy_2) && !isEmptyCell(dx_1, dy_1) && samePlayer(dx_1, dy_1, CheckersPieceType.RED)))
                                        allOkay = false;
                                }
                                if (allOkay) humanScore[5]++;
                            }
                        }
                    } else { // CPU __ red
                        // Pawn, or King
                        if (isKing(r, c)) cpuScore[1]++;
                        else cpuScore[0]++;
                        if (r == 0 || r == 7) {
                            cpuScore[5]++;
                            cpuScore[6]++;
                        } else {
                            // Checking if this piece can be captured
                            for (int[] p : MOVES) {
                                if (canCapture(CheckersPieceType.RED, r, c, p[0], p[1]))
                                    cpuScore[4]++;
                            }
                            // Checking for centrally positioned
                            if (r == 3 || r == 4) {
                                if (c >= 2 && c <= 5) cpuScore[2]++;
                                else cpuScore[3]++;
                            }
                            // Checking for protected checkers
                            if (c == 0 || c == 7) cpuScore[5]++;
                            else {
                                boolean allOkay = true;
                                for (int[] move : MOVES) {
                                    int dx_1 = r + move[0], dy_1 = c + move[1];
                                    int dx_2 = r - move[0], dy_2 = c - move[1];
                                    if ((isEmptyCell(dx_1, dy_1) && !isEmptyCell(dx_2, dy_2) && samePlayer(dx_2, dy_2, CheckersPieceType.BLACK)) ||
                                            (isEmptyCell(dx_2, dy_2) && !isEmptyCell(dx_1, dy_1) && samePlayer(dx_1, dy_1, CheckersPieceType.BLACK)))
                                        allOkay = false;
                                }
                                if (allOkay) cpuScore[5]++;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) cpuScore[i] -= humanScore[i];
        return cpuScore;
    }

    @Override
    public int getBoardScore() {
        int[] heuristic = heuristic();
        double res = 0.0;
        for (int i = 0; i < heuristic.length; i++) {
            res += heuristic[i] * WEIGHTS[i];
        }
        return (int) res;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("    00   01   02   03   04   05   06   07\n");
        for (int r = 0; r < ROWS; r++) {
            s.append(r).append(" │ ");
            for (int c = 0; c < COLS; c++) {
                if (board[r][c] == null) s.append("__ │ ");
                else {
                    switch (board[r][c].getPieceType()) {
                        case BLACK:
                            s.append(board[r][c].isKing() ? "Q" : "B").append("B │ ");
                            break;
                        case RED:
                            s.append(board[r][c].isKing() ? "Q" : "R").append("R │ ");
                            break;
                    }
                }
            }
            s.append("\n");
        }
        return s.toString();
    }
}
