package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class GameResultSchulte extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView lastTimeRecord, thisTimeRecord, recordCompare, differSeconds;
    String userID;

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
        fStore = FirebaseFirestore.getInstance();

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_game_result_schulte);

        //取得各格 id 進一步設定要放值的地方
        lastTimeRecord = findViewById(R.id.lastTimeRecord);
        thisTimeRecord = findViewById(R.id.thisTimeRecord);
        recordCompare = findViewById(R.id.recordCompare);
        differSeconds = findViewById(R.id.differSeconds);
        ArrayList recordList = new ArrayList<>();
        ArrayList compareList = new ArrayList<>();

        //        userID = fAuth.getCurrentUser().getUid();

        //寫入個人資料資料 ps 先把 UID 寫死不然大家會不好測試
        fStore.collection("game_record").document("game_record_schulte").collection("data")
                .orderBy("createdAt")
                .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                .limitToLast(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recordList.add(document.getString("record"));
                                compareList.add(document.get("secondRecord"));
                            }
                            if(recordList.size() == 2 && compareList.size() == 2){
                                lastTimeRecord.setText(recordList.get(recordList.size()-2).toString());
                                thisTimeRecord.setText(recordList.get(recordList.size()-1).toString());
                                int compareRecord = Integer.parseInt(compareList.get(compareList.size()-1).toString()) - Integer.parseInt(compareList.get(compareList.size()-2).toString());
                                differSeconds.setText(valueOf(Math.abs(compareRecord)));
                                if(compareRecord > 0){
                                    recordCompare.setText("比最佳秒數退步"+Math.abs(compareRecord));
                                }
                                else{
                                    recordCompare.setText("比最佳秒數進步"+Math.abs(compareRecord));
                                }
                            }
                            else{
                                lastTimeRecord.setText("尚未有先前記錄");
                                thisTimeRecord.setText(recordList.get(recordList.size()-1).toString());
                                recordCompare.setText("尚未有先前記錄");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

//        lastTimeRecord.setText(list.get(1).toString());
        //header:頁面跳轉->home
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResultSchulte.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameResultSchulte.this).toBundle());
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResultSchulte.this , GameHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameResultSchulte.this).toBundle());
            }
        });

        Button btn_index=findViewById(R.id.index);
        btn_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResultSchulte.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameResultSchulte.this).toBundle());
            }
        });
    }
}