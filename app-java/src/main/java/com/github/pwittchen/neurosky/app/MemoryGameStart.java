package com.github.pwittchen.neurosky.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryGameStart extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_memory_game_start);

        VideoView video = (VideoView) findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/trattention-f3daa.appspot.com/o/video.mp4?alt=media&token=e7832ebd-8eab-4dff-b17b-a51ca1b3d1d7"));
        video.setMediaController(new MediaController(this));
        video.start();

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MemoryGameStart.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MemoryGameStart.this , InstructionHome.class);
                startActivity(intent);
            }
        });

        Button practiceBtn = (Button)findViewById(R.id.btn_practice);
        practiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MemoryGameStart.this , MemoryGamePractice.class);
                startActivity(intent);
            }
        });

        Button trainBtn = (Button)findViewById(R.id.btn_train);
        trainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MemoryGameStart.this , MemoryGameEasy.class);
                startActivity(intent);
            }
        });
    }
}