package com.example.engine.games.tic_tac_toe;

import com.example.engine.games.Point;

import java.util.LinkedList;
import java.util.List;

import static com.example.engine.Constants.*;

// "O" CPU player (number 2), "X" Human Player (number 1)
public class XOBoard implements IXOBoard {

    // Initialization :)
    private final char[][] board;
    private final int[][] winsComb = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    public XOBoard() {
        this.board = new char[XO_WIDTH][XO_WIDTH];
        for (int i = 0; i < XO_WIDTH; i++) {
            for (int j = 0; j < XO_WIDTH; j++) {
                this.board[i][j] = '0';
            }
        }
    }

    // For Testing
    public XOBoard(char[][] board) {
        this.board = board;
    }

    private boolean inBound(int i, int j) {
        return i >= 0 && i < board.length && j >= 0 && j < board[0].length;
    }

    private boolean pieceAtSpot(int i, int j) {
        return board[i][j] != EMPTY;
    }

    @Override
    public boolean validMoves(int i, int j) {
        return inBound(i, j) && !pieceAtSpot(i, j);
    }

    @Override
    public void addPiece(int i, int j, char player) {
        board[i][j] = player;
    }

    @Override
    public void removePiece(int i, int j) {
        board[i][j] = EMPTY;
    }


    @Override
    public boolean update(int i, int j, char player) {
        if (validMoves(i, j)) {
            addPiece(i, j, player);
            return true;
        }
        return false;
    }


    @Override
    public List<Point> emptySpots() {
        List<Point> spots = new LinkedList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == EMPTY)
                    spots.add(new Point(i, j));
            }
        }
        return spots;
    }

    @Override
    public boolean isTie() {
        for (char[] row : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (row[j] == EMPTY)
                    return false;
            }
        }
        return true;
    }

    @Override
    public boolean isWinner(int player) {
        for (int[] comb : winsComb) {
            int x1 = comb[0];
            int x2 = comb[1];
            int x3 = comb[2];

            if (board[x1 / 3][x1 % 3] == player &&
                    board[x2 / 3][x2 % 3] == player &&
                    board[x3 / 3][x3 % 3] == player)
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("** The Board **\n");
        for (char[] row : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (row[j] == HUMAN_PLAYER) s.append("X ");
                else if (row[j] == CPU_PLAYER) s.append("O ");
                else s.append("_ ");
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
