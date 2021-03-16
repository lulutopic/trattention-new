package com.github.pwittchen.neurosky.app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        //header:頁面跳轉->點選導覽列的小燈泡->指南
        ImageView btn_safari = findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, InstructionHome.class);
                startActivity(intent);
            }
        });
        //頁面跳轉->選擇遊戲
        ImageView btn_start = findViewById(R.id.start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, GameHome.class);
                startActivity(intent);
            }
        });

        //頁面跳轉->選擇專注力分析
        ImageView btn_focus =(ImageView)findViewById(R.id.focus);
        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Home.this, AttentionTestHome.class);
                startActivity(intent);
            }
        });


        //頁面跳轉->訓練記錄
        ImageView btn_record = findViewById(R.id.focus);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, AttentionTestHome.class);
                startActivity(intent);
            }
        });

        //頁面跳轉->個人化設置
        ImageView btn_personal = findViewById(R.id.personal);
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, Personal.class);
                startActivity(intent);

            }



        });
    }
}