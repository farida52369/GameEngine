package com.example.engine.games;

public class Move <T> {

    public Point from;
    public T to;

    public Move(Point from, T to) {
        this.from = from;
        this.to = to;
    }
}
