package com.alex.myproj;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by alex on 14/05/2016.
 */



public class AnimatedView extends ImageView implements SendingBall {//extends view

    private static final String LOG_TAG = "AnimatedView";
    private Bitmap mBitmap;
    public int []arr;
    private int xMax;
    private int yMax;

    public int xLocation = 0;
    public int yLocation = 0;


    private boolean AnimationNextOn = false;
    private long AnimationStartCurrentTimeMilli;
    private int ballWidth = 90;
    private int ballHeight = 90;
    private Rect dst;
    private OnAnimationEndListener onAnimationEndListener;

    public AnimatedView(Context context) {
        this(context, null);
    }

    public AnimatedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimatedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball2);
        mBitmap = Bitmap.createScaledBitmap(bitmap, ballWidth, ballHeight, true);
        dst = new Rect(0,0,100,100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        xMax = getMeasuredWidth() - ballWidth;
        yMax = getMeasuredHeight() - ballHeight;

        //the first location of the ball
        xLocation = xMax / 2;
        yLocation = yMax - 30;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = ballWidth;
        int height = ballHeight;


        if(AnimationNextOn) {
            long time =System.currentTimeMillis()- AnimationStartCurrentTimeMilli;//the time from animation start

            float scale = 1 - time/2000f; //2 second.
            width = (int)( ballWidth*scale);
            height = (int)(ballHeight*scale);

            if (scale < 0){
                onAnimationEndListener.onEnd();
                AnimationNextOn = false;
            }
        }

        dst.left = xLocation;
        dst.top = yLocation;
        dst.right = xLocation + width;
        dst.bottom = yLocation + height;
        canvas.drawBitmap(mBitmap, null, dst,null);

        invalidate();
    }

    public void moveBall(float xValue, float yValue) {

        xLocation -= (int) xValue;
        yLocation += (int) yValue;


        //Log.d(LOG_TAG, "xLocation:   " + xLocation);
        //Log.d(LOG_TAG, "yLocation:   " + yLocation);

        //out of display
        if (xLocation > xMax) {
            xLocation = xMax;
        } else if (xLocation < -xMax) {
            xLocation = -xMax;
        }
        if (yLocation > yMax) {
            yLocation = yMax;
        } else if (yLocation < -yMax) {
            yLocation = -yMax;
        }

        if (xLocation < 0) {
            xLocation = 0;
        }
        if (yLocation < 0) {
            yLocation = 0;
        }


    }

    public void resize(int ballWidth,int ballHeight){
        this.ballWidth = ballWidth;
        this.ballHeight = ballHeight;
    }


    public double getRadius(){
        return ballWidth/2;
    }
    public int getXLocation() {
        return xLocation;
    }

    public int getYLocation() {
        return yLocation;
    }

    public int getBallWidth() {
        return ballWidth;
    }

    public int getBallHeight() {
        return ballHeight;
    }

    public void setyMax(int screenHeight) {
        yMax = screenHeight - ballHeight;
    }

    public void addXLocation(int x){
        xLocation += x;
    }
    public void addYLocation(int y){
        yLocation += y;
    }



    @Override
    public void resetLocation() {
        xMax = getMeasuredWidth() - ballWidth;
        yMax = getMeasuredHeight() - ballHeight;

        xLocation = xMax / 2;
        yLocation = yMax - 30;
    }

    @Override
    public boolean getAnimationNextOn() {
        return AnimationNextOn;
    }

    @Override
    public void NextLevelAnimationOn(OnAnimationEndListener onAnimationEndListener)
    {
        GameControllerActivity.soundPool.play(GameControllerActivity.soundLevelID, 1, 1, 0, 0, 1);

        AnimationNextOn=true;
        AnimationStartCurrentTimeMilli=System.currentTimeMillis();
        this.onAnimationEndListener = onAnimationEndListener;
    }




}