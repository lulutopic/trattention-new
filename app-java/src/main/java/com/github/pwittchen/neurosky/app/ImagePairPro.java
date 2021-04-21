package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import com.madgaze.watchsdk.MGWatch;
import com.madgaze.watchsdk.MobileActivity;
import com.madgaze.watchsdk.WatchException;
import com.madgaze.watchsdk.WatchGesture;

public class ImagePairPro extends MobileActivity {
    private MediaPlayer music;
    private Long spentTime, pauseTime=0L, pauseTotal=0L, startTime, hour, minutes, seconds, totalSeconds; //初始時間
    private Chronometer timer; //已經過時間
    private Handler handler = new Handler(); //計時器的執行緒宣告
    private String formattedTime, recordSeconds;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String createdAt;


    private ArrayList<String> colorNames = new ArrayList<>(); //文字意思的顏色
    private ArrayList<Integer> colorValues = new ArrayList<>(); //文字真正的底色
    private ArrayList<TextView> button = new ArrayList<>(); // ABC選項
    private ArrayList<TextView> button1 = new ArrayList<>(); // ABC選項(不隨機)

    private TextView colorTextView; // 題目的文字

    private int red, blue, green;

    private int optiona, optionb, optionc, optiona_border, optionb_border, optionc_border;

    ImageView btn_right,btn_ok,btn_left;

    private int optiona;
    private int optionb;
    private int optionc;
    private int optiona_border;
    private int optionb_border;
    private int optionc_border;

    ImageView btn_right,btn_ok;


    private TextView ImageButtonA, ImageButtonB, ImageButtonC;

    int count = 0; //計算遊戲答對題數
    int clicked = 0;

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
            WatchGesture.MOVE_FOREARM_DOWN,
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
            //手臂快速移動
            WatchGesture.MOVE_FOREARM_DOWN,
            WatchGesture.MOVE_FOREARM_LEFT,
            WatchGesture.MOVE_FOREARM_UP,
            WatchGesture.MOVE_FOREARM_RIGHT,
    };
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

        setContentView(R.layout.activity_image_pair_pro);



        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);
        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);
        //音樂
        music = MediaPlayer.create(this, R.raw.star);
        music.setLooping(true);
        music.start();
        //頁面跳轉  點選 pause
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stop time
                pauseTime=System.currentTimeMillis();
                handler.removeCallbacks(updateTimer);
                //音樂暫停
                music.pause();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImagePairPro.this);
                LayoutInflater inflater = ImagePairPro.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_stop_button, null));
                alertDialogBuilder
                        .setNeutralButton("離開",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                Intent intent = new Intent();
                                intent.setClass(ImagePairPro.this,GameHome.class);
                                startActivity(intent);
                                //音樂釋放
                                music.release();
                                music=null;
                                finish();
                            }
                        })
                        .setNegativeButton("繼續",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                pauseTotal+=System.currentTimeMillis()-pauseTime;
                                handler.post(updateTimer);
                                pauseTime=0L;
                                //音樂繼續
                                music.start();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(340, 400);
            }
        });

        ImageView button5 = findViewById(R.id.imagetips);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImagePairPro.this);
                LayoutInflater inflater = ImagePairPro.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_image_pair_pro_tips, null));

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //計算當前時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.TAIWAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        createdAt = sdf.format(new Date()); //-prints-> 2015-01-22T03:23:26Z

        //呼叫函式
        populateBothArraylists();
        getRandomColor();
        deter();
        setupViewsAndListeners();
    }

    //監聽事件的函式
    private void setupViewsAndListeners(){
        button1.get(0).setBackgroundResource(optiona_border);
        btn_right.setOnClickListener(new View.OnClickListener(){
            @Override
            //設定點擊事件
            public void onClick(View v){
                switch(clicked){
                    case(0):
                        button1.get(0).setBackgroundResource(optiona);
                        button1.get(1).setBackgroundResource(optionb_border);
                        System.out.println();
                        clicked++;
                        break;
                    case(1):
                        button1.get(1).setBackgroundResource(optionb);
                        button1.get(2).setBackgroundResource(optionc_border);
                        clicked++;
                        break;
                    case(2):
                        button1.get(2).setBackgroundResource(optionc);
                        button1.get(0).setBackgroundResource(optiona_border);
                        clicked-=2;
                        break;
                }

            }
        });

        // btn_left.setOnClickListener(new View.OnClickListener(){
        //     @Override
        //     //設定點擊事件
        //     public void onClick(View v){
        //         switch(clicked){
        //             case(0):
        //                 button1.get(0).setBackgroundResource(optiona);
        //                 button1.get(2).setBackgroundResource(optionc_border);
        //                 System.out.println();
        //                 clicked+=2;
        //                 break;
        //             case(2):
        //                 button1.get(2).setBackgroundResource(optionc);
        //                 button1.get(1).setBackgroundResource(optionb_border);
        //                 clicked--;
        //                 break;
        //             case(1):
        //                 button1.get(1).setBackgroundResource(optionb);
        //                 button1.get(0).setBackgroundResource(optiona_border);
        //                 clicked--;
        //                 break;
        //         }

        //     }
        // });

        btn_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            //設定點擊事件
            public void onClick(View v){
                //回傳題目的文字底色的文字標籤
                String Tag = (String) colorTextView.getTag();
                //如果選項Ａ的文字意思等於標籤Ａ
                if(button1.get(clicked).getText() == Tag){
                    count++;
                    getRandomColor();
                    deter();
                    checkEnd();
                }

            }
        });

    }

    //三個串列的隨機排列
    private void getRandomColor(){
        //Collections.shuffle 隨機排列三個串列
        Collections.shuffle(colorNames);
        Collections.shuffle(colorValues);
        Collections.shuffle(button);

        //colorChosen 取出colorNames裡的資料 當作題目的文字
        String colorChosen1 = colorNames.get(0);
        String colorChosen2 = colorNames.get(1);
        String colorChosen3 = colorNames.get(2);

        //setText 將文字設定給 題目 and 答案
        colorTextView.setText(colorChosen1);
        button.get(0).setText(colorChosen1);
        button.get(1).setText(colorChosen2);
        button.get(2).setText(colorChosen3);

        //setTextColor 設定文字底色給 題目 and 答案
        colorTextView.setTextColor(colorValues.get(0));
        button.get(0).setTextColor(colorValues.get(0));
        button.get(1).setTextColor(colorValues.get(1));
        button.get(2).setTextColor(colorValues.get(2));
    }
    //檢查是否遊戲完成並且題目跳轉
    private void checkEnd(){
        if(count == 10){
            //停止計時器的執行緒
            handler.removeCallbacks(updateTimer);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImagePairPro.this);
            alertDialogBuilder
                    .setMessage("恭喜!遊戲結束~")
                    .setCancelable(false)
                    .setPositiveButton("查看結果",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(ImagePairPro.this, GameResultImagePair.class);
                            startActivity(intent);
                            //音樂釋放
                            music.release();
                            music=null;
                            finish();
                        }
                    })
                    .setNegativeButton("離開",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(ImagePairPro.this, GameHome.class);
                            startActivity(intent);
                            //音樂釋放
                            music.release();
                            music=null;
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //        先寫死，後期在統一改 UID
//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("game_record").document(userID);
            //自動產生 document id
            DocumentReference documentReference = fStore.collection("game_record").document("game_record_imagepair").collection("data").document();
            Map<String,Object> gameresult = new HashMap<>();
//        user.put("user", userID);
            recordSeconds = String.valueOf(totalSeconds);
            gameresult.put("record", formattedTime);
            gameresult.put("secondRecord", recordSeconds);
            gameresult.put("createdAt", createdAt);
            gameresult.put("user","MELJmK6vYxeoKCrWhvJyy4Xfriq");
            documentReference.set(gameresult).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    //幫顏色設定標籤（判斷文字底色是否等於colorValues裡的顏色）
    private void deter(){
        int col = colorTextView.getCurrentTextColor();
        if (col == -571050){
            colorTextView.setTag("紅色\nred");
        }
        else if (col == -5973084){
            colorTextView.setTag("綠色\ngreen");
        }
        else{
            colorTextView.setTag("藍色\nblue");
        }
    }

    //接前端的id
    private void populateBothArraylists(){
        //question
        colorTextView = (TextView) findViewById(R.id.question);

        //按鈕選項
        btn_right=(ImageView)findViewById(R.id.right_arrow);
        btn_ok=(ImageView)findViewById(R.id.ok);

        //ABC選項
        ImageButtonA = (TextView) findViewById(R.id.optionA);
        ImageButtonB = (TextView)findViewById(R.id.optionB);
        ImageButtonC = (TextView) findViewById(R.id.optionC);

        //把顏色字串加入coloNames ArrayLists
        colorNames.add("紅色\nred");
        colorNames.add("綠色\ngreen");
        colorNames.add("藍色\nblue");

        //把放在color.xml裡面的顏色指定給相對應的變數
        red = R.color.colorRed;
        green = R.color.colorGreen;
        blue = R.color.colorBlue;

        optiona = R.drawable.optiona;
        optionb = R.drawable.optionb;
        optionc = R.drawable.optionc;

        optiona_border = R.drawable.optiona_border;
        optionb_border = R.drawable.optionb_border;
        optionc_border = R.drawable.optionc_border;

        //Add color values to the arraylist [-571050, -5973084, -9328385]
        colorValues.add(ContextCompat.getColor(this,red));
        colorValues.add(ContextCompat.getColor(this,green));
        colorValues.add(ContextCompat.getColor(this,blue));

        //把ＡＢＣ選項加入到button ArrayLists
        button.add(ImageButtonA);
        button.add(ImageButtonB);
        button.add(ImageButtonC);

        button1.add(ImageButtonA);
        button1.add(ImageButtonB);
        button1.add(ImageButtonC);
    }

    //計時器的計時方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (Chronometer) findViewById(R.id.timer);
            spentTime = System.currentTimeMillis() - startTime - pauseTotal;
            //計算目前已過小時數
            hour = (spentTime/1000)/3600;
            //計算目前已過分鐘數
            minutes = ((spentTime/1000)/60) % 60;
            //計算目前已過秒數
            seconds = (spentTime/1000) % 60;
            totalSeconds = spentTime/1000;
            formattedTime = String.format("%02d:%02d:%02d",hour, minutes, seconds);
            time.setText(formattedTime);
            handler.postDelayed(this, 1000);
        }
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
        setText(R.id.result, gesture.toString());


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //手勢控制向右
                if(gesture == WatchGesture.HANDBACK_RIGHT || gesture == WatchGesture.JOINTTAP_MIDDLE_RING
                        || gesture == WatchGesture.JOINTTAP_UPPER_RING || gesture == WatchGesture.JOINTTAP_MIDDLE_MIDDLE
                        ||gesture == WatchGesture.JOINTTAP_UPPER_MIDDLE ||gesture == WatchGesture.JOINTTAP_MIDDLE_INDEX
                        || gesture == WatchGesture.JOINTTAP_UPPER_INDEX){
                    switch(clicked){
                        case(0):
                            button1.get(0).setBackgroundResource(optiona);
                            button1.get(1).setBackgroundResource(optionb_border);
                            System.out.println();
                            clicked++;
                            break;
                        case(1):
                            button1.get(1).setBackgroundResource(optionb);
                            button1.get(2).setBackgroundResource(optionc_border);
                            clicked++;
                            break;
                        case(2):
                            button1.get(2).setBackgroundResource(optionc);
                            button1.get(0).setBackgroundResource(optiona_border);
                            clicked-=2;
                            break;
                    }
                }
                //手勢控制確認選取
                else if (gesture == WatchGesture.THUMBTAP_INDEX || gesture == WatchGesture.THUMBTAP_INDEX_2
                        || gesture == WatchGesture.THUMBTAP_MIDDLE || gesture == WatchGesture.THUMBTAP_MIDDLE_2
                        ||gesture == WatchGesture.THUMBTAP_INDEX_MIDDLE || gesture == WatchGesture.THUMBTAP_INDEX_MIDDLE_2
                        || gesture == WatchGesture.FINGER_SNAP) {
                    //回傳題目的文字底色的文字標籤
                    String Tag = (String) colorTextView.getTag();
                    //如果選項Ａ的文字意思等於標籤Ａ
                    if(button1.get(clicked).getText() == Tag){
                        count++;
                        getRandomColor();
                        deter();
                        checkEnd();
                    }
                }
            }
        });
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
                        MGWatch.connect(ImagePairPro.this);
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
                        MGWatch.trainRequiredGestures(ImagePairPro.this);
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
                MGWatch.trainRequiredGestures(ImagePairPro.this);
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


