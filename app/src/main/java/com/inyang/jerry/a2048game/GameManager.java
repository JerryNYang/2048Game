package com.inyang.jerry.a2048game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.inyang.jerry.a2048game.Sprite.EndGame;
import com.inyang.jerry.a2048game.Sprite.Grid;
import com.inyang.jerry.a2048game.Sprite.Score;

public class GameManager extends SurfaceView implements SurfaceHolder.Callback, SwipeCallback , GameManageCallback {

    private static final String APP_NAME = "2048 clone";
    private MainThread thread;
    private Grid grid;
    private int scWidth, scHeight, standardSize;
    private TileManager tileManager;
    private boolean endGame = false;
    private EndGame endgameSprite;
    private Score score;
    private Bitmap restartButton;
    private int restartButtonX, restartButtonY, restartButtonSize;



    private SwipeListener swipe;

    public GameManager(Context context, AttributeSet attrs)
    {

        super(context,attrs);

        // intercept the OnTouchEvent and onSwipe
        setLongClickable(true);

        getHolder().addCallback(this);
        swipe = new SwipeListener(getContext(),this);

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        scWidth = dm.widthPixels;
        scHeight = dm.heightPixels;
        standardSize = (int)(scWidth * .88)/4;

        grid = new Grid(getResources(), scWidth, scHeight,standardSize);
        tileManager = new TileManager(getResources(), standardSize, scWidth,scHeight, this);

        endgameSprite = new EndGame(getResources(),scWidth,scHeight);
        score = new Score(getResources(),scWidth,scHeight,standardSize,getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));
        Bitmap bmpRestart = BitmapFactory.decodeResource(getResources(), R.drawable.restart);

        restartButtonSize = (int)getResources().getDimension(R.dimen.restart_button_size);
        restartButton = Bitmap.createScaledBitmap(bmpRestart, restartButtonSize,restartButtonSize,false);

        restartButtonX = scWidth  / 2 + 2 * standardSize - restartButtonSize;
        restartButtonY = scHeight  / 2 - 2 * standardSize - 3 * restartButtonSize;


    }

    public void initGame()
    {
        endGame = false;
        tileManager.InitGame();
        // score restart or reset to zero
        score = new Score(getResources(),scWidth,scHeight,standardSize,getContext().getSharedPreferences(APP_NAME,Context.MODE_PRIVATE));

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(holder, this);
        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceHolder(holder);

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        boolean retry = true;
        while(retry)
        {
            try {
                thread.setRunning((false));
                thread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }


        }

    }

    public void update()
    {
        if(!endGame)
        {
            tileManager.update();
        }


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // white background
        canvas.drawRGB(255,255,255);

        // draw grid
        grid.draw(canvas);
       // System.out.println("Test 2048 Game");
        tileManager.draw(canvas);
        score.draw(canvas);
        canvas.drawBitmap(restartButton,restartButtonX,restartButtonY,null);
        if(endGame)
        {
            endgameSprite.draw(canvas);

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(endGame)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                tileManager.InitGame();
            }

        }else {
            float eventX = event.getAxisValue(MotionEvent.AXIS_X);
            float eventY = event.getAxisValue(MotionEvent.AXIS_Y);
            if(event.getAction() == MotionEvent.ACTION_DOWN &&
                eventX > restartButtonX && eventX < restartButtonX + restartButtonSize &&
                    eventY > restartButtonY && eventY < restartButtonY + restartButtonSize)
            {
                initGame();
            }
            swipe.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSwipe(Direction direction) {

        tileManager.onSwipe(direction);
    }

    @Override
    public void gameOver() {

        endGame = true;

    }

    @Override
    public void updateScore(int delta) {

        score.updateScore(delta);

    }

    @Override
    public void reached2048() {

        score.reached2048();
    }
}
