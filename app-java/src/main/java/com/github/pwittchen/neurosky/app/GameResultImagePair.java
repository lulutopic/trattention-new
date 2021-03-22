package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class GameResultImagePair extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView lastTimeRecord, thisTimeRecord;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_game_result);

        //取得各格 id 進一步設定要放值的地方
        lastTimeRecord = findViewById(R.id.lastTimeRecord);
        thisTimeRecord = findViewById(R.id.thisTimeRecord);
        ArrayList list = new ArrayList<>();
        //        userID = fAuth.getCurrentUser().getUid();

        //寫入個人資料資料 ps 先把 UID 寫死不然大家會不好測試
        fStore.collection("game_record").document("game_record_imagepair").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq")
                .orderBy("createdAt")
                .limitToLast(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getString("record"));
                            }
                            Log.d("document",list.toString());
                            if(list.size() == 2){
                                lastTimeRecord.setText(list.get(list.size()-2).toString());
                                thisTimeRecord.setText(list.get(list.size()-1).toString());
                            }
                            else{
                                lastTimeRecord.setText("尚未有先前記錄");
                                thisTimeRecord.setText(list.get(list.size()-1).toString());
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
                intent.setClass(GameResultImagePair.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResultImagePair.this , GameHome.class);
                startActivity(intent);
            }
        });

        Button btn_index=findViewById(R.id.index);
        btn_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResultImagePair.this , Home.class);
                startActivity(intent);
            }
        });
    }
}