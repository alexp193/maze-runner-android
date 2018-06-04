package com.alex.myproj;

import android.graphics.PointF;

/**
 * Created by alex on 23/08/16.
 */

public interface ObstacleAdder {

    void wipeBalls();
    /** addBall
     * @param pointA point a.
     * @param pointB point b.
     * @param speed speed the ball. in pixel per seconds.
     */
    void addBall(PointF pointA, PointF pointB, double speed,float radius);
}
