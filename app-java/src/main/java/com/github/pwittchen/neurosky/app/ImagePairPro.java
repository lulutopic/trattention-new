package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

public class ImagePairPro extends AppCompatActivity {
    private MediaPlayer music;
    private Long spentTime, pauseTime=0L, pauseTotal=0L, startTime, hour, minutes, seconds, totalSeconds; //初始時間
    private Chronometer timer; //已經過時間
    private Handler handler = new Handler(); //計時器的執行緒宣告
    private String formattedTime;
    public static final String TAG = "TAG";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String createdAt;

    private ArrayList<String> colorNames = new ArrayList<>(); //文字意思的顏色
    private ArrayList<Integer> colorValues = new ArrayList<>(); //文字真正的底色
    private ArrayList<TextView> button = new ArrayList<>(); // ABC選項
    private ArrayList<TextView> button1 = new ArrayList<>(); // ABC選項(不隨機)

    private TextView colorTextView; // 題目的文字

    private int red, blue, green, optiona, optionb, optionc, optiona_border, optionb_border, optionc_border, clicked = 0, count = 0;

    ImageView btn_right,btn_ok,btn_left;

    private TextView ImageButtonA, ImageButtonB, ImageButtonC;

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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ImagePairPro.this);
                dialog.setTitle("請點擊以下按鈕，選擇離開 / 繼續？");
                dialog.setNegativeButton("離開",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ImagePairPro.this, "離開訓練",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(ImagePairPro.this,GameHome.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
                        //音樂釋放
                        music.release();
                        music=null;
                        finish();
                    }

                });
                dialog.setPositiveButton("繼續",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(ImagePairPro.this, "繼續訓練",Toast.LENGTH_SHORT).show();
                        pauseTotal+=System.currentTimeMillis()-pauseTime;
                        handler.post(updateTimer);
                        pauseTime=0L;
                        //音樂繼續
                        music.start();
                    }

                });
                dialog.setCancelable(false);
                dialog.create();
                dialog.show();
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

        ImageView button6 = findViewById(R.id.imagebgm);
        button6.setTag("0");
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(button6.getTag().equals("0")){
                    button6.setImageResource(R.drawable.bgm_off);
                    button6.setTag("1");
                    music.pause();
                }
                else{
                    button6.setImageResource(R.drawable.bgm_on);
                    button6.setTag("0");
                    music.start();
                }

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
        TextView result = (TextView) findViewById(R.id.result);

        button1.get(0).setBackgroundResource(optiona_border);
        btn_right.setOnClickListener(new View.OnClickListener(){
            @Override
            //設定點擊事件
            public void onClick(View v){
                result.setText("HANDBACK_RIGHT");
                setBtnStyle(v);
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
                result.setText("HANDBACK_LEFT");
                setBtnStyle(v);
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
                result.setText("THUMBTAP_INDEX_MIDDLE");
                btn_ok.setImageResource(R.drawable.ok2);
                Timer t = new Timer(false);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                btn_ok.setImageResource(R.drawable.ok1);
                            }
                        });
                    }
                }, 500);
//                setBtnStyle(v);
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
                    .setTitle("恭喜 ! 遊戲結束 ~")
                    .setCancelable(false)
                    .setPositiveButton("查看結果",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(ImagePairPro.this, GameResultImagePair.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
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
                            overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
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
            gameresult.put("record", formattedTime);
            gameresult.put("secondRecord", totalSeconds);
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
        btn_left=(ImageView)findViewById(R.id.left_arrow);

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
    private void setBtnStyle(View view){
        view.setBackgroundResource(R.drawable.buttonshadow);
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        view.setBackgroundResource(0);
                    }
                });
            }
        }, 500);
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
