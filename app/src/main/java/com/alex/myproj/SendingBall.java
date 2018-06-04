package com.alex.myproj;

/**
 * Created by alex on 14/05/2016.
 */

public interface SendingBall {
    public void resetLocation();

    public boolean getAnimationNextOn();

    public void NextLevelAnimationOn(OnAnimationEndListener onAnimationEndListener);

    public void addXLocation(int x);
    public void addYLocation(int y);
    public void setyMax(int screenHeight);

    public int getXLocation();
    public int getYLocation();
    public double getRadius();
    public int getBallWidth();
    public int getBallHeight();


    public void resize(int ballWidth,int ballHeight);
}

