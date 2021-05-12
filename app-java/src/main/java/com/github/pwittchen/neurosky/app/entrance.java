package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class entrance extends AppCompatActivity {
    private ImageView bus;
    private ImageView title_bg,title;
    private TextView tips;
    private RelativeLayout bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_entrance);

        bus = (ImageView) findViewById(R.id.bus);
        title_bg = (ImageView) findViewById(R.id.title_bg);
        title = (ImageView) findViewById(R.id.title);
        tips = (TextView)findViewById(R.id.tips);
        bg = (RelativeLayout)findViewById(R.id.bg);

        bus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.transition));

        Animator animator = AnimatorInflater.loadAnimator(this,R.animator.alpha);
        // 载入XML动画
        animator.setTarget(title_bg);
        // 设置动画对象
        animator.start();
        // 启动动画

        tips.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha1));
        title.startAnimation(AnimationUtils.loadAnimation(this,R.anim.alpha));

        Animator animator1 = AnimatorInflater.loadAnimator(this,R.animator.transition);

        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tips.setText("出發囉！！");
                animator1.setTarget(bus);
                animator1.start();
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        Intent intent = new Intent();
                        intent.setClass(entrance.this, Login.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
                        finish();
                    }
                }, 1000); //延遲1.5秒
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