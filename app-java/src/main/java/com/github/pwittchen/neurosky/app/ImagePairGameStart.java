package com.github.pwittchen.neurosky.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class ImagePairGameStart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //轉場動畫
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide= TransitionInflater.from(this).inflateTransition(R.transition.slide);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        //退出
        getWindow().setExitTransition(explode);
        //第一次進入
        getWindow().setEnterTransition(fade);
        //再次進入
        getWindow().setReenterTransition(slide);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_image_pair_game_start);

        VideoView video = (VideoView) findViewById(R.id.videoView);
        String uriPath = "android.resource://"+  getPackageName() + "/raw/"+R.raw.pair;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.start();

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ImagePairGameStart.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ImagePairGameStart.this , SafariHome.class);
                startActivity(intent);
            }
        });

        Button practiceBtn = (Button)findViewById(R.id.btn_practice);
        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ImagePairGameStart.this , ImagePairPractice.class);
                startActivity(intent);
            }
        });

        Button trainBtn = (Button)findViewById(R.id.btn_train);
        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ImagePairGameStart.this , ImagePairEasy.class);
                startActivity(intent);
            }
        });
    }
}