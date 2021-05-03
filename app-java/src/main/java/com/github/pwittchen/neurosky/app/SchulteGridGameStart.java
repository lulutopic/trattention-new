package com.github.pwittchen.neurosky.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SchulteGridGameStart extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_schulte_game_start);

        VideoView video = (VideoView) findViewById(R.id.videoView);
        String uriPath = "android.resource://"+  getPackageName() + "/raw/"+R.raw.schutle;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.start();

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                video.start();
            }
        });


        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SchulteGridGameStart.this , Home.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SchulteGridGameStart.this , SafariHome.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
            }
        });

        Button practiceBtn = (Button)findViewById(R.id.btn_practice);
        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SchulteGridGameStart.this , SchulteGridPractice.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
            }
        });

        Button trainBtn = (Button)findViewById(R.id.btn_train);
        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SchulteGridGameStart.this , SchulteGridEasy.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
            }
        });
    }
}