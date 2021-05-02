package com.github.pwittchen.neurosky.app;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;



public class GameHome extends AppCompatActivity {

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
        getWindow().setEnterTransition(slide);
        //再次進入
        getWindow().setReenterTransition(slide);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_game_home);

        //header:頁面跳轉->home
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameHome.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameHome.this).toBundle());
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameHome.this , SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameHome.this).toBundle());
            }
        });

        //頁面跳轉  點選 圖形配對遊戲->進入遊戲
        ImageView button = findViewById(R.id.imageView1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(GameHome.this, ImagePairGameStart.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameHome.this).toBundle());
            }
        });
        //頁面跳轉  點選 舒方遊戲->進入遊戲
        ImageView button1 = findViewById(R.id.imageView2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(GameHome.this, SchulteGridGameStart.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameHome.this).toBundle());
            }
        });
        //頁面跳轉  點選 記憶力遊戲->進入遊戲
        ImageView button2 = findViewById(R.id.imageView3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(GameHome.this, MemoryGameStart.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameHome.this).toBundle());
            }
        });
    }
}