package com.example.aakash.bmi_calc_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    ImageView imgBmi;
    TextView txtMessage;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgBmi=findViewById(R.id.imgBmi);
        txtMessage=findViewById(R.id.txtMessage);
        animation=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.a1);
        imgBmi.startAnimation(animation);
        txtMessage.startAnimation(animation);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Thread.sleep(5000);
                    Intent i=new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
