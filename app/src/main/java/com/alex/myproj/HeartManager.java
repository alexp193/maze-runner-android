package com.alex.myproj;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by alex on 12/07/2016.
 */

class HeartManager {
    private int countHeart = 3;

    private ImageView heart1;
    private ImageView heart2;
    private ImageView heart3;
    private  GameControllerActivity activity;

    HeartManager(GameControllerActivity activity) {
        this.activity = activity;
        heart1 = (ImageView)activity.findViewById(R.id.heart1);
        heart2 = (ImageView)activity.findViewById(R.id.heart2);
        heart3 = (ImageView)activity.findViewById(R.id.heart3);
    }

    public void damage() {
        countHeart--;

        switch (countHeart){
            case 0:
                heart1.setVisibility(View.GONE);
                heart2.setVisibility(View.GONE);
                heart3.setVisibility(View.GONE);
                break;
            case 1:
                heart1.setVisibility(View.VISIBLE);
                heart2.setVisibility(View.GONE);
                heart3.setVisibility(View.GONE);
                break;
            case 2:
                heart1.setVisibility(View.VISIBLE);
                heart2.setVisibility(View.VISIBLE);
                heart3.setVisibility(View.GONE);
                break;
            case 3:
                heart1.setVisibility(View.VISIBLE);
                heart2.setVisibility(View.VISIBLE);
                heart3.setVisibility(View.VISIBLE);
                break;
        }

        if (countHeart < 0){
            Intent i = new Intent(activity, GameOver.class);
            activity.startActivity(i);
            activity.finish();
        }
    }
}
