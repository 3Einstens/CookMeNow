package com.einstens3.ironchef.activities;

/**
 * Created by raprasad on 3/19/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.einstens3.ironchef.BuildConfig;
import com.einstens3.ironchef.R;
import com.einstens3.ironchef.models.Challenge;
import com.einstens3.ironchef.models.Recipe;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.interceptors.ParseLogInterceptor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @BindView(R.id.imgLogo)
    ImageView imgFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
      //  Glide.with(this).load(R.raw.ic_fire_burning).asGif().into(imgFire);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);

             //   overridePendingTransition(R.anim.fadin,R.anim.fadeout);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}