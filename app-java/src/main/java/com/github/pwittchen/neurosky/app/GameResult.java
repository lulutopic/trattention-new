package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameResult extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView lastTimeRecord, thisTimeRecord;
    String userID, lastTime, thisTime;

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
//        DocumentReference documentReference = fStore.collection("game_record").document("game_record_memory").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq").document(
//                "Q0FeKrk3dXxhSmTwIwqL");
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//                lastTimeRecord.setText(documentSnapshot.getString("record"));
//                Log.d("lastTimeRecord", documentSnapshot.getString("record"));
//            }
//        });

//        CollectionReference documentReference = fStore.collection("game_record").document("game_record_memory").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq");
//        Query query =
        fStore.collection("game_record").document("game_record_memory").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq")
                .orderBy("createdAt")
                .limitToLast(2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("document", document.getId() + " => " + document.getData());
//                                list.add(document.getData());
                                Log.d("record",document.getString("record"));
                                list.add(document.getString("record"));
                            }
                            Log.d("document",list.toString());
                            lastTimeRecord.setText(list.get(0).toString());
                            thisTimeRecord.setText(list.get(1).toString());
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
                intent.setClass(GameResult.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResult.this , GameHome.class);
                startActivity(intent);
            }
        });

        Button btn_index=findViewById(R.id.index);
        btn_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(GameResult.this , Home.class);
                startActivity(intent);
            }
        });
    }
}