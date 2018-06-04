package com.alex.myproj;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alex on 17/05/2016.
 */




public class GameControllerActivity extends AppCompatActivity implements SensorEventListener {

    private static final String LOG_TAG = "GameControllerActivity";
    public static final String KEY_NAME = "Name";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private MazeView mazeView;
    private AnimatedView animatedBall;

    private String userName;
    private PointF lestValue;
    //timer
    static TextView timerText;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;      //////
    long ssupdatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Handler handler = new Handler();
    boolean flag = true;

    public static SoundPool soundPool ;
    public  static int soundLevelID;
    public  static int soundMagic;
    public  static int particle;
    public static final String KEY_SOUND = "Sound";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_controller);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        View viewToParticleSystem = findViewById(R.id.viewToParticleSystem);
        HeartManager heartManager = new HeartManager(this);
        Obstacle1 obstacle1 = (Obstacle1)findViewById(R.id.Obstacle1);
        mazeView = (MazeView) findViewById(R.id.maze);
        animatedBall = (AnimatedView) findViewById(R.id.animatedBall);
        obstacle1.init(animatedBall,heartManager,viewToParticleSystem,this);
        mazeView.init(this,animatedBall,obstacle1);

        timerText = (TextView) findViewById(R.id.timerText);//create timer text

        //get user name from main activity.
        boolean playSound = true;
        //get user name from main activity.
        Bundle extras = getIntent().getExtras();//name of playar,playSound
        if (extras != null) {
            userName = extras.getString(KEY_NAME);
            TextView name = (TextView)findViewById(R.id.name);
            name.setText(userName);

            playSound = extras.getBoolean(KEY_SOUND);
        }

        //SOUND//SOUND//SOUND//SOUND//SOUND//SOUND//SOUND//SOUND//SOUND//SOUND//SOUND//SOUND
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        /** soundId for Later handling of sound pool **/
        if(playSound) {
            soundLevelID = soundPool.load(this, R.raw.sound_level, 1);
            soundMagic = soundPool.load(this, R.raw.magic, 1);
            particle = soundPool.load(this, R.raw.particle, 1);
        }
    }

    public void onClickResize(View v){//resize the ball of the player
        mazeView.resizeBall(0.5f);
        Log.i(LOG_TAG,"resizeBall 0.5");

        final Button button = (Button)v;
        button.setClickable(false);
        button.setVisibility(View.INVISIBLE);

        final Runnable runnable = new Runnable(){
            @Override
            public void run() {
                mazeView.resizeBall(0.7f);
                Log.i(LOG_TAG,"resizeBall 0.7");
                button.setClickable(true);
                button.setVisibility(View.VISIBLE);
            }
        };

        final Handler handler = new Handler();
        handler.postDelayed(runnable,5000); //run() 5 sec

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.mySound != null)
            MainActivity.mySound.start();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MainActivity.mySound != null)
            MainActivity.mySound.pause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float xValue = event.values[0] * 4;
            float yValue = event.values[1] * 4;

            //Log.d(LOG_TAG, "onSensorChanged. xValue = " + xValue + ", yValue = " + yValue);

            mazeView.checkCanMove(animatedBall.getBallWidth(), animatedBall.getBallHeight(), animatedBall.getXLocation(), animatedBall.getYLocation());

            //magic wall
            if(mazeView.playerColliedMagicWall > -2){
                if (mazeView.playerColliedMagicWall == -1){
                    lestValue = new PointF();
                    lestValue.x = xValue * -2;
                    lestValue.y = yValue * -2;
                    mazeView.playerColliedMagicWall = System.currentTimeMillis();
                }
                xValue = lestValue.x;
                yValue = lestValue.y;
                soundPool.play(soundMagic, 1, 1, 0, 0, 1);

                if ( System.currentTimeMillis() > mazeView.playerColliedMagicWall + 100 ) {//100 milliseconds letter.
                    mazeView.playerColliedMagicWall = -2;
                    lestValue = null;
                }
            }
            //  collision on right
            if (!mazeView.canMoveRight)
                if (xValue < 0) xValue = 0;

            //collision on left
            if (!mazeView.canMoveLeft)
                if (xValue > 0) xValue = 0;

            //collision on top
            if (!mazeView.canMoveTop)
                if (yValue < 0) yValue = 0;

            //collision on butoom
            if (!mazeView.canMoveBottom)
                if (yValue > 0) yValue = 0;

            animatedBall.moveBall(xValue, yValue);


        }

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("finish the game");
        adb.setMessage("do you want move to main menu?");
        adb.setIcon(android.R.drawable.ic_menu_info_details);
        LayoutInflater inflater = getLayoutInflater(); //name_user

        adb.setNegativeButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(GameControllerActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
            adb.setPositiveButton("return ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.show();
    }

    public Runnable updateTimer = new Runnable() {//set timer on text
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            timerText.setText("" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds));
            handler.postDelayed(this, 15);
        }
    };
    public void startTimer() {//start timer, start handler

        Log.d(LOG_TAG, "start ");
        starttime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimer, 0);//start timer
    }


    public long stopAndGetRecord() {//stop timer and set 0 for all values.and return time in Milliseconds.
        long record = SystemClock.uptimeMillis() - starttime;
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        secs = 0;
        mins = 0;
        milliseconds = 0;
        handler.removeCallbacks(updateTimer);//off the timer

        String time = timerText.getText().toString();
        timerText.setText("");

        return record;

    }

    public void saveResult(){
        long record = stopAndGetRecord();
        String level = "Level "+mazeView.getLevelManagement().getCurrentLevel();
        Log.i(LOG_TAG,"saveResult:");

        PersonSql personSQL = new PersonSql(getApplicationContext(), "db1", null, 1);
        List<PersonData> records = personSQL.getRecords(level);
        if (records.size() == 0 || (records.size() > 0 && records.get(0).getTimeRecord() > record) ) {//get max rec
            Log.i(LOG_TAG,"new record");

            personSQL.delPerson(level,record);

            PersonData newRecordPerson = new PersonData(userName,level,record);
            personSQL.addNewPerson(newRecordPerson);

         }

        personSQL.close();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



}
