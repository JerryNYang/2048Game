package com.inyang.jerry.a2048game;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeListener implements GestureDetector.OnGestureListener {

    private GestureDetector detector;
    private SwipeCallback callback;

    public SwipeListener(Context context, SwipeCallback callback)
    {
        this.callback = callback;
        detector = new GestureDetector(context, this);
    }

    public void onTouchEvent(MotionEvent event)
    {
        detector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    // tell a fling motion
    // e1 original motion
    // e2 final motion
    // velocityX and velocityY important
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if(Math.abs(velocityX) > Math.abs(velocityY))
        {
            if(velocityX > 0)
            {
                callback.onSwipe(SwipeCallback.Direction.RIGHT);
            }

            else
            {
                callback.onSwipe(SwipeCallback.Direction.LEFT);
            }

        }

        else
        {

            if(velocityY > 0)
            {
                callback.onSwipe(SwipeCallback.Direction.DOWN);
            }

            else
            {
                callback.onSwipe(SwipeCallback.Direction.UP);
            }

        }
        return false;
    }
}
