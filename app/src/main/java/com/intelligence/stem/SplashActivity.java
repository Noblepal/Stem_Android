package com.intelligence.stem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imageview);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        imageView.startAnimation(animation);


        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(4000);

                    if (SharedPrefManager.getInstance(SplashActivity.this).isLoggedIn()) {
                        startActivity(new Intent(SplashActivity.this, PINActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LogInActivity.class));
                    }
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.start();

    }
}
