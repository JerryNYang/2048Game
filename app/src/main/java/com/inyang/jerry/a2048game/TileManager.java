package com.inyang.jerry.a2048game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.service.quicksettings.Tile;

//import android.service.quicksettings.Tile;

import com.inyang.jerry.a2048game.Sprite.Sprite;
import com.inyang.jerry.a2048game.Sprite.TileX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class TileManager implements TileManagerCallback, Sprite {

    private Resources resources;
    private int standardSize;
    private int screenWidth;
    private int screenHeight;
   // private TileX t;
    private ArrayList<Integer> drawables = new ArrayList<>();
    private HashMap<Integer, Bitmap> tileBitmaps = new HashMap<>();
    private TileX[][] matrix = new TileX[4][4];
    private boolean moving = false;
    private ArrayList<TileX> movingTiles;
    private boolean toSpawn = false;
    private boolean endGame = false;
    private GameManageCallback callback;


    public TileManager(Resources resources, int standardSize, int screenWidth, int screenHeight, GameManageCallback callback) {
        this.resources = resources;
        this.standardSize = standardSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.callback = callback;

        InitBitmap();
        InitGame();
//        t = new TileX(standardSize, screenWidth, screenHeight, this, 1, 1);
//        matrix[1][1] = t;



    }

    @Override
    public void finishMoving(TileX t) {

        movingTiles.remove(t);
        if(movingTiles.isEmpty())
        {
            moving = false;
            spawn();
            checkEndgame();
        }


    }

    private void checkEndgame()
    {
        endGame = true;
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if(matrix[i][j] == null)
                {
                    endGame = false;
                    break;
                }
            }
        }
        if(endGame)
        {
            for(int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {

                    if((i > 0 && matrix[i-1][j].getValue() == matrix[i][j].getValue()) ||
                            (i < 3 && matrix[i+1][j].getValue() == matrix[i][j].getValue()) ||
                            (j < 0 && matrix[i][j-1].getValue() == matrix[i][j].getValue()) ||
                            (j < 3 && matrix[i][j+1].getValue() == matrix[i][j].getValue())) {

                        endGame = false;
                    }
                }
            }
        }


    }

    private void spawn()
    {
        if(toSpawn)
        {
            toSpawn = false;
            TileX t = null;

            while (t == null)
            {
                int x = new Random().nextInt(4);
                int y = new Random().nextInt(4);
                if(matrix[x][y] == null)
                {
                    t = new TileX(standardSize,screenWidth,screenHeight, this,x,y);
                    matrix[x][y] = t;

                }

            }
        }

    }



    public void InitGame() {

        matrix = new TileX[4][4];

        movingTiles = new ArrayList<>();
        for(int i  = 0; i < 2; i++)
        {
            int x = new Random().nextInt(4);
            int y = new Random().nextInt(4);

            if(matrix[x][y] == null)
            {
                TileX tile = new TileX(standardSize, screenWidth, screenHeight, this, x ,y);
                matrix[x][y] = tile;
            }

            else
            {
                // postfix
                i--;
            }
        }

//        for(int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                if( i != 3 || j != 3)
//                {
//                    TileX t = new TileX(standardSize, screenWidth, screenHeight, this,i,j, 3 * i + j + 4);
//                    matrix[i][j] = t;
//
//                }
//
//            }
//        }

    }

    private void InitBitmap()
    {
        drawables.add(R.drawable.one);
        drawables.add(R.drawable.two);
        drawables.add(R.drawable.three);
        drawables.add(R.drawable.four);
        drawables.add(R.drawable.five);
        drawables.add(R.drawable.six);
        drawables.add(R.drawable.seven);
        drawables.add(R.drawable.eight);


        drawables.add(R.drawable.nine);
        drawables.add(R.drawable.ten);
        drawables.add(R.drawable.eleven);
        drawables.add(R.drawable.twelve);
        drawables.add(R.drawable.thirteen);
        drawables.add(R.drawable.fourteen);
        drawables.add(R.drawable.fifteen);
        drawables.add(R.drawable.sixteen);

        for(int i = 1; i <= 16; i++)
        {

            Bitmap bmp = BitmapFactory.decodeResource(resources,drawables.get(i-1));
            Bitmap tilebmp = Bitmap.createScaledBitmap(bmp, standardSize,standardSize,false);
             // 16 bitmap...
            tileBitmaps.put(i, tilebmp);
        }
    }

    @Override
    public Bitmap getBitmap(int count) {
        return tileBitmaps.get(count);
    }



    @Override
    public void draw(Canvas canvas) {

       // t.draw(canvas);
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if(matrix[i][j] != null)
                {
                    matrix[i][j].draw(canvas);
                }
            }
        }
        if(endGame)
        {
            callback.gameOver();

        }
    }

    @Override
    public void update() {
        //t.update();
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if(matrix[i][j] != null)
                {
                    matrix[i][j].update();
                }
            }
        }
    }

    public void onSwipe(SwipeCallback.Direction direction)
    {
        if (!moving) {
            moving = true;
            TileX[][] newMatrix = new TileX[4][4];

            switch (direction) {
                case UP:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null) {
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = i - 1; k >= 0; k--) {
                                    if (newMatrix[k][j] == null) {
                                        newMatrix[k][j] = matrix[i][j];
                                        if (newMatrix[k + 1][j] == matrix[i][j]) {
                                            newMatrix[k + 1][j] = null;
                                        }
                                    } else if (newMatrix[k][j].getValue() == matrix[i][j].getValue() && !newMatrix[k][j].toIncrement()) {
                                        newMatrix[k][j] = matrix[i][j].increment();
                                        if (newMatrix[k + 1][j] == matrix[i][j]) {
                                            newMatrix[k + 1][j] = null;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;

                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t) {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null) {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }
                        }
                    }
                    break;

                case DOWN:
                    for (int i = 3; i >= 0; i--) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null) {
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = i + 1; k < 4; k++) {
                                    if (newMatrix[k][j] == null) {
                                        newMatrix[k][j] = matrix[i][j];
                                        if (newMatrix[k - 1][j] == matrix[i][j]) {
                                            newMatrix[k - 1][j] = null;
                                        }
                                    } else if (newMatrix[k][j].getValue() == matrix[i][j].getValue() && !newMatrix[k][j].toIncrement()) {
                                        newMatrix[k][j] = matrix[i][j].increment();
                                        if (newMatrix[k - 1][j] == matrix[i][j]) {
                                            newMatrix[k - 1][j] = null;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 3; i >= 0; i--) {
                        for (int j = 0; j < 4; j++) {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t) {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null) {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }
                        }
                    }
                    break;

                case LEFT:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null) {
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = j - 1; k >= 0; k--) {
                                    if (newMatrix[i][k] == null) {
                                        newMatrix[i][k] = matrix[i][j];
                                        if (newMatrix[i][k + 1] == matrix[i][j]) {
                                            newMatrix[i][k + 1] = null;
                                        }
                                    } else if (newMatrix[i][k].getValue() == matrix[i][j].getValue() && !newMatrix[i][k].toIncrement()) {
                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if (newMatrix[i][k + 1] == matrix[i][j]) {
                                            newMatrix[i][k + 1] = null;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t) {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null) {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }
                        }
                    }
                    break;

                case RIGHT:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 3; j >= 0; j--) {
                            if (matrix[i][j] != null) {
                                newMatrix[i][j] = matrix[i][j];
                                for (int k = j + 1; k < 4; k++) {
                                    if (newMatrix[i][k] == null) {
                                        newMatrix[i][k] = matrix[i][j];
                                        if (newMatrix[i][k - 1] == matrix[i][j]) {
                                            newMatrix[i][k - 1] = null;
                                        }
                                    } else if (newMatrix[i][k].getValue() == matrix[i][j].getValue() && !newMatrix[i][k].toIncrement()) {
                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if (newMatrix[i][k - 1] == matrix[i][j]) {
                                            newMatrix[i][k - 1] = null;
                                        }
                                    } else {
                                        break;
                                    }

                                }
                            }
                        }
                    }


                    for (int i = 0; i < 4; i++) {
                        for (int j = 3; j >= 0; j--) {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++) {
                                for (int b = 0; b < 4; b++) {
                                    if (newMatrix[a][b] == t) {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if (newT != null) {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }
                        }
                    }
                    break;
            }

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (newMatrix[i][j] != matrix[i][j]) {
                        toSpawn = true;
                        break;
                    }
                }
            }
            matrix = newMatrix;
        }
        ///////////////////////////////////////////
        /* if(!moving) {
           //moving = true;
            TileX[][] newMatrix = new TileX[4][4];

            switch (direction) {
                case UP:
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (matrix[i][j] != null) {
                                newMatrix[i][j] = matrix[i][j];
                                for(int k = i - 1; k >= 0; k--)
                                {
                                    if(newMatrix[k][j] == null)
                                    {
                                        newMatrix[k][j] = matrix[i][j];
                                        if(newMatrix[k+1][j] == matrix[i][j])
                                        {
                                            newMatrix[k+1][j] = null;
                                        }

                                    }else if(newMatrix[k][j].getValue() == matrix[k][j].getValue() && !newMatrix[k][j].toIncrement()){

                                        newMatrix[k][j] = matrix[i][j].increment();
                                        if(newMatrix[k+1][j] == matrix[i][j])
                                        {
                                                newMatrix[k+1][j] = null;
                                        }
                                    }else {
                                        break;

                                    }
                                }
                            }
                        }
                    }
                    for(int i = 0; i < 4; i++)
                    {
                        for(int j = 0; j < 4; j++)
                        {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;

                            for(int a = 0; a < 4; a++)
                            {
                                for(int b = 0; b < 4; b++)
                                {
                                    if(newMatrix[a][b] ==  t)
                                    {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;


                                    }

                                }

                            }
                            if(newT != null)
                            {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }
                        }
                    }
                    break;
                case DOWN:
                    //t.move(3,1);
                    for(int i = 3; i >= 0; i--)
                    {
                        for(int j = 0; j < 4; j++)
                        {
                            if(matrix[i][j] != null)
                            {
                                newMatrix[i][j] = matrix[i][j];
                                for(int k = i + 1; k < 4; k++)
                                {
                                   if(newMatrix[k][j] == null)
                                   {
                                       newMatrix[k][j] = matrix[i][j];
                                       if(newMatrix[k-1][j] == matrix[i][j])
                                       {
                                           newMatrix[k-1][j] = null;
                                       }
                                   }else if(newMatrix[k-1][j].getValue() == matrix[i][j].getValue() && !newMatrix[k][j].toIncrement())
                                   {
                                       newMatrix[k][j] = matrix[i][j].increment();
                                       if(newMatrix[k-1][j] == matrix[i][j])
                                       {
                                           newMatrix[k-1][j] = null;
                                       }
                                   }else{
                                       break;
                                   }
                                }

                            }
                        }
                    }
                    for(int i = 3; i >=0; i--)
                    {
                        for(int j = 0; j < 4; j++)
                        {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for(int a = 0; a < 4; a++)
                            {
                                for(int b = 0; b < 4; b++)
                                {
                                    if(newMatrix[a][b] == t)
                                    {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }
                                }
                            }
                            if(newT != null)
                            {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }

                        }
                    }

                    break;
                case LEFT:
                    for(int i = 0; i < 4; i++)
                    {
                        for(int j = 0; j < 4; j++)
                        {
                            if(matrix[i][j] != null)
                            {
                                newMatrix[i][j] = matrix[i][j];
                                for(int k = j - 1; k >= 0; k--)
                                {
                                    if(newMatrix[i][k] == null)
                                    {
                                        newMatrix[i][k] = matrix[i][j];
                                        if(newMatrix[i][k+1] == matrix[i][j])
                                        {
                                            newMatrix[i][k+1] = null;


                                        }

                                    }else if (newMatrix[i][k].getValue() == newMatrix[i][j].getValue() && !newMatrix[i][k].toIncrement() )
                                    {

                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if(newMatrix[i][k+1] == matrix[i][j])
                                        {
                                            newMatrix[i][k+1] = null;
                                        }
                                    }else
                                    {
                                        break;
                                    }
                                }
                            }

                        }
                    }
                    for(int i = 0; i < 4; i++)
                    {
                        for(int j = 0; j < 4; j++) {
                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++)
                            {
                                for(int b = 0; b < 4; b++)
                                {
                                  if(newMatrix[a][b] == t)
                                  {
                                      newT = newMatrix[a][b];
                                      matrixX = a;
                                      matrixY = b;
                                      break;
                                  }

                                }
                            }
                            if(newT != null)
                            {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }
                        }
                    }
                    // t.move(1,0);
                    break;

                case RIGHT:
                    for(int i = 0; i < 4; i++)
                    {
                        for(int j = 3; j >= 0; j--)
                        {
                            if(matrix[i][j] != null){
                                newMatrix[i][j] = matrix[i][j];
                                for(int k = j + 1; k < 4; k++)
                                {
                                    if(newMatrix[i][k] == null)
                                    {
                                        newMatrix[i][k] = matrix[i][j];
                                        if(newMatrix[i][k-1] == matrix[i][j])
                                        {
                                            newMatrix[i][k-1] = null;
                                        }
                                    }else if(newMatrix[i][k].getValue() == matrix[i][j].getValue() && !newMatrix[i][k].toIncrement()) {

                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if(newMatrix[i][k-1] == matrix[i][j])
                                        {
                                            newMatrix[i][k-1] = null;

                                        }
                                    }else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for(int i = 0; i < 4; i++)
                    {
                        for(int j = 3; j >= 0; j--)
                        {
                            if(matrix[i][j] != null){
                                newMatrix[i][j] = matrix[i][j];
                                for(int k = j + 1; k < 4; k++)
                                {
                                    if(newMatrix[i][k] == null)
                                    {
                                        newMatrix[i][k] = matrix[i][j];
                                        if(newMatrix[i][k-1] == matrix[i][j])
                                        {
                                            newMatrix[i][k-1] = null;
                                        }
                                    }else if(newMatrix[i][k].getValue() == matrix[i][j].getValue() && !newMatrix[i][k].toIncrement()) {

                                        newMatrix[i][k] = matrix[i][j].increment();
                                        if(newMatrix[i][k-1] == matrix[i][j])
                                        {
                                            newMatrix[i][k-1] = null;

                                        }
                                    }else {
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for(int i = 0; i < 4; i++) {

                        for (int j = 3; j >= 0; j--) {

                            TileX t = matrix[i][j];
                            TileX newT = null;
                            int matrixX = 0;
                            int matrixY = 0;
                            for (int a = 0; a < 4; a++)
                            {
                                for(int b = 0; b < 4; b++)
                                {
                                    if(newMatrix[a][b] == t)
                                    {
                                        newT = newMatrix[a][b];
                                        matrixX = a;
                                        matrixY = b;
                                        break;
                                    }

                                }
                            }
                            if(newT != null)
                            {
                                movingTiles.add(t);
                                t.move(matrixX, matrixY);
                            }


                        }
                    }

                    // t.move(1,3);
                    break;
            }

            for(int i = 0; i < 4; i++)
            {
                for(int j = 0; j < 4; j++) {
                    if (newMatrix[i][j] != matrix[i][j])
                    {
                        toSpawn = true;
                        break;
                    }
                }
            }
            matrix = newMatrix;




        }

        */
    }

    @Override
    public void updateScore(int delta) {

            callback.updateScore(delta);


    }

    @Override
    public void reached2048() {

        callback.reached2048();
    }
}

