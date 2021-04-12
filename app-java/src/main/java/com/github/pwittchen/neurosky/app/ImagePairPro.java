package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
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

public class ImagePairPro extends AppCompatActivity {

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

    private int red;
    private int blue;
    private int green;

    private int optiona;
    private int optionb;
    private int optionc;
    private int optiona_border;
    private int optionb_border;
    private int optionc_border;

    ImageView btn_right,btn_ok;


    private TextView ImageButtonA;
    private TextView ImageButtonB;
    private TextView ImageButtonC;

    int count = 0; //計算遊戲答對題數
    int clicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_image_pair_pro);
        //設定隱藏標題
        getSupportActionBar().hide();


        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);
        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);

        //頁面跳轉  點選 pause
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stop time
                pauseTime=System.currentTimeMillis();
                handler.removeCallbacks(updateTimer);

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
                                finish();
                            }
                        })
                        .setNegativeButton("繼續",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                pauseTotal+=System.currentTimeMillis()-pauseTime;
                                handler.post(updateTimer);
                                pauseTime=0L;
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
        Log.d("MainActivity", "Current Timestamp: " + sdf.format(new Date()));

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
            Log.d("MainActivity", "formattedTime " + formattedTime);
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
                            finish();
                        }
                    })
                    .setNegativeButton("離開",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(ImagePairPro.this, GameHome.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //        先寫死，後期在統一改 UID
//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("game_record").document(userID);
            //自動產生 document id
            DocumentReference documentReference = fStore.collection("game_record").document("game_record_imagepair").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq").document();
            Map<String,Object> gameresult = new HashMap<>();
//        user.put("user", userID);
            recordSeconds = String.valueOf(totalSeconds);
            gameresult.put("record", formattedTime);
            gameresult.put("secondRecord", recordSeconds);
            gameresult.put("createdAt", createdAt);
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
            colorTextView.setTag("紅色");
        }
        else if (col == -5973084){
            colorTextView.setTag("綠色");
        }
        else{
            colorTextView.setTag("藍色");
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
        colorNames.add("紅色");
        colorNames.add("綠色");
        colorNames.add("藍色");

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
}
