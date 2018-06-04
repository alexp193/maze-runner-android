package com.alex.myproj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 27/06/2016.
 */

public class LevelManagement{
    final String LOG_TAG = "myLogs";
    private MazeView mazeView;
    private ObstacleAdder obstacleAdder;
    private int currentLevel = 0;
    Context context;

    String str;
    String goodPlay;
    String lev;
    String goodRecord;
    long max = 0;
    long min;
    long goodRes;



    public LevelManagement(MazeView mazeView,ObstacleAdder obstacleAdder) {
        this.mazeView = mazeView;
        this.obstacleAdder = obstacleAdder;
    }

    public void moveToNextLevel() {
        currentLevel++;

        switch (currentLevel) {
            case 1:
                mazeView.control.startTimer();
                mazeView.tileType = new int[][]{   //level1
                        {0, 0, 0, 0, 0, 0, 0, 0,0,0,0},
                        {0, 4, 2, 2, 2, 2, 2, 2,2,0,0},
                        {0, 0, 0, 0, 0, 0, 0, 2,2,0,0},
                        {0, 0, 0, 2, 2, 2, 2, 2,2,0,0},
                        {0, 0, 0, 2, 0, 0, 0, 0,0,0,0},
                        {0, 2, 2, 2, 0, 0, 0, 0,0,0,0},
                        {0, 2, 2, 2, 0, 0, 0, 0,0,0,0},
                        {0, 2, 2, 2, 0, 0, 0, 0,0,0,0},
                        {0, 0, 2, 0, 0, 0, 2, 2,2,2,0},
                        {0, 0, 2, 2, 2, 2, 2, 0,0,2,0},
                        {0, 0, 0, 0, 0 ,0, 0, 0,0,2,0},
                        {2, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {2, 0, 0, 0, 0, 0, 0, 0,0,0,0},
                        {2, 0, 0, 0, 0, 0, 0, 0,0,0,0},
                        {2, 2, 2, 2, 2, 2, 0, 0,0,0,0},
                        {2, 2, 2, 2, 2, 2, 0, 0,0,0,0}
                };


                break;
            case 2:
                mazeView.control.startTimer();
                mazeView.tileType = new int[][]{
                        {0, 0, 0, 0, 0, 0, 0, 0,0,0,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 0, 2, 2, 2, 2, 0,0,2,0},
                        {0, 2, 0, 2, 2, 2, 2, 0,0,2,0},
                        {0, 2, 0, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 0, 0, 0, 0, 0, 0,0,2,0},
                        {0, 2, 2, 2, 2, 4, 0, 0,0,2,0},
                        {0, 0, 0, 0, 0, 0, 0, 2,2,2,2},
                        {0, 0, 0, 0, 0, 0, 0, 0,0,2,0},
                        {2, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 0, 0, 0, 0, 0,0,0,0},
                        {0, 2, 0, 0, 0 ,0, 0, 0,0,0,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,0,0},
                        {0, 0, 2, 2, 0, 0, 0, 0,2,0,0},
                        {0, 0, 2, 2, 0, 2, 2, 2,2,0,0},
                        {0, 0, 0, 0, 0, 2, 0, 0,0,0,0}
                };

                obstacleAdder.wipeBalls();
                addObstacle1(3,13,3,15,100,40);
                addObstacle1(1,10,5,10,200,40);
                addObstacle1(8,8,11,8,200,40);

                addObstacle1(4,2,7,5,300,40);
                addObstacle1(7,2,4,5,300,40);

                mazeView.sendingBall.resetLocation();
                mazeView.postInvalidate();
                break;
            case 3:
                mazeView.control.startTimer();
                mazeView.tileType = new int[][]{
                        {5, 0, 0, 0, 2, 4, 2, 0,0,0,0},
                        {2, 2, 2, 0, 2, 0, 2, 2,2,2,2},
                        {2, 0, 2, 0, 2, 0, 5, 5,5,2,2},
                        {2, 0, 2, 0, 2, 0, 5, 5,2,2,2},
                        {2, 0, 2, 0, 2, 2, 0, 2,2,2,5},
                        {2, 0, 2, 0, 5, 2, 0, 2,2,2,5},
                        {2, 0, 2, 0, 2, 2, 0, 2,2,2,5},
                        {2, 0, 2, 0, 2, 0, 2, 2,2,2,2},
                        {2, 0, 2, 0, 2, 2, 0, 0,5,2,2},
                        {2, 0, 2, 0, 5, 2, 0, 5,5,2,2},
                        {2, 0, 2, 0, 2, 2, 0, 5,5,2,2},
                        {2, 0, 2, 0, 2 ,0, 2, 2,2,2,2},
                        {2, 0, 2, 0, 2, 0, 2, 0,0,0,0},
                        {2, 0, 2, 2, 2, 0, 2, 2,2,2,2},
                        {2, 0, 5, 0, 0, 0, 0, 0,0,0,2},
                        {2, 2, 2, 2, 2, 2, 2, 2,2,2,2}
                };

                obstacleAdder.wipeBalls();
                addObstacle1(7,8,11,8,100,40);
                addObstacle1(11,2,11,4,200,40);
                addObstacle1(11,10,11,12,300,40);
                addObstacle1(9,5,9,7,200,40);


                mazeView.sendingBall.resetLocation();
                mazeView.postInvalidate();
                break;
            case 4://////////////////////////////////////////////////////////////////////////////
                mazeView.control.startTimer();
                mazeView.tileType = new int[][]{
                        {2, 2, 2, 2, 2, 2, 2, 2,2,2,2},
                        {2, 0, 0, 0, 0, 0, 0, 0,0,0,2},
                        {2, 0, 2, 2, 2, 2, 2, 2,2,0,2},
                        {2, 5, 2, 0, 0, 0, 0, 0,2,0,2},
                        {2, 0, 2, 0, 2, 2, 2, 0,2,0,2},
                        {2, 5, 2, 0, 2, 0, 2, 0,2,0,2},
                        {2, 0, 2, 0, 2, 0, 2, 0,2,0,2},
                        {2, 0, 2, 0, 2, 0, 2, 0,2,0,2},
                        {2, 5, 2, 0, 2, 0, 2, 0,2,0,2},
                        {2, 0, 2, 0, 2, 0, 4, 0,2,0,2},
                        {2, 5, 2, 0, 2, 0, 0, 0,2,0,2},
                        {2, 0, 2, 0, 2 ,2, 2, 2,2,0,2},
                        {2, 0, 2, 0, 0, 0, 5, 2,2,0,2},
                        {2, 0, 2, 2, 2, 2, 2, 0,0,0,2},
                        {2, 0, 0, 0, 0, 5, 2, 2,0,0,2},
                        {2, 2, 2, 2, 2, 2, 0, 2,2,2,2}
                };

                obstacleAdder.wipeBalls();
                addObstacle1(1,1,11,2,200,10);
                addObstacle1(11,1,10,16,200,10);
                addObstacle1(8,16,6,14,300,20);

                mazeView.sendingBall.resetLocation();
                mazeView.postInvalidate();
                break;
            case 5:
                mazeView.control.startTimer();
                mazeView.tileType = new int[][]{
                        {0, 0, 0, 0, 0, 0, 0, 0,0,0,0},
                        {0, 2, 2, 2, 4, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2 ,2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 2, 2, 2, 2, 2, 2, 2,2,2,0},
                        {0, 0, 0, 0, 0, 2, 0, 0,0,0,0}
                };

                obstacleAdder.wipeBalls();
                addObstacle1(2,15,10,15,100,20);
                addObstacle1(10,14,2,14,120,25);
                addObstacle1(2,13,10,13,700,30);
                addObstacle1(10,12,2,12,160,35);
                addObstacle1(10,11,2,11,200,10);
                addObstacle1(10,10,2,10,500,20);
                addObstacle1(10,9,2,9,100,30);
                addObstacle1(10,8,2,8,300,15);
                addObstacle1(10,7,2,7,400,5);
                addObstacle1(10,6,2,6,350,30);
                addObstacle1(10,5,2,5,600,20);
                addObstacle1(10,4,2,4,500,10);
                addObstacle1(10,3,2,3,200,50);
                mazeView.sendingBall.resetLocation();
                mazeView.postInvalidate();
                break;
            case 6:
            Intent i = new Intent(mazeView.control, GameOver.class);
            mazeView.control.startActivity(i);
            mazeView.control.finish();
                break;

        }


    }

    int getCurrentLevel() {
        return currentLevel;
    }


    private void addObstacle1(int colA,int rowA,int colB,int rowB,int speed,float radius){ // convert index to pixels points
        PointF pointA = new PointF();
        pointA.x = mazeView.getCellWidth()*colA - (mazeView.getCellWidth()/2);//center 1
        pointA.y = mazeView.getCellHeight()*rowA - (mazeView.getCellHeight()/2);//center 2

        PointF pointB = new PointF();
        pointB.x = mazeView.getCellWidth()*colB - (mazeView.getCellWidth()/2); //center 2
        pointB.y = mazeView.getCellHeight()*rowB - (mazeView.getCellHeight()/2);//center2

        obstacleAdder.addBall(pointA,pointB,speed,radius);
        Log.i("LevelManagement","pointA:"+pointA.toString()+" pointB:"+pointB.toString());

    }
}
