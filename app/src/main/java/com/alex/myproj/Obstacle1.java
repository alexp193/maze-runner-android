package com.alex.myproj;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.plattysoft.leonids.ParticleSystem;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by alex on 03/08/2016.
 */

public class Obstacle1 extends View implements ObstacleAdder{

    private SendingBall sendingBall;
    private ArrayList<Ball> balls;
    private Ball objectBallToRemove;
    private HeartManager heartManager;
    private View viewToParticleSystem;
    private GameControllerActivity gameControllerActivity;

    public Obstacle1(Context context) {
        this(context, null);
    }

    public Obstacle1(Context context, AttributeSet attrs) {
        super(context, attrs);
        balls=new ArrayList<>();
    }

    void init(SendingBall sendingBall,HeartManager heartManager,View viewToParticleSystem,GameControllerActivity gameControllerActivity) {
        this.sendingBall = sendingBall;
        this.viewToParticleSystem = viewToParticleSystem;
        this.heartManager = heartManager;
        this.gameControllerActivity = gameControllerActivity;
    }

    public void wipeBalls(){
        balls=new ArrayList<>();
    }//new ball


    public void addBall(PointF pointA,PointF pointB,double speed,float radius){//distance between 2 points
        double distance = Math.sqrt(
                Math.pow((pointB.x-pointA.x),2) +
                        Math.pow((pointA.y-pointB.y),2)
        );

        Paint paint = new Paint();
        Ball ball = new Ball(pointA,pointB,(int) ((distance/speed)*1000),paint,radius);//time*1000=milliSecond
        ball.randomColor();
        balls.add(ball);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        for (Ball ball:balls){
            ball.update();
        }

        if (objectBallToRemove != null){
            balls.remove(objectBallToRemove);
            objectBallToRemove = null;
        }

        for (Ball ball:balls)
            canvas.drawCircle(ball.point.x,ball.point.y,ball.radius,ball.paint);

        postInvalidate();//loop
    }

    private class Ball{
        private PointF KeyFrameA;
        private PointF KeyFrameB;
        private long TimeBetweenKey;

        private boolean backToKeyA = false;
        private long lestKeyTime = System.currentTimeMillis();

        private PointF point;
        private Paint paint;
        private float radius;

        /** storage ball and all mover algorithm.
         * @param myKeyFrameA point A.
         * @param myKeyFrameB point B.
         * @param TimeBetweenKey the time for ball to go from point A to point B.in milliseconds.
         * @param paint paint {@link Paint}.
         * @param radius radius.
         */
        Ball(PointF myKeyFrameA, PointF myKeyFrameB, int TimeBetweenKey, Paint paint, float radius) {
            this.KeyFrameA = myKeyFrameA;
            this.KeyFrameB = myKeyFrameB;
            this.TimeBetweenKey = TimeBetweenKey;
            this.paint = paint;
            this.radius = radius;
            this.point = new PointF(KeyFrameA.x,KeyFrameA.y);
        }

        private void update(){//update locations in track
            //location
            long time = System.currentTimeMillis() - lestKeyTime;
            long timeCount = time;

            if (backToKeyA)//from b to a its true
                timeCount = TimeBetweenKey - time;
            //find location between a and b
            point.x = ((KeyFrameB.x - KeyFrameA.x) * timeCount / TimeBetweenKey) + KeyFrameA.x;
            point.y = ((KeyFrameB.y - KeyFrameA.y) * timeCount / TimeBetweenKey) + KeyFrameA.y;


            if (time > TimeBetweenKey) {//if the ball finish 1 track to points b
                lestKeyTime = System.currentTimeMillis();
                backToKeyA = !backToKeyA;

                randomColor();
            }

            //Log.i("Obstacle1","time: "+time+" backToKeyA: "+backToKeyA);
            //Log.i("Obstacle1","A: " + KeyFrameA.toString() + "B: " + KeyFrameB.toString() + "Point: "+point.toString());

            //check if player touch the ball.
            double distance = Math.sqrt(
                    Math.pow(point.x - (sendingBall.getXLocation() + sendingBall.getBallWidth()/2),2) +
                            Math.pow(point.y - (sendingBall.getYLocation() + sendingBall.getBallHeight()/2),2)
            );

            if (distance < radius + sendingBall.getRadius()) {
                Log.i("Obstacle1", "player touch the ball~" + distance);
                heartManager.damage();
                objectBallToRemove = this;
                sendingBall.resetLocation();
                particleEffect();

                GameControllerActivity.soundPool.play(GameControllerActivity.particle, 1, 1, 0, 0, 1);
            }
        }
        private void particleEffect(){
            viewToParticleSystem.setX(point.x);
            viewToParticleSystem.setY(point.y);
            new ParticleSystem(gameControllerActivity, 100, R.drawable.star_pink, 5000)
                    .setSpeedRange(0.1f, 0.25f)
                    .setRotationSpeedRange(90, 180)
                    .setInitialRotationRange(0, 360)
                    .oneShot(viewToParticleSystem, 100);
        }
        void randomColor(){
            paint.setARGB(255,
                    new Random().nextInt(255),
                    new Random().nextInt(255),
                    new Random().nextInt(255)
            );
        }


    }
}
