package com.github.pwittchen.neurosky.app;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SchulteGridPractice extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schulte_game_practice);
        //設定隱藏標題
        getSupportActionBar().hide();

    }
}