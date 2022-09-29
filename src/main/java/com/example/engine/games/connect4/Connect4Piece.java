package com.example.engine.games.connect4;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.engine.Constants.PATH_CONNECT4;
import static com.example.engine.Constants.SPIRIT_SIZE_CONNECT4;

public class Connect4Piece {

    private final Connect4PieceType pieceType;

    public Connect4Piece(Connect4PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public Connect4PieceType getPieceType() {
        return pieceType;
    }

    public ImageView getSprite() {
        String location = PATH_CONNECT4 + pieceType.type + ".png";
        ImageView imageView = new ImageView(new Image(location));
        imageView.setFitWidth(SPIRIT_SIZE_CONNECT4);
        imageView.setFitHeight(SPIRIT_SIZE_CONNECT4);
        return imageView;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Connect4Piece piece = (Connect4Piece) obj;
        return this.pieceType == piece.pieceType;
    }
}
