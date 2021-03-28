package com.github.pwittchen.neurosky.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_instruction_home);

        //頁面跳轉  點選導覽列的home->home
        ImageView button = findViewById(R.id.imagehome);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(InstructionHome.this, Home.class);
                startActivity(intent);
            }
        });

        //頁面跳轉  點選腦波儀問題->腦波儀問題
        ImageView button3 = findViewById(R.id.test_ins);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(InstructionHome.this, SafariBrain.class);
                startActivity(intent);
            }
        });

        //頁面跳轉  點選 app問題->app問題
        ImageView button1 = findViewById(R.id.app_ins);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(InstructionHome.this, SafariApp.class);
                startActivity(intent);
            }
        });
        //頁面跳轉  點選 watch問題->watch問題
        ImageView button2 = findViewById(R.id.watch_ins);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(InstructionHome.this, SafariWatch.class);
                startActivity(intent);
            }
        });


    }
}