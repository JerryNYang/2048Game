package com.inyang.jerry.a2048game;

import android.graphics.Bitmap;

import com.inyang.jerry.a2048game.Sprite.TileX;

public interface TileManagerCallback {

    Bitmap getBitmap(int count);
    void finishMoving(TileX t);
    void updateScore(int delta);
    void reached2048();

}
