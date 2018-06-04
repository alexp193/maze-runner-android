package com.alex.myproj;

import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;

/**
 * Created by alex on 10/08/2016.
 */

public class GameOver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(GameOver.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };

        Handler handler = new Handler();
        handler.postDelayed(runnable,3000);
    }
}
