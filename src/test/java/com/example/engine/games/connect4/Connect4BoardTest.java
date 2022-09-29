package com.example.engine.games.connect4;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

// make the board cols and rows changeable to make test easier
class Connect4BoardTest {

    @Test
    public void testAddPiece() {
        IConnect4Board board = new Connect4Board(2, 3);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertFalse(board.pieceCouldBeAdded(0));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(2, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.pieceCouldBeAdded(1));
        Assertions.assertTrue(board.pieceCouldBeAdded(2));
        System.out.println(board);
    }

    @Test
    public void testRemovingPiece() {
        IConnect4Board board = new Connect4Board(3, 2);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertEquals(new Connect4Piece(Connect4PieceType.RED), board.getPiece(2, 0));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertEquals(new Connect4Piece(Connect4PieceType.YELLOW), board.getPiece(1, 0));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertEquals(new Connect4Piece(Connect4PieceType.RED), board.getPiece(0, 0));
        Assertions.assertFalse(board.pieceCouldBeAdded(0));
        board.removePiece(0);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertEquals(new Connect4Piece(Connect4PieceType.YELLOW), board.getPiece(0, 0));

        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertFalse(board.pieceCouldBeAdded(1));
        board.removePiece(1);
        Assertions.assertTrue(board.pieceCouldBeAdded(1));
        System.out.println(board);
    }

    @Test
    public void testIsTie() {
        // pointless to fill 6x7 cell to test a tie :))))
        IConnect4Board board = new Connect4Board(3, 1);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.isTie());
        board.removePiece(0);
        Assertions.assertFalse(board.isTie());
        System.out.println(board);
    }

    @Test
    public void testIsWinVertically() {
        IConnect4Board board = new Connect4Board(4, 1);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.isWinner(Connect4PieceType.YELLOW));

        board.removePiece(0);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertFalse(board.isWinner(Connect4PieceType.YELLOW));
        Assertions.assertFalse(board.isWinner(Connect4PieceType.RED));
    }

    @Test
    public void testIsWinHorizontally() {
        IConnect4Board board = new Connect4Board(1, 4);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(2, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(3, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.isWinner(Connect4PieceType.YELLOW));

        board.removePiece(2);
        Assertions.assertTrue(board.addPiece(2, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertFalse(board.isWinner(Connect4PieceType.YELLOW));
        Assertions.assertFalse(board.isWinner(Connect4PieceType.RED));
    }

    @Test
    public void testIsWinPositiveDiagonally() {
        IConnect4Board board = new Connect4Board(4, 4);
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.YELLOW)));
        IntStream.range(0, 2).forEach(e -> board.addPiece(2, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(2, new Connect4Piece(Connect4PieceType.YELLOW)));
        IntStream.range(0, 3).forEach(e -> board.addPiece(3, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(3, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.isWinner(Connect4PieceType.YELLOW));
        board.removePiece(3);
        Assertions.assertTrue(board.addPiece(3, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertFalse(board.isWinner(Connect4PieceType.YELLOW));
        Assertions.assertTrue(board.isWinner(Connect4PieceType.RED));
    }

    @Test
    public void testIsWinNegativeDiagonally() {
        IConnect4Board board = new Connect4Board(4, 4);
        IntStream.range(0, 3).forEach(e -> board.addPiece(e, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(3, new Connect4Piece(Connect4PieceType.YELLOW)));
        IntStream.range(0, 3).forEach(e -> board.addPiece(e, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(1, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertTrue(board.addPiece(0, new Connect4Piece(Connect4PieceType.YELLOW)));
        Assertions.assertTrue(board.isWinner(Connect4PieceType.YELLOW));
        board.removePiece(3);
        Assertions.assertTrue(board.addPiece(3, new Connect4Piece(Connect4PieceType.RED)));
        Assertions.assertFalse(board.isWinner(Connect4PieceType.YELLOW));
        Assertions.assertTrue(board.isWinner(Connect4PieceType.RED));
    }
}