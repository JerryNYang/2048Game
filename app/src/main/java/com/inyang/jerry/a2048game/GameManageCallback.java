package com.inyang.jerry.a2048game;

public interface GameManageCallback {

    void gameOver();
    void updateScore(int delta);
    void reached2048();

}
