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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class AttentionTestResult extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String createdAt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_attention_test_result);

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestResult.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestResult.this , InstructionHome.class);
                startActivity(intent);
            }
        });

        //抓取上一頁數值：測驗成績
        Intent intent = getIntent();
        String attention_value = intent.getStringExtra("attention");
        TextView attention_result=(TextView) findViewById(R.id.cur_attention);
        attention_result.setText(attention_value);

        //計算當前時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.TAIWAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        createdAt = sdf.format(new Date()); //-prints-> 2015-01-22T03:23:26Z
        Log.d("MainActivity", "Current Timestamp: " + sdf.format(new Date()));

//        先寫死，後期在統一改 UID
//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("mindwave_record").document(userID);
        //自動產生 document id
        DocumentReference documentReference = fStore.collection("mindwave_record").document();
        Map<String,Object> mindwave = new HashMap<>();
//        user.put("user", userID);
        mindwave.put("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq2");
        mindwave.put("attention_result", attention_value);
        mindwave.put("createdAt", createdAt);
        documentReference.set(mindwave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "成功");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "失敗：" + e.toString());
            }
        });


    }

}