package com.alex.myproj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alex on 14/05/2016.
 */


public class MainActivity extends Activity {

    static MediaPlayer mySound;
    final String LOG_TAG = "myLogs";
    private ArrayList<String> personArrayList = new ArrayList<String>();

    ListView listHighScore;
    String str;
    private boolean settingSoundPlay=true;
    String n;
    String l;
    String r;

    ArrayAdapter myArrayAdapter;
    View viewGetAll;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //main layout

        mySound = MediaPlayer.create(this, R.raw.playgame);
        mySound.setLooping(true);
        mySound.start();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private EditText txtName;

    public void onClickPlay(View v) {
        Log.d(LOG_TAG, "add dialog ");


        AlertDialog.Builder adb = new AlertDialog.Builder(this); //open dialog new player
        adb.setCancelable(false);
        adb.setTitle("New player");
        adb.setMessage("set a new name player");
        adb.setIcon(android.R.drawable.ic_menu_info_details);
        LayoutInflater inflater = getLayoutInflater(); //name_user
        View viewAdd = inflater.inflate(R.layout.name_user, null);
        txtName = (EditText) viewAdd.findViewById(R.id.txtNameEdit);
        adb.setView(viewAdd);//add edit text.

        adb.setNegativeButton("Cancle", new DialogInterface.OnClickListener() { //cancle button
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {//ok button(add player to list) and play
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MainActivity.this, GameControllerActivity.class);
                i.putExtra(GameControllerActivity.KEY_NAME, txtName.getText().toString());//move name for score
                i.putExtra(GameControllerActivity.KEY_SOUND, settingSoundPlay );//sent soundPlay to control
                startActivity(i);
                finish();

            }
        });

        adb.show();
    }
    public void OnClickInst(View v) {//instractions
        Intent i = new Intent(MainActivity.this, Instractions.class);
        startActivity(i);
    }

    public void OnClickSettings(View v) {//settings

        CharSequence Choices[] = {"on","off"};

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("play sound?");
        adb.setSingleChoiceItems(Choices, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    settingSoundPlay = true;
                    mySound.start();
                }else {
                    settingSoundPlay = false;
                    mySound.pause();
                }
            }
        });
        adb.show();
    }
    public void OnClickHigh(View v) {//show all player with scores

        PersonSql personSQL = new PersonSql(getApplicationContext(), "db1", null, 1);
        List<PersonData> listPD;

        listPD = personSQL.getAllPerson();
        str = "Name:     level:     record:\n";


        for (PersonData pd : listPD) {
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSSS");
            Date date = new Date(pd.getTimeRecord());
            String Record = sdf.format(date);

            str += pd.getPerson() + "       " + pd.getLevel() + "           " + Record + "\n";
        }

        personSQL.close();

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setCancelable(false);
        adb.setTitle("Score");
        adb.setMessage(str);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }


    @Override//back to menu button
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        System.exit(0);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.alex.myproj/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.alex.myproj/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mySound != null)
            mySound.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mySound != null)
            mySound.pause();
    }

}