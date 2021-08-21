package com.github.pwittchen.neurosky.app;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class MemoryGamePro extends AppCompatActivity {

    private MediaPlayer music;

    protected Long startTime;
    private Long spentTime, pauseTime=0L, pauseTotal, hour, minutes, seconds, totalSeconds;
    private Chronometer timer;
    private Handler handler = new Handler();
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String createdAt;
    private String formattedTime;
    private ImageView temp, collect;
    private int moved=1;
    ImageView iv_11,iv_12,iv_13,iv_14,
            iv_21,iv_22,iv_23,iv_24,
            iv_31,iv_32,iv_33,iv_34,
            iv_41,iv_42,iv_43,iv_44,
            iv_100;

    //array for the images
    Integer[] cardsArray = {102,102,102,102,102,102,107,107,107,107,107,106,106,106,106,106};
    //問題的陣列
    Integer[] questionArray = {5,5,5,4,4,4,1,1};

    //actual images
    int questionCard;//題目
    int image102,image107,image106,image101,image103,image105;
    int firstCard,secondCard;
    int clickedFirst,clickedSecond;
    int cardNumber=1;
    int questionCount = 0;
    int i=0;
int test;



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
        setContentView(R.layout.activity_memory_game_hard);

        //計算當前時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.TAIWAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        createdAt = sdf.format(new Date()); //-prints-> 2015-01-22T03:23:26Z



        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);
        //暫停時間
        pauseTotal= getIntent().getLongExtra("pause",0);

        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);

        //題目洗牌
        Collections.shuffle(Arrays.asList(questionArray));
        questionArray=Arrays.copyOf(questionArray,9);
        questionArray[8] =0;
        //音樂
        music = MediaPlayer.create(this, R.raw.star);
        music.setLooping(true);
        music.start();
        //設定第一題
        questionCard = questionArray[questionCount];
        //點pause
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //stop time
                pauseTime=System.currentTimeMillis();
                handler.removeCallbacks(updateTimer);
                //音樂暫停
                music.pause();
                AlertDialog.Builder dialog = new AlertDialog.Builder(MemoryGamePro.this);
                dialog.setTitle("請點擊以下按鈕，選擇離開 / 繼續？");
                dialog.setNegativeButton("離開",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MemoryGamePro.this, "離開訓練",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(MemoryGamePro.this,GameHome.class);
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
                        Toast.makeText(MemoryGamePro.this, "繼續訓練",Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemoryGamePro.this);
                LayoutInflater inflater = MemoryGamePro.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_memory_tips, null));

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

        //game
        //題目顏色
        iv_100=(ImageView)findViewById(R.id.iv_100);
        iv_11=(ImageView)findViewById(R.id.iv_11);
        iv_12=(ImageView)findViewById(R.id.iv_12);
        iv_13=(ImageView)findViewById(R.id.iv_13);
        iv_14=(ImageView)findViewById(R.id.iv_14);
        iv_21=(ImageView)findViewById(R.id.iv_21);
        iv_22=(ImageView)findViewById(R.id.iv_22);
        iv_23=(ImageView)findViewById(R.id.iv_23);
        iv_24=(ImageView)findViewById(R.id.iv_24);
        iv_31=(ImageView)findViewById(R.id.iv_31);
        iv_32=(ImageView)findViewById(R.id.iv_32);
        iv_33=(ImageView)findViewById(R.id.iv_33);
        iv_34=(ImageView)findViewById(R.id.iv_34);
        iv_41=(ImageView)findViewById(R.id.iv_41);
        iv_42=(ImageView)findViewById(R.id.iv_42);
        iv_43=(ImageView)findViewById(R.id.iv_43);
        iv_44=(ImageView)findViewById(R.id.iv_44);

        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_21.setTag("4");
        iv_22.setTag("5");
        iv_23.setTag("6");
        iv_24.setTag("7");
        iv_31.setTag("8");
        iv_32.setTag("9");
        iv_33.setTag("10");
        iv_34.setTag("11");
        iv_41.setTag("12");
        iv_42.setTag("13");
        iv_43.setTag("14");
        iv_44.setTag("15");
        //load the card images
        frontOfCardsResources();
        //第一題的顏色
        if(questionCard==5){
            iv_100.setImageResource(image101);
        }else if(questionCard==4){
            iv_100.setImageResource(image105);
        }else if(questionCard==1) {
            iv_100.setImageResource(image103);
        }

        Collections.shuffle(Arrays.asList(cardsArray));

        //Listener 等待使用者點擊此事件
        //override 覆蓋掉原本android studio 上層物件

        ImageView right_arrow = findViewById(R.id.right_arrow);
        ImageView left_arrow = findViewById(R.id.left_arrow);
        ImageView up_arrow = findViewById(R.id.up_arrow);
        ImageView down_arrow = findViewById(R.id.down_arrow);
        ImageView ok = findViewById(R.id.ok);

        ImageView[] imageArray = {iv_11,iv_12,iv_13,iv_14,
                iv_21,iv_22,iv_23,iv_24,
                iv_31,iv_32,iv_33,iv_34,
                iv_41,iv_42,iv_43,iv_44};

        temp = imageArray[i];

        //初始從第一個開始
        iv_11.setImageResource(R.drawable.memorybackground);
        TextView result = (TextView) findViewById(R.id.result);

        right_arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("HANDBACK_RIGHT");
                moved=1;
                setBtnStyle(view);
                ok.setVisibility(View.VISIBLE);
                int j=i;
                //如果現在在最右邊的話，從最左邊開始
                if(i==15) {
                    i =0;
                }
                //不是的話，往右移
                else {
                    i ++ ;
                }
                //temp:現在新到的格子
                temp = imageArray[i];
                //當現在要前往的格子是消失的話，往右找沒有消失的
                while (temp.getVisibility() == View.INVISIBLE) {
                    if (i==15){
                        i=0;
                    }
                    else {
                        i++;
                    }
                    temp = imageArray[i];
                }

                //消除動作：prev：到下一格後的上一格
                ImageView prev = imageArray[j];
                //若prev不是翻開過的，設為蓋起來的黑框背景
                if (prev != collect) {
                    prev.setImageResource(R.drawable.memoryback);
                }
                //若現在新到的這格不是翻開過的，設為聚焦的藍框背景
                if (temp != collect){
                    temp.setImageResource(R.drawable.memorybackground);
                }

            };
        });


        left_arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("HANDBACK_LEFT");

                moved=1;
                setBtnStyle(view);
                ok.setVisibility(View.VISIBLE);
                int j=i;
                //如果現在在最右邊的話，從最左邊開始
                if(i==0) {
                    i =15;
                }
                //不是的話，往右移
                else {
                    i -- ;
                }
                //temp:現在新到的格子
                temp = imageArray[i];
                //當現在要前往的格子是消失的話，往右找沒有消失的
                while (temp.getVisibility() == View.INVISIBLE) {
                    if (i==0){
                        i=15;
                    }
                    else {
                        i--;
                    }
                    temp = imageArray[i];
                }
                ImageView prev = imageArray[j];
                if (prev != collect) {
                    prev.setImageResource(R.drawable.memoryback);
                }
                if (temp != collect){
                    temp.setImageResource(R.drawable.memorybackground);
                }

            };

        });
        up_arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("HANDBACK_UP");
                moved=1;
                setBtnStyle(view);
                ok.setVisibility(View.VISIBLE);
                int j=i;
                if(i==0 ||i==1||i==2||i==3) {
                    i = i+12;
                }
                else {
                    i = i - 4;
                }
                temp = imageArray[i];
                while (temp.getVisibility() == View.INVISIBLE) {

                    if(i==0 ||i==1||i==2||i==3) {
                        i = i+12;
                    }
                    else {
                        i = i - 4;
                    }
                    temp = imageArray[i];
                }
                ImageView prev = imageArray[j];
                if (prev != collect) {
                    prev.setImageResource(R.drawable.memoryback);
                }
                if (temp != collect){
                    temp.setImageResource(R.drawable.memorybackground);
                }
            };
        });

        down_arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("HANDBACK_DOWN");
                moved=1;
                setBtnStyle(view);
                ok.setVisibility(View.VISIBLE);
                int j=i;

                if(i==12 ||i==13||i==14||i==15) {
                    i = i-12;
                }
                else {
                    i = i + 4;
                }
                temp = imageArray[i];
                while (temp.getVisibility() == View.INVISIBLE) {
                    if(i==12 ||i==13||i==14||i==15) {
                        i = i-12;
                    }
                    else {
                        i = i + 4;
                    }
                    temp = imageArray[i];
                }
                ImageView prev = imageArray[j];
                if (prev != collect) {
                    prev.setImageResource(R.drawable.memoryback);
                }
                if (temp != collect){
                    temp.setImageResource(R.drawable.memorybackground);
                }
            };
        });

        ok.setVisibility(View.VISIBLE);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("THUMBTAP_INDEX_MIDDLE");
                ok.setImageResource(R.drawable.ok2);
                Timer t = new Timer(false);
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ok.setImageResource(R.drawable.ok1);
                            }
                        });
                    }
                }, 500);
                if (moved==1) {
                    int theCard = Integer.parseInt((String) temp.getTag());
                    //如果當前選取的不是已經選取過的
                    if (temp != collect) {
                        temp.setImageResource(R.drawable.memorybackground);
                        if (cardNumber == 1) {
                            collect = temp;
                        } else {
                            collect = null;
                        }
                        doStuff(temp, theCard);
                    }
                    moved=0;
                    ok.setImageResource(R.drawable.ok2);
                }

            }
        });
    }

    //set the conect image to the imageview
    private void doStuff(ImageView iv,int card){
        if(cardsArray[card]==102){
            iv.setImageResource(image102);
        }else if(cardsArray[card]==107){
            iv.setImageResource(image107);
        }else if(cardsArray[card]==106) {
            iv.setImageResource(image106);
        }

        //check which image is selected and save
        if(cardNumber==1){
            firstCard=cardsArray[card];
            cardNumber=2;
            clickedFirst=card;
            iv.setEnabled(false);
        }else if(cardNumber==2){
            secondCard=cardsArray[card];
            cardNumber=1;
            clickedSecond=card;

            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);
            iv_41.setEnabled(false);
            iv_42.setEnabled(false);
            iv_43.setEnabled(false);
            iv_44.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //check if the selected images are equal
                    calculate();
                }
            },500);
        }
    }

    //是否要消掉
    private void calculate(){

        if(questionCard==Math.abs(firstCard-secondCard)) {
            //讓題目變下一題
            questionCard = questionArray[++questionCount];
            if (questionCard == 5) {
                iv_100.setImageResource(image101);
            } else if (questionCard == 4) {
                iv_100.setImageResource(image105);
            } else if (questionCard == 1) {
                iv_100.setImageResource(image103);
            }


            if(clickedFirst==0){
                iv_11.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==1){
                iv_12.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==2){
                iv_13.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==3){
                iv_14.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==4){
                iv_21.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==5){
                iv_22.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==6){
                iv_23.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==7){
                iv_24.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==8){
                iv_31.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==9){
                iv_32.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==10){
                iv_33.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==11){
                iv_34.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==12){
                iv_41.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==13){
                iv_42.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==14){
                iv_43.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==15){
                iv_44.setVisibility(View.INVISIBLE);
            }


            if(clickedSecond==0){
                iv_11.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==1){
                iv_12.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==2){
                iv_13.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==3){
                iv_14.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==4){
                iv_21.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==5){
                iv_22.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==6){
                iv_23.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==7){
                iv_24.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==8){
                iv_31.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==9){
                iv_32.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==10){
                iv_33.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==11){
                iv_34.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==12){
                iv_41.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==13){
                iv_42.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==14){
                iv_43.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==15){
                iv_44.setVisibility(View.INVISIBLE);
            }
        }else{
            iv_11.setImageResource(R.drawable.memoryback);
            iv_12.setImageResource(R.drawable.memoryback);
            iv_13.setImageResource(R.drawable.memoryback);
            iv_14.setImageResource(R.drawable.memoryback);
            iv_21.setImageResource(R.drawable.memoryback);
            iv_22.setImageResource(R.drawable.memoryback);
            iv_23.setImageResource(R.drawable.memoryback);
            iv_24.setImageResource(R.drawable.memoryback);
            iv_31.setImageResource(R.drawable.memoryback);
            iv_32.setImageResource(R.drawable.memoryback);
            iv_33.setImageResource(R.drawable.memoryback);
            iv_34.setImageResource(R.drawable.memoryback);
            iv_41.setImageResource(R.drawable.memoryback);
            iv_42.setImageResource(R.drawable.memoryback);
            iv_43.setImageResource(R.drawable.memoryback);
            iv_44.setImageResource(R.drawable.memoryback);
        }
        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        iv_34.setEnabled(true);
        iv_41.setEnabled(true);
        iv_42.setEnabled(true);
        iv_43.setEnabled(true);
        iv_44.setEnabled(true);

        //檢查遊戲結束
        checkEnd();
    }
    private void checkEnd() {
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE &&
                iv_41.getVisibility() == View.INVISIBLE &&
                iv_42.getVisibility() == View.INVISIBLE &&
                iv_43.getVisibility() == View.INVISIBLE &&
                iv_44.getVisibility() == View.INVISIBLE) {
            //停止計時器的執行緒
            handler.removeCallbacks(updateTimer);
            //頁面跳轉
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemoryGamePro.this);
            alertDialogBuilder
                    .setTitle("恭喜 ! 遊戲結束 ~")
                    .setCancelable(false)
                    .setPositiveButton("查看結果",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(MemoryGamePro.this, GameResultMemory.class);
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
                            intent.setClass(MemoryGamePro.this, GameHome.class);
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
            DocumentReference documentReference = fStore.collection("game_record").document("game_record_memory").collection("data").document();
            Map<String,Object> gameresult = new HashMap<>();
//        user.put("user", userID);
            gameresult.put("record", formattedTime);
            gameresult.put("secondRecord", totalSeconds);
            gameresult.put("createdAt", createdAt);
            gameresult.put("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq");
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

    private void frontOfCardsResources(){
        image103=R.drawable.memory103;
        image101=R.drawable.memory101;
        image105=R.drawable.memory105;

        image102=R.drawable.memory102;
        image107=R.drawable.memory107;
        image106=R.drawable.memory106;
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

    //固定要執行的方法
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
            //計算總秒數
            totalSeconds = spentTime/1000;
            formattedTime = String.format("%02d:%02d:%02d",hour, minutes, seconds);
            time.setText(formattedTime);
            handler.postDelayed(this, 1000);
        }
    };

    public void btnClick(View view) {
        timer.setBase(SystemClock.elapsedRealtime());//計時器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0"+String.valueOf(hour)+":%s");
    }
}
