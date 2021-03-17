package com.inyang.jerry.a2048game.Sprite;

import android.graphics.Canvas;

import com.inyang.jerry.a2048game.Sprite.Sprite;
import com.inyang.jerry.a2048game.TileManagerCallback;

import java.util.Random;


public class TileX implements Sprite {

        private int screenWidth, screenHeight, stardardSize;
        private TileManagerCallback callback;

        // 2^(3) = 8
        private int count = 3;
        private int currentX, currentY;
        private int destinationX, destinationY;
        private boolean moving = false;
        private int speed = 200;

        private boolean increment = false;

    public TileX(int standardSize, int screenWidth , int screenHeight, TileManagerCallback callback, int matrixX , int matrixY, int count )
    {
        this(standardSize, screenWidth,screenHeight,callback,matrixX , matrixY);
        this.count = count;

    }

        public TileX(int standardSize, int screenWidth , int screenHeight, TileManagerCallback callback, int matrixX , int matrixY)
        {
            this.stardardSize = standardSize;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.callback = callback;

            // set or initialize currentX, currentY , destionationX, destinationY

            currentX = destinationX = screenWidth / 2 - 2 * standardSize + matrixY * standardSize;
            currentY = destinationY = screenHeight / 2 - 2 * standardSize + matrixX * standardSize;

            int chance = new Random().nextInt(100);

            // a 10% chance of element of 2^(2)
            if(chance >= 90)
            {
                count = 2;
            }
        }

        public void move(int matrixX, int matrixY)
        {
            moving = true;
            destinationX = screenWidth / 2 - 2 * stardardSize + matrixY *stardardSize;
            destinationY = screenHeight / 2 - 2 * stardardSize + matrixX * stardardSize;


        }

        public int getValue()
        {
            return count;
        }

        public TileX increment()
        {
            increment = true;
            return this;
        }


        public boolean toIncrement()
        {
            return increment;
        }

        @Override
        public void draw(Canvas canvas) {

            // move number tile up 1
            canvas.drawBitmap(callback.getBitmap(count), currentX, currentY, null);
            if(moving && currentX == destinationX && currentY == destinationY)
            {
                moving = false;
                if(increment)
                {
                    count++;
                    increment = false;
                    int amount = (int)Math.pow(2,count);
                    callback.updateScore(amount);
                    if(count == 11)
                    {
                        callback.reached2048();
                    }

                }
                callback.finishMoving(this);
            }
        }

        @Override
        public void update() {

            if(currentX < destinationX)
            {
                if(currentX + speed > destinationX)
                {
                    currentX = destinationX;
                }

                else
                {
                    currentX += speed;
                }
            }

            else if(currentX > destinationX)
            {
                if(currentX - speed < destinationX)
                {
                    currentX = destinationX;
                }
                else
                {
                    currentX -= speed;
                }

            }
            if(currentY < destinationY)
            {
                if(currentY + speed > destinationY)
                {
                    currentY = destinationY;
                }
                else
                {
                    currentY += speed;

                }
            }
            else if(currentY > destinationY)
            {
                if(currentY - speed  < destinationY)
                {
                    currentY = destinationY;

                }
                else {
                    currentY -= speed;
                }
            }



        }
    }

