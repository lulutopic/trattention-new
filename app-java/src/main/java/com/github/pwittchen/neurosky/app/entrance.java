package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class entrance extends AppCompatActivity {
    private ImageView bus;
    private ImageView title;
    private Button enter;
    private TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_entrance);

        bus = (ImageView) findViewById(R.id.imageView);
        title = (ImageView) findViewById(R.id.title);
        enter = (Button) findViewById(R.id.enter);
        tips = (TextView)findViewById(R.id.tips);

        bus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.transition));
        title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.transition1));
        enter.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha));
        tips.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha1));

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtnStyle(v);
                Intent intent = new Intent();
                intent.setClass(entrance.this , Login.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(entrance.this).toBundle());
            }
        });

    }


    private void setBtnStyle(View view){
        view.setBackgroundResource(R.drawable.buttonshadow);
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        view.setBackgroundResource(0);
                    }
                });
            }
        }, 500);
    }
}