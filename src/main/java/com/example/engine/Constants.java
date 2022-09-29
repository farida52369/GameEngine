package com.example.engine;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public final class Constants {

    // Basic games requirements
    public static final int XO_WIDTH = 3;
    public static final int CONNECT4_ROWS = 6;
    public static final int CONNECT4_COLS = 7;
    public static final int ROWS = 8;
    public static final int COLS = 8;

    // STYLING
    // Sprites dimensions
    public static final int SPIRIT_SIZE_XO = 120;
    public static final int SPIRIT_SIZE_CONNECT4 = 80;
    public static final int SQUARE_SIZE_CONNECT4 = 90;
    public static final int SPIRIT_SIZE = 64;

    // Making UX Responsive as possible
    public static final int PADDING_XO = 40;
    public static final int PADDING_CONNECT4 = 20;
    public static final int PADDING = 15;
    public static final int GAP = 1;

    // Who is playing NOW recognition ImageView Width and Height
    public static final int RECOGNIZE_PLAYER_WIDTH_CONNECT4 = 60;
    public static final int RECOGNIZE_PLAYER_HEIGHT_CONNECT4 = 40;
    public static final int RECOGNIZE_PLAYER_SIZE_XO = 40;
    public static final int RECOGNIZE_PLAYER = 40; // For checker, and Chess

    public static final int LABEL = 20;
    public static final int LABEL_WIDTH = 60;
    public static final int HEADER_FOOTER_HEIGHT = 60;

    public static final String BUTTON_ATTRIBUTES = "-fx-background-radius: 25px; -fx-background-color: #D46C4E;";
    public static final String FONT_FAMILY = "sans-serif";
    public static final int FONT_SIZE_LABEL = 26;
    public static final int FONT_SIZE_BUTTONS = 19;

    // Paths
    public static final String PATH_XO = "file:src/main/resources/images_xo/";
    public static final String PATH_CONNECT4 = "file:src/main/resources/images_connect4/";
    public static final String PATH_CHECKERS = "file:src/main/resources/images_checkers/";
    public static final String PATH_CHESS = "file:src/main/resources/images_chess/";
    public static final String PATH_GENERAL = "file:src/main/resources/images_games/";
    public static final String PATH_AUDIO = "file:src/main/resources/audio/";

    // States
    public static final char EMPTY = '0';
    public static final char HUMAN_PLAYER = '1';
    public static final char CPU_PLAYER = '2';

    // Colors
    public static final Background SKY_BLUE = new Background(new BackgroundFill(Color.SKYBLUE, null, null));
    public static final Background GREY = new Background(new BackgroundFill(Color.GRAY, null, null));
    public static final Background WHITE = new Background(new BackgroundFill(Color.WHITE, null, null));
    public static final Background GREEN = new Background(new BackgroundFill(Color.rgb(50, 205, 50), null, null));
    public static final Background BLACK_RED = new Background(new BackgroundFill(Color.rgb(128, 0, 0), null, null));
    public static final Background WHITE_RED = new Background(new BackgroundFill(Color.rgb(165, 42, 42), null, null));
    public static final Background BLACK_BLUE = new Background(new BackgroundFill(Color.rgb(0, 191, 255), null, null));
    public static final Background WHITE_BLUE = new Background(new BackgroundFill(Color.rgb(135, 206, 250), null, null));
}
