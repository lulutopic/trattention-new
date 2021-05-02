package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Personal extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView mEmail, mName, mAge, mGender;
    Button mLogoutBtn;
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
        getWindow().setEnterTransition(slide);
        //再次進入
        getWindow().setReenterTransition(slide);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_personal);
        fStore = FirebaseFirestore.getInstance();
        //取得各格 id 進一步設定要放值的地方
        mEmail = findViewById(R.id.email);
        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mGender = findViewById(R.id.gender);
        mLogoutBtn = findViewById(R.id.logout);
//        userID = fAuth.getCurrentUser().getUid();

        //寫入個人資料資料 ps 先把 UID 寫死不然大家會不好測試
        DocumentReference documentReference = fStore.collection("users").document("MELJmK6vYxeoKCrWhvJyy4Xfriq2");
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                mEmail.setText(documentSnapshot.getString("email"));
                mName.setText(documentSnapshot.getString("name"));
                mAge.setText(documentSnapshot.getString("age"));
                mGender.setText(documentSnapshot.getString("gender"));
            }
        });

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Personal.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Personal.this).toBundle());
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Personal.this , SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Personal.this).toBundle());
            }
        });

        mLogoutBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View mLoginBtn) {
                            startActivity(new Intent(getApplicationContext(),Login.class));//要再判斷是不是第一次進入該頁面，若為第一次則需進行初始測驗
            }
        });

        mEmail.setHorizontallyScrolling(true);
        mEmail.setSelected(true);


    }
}
