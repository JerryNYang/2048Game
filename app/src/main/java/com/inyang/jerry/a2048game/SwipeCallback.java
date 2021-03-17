package com.inyang.jerry.a2048game;

public interface SwipeCallback {

    void onSwipe(Direction direction);

    enum Direction
    {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
