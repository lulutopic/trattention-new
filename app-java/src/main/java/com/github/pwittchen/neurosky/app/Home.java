package com.github.pwittchen.neurosky.app;


import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.madgaze.watchsdk.MGWatch;
import com.madgaze.watchsdk.MobileActivity;
import com.madgaze.watchsdk.WatchException;
import com.madgaze.watchsdk.WatchGesture;

public class Home extends MobileActivity {

    private final String MGTAG = MainActivity.class.getSimpleName();

    public final WatchGesture[] REQUIRED_WATCH_GESTURES = {
            //彈指
            WatchGesture.FINGER_SNAP,
            //手臂
            WatchGesture.FOREARM_LEFT,
            WatchGesture.FOREARM_RIGHT,
            //手背
            WatchGesture.HANDBACK_UP,
            WatchGesture.HANDBACK_DOWN,
            WatchGesture.HANDBACK_LEFT,
            WatchGesture.HANDBACK_RIGHT,
            //拇指中指捏捏
            WatchGesture.THUMBTAP_MIDDLE,
            //三指捏捏
            WatchGesture.THUMBTAP_INDEX_MIDDLE,
            //拇指食指捏捏
            WatchGesture.THUMBTAP_INDEX,
            //指頭
            WatchGesture.JOINTTAP_LOWER_THUMB,
            WatchGesture.JOINTTAP_UPPER_THUMB,
            WatchGesture.JOINTTAP_MIDDLE_INDEX,
            WatchGesture.JOINTTAP_UPPER_INDEX,
            WatchGesture.JOINTTAP_MIDDLE_MIDDLE,
            WatchGesture.JOINTTAP_UPPER_MIDDLE,
            WatchGesture.JOINTTAP_MIDDLE_RING,
            WatchGesture.JOINTTAP_UPPER_RING,
            WatchGesture.JOINTTAP_MIDDLE_LITTLE,

    };


    @Override
    public void onWatchGestureReceived(WatchGesture gesture) {
        Log.d(MGTAG, "onWatchGestureReceived: "+gesture.name());
        setResultText(gesture);
    }

    @Override
    public void onWatchGestureError(WatchException error) {
        Log.d(MGTAG, "onWatchGestureError: "+error.getMessage());
        setStatusText(error.getMessage());
    }

    @Override
    public void onWatchDetectionOn() {
        Log.d(MGTAG, "onWatchDetectionOn: ");
        setStatusText("Listening");
    }

    @Override
    public void onWatchDetectionOff() {
        Log.d(MGTAG, "onWatchDetectionOff: ");
        setStatusText("Idle");
    }

    @Override
    public void onMGWatchServiceReady() {
        setStatusText("Service Connected");
        tryStartDetection();
    }

    @Override
    public void onPause(){
        super.onPause();

        if (MGWatch.isWatchGestureDetecting(this))
            MGWatch.stopGestureDetection(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (MGWatch.isMGWatchServiceReady(this))
            tryStartDetection();
    }

    @Override
    public void onWatchConnected() {
        setStatusText("Watch Connected");
    }

    @Override
    public void onWatchDisconnected() {
        setStatusText("Watch Disconnected");
        showConnectDialog();
    }

    @Override
    protected WatchGesture[] getRequiredWatchGestures(){
        return REQUIRED_WATCH_GESTURES;
    }

    private void tryStartDetection(){

        if (!MGWatch.isWatchConnected(this)) {
            setStatusText("Connecting");
            showConnectDialog();
            return;
        }

        if (!MGWatch.isGesturesTrained(this)) {
            showTrainingDialog();
            return;
        }

        if (!MGWatch.isWatchGestureDetecting(this)) {
            MGWatch.startGestureDetection(this);
        }
    }


    private void setStatusText(String text){
        setText(R.id.status, "Status: " + text);
    }

    private void setResultText(final WatchGesture gesture){

    }

    public void setDefinedGestures(){
        setText(R.id.definedGestures, TextUtils.join(", ", getRequiredWatchGestures()));
    }

    public void showConnectDialog(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("尚未連線成功")
                .setMessage("請開啟藍芽，並將平板和手錶進行連線")
                .setPositiveButton("前往連線", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MGWatch.connect(Home.this);
                    }
                })
                .setCancelable(false);
        dialog.show();
    }
    public void showTrainingDialog(){
        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("尚未完成手勢訓練")
                .setMessage("請配戴手錶並完成所有手勢訓練")
                .setPositiveButton("前往訓練手勢", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MGWatch.trainRequiredGestures(Home.this);
                    }
                })
                .setNegativeButton("稍後訓練", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setStatusText("尚未完成手勢訓練");
                        ((Button)findViewById(R.id.trainButton)).setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }
    public void setListeners(){
        ((Button)findViewById(R.id.trainButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                MGWatch.trainRequiredGestures(Home.this);
            }
        });
    }

    public void setText(final int resId, final String text){
        if (Looper.myLooper() == Looper.getMainLooper()) {
            TextView textView = ((TextView) findViewById(resId));
            if (textView != null)
                textView.setText(text);
        } else runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = ((TextView)findViewById(resId));
                if (textView != null)
                    textView.setText(text);
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
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
                intent.setClass(Home.this, SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            }
        });
        //頁面跳轉->選擇遊戲
        ImageView btn_start = findViewById(R.id.start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, GameHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            }
        });

        //頁面跳轉->選擇專注力分析
        ImageView btn_focus =(ImageView)findViewById(R.id.focus);
        btn_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Home.this, AttentionTestHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            }
        });


        //頁面跳轉->訓練記錄
        ImageView btn_record = findViewById(R.id.record);
        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, TrainRecord.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());
            }
        });

        //頁面跳轉->個人化設置
        ImageView btn_personal = findViewById(R.id.personal);
        btn_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(Home.this, Personal.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Home.this).toBundle());

            }



        });
    }
}