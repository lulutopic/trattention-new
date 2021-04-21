package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Random;

public class ImagePairEasy extends AppCompatActivity {
    private MediaPlayer music;
    private Long startTime; //初始時間
    private Chronometer timer; //已經過時間
    private Long spentTime;
    private Long pauseTime=0L;
    private Long pauseTotal;
    private Handler handler = new Handler(); //計時器的執行緒宣告
    private String formattedTime;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String createdAt;


    private ArrayList<String> FruitNames = new ArrayList<>(); //文字意思的顏色
    private ArrayList<Integer> FruitIcon = new ArrayList<>(); //水果圖案
    private ArrayList<ImageView> button = new ArrayList<>(); // ABC選項
    private ArrayList<ImageView> button1 = new ArrayList<>(); // ABC選項(不隨機)

    private TextView FruitQuestion; // 題目的文字

    private int apple;
    private int pear;
    private int orange;

    private int optiona;
    private int optionb;
    private int optionc;
    private int optiona_border;
    private int optionb_border;
    private int optionc_border;


//    private int kiwi;
//    private int mango;

    int clicked = 0;



    private ImageView ImageButtonA;
    private ImageView ImageButtonB;
    private ImageView ImageButtonC;

    ImageView btn_right,btn_ok,btn_left;

    int count = 0; //計算遊戲答對題數

    Random ran = new Random();

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
        setContentView(R.layout.activity_image_pair_easy);



        //取得目前時間
        startTime = System.currentTimeMillis();
        pauseTotal= getIntent().getLongExtra("pause",0);

        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);

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

        //音樂
        music = MediaPlayer.create(this, R.raw.bit1);
        music.setLooping(true);
        music.start();

        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stop time
                pauseTime=System.currentTimeMillis();
                handler.removeCallbacks(updateTimer);
                //音樂暫停
                music.pause();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImagePairEasy.this);
                LayoutInflater inflater = ImagePairEasy.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_stop_button, null));
                alertDialogBuilder
                        .setNeutralButton("離開",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                Intent intent = new Intent();
                                intent.setClass(ImagePairEasy.this,GameHome.class);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImagePairEasy.this);
                LayoutInflater inflater = ImagePairEasy.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_image_pair_tips, null));

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
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

        btn_left.setOnClickListener(new View.OnClickListener(){
            @Override
            //設定點擊事件
            public void onClick(View v){
                switch(clicked){
                    case(0):
                        button1.get(0).setBackgroundResource(optiona);
                        button1.get(2).setBackgroundResource(optionc_border);
                        System.out.println();
                        clicked+=2;
                        break;
                    case(2):
                        button1.get(2).setBackgroundResource(optionc);
                        button1.get(1).setBackgroundResource(optionb_border);
                        clicked--;
                        break;
                    case(1):
                        button1.get(1).setBackgroundResource(optionb);
                        button1.get(0).setBackgroundResource(optiona_border);
                        clicked--;
                        break;
                }

            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            //設定點擊事件
            public void onClick(View v){
                //回傳題目的文字底色的文字標籤
                Integer Tag = (Integer) FruitQuestion.getTag();
                System.out.println(Tag);//2131165271 apple
                System.out.println(button.get(clicked).getTag());
                //如果選項Ａ的文字意思等於標籤Ａ
                if(Tag.equals(button.get(clicked).getTag())){
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
        Collections.shuffle(FruitNames);
        Collections.shuffle(FruitIcon);
        //Collections.shuffle(button);

        //colorChosen 取出colorNames裡的資料 當作題目的文字
        String Fruitchosen = FruitNames.get(0);

        //setText 將文字設定給 題目 and 答案
        FruitQuestion.setText(Fruitchosen);

        //setTextColor 設定文字底色給 題目 and 答案
        button.get(0).setImageResource(FruitIcon.get(0));
        button.get(0).setTag(FruitIcon.get(0));

        button.get(1).setImageResource(FruitIcon.get(1));
        button.get(1).setTag(FruitIcon.get(1));

        button.get(2).setImageResource(FruitIcon.get(2));
        button.get(2).setTag(FruitIcon.get(2));
    }
    //檢查是否遊戲完成並且題目跳轉
    private void checkEnd(){
        if(count == 10){
            //頁面跳轉
            Intent intent = new Intent();
            intent.setClass(ImagePairEasy.this, ImagePairMed.class);
            intent.putExtra("time",startTime);
            //音樂釋放
            music.release();
            music=null;
            startActivity(intent);
            finish();

        }
    }

    //幫顏色設定標籤（判斷文字底色是否等於colorValues裡的顏色）
    private void deter(){
        String fruit = (String) FruitQuestion.getText();
        if (fruit == "蘋果\napple"){
            FruitQuestion.setTag(apple);
        }
        else if (fruit == "橘子\norange"){
            FruitQuestion.setTag(orange);
        }
        else{
            FruitQuestion.setTag(pear);
        }
    }

    //接前端的id
    private void populateBothArraylists(){
        //question
        FruitQuestion = (TextView) findViewById(R.id.question);

        //ABC選項
        ImageButtonA = (ImageView) findViewById(R.id.optionA);
        ImageButtonB = (ImageView) findViewById(R.id.optionB);
        ImageButtonC = (ImageView) findViewById(R.id.optionC);

        //按鈕選項
        btn_right=(ImageView)findViewById(R.id.right_arrow);
        btn_ok=(ImageView)findViewById(R.id.ok);
        btn_left=(ImageView)findViewById(R.id.left_arrow);

        //把顏色字串加入coloNames ArrayLists
        FruitNames.add("蘋果\napple");
        FruitNames.add("橘子\norange");
        FruitNames.add("梨子\npear");
//        FruitNames.add("芒果");
//        FruitNames.add("奇異果");

        //把放在color.xml裡面的顏色指定給相對應的變數
        apple = R.drawable.apple;
        orange = R.drawable.orange;
        pear = R.drawable.pear;

        optiona = R.drawable.optiona;
        optionb = R.drawable.optionb;
        optionc = R.drawable.optionc;

        optiona_border = R.drawable.optiona_border;
        optionb_border = R.drawable.optionb_border;
        optionc_border = R.drawable.optionc_border;
//        mango = R.drawable.mango;
//        Log.d("MainActivity", "mango " + mango);
//        kiwi = R.drawable.kiwi;
//        Log.d("MainActivity", "kiwi " + kiwi);

        //Add color values to the arraylist [-571050, -5973084, -9328385]
        FruitIcon.add(apple);
        FruitIcon.add(orange);
        FruitIcon.add(pear);
//        FruitIcon.add(mango);
//        FruitIcon.add(kiwi);

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
            Long hour = (spentTime/1000)/3600;
            //計算目前已過分鐘數
            Long minius = ((spentTime/1000)/60) % 60;
            //計算目前已過秒數
            Long seconds = (spentTime/1000) % 60;
            formattedTime = String.format("%02d:%02d:%02d",hour, minius, seconds);
            time.setText(formattedTime);
            handler.postDelayed(this, 1000);
        }
    };
}
