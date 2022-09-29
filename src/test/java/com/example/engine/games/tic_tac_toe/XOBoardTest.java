package com.example.engine.games.tic_tac_toe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.example.engine.Constants.CPU_PLAYER;
import static com.example.engine.Constants.HUMAN_PLAYER;

class XOBoardTest {

    @Test
    public void testAddPieceToEmptySpot() {
        IXOBoard board = new XOBoard();
        Assertions.assertEquals(9, board.emptySpots().size());
        Assertions.assertTrue(board.validMoves(0, 0));
        Assertions.assertTrue(board.update(0, 0, HUMAN_PLAYER));
        Assertions.assertFalse(board.update(0, 0, HUMAN_PLAYER));
        Assertions.assertFalse(board.update(0, 0, CPU_PLAYER));
        Assertions.assertEquals(8, board.emptySpots().size());
        Assertions.assertTrue(board.validMoves(1, 1));
        Assertions.assertTrue(board.update(1, 1, CPU_PLAYER));
        Assertions.assertEquals(7, board.emptySpots().size());
        Assertions.assertTrue(board.validMoves(2, 2));
        Assertions.assertTrue(board.update(2, 2, HUMAN_PLAYER));
        Assertions.assertEquals(6, board.emptySpots().size());
    }

    @Test
    public void testRemovePieceFromSpecificSpot() {
        IXOBoard board = new XOBoard();
        Assertions.assertTrue(board.validMoves(1, 1));
        board.addPiece(1, 1, HUMAN_PLAYER);
        Assertions.assertTrue(board.validMoves(1, 2));
        board.addPiece(1, 2, CPU_PLAYER);
        Assertions.assertTrue(board.validMoves(0, 2));
        board.addPiece(0, 2, HUMAN_PLAYER);
        Assertions.assertTrue(board.validMoves(2, 0));
        board.addPiece(2, 0, CPU_PLAYER);
        Assertions.assertEquals(5, board.emptySpots().size());
        System.out.println(board);

        Assertions.assertFalse(board.validMoves(1, 1));
        board.removePiece(1, 1);
        Assertions.assertTrue(board.validMoves(1, 1));
        Assertions.assertEquals(6, board.emptySpots().size());
        System.out.println(board);

        board.removePiece(2, 0);
        board.removePiece(0, 2);
        Assertions.assertEquals(8, board.emptySpots().size());
        System.out.println(board);
    }

    @Test
    public void testIsTie() {
        // Human -> 1
        // CPU -> 2
        char[][] board_1 = {
                {'1', '2', '1'},
                {'1', '2', '2'},
                {'2', '1', '1'}
        };
        IXOBoard board = new XOBoard(board_1);
        Assertions.assertTrue(board.isTie());
        Assertions.assertEquals(0, board.emptySpots().size());
    }

    @Test
    public void testTheWinner() {
        char[][] board_1 = {
                {'1', '2', '1'},
                {'1', '2', '2'},
                {'2', '1', '1'}
        };
        IXOBoard board = new XOBoard(board_1);
        Assertions.assertFalse(board.isWinner(CPU_PLAYER));
        Assertions.assertFalse(board.isWinner(HUMAN_PLAYER));
    }

    @Test
    public void testTheWinnerVertically() {
        char[][] board_2 = {
                {'0', '0', '2'},
                {'0', '1', '2'},
                {'1', '1', '2'}
        };
        IXOBoard board = new XOBoard(board_2);
        Assertions.assertFalse(board.isWinner(HUMAN_PLAYER));
        Assertions.assertTrue(board.isWinner(CPU_PLAYER));
    }

    @Test
    public void testTheWinnerHorizontally() {
        char[][] board_2 = {
                {'0', '0', '1'},
                {'2', '2', '2'},
                {'1', '1', '0'}
        };
        IXOBoard board = new XOBoard(board_2);
        Assertions.assertFalse(board.isWinner(HUMAN_PLAYER));
        Assertions.assertTrue(board.isWinner(CPU_PLAYER));
    }

    @Test
    public void testTheWinnerDiagonally() {
        char[][] board_2 = {
                {'0', '0', '1'},
                {'2', '1', '2'},
                {'1', '2', '0'}
        };
        IXOBoard board = new XOBoard(board_2);
        // Although This is impossible in practice, human can't beat cpu
        // at least in tic-tac-toe :)
        Assertions.assertTrue(board.isWinner(HUMAN_PLAYER));
        Assertions.assertFalse(board.isWinner(CPU_PLAYER));
    }

}