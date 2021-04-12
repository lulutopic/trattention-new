package com.github.pwittchen.neurosky.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MemoryGameStart extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game_start);
        //設定隱藏標題
        getSupportActionBar().hide();
        VideoView video = (VideoView) findViewById(R.id.videoView);
        video.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/trattention-f3daa.appspot.com/o/video.mp4?alt=media&token=e7832ebd-8eab-4dff-b17b-a51ca1b3d1d7"));
        video.setMediaController(new MediaController(this));
        video.start();


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