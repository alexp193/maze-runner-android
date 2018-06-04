package com.alex.myproj;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by alex on 16/05/2016.
 */


public class MazeView extends View {

    private static final int TILE_TYPE_EMPTY = 2;
    private static final int TILE_TYPE_HOLE = 4;
    private static final int TILE_TYPE_MAGIC_WALL = 5;

    private static final boolean CHEAT_DEBUGRENDER =false;
    private static final boolean CHEAT_GHOSTBALL = false;//off all collision
    private static final String LOG_TAG = "MazeView";
    private  RectF drawRect = new RectF();





   // public long playerColliedMagicWall;

    private Bitmap[] bitmaps;
    public int[][] tileType; //mat of the maze
    private float screenWidth;
    private float screenHeight;
    private int numOfColumns;
    private int numOfRows;
    private AnimatedView animatedBall;
    private LevelManagement levelManagement;
    private int RectWidth;
    private int RectHeight;
    SendingBall sendingBall;
    GameControllerActivity control;
    private int resizeHole = 10;
    private RectF bottomLine;

    public MazeView(Context context) {
        this(context, null);
    }

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(GameControllerActivity control,SendingBall sendingBall,ObstacleAdder obstacleAdder) {
        this.sendingBall = sendingBall;
        this.control = control;

        levelManagement = new LevelManagement(this, obstacleAdder);
        levelManagement.moveToNextLevel();

        numOfColumns = tileType[0].length;
        numOfRows = tileType.length;

        //bitmaps
        bitmaps =  new Bitmap[]{
                BitmapFactory.decodeResource(getResources(), R.drawable.wall3),//0
                BitmapFactory.decodeResource(getResources(), R.drawable.brick),//1
                null,//empty 2
                BitmapFactory.decodeResource(getResources(), R.drawable.hole1), //3
                BitmapFactory.decodeResource(getResources(), R.drawable.gatew),//4
                BitmapFactory.decodeResource(getResources(), R.drawable.wall1)//5
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.screenWidth = getMeasuredWidth();
        this.screenHeight = getMeasuredHeight();
        RectWidth = ((int)screenWidth / numOfColumns);
        RectHeight = ((int)screenHeight / numOfRows);

        drawRect.set(0, 0, RectWidth, RectHeight);

        //yMax
        sendingBall.setyMax( numOfRows*RectHeight );

        //size ball

        resizeBall(0.7f);

//bottomLine
        float bottomLineLeft = 0;
        float bottomLineTop = getCellHeight()*numOfRows;
        float bottomLineRight =  getCellWidth()*numOfColumns;
        float bottomLineBottom = bottomLineTop+10;
        bottomLine = new RectF(bottomLineLeft,bottomLineTop,bottomLineRight,bottomLineBottom);
        Log.i(LOG_TAG,bottomLine.toString());


    }

    /** @param size is number between 0 to 1 */
    public void resizeBall(float size){
        int diameter = (int)(RectWidth * size);
        sendingBall.resize(diameter,diameter);
    }

    public LevelManagement getLevelManagement() {
        return levelManagement;
    }

    public  float getCellWidth() {
        return drawRect.width();
    }
    public  float getCellHeight() {
        return drawRect.height();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int tileX = 0;
        int tileY = 0;
        float xCoordinate = 0;
        float yCoordinate = 0;

        while (tileY < tileType.length && yCoordinate <= screenHeight) {
            // Begin drawing a new column
            tileX = 0;
            xCoordinate = 0;
            while (tileX < tileType[tileY].length && xCoordinate <= screenWidth) {

                // Check if the tile is not null
                if (bitmaps[tileType[tileY][tileX]] != null) {

                    // This tile is not null, so check if it has to be drawn
                    if (xCoordinate + drawRect.width() >= 0 && yCoordinate + drawRect.height() >= 0) {

                        // The tile visible to the user,  draw it!
                        drawRect.offsetTo(xCoordinate, yCoordinate); // Move the rectangle to the coordinates
                        canvas.drawBitmap(bitmaps[tileType[tileY][tileX]], null, drawRect, null);

                    }
                }

                // Move to the next tile on the X axis
                tileX++;
                xCoordinate += drawRect.width();
            }

            // Move to the next tile on the Y axis
            tileY++;
            yCoordinate += drawRect.height();
        }

        canvas.drawBitmap(bitmaps[0], null, bottomLine, null);

        if ((CHEAT_DEBUGRENDER)){

            Paint paint = new Paint();
            paint.setColor(Color.GREEN);

            for (int i=0; i < numOfRows+1;i++){
                float y = RectHeight * i;
                canvas.drawLine(0,y,getMeasuredWidth(),y,paint);
            }

            for (int i=0; i < numOfColumns+1;i++) {
                float x = RectWidth * i;
                canvas.drawLine(x,0,x,getMeasuredHeight(),paint);
            }

            paint.setColor(Color.BLUE);

            for (Point point:verticalCutPoint)
                canvas.drawCircle(point.x,point.y,10,paint);

            paint.setColor(Color.RED);

            for (Point point:horizontalCutPoint)
                canvas.drawCircle(point.x,point.y,10,paint);

            invalidate();
        }
    }


    public boolean canMoveLeft = true;
    public boolean canMoveRight = true;
    public boolean canMoveTop = true;
    public boolean canMoveBottom = true;

    /** -2 = no action.
     *  -1 = need start magic.
     *  value big from 0 is lest System.currentTimeMillis*/
    public long playerColliedMagicWall=-2;

    private  ArrayList<Point> verticalCutPoint;
    private  ArrayList<Point> horizontalCutPoint;

    public void checkCanMove(int ballWidth, int ballHeight, int left, int top){

        //box
        int right = left + ballWidth;
        int bottom = top + ballHeight;
        int horizontalCenter = left + (ballWidth/2);
        int verticalCenter = top + (ballHeight/2);

        if (CHEAT_GHOSTBALL){
            //decide movement
            canMoveLeft = true;
            canMoveRight = true;
            canMoveTop = true;
            canMoveBottom = true;
            checkIsInHole(left,right,bottom,top,horizontalCenter,verticalCenter);
            return;
        }

        int radius = ballWidth/2;
        Point center = new Point(horizontalCenter,verticalCenter);

        //vertical
        //find all potential vertical point can cut the ball it the wall, and check if is do it. Y
        verticalCutPoint = new ArrayList<>();

        for (int i=0; i < numOfColumns+1;i++){
            float x = RectWidth * i;

            double sqrt = Math.pow(radius,2) -  Math.pow(x-center.x,2);
            if (sqrt < 0)
                continue;
            int y1 = (int)(center.y + Math.sqrt( sqrt ));
            int y2 = (int)(center.y - Math.sqrt( sqrt ));

            //check if it is cut the wall.
            int xIndex = (int) (x / getCellWidth());
            int y1Index = (int) (y1 / getCellHeight());
            int y2Index = (int) (y2 / getCellHeight());


            if (isTypeObstacle(y1Index, xIndex - 1) || isTypeObstacle(y1Index, xIndex)) {

                verticalCutPoint.add( new Point((int)x,y1) );
            }
            if( isTypeObstacle(y2Index,xIndex - 1) || isTypeObstacle(y2Index,xIndex)){
                verticalCutPoint.add( new Point((int)x,y2) );
            }

        }

        //horizontal'
        //find all potential horizontal point can cut the ball in the wall, and check if is do it.X
        horizontalCutPoint = new ArrayList<>();

        for (int i=0; i < numOfRows+1;i++){
            float y = RectHeight * i;

            double sqrt = Math.pow(radius,2) -  Math.pow(y-center.y,2);
            if (sqrt < 0)
                continue;
            int x1 = (int)(center.x + Math.sqrt( sqrt ));
            int x2 = (int)(center.x - Math.sqrt( sqrt ));

            //check if is cut the wall.
            int yIndex = (int) (y / getCellHeight());
            int x1Index = (int) (x1 / getCellWidth());
            int x2Index = (int) (x2 / getCellWidth());

            if( isTypeObstacle(yIndex -1,x1Index) || isTypeObstacle(yIndex,x1Index) ) {
                horizontalCutPoint.add( new Point((int)x1,(int)y) );
            }

            if (isTypeObstacle(yIndex - 1, x2Index) || isTypeObstacle(yIndex, x2Index)) {
                horizontalCutPoint.add( new Point((int)x2,(int)y) );
            }
        }


        //decide movement
        canMoveLeft = true;
        canMoveRight = true;
        canMoveTop = true;
        canMoveBottom = true;

        //vertical
        if (verticalCutPoint.size() == 2) {//slippery wall

            if (verticalCutPoint.get(0).y != verticalCutPoint.get(1).y){///if the ball is inside the wall reverse it.
                if (verticalCutPoint.get(1).x > center.x) //right
                    sendingBall.addXLocation(-1);
                else//left
                    sendingBall.addXLocation( 1 );
            }

            if (verticalCutPoint.get(1).x > center.x)
                canMoveRight = false;
            else
                canMoveLeft = false;
        }else for(Point point : verticalCutPoint){
            //find quadrant of the ball
            if(point.x > center.x && point.y < center.y){//quadrant I R UP
                canMoveRight = false;
                canMoveTop = false;
            }else if (point.x < center.x && point.y < center.y){//quadrant II L UP
                canMoveLeft = false;
                canMoveTop = false;
            }else if (point.x < center.x && point.y > center.y){//quadrant III L DOWN
                canMoveLeft = false;
                canMoveBottom = false;
            }else if (point.x > center.x && point.y > center.y){//quadrant IV R DOWN
                canMoveRight = false;
                canMoveBottom = false;
            }
        }

        //horizontal
        if (horizontalCutPoint.size() == 2) {//slippery wall
            if (horizontalCutPoint.get(0).x != horizontalCutPoint.get(1).x){///if the ball is inside the wall reverse it.
                //Log.i(LOG_TAG,"bad");
                if (horizontalCutPoint.get(1).y > center.y)//bottom the ball
                    sendingBall.addYLocation( -1 );

                else {//top the ball
                    sendingBall.addYLocation(1);
                }
            }/*else
                Log.i(LOG_TAG,"god");*/

            if (horizontalCutPoint.get(1).y > center.y)
                canMoveBottom = false;
            else
                canMoveTop = false;

        }else for(Point point : horizontalCutPoint){
            //find quadrant of the ball
            if(point.x > center.x && point.y < center.y){//quadrant I
                canMoveRight = false;
                canMoveTop = false;
            }else if (point.x < center.x && point.y < center.y){//quadrant II
                canMoveLeft = false;
                canMoveTop = false;
            }else if (point.x < center.x && point.y > center.y){//quadrant III
                canMoveLeft = false;
                canMoveBottom = false;
            }else if (point.x > center.x && point.y > center.y){//quadrant IV
                canMoveRight = false;
                canMoveBottom = false;
            }
        }

        //hole
        checkIsInHole(left,right,bottom,top,horizontalCenter,verticalCenter);
    }

    private void checkIsInHole(int leftBall,int rightBall,int bottomBall, int topBall,int horizontalCenterBall, int verticalCenterBall){ //check if the ball is in hole to next level

        if (sendingBall.getAnimationNextOn()) {//if animation on
            canMoveLeft = false;
            canMoveRight = false;
            canMoveTop = false;
            canMoveBottom = false;
            return;
        }

        int col = (int) (horizontalCenterBall / getCellWidth());
        int row = (int) (verticalCenterBall / getCellHeight());
        if (row < tileType.length && col < tileType[row].length)
            if (tileType[row][col] == 4) {
                int leftHole = col * (int)getCellWidth();
                int rightHole = leftHole + RectWidth;
                int topHole = row * RectHeight;
                int  bottomHole = topHole + RectHeight;

                leftHole -= resizeHole;//grow the hole
                rightHole += resizeHole;
                topHole -= resizeHole;
                bottomHole += resizeHole;


                //box if.
                if (leftBall > leftHole &&
                        rightBall < rightHole &&
                        topBall > topHole &&
                        bottomBall < bottomHole) {

                    sendingBall.NextLevelAnimationOn(new OnAnimationEndListener() {
                        @Override
                        public void onEnd() {

                            control.saveResult();
                            levelManagement.moveToNextLevel();
                        }
                    });
                }
            }
    }

    //also turn on magic wall triger.
    private boolean isTypeObstacle(int row,int col){
        if (row < 0 || row >= numOfRows)
            return true;
        else
        if (col < 0 || col >= numOfColumns)
            return true;
        else {

            if (tileType[row][col] == TILE_TYPE_MAGIC_WALL) {
                playerColliedMagicWall = -1;
            }

            return (tileType[row][col] != TILE_TYPE_EMPTY && tileType[row][col] != TILE_TYPE_HOLE);//collision in a maze
        }
    }

}



