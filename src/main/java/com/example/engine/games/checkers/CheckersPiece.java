package com.example.engine.games.checkers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.engine.Constants.PATH_CHECKERS;
import static com.example.engine.Constants.SPIRIT_SIZE;

public class CheckersPiece implements Cloneable {

    private boolean isKing;
    private final CheckersPieceType pieceType;
    private int row, col;

    public CheckersPiece(CheckersPieceType pieceType, int row, int col) {
        this.pieceType = pieceType;
        this.row = row;
        this.col = col;
        this.isKing = false;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setCoOrdinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        this.isKing = true;
    }

    public CheckersPieceType getPieceType() {
        return pieceType;
    }

    public ImageView getSprite() {
        String location = PATH_CHECKERS;
        switch (pieceType) {
            case BLACK:
                location += (isKing ? "queen_" : "") + "black";
                break;
            case RED:
                location += (isKing ? "queen_" : "") + "red";
                break;
        }
        location += ".png";

        Image image = new Image(location);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SPIRIT_SIZE);
        imageView.setFitHeight(SPIRIT_SIZE);

        return imageView;
    }

    @Override
    public String toString() {
        return "Piece\n" + "row: " + row + " col: " + col + "\n" +
                (isKing ? "Is" : "Not") + " King!\n" +
                "Type: " + pieceType;
    }

    @Override
    public CheckersPiece clone() {
        try {
            return (CheckersPiece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
