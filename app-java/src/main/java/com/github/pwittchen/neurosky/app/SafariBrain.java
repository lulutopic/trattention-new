package com.github.pwittchen.neurosky.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SafariBrain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_safaribrain);

        //header:頁面跳轉->home
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SafariBrain.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SafariBrain.this , SafariHome.class);
                startActivity(intent);
            }
        });


        View btn_Q1=(View)findViewById(R.id.Q1);
        TextView btn_A1=(TextView)findViewById(R.id.A1);
        View btn_Q2=(View)findViewById(R.id.Q2);
        TextView btn_A2=(TextView)findViewById(R.id.A2);

        btn_Q1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(btn_A1.getVisibility()==View.GONE) {
                    btn_A1.setVisibility(View.VISIBLE);
                    v.setBackgroundResource(R.drawable.safariback_active);
                }
                else{
                    v.setBackgroundResource(R.drawable.safariback);
                    btn_A1.setVisibility(View.GONE);
                }
            }
        });

        btn_Q2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(btn_A2.getVisibility()==View.GONE) {
                    v.setBackgroundResource(R.drawable.safariback_active);
                    btn_A2.setVisibility(View.VISIBLE);
                }
                else{
                    v.setBackgroundResource(R.drawable.safariback);
                    btn_A2.setVisibility(View.GONE);
                }
            }
        });

    }

}