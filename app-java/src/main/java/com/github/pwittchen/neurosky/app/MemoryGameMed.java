package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class MemoryGameMed extends AppCompatActivity {
    protected Long startTime;
    private Long spentTime;
    private Long pauseTime=0L;
    private Long pauseTotal;
    private Chronometer timer;
    private Handler handler = new Handler();
    private MediaPlayer music;
    private ImageView temp;
    private ImageView collect;
    private int moved=1;

    ImageView iv_11,iv_12,iv_13,iv_14,
            iv_21,iv_22,iv_23,iv_24,
            iv_31,iv_32,iv_33,iv_34;

    //array for the images
    Integer[] cardsArray = {101,102,103,104,105,106,201,202,203,204,205,206};

    //actual images
    int questionCard;//題目
    int questionCount = 0;
    int i=0;

    //actual images
    int image101,image102,image103,image104,image105,image106,image107,image108,image109,image110,
            image201,image202,image203,image204,image205,image206,image207,image208,image209,image210;
    int firstCard,secondCard;
    int clickedFirst,clickedSecond;
    int cardNumber=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_memory_game_med);

        //音樂
        music = MediaPlayer.create(this, R.raw.preview);
        music.setLooping(true);
        music.start();
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(MemoryGameMed.this);
                dialog.setTitle("請點擊以下按鈕，選擇離開 / 繼續？");
                dialog.setNegativeButton("離開",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MemoryGameMed.this, "離開訓練",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(MemoryGameMed.this,GameHome.class);
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
                        Toast.makeText(MemoryGameMed.this, "繼續訓練",Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MemoryGameMed.this);
                LayoutInflater inflater = MemoryGameMed.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_memory_easy_tips, null));

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


        //設定隱藏標題
        getSupportActionBar().hide();
        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);
        //接續前段時間
        pauseTotal= getIntent().getLongExtra("pause",0);
        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);


        //game
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



        //load the card images
        frontOfCardsResources();
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
                iv_31,iv_32,iv_33,iv_34};

        temp = imageArray[i];

        iv_11.setImageResource(R.drawable.memorybackground);
        TextView result = (TextView) findViewById(R.id.result);

        right_arrow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                result.setText("HANDBACK_RIGHT");
                moved=1;
                ok.setVisibility(View.VISIBLE);
                int j=i;
                //如果現在在最右邊的話，從最左邊開始
                if(i==11) {
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
                    if (i==11){
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
                    i =11;
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
                        i=11;
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

                Log.d("walktest-left:i",""+i);
                Log.d("walktest-left:j",""+j);
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
                    i = i+8;
                }
                else {
                    i = i - 4;
                }
                temp = imageArray[i];
                while (temp.getVisibility() == View.INVISIBLE) {

                    if(i==0 ||i==1||i==2||i==3) {
                        i = i+8;
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
                Log.d("walktest-up:i",""+i);
                Log.d("walktest-up:j",""+j);
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

                if(i==8 ||i==9||i==10 ||i==11) {
                    i = i-8;
                }
                else {
                    i = i + 4;
                }
                temp = imageArray[i];
                while (temp.getVisibility() == View.INVISIBLE) {

                    if(i==8 ||i==9||i==10 ||i==11||i==12) {
                        i = i-8;
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
                    setBtnStyle(view);
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
        if (cardsArray[card]==101) {
            iv.setImageResource(image101);
        }else if(cardsArray[card]==102){
            iv.setImageResource(image102);
        }else if(cardsArray[card]==103){
            iv.setImageResource(image103);
        }else if(cardsArray[card]==104){
            iv.setImageResource(image104);
        }else if(cardsArray[card]==105){
            iv.setImageResource(image105);
        }else if(cardsArray[card]==106){
            iv.setImageResource(image106);
        }else if(cardsArray[card]==107){
            iv.setImageResource(image107);
        }else if(cardsArray[card]==108){
            iv.setImageResource(image108);
        }else if(cardsArray[card]==109){
            iv.setImageResource(image109);
        }else if(cardsArray[card]==110){
            iv.setImageResource(image110);
        }else if(cardsArray[card]==201){
            iv.setImageResource(image201);
        }else if(cardsArray[card]==202){
            iv.setImageResource(image202);
        }else if(cardsArray[card]==203){
            iv.setImageResource(image203);
        }else if(cardsArray[card]==204){
            iv.setImageResource(image204);
        }else if(cardsArray[card]==205){
            iv.setImageResource(image205);
        }else if(cardsArray[card]==206){
            iv.setImageResource(image206);
        }else if(cardsArray[card]==207){
            iv.setImageResource(image207);
        }else if(cardsArray[card]==208){
            iv.setImageResource(image208);
        }else if(cardsArray[card]==209){
            iv.setImageResource(image209);
        }else if(cardsArray[card]==210){
            iv.setImageResource(image210);
        }

        //check which image is selected and save
        if(cardNumber==1){
            firstCard=cardsArray[card];
            if(firstCard>200){
                firstCard=firstCard-100;
            }
            cardNumber=2;
            clickedFirst=card;
            iv.setEnabled(false);
        }else if(cardNumber==2){
            secondCard=cardsArray[card];
            if(secondCard>200){
                secondCard=secondCard-100;
            }
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


    private void calculate(){

        if(firstCard==secondCard){
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
                iv_34.getVisibility() == View.INVISIBLE
             ) {
            //停止計時器的執行緒
            handler.removeCallbacks(updateTimer);
            //頁面跳轉
            Intent intent = new Intent();
            intent.setClass(MemoryGameMed.this, MemoryGamePro.class);
            intent.putExtra("time",startTime);
            intent.putExtra("pause",pauseTotal);
            startActivity(intent);
            overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
            //音樂釋放
            music.release();
            music=null;
            finish();

        }
    }

    private void frontOfCardsResources(){
        image101=R.drawable.memory101;
        image102=R.drawable.memory102;
        image103=R.drawable.memory103;
        image104=R.drawable.memory104;
        image105=R.drawable.memory105;
        image106=R.drawable.memory106;
        image107=R.drawable.memory107;
        image108=R.drawable.memory108;
        image109=R.drawable.memory109;
        image110=R.drawable.memory110;
        image201=R.drawable.memory201;
        image202=R.drawable.memory202;
        image203=R.drawable.memory203;
        image204=R.drawable.memory204;
        image205=R.drawable.memory205;
        image206=R.drawable.memory206;
        image207=R.drawable.memory207;
        image208=R.drawable.memory208;
        image209=R.drawable.memory209;
        image210=R.drawable.memory210;
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
            Long hour = (spentTime/1000)/3600;
            //計算目前已過分鐘數
            Long minius = ((spentTime/1000)/60) % 60;
            //計算目前已過秒數
            Long seconds = (spentTime/1000) % 60;
            String formattedTime = String.format("%02d:%02d:%02d",hour, minius, seconds);
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