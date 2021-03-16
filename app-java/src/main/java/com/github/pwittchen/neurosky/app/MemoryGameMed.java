package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;

import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;

import java.util.Arrays;
import java.util.Collections;


public class MemoryGameMed extends AppCompatActivity {
    protected Long startTime;
    private Chronometer timer;
    private Handler handler = new Handler();

    ImageView iv_11,iv_12,iv_13,iv_14,iv_15,
            iv_21,iv_22,iv_23,iv_24,iv_25,
            iv_31,iv_32,iv_33,iv_34,iv_35,
            iv_41,iv_42,iv_43,iv_44,iv_45;

    //array for the images
    Integer[] cardsArray = {101,102,103,104,105,106,107,108,109,110,201,202,203,204,205,206,207,208,209,210};

    //actual images
    int image101,image102,image103,image104,image105,image106,image107,image108,image109,image110,
            image201,image202,image203,image204,image205,image206,image207,image208,image209,image210;
    int firstCard,secondCard;
    int clickedFirst,clickedSecond;
    int cardNumber=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game_med);

        //設定隱藏標題
        getSupportActionBar().hide();
        timer = (Chronometer) findViewById(R.id.timer);
        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);


        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);


        //game
        iv_11=(ImageView)findViewById(R.id.iv_11);
        iv_12=(ImageView)findViewById(R.id.iv_12);
        iv_13=(ImageView)findViewById(R.id.iv_13);
        iv_14=(ImageView)findViewById(R.id.iv_14);
        iv_15=(ImageView)findViewById(R.id.iv_15);
        iv_21=(ImageView)findViewById(R.id.iv_21);
        iv_22=(ImageView)findViewById(R.id.iv_22);
        iv_23=(ImageView)findViewById(R.id.iv_23);
        iv_24=(ImageView)findViewById(R.id.iv_24);
        iv_25=(ImageView)findViewById(R.id.iv_25);
        iv_31=(ImageView)findViewById(R.id.iv_31);
        iv_32=(ImageView)findViewById(R.id.iv_32);
        iv_33=(ImageView)findViewById(R.id.iv_33);
        iv_34=(ImageView)findViewById(R.id.iv_34);
        iv_35=(ImageView)findViewById(R.id.iv_35);
        iv_41=(ImageView)findViewById(R.id.iv_41);
        iv_42=(ImageView)findViewById(R.id.iv_42);
        iv_43=(ImageView)findViewById(R.id.iv_43);
        iv_44=(ImageView)findViewById(R.id.iv_44);
        iv_45=(ImageView)findViewById(R.id.iv_45);

        iv_11.setTag("0");
        iv_12.setTag("1");
        iv_13.setTag("2");
        iv_14.setTag("3");
        iv_15.setTag("4");
        iv_21.setTag("5");
        iv_22.setTag("6");
        iv_23.setTag("7");
        iv_24.setTag("8");
        iv_25.setTag("9");
        iv_31.setTag("10");
        iv_32.setTag("11");
        iv_33.setTag("12");
        iv_34.setTag("13");
        iv_35.setTag("14");
        iv_41.setTag("15");
        iv_42.setTag("16");
        iv_43.setTag("17");
        iv_44.setTag("18");
        iv_45.setTag("19");


        //load the card images
        frontOfCardsResources();
        Collections.shuffle(Arrays.asList(cardsArray));

        //Listener 等待使用者點擊此事件
        //override 覆蓋掉原本android studio 上層物件
        iv_11.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_11,theCard);
            }
        });
        iv_12.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_12,theCard);
            }
        });
        iv_13.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_13,theCard);
            }
        });
        iv_14.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_14,theCard);
            }
        });
        iv_15.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_15,theCard);
            }
        });
        iv_21.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_21,theCard);
            }
        });
        iv_22.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_22,theCard);
            }
        });
        iv_23.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_23,theCard);
            }
        });
        iv_24.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_24,theCard);
            }
        });
        iv_25.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_25,theCard);
            }
        });
        iv_31.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_31,theCard);
            }
        });
        iv_32.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_32,theCard);
            }
        });
        iv_33.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_33,theCard);
            }
        });
        iv_34.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_34,theCard);
            }
        });
        iv_35.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_35,theCard);
            }
        });
        iv_41.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_41,theCard);
            }
        });
        iv_42.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_42,theCard);
            }
        });
        iv_43.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_43,theCard);
            }
        });
        iv_44.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_44,theCard);
            }
        });
        iv_45.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(iv_45,theCard);
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
            iv_15.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);
            iv_25.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);
            iv_35.setEnabled(false);
            iv_41.setEnabled(false);
            iv_42.setEnabled(false);
            iv_43.setEnabled(false);
            iv_44.setEnabled(false);
            iv_45.setEnabled(false);

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
                iv_15.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==5){
                iv_21.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==6){
                iv_22.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==7){
                iv_23.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==8){
                iv_24.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==9){
                iv_25.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==10){
                iv_31.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==11){
                iv_32.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==12){
                iv_33.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==13){
                iv_34.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==14){
                iv_35.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==15){
                iv_41.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==16){
                iv_42.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==17){
                iv_43.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==18){
                iv_44.setVisibility(View.INVISIBLE);
            }else if(clickedFirst==19){
                iv_45.setVisibility(View.INVISIBLE);

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
                iv_15.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==5){
                iv_21.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==6){
                iv_22.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==7){
                iv_23.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==8){
                iv_24.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==9){
                iv_25.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==10){
                iv_31.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==11){
                iv_32.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==12){
                iv_33.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==13){
                iv_34.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==14){
                iv_35.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==15){
                iv_41.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==16){
                iv_42.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==17){
                iv_43.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==18){
                iv_44.setVisibility(View.INVISIBLE);
            }else if(clickedSecond==19){
                iv_45.setVisibility(View.INVISIBLE);
            }
        }else{
            iv_11.setImageResource(R.drawable.memoryback);
            iv_12.setImageResource(R.drawable.memoryback);
            iv_13.setImageResource(R.drawable.memoryback);
            iv_14.setImageResource(R.drawable.memoryback);
            iv_15.setImageResource(R.drawable.memoryback);
            iv_21.setImageResource(R.drawable.memoryback);
            iv_22.setImageResource(R.drawable.memoryback);
            iv_23.setImageResource(R.drawable.memoryback);
            iv_24.setImageResource(R.drawable.memoryback);
            iv_25.setImageResource(R.drawable.memoryback);
            iv_31.setImageResource(R.drawable.memoryback);
            iv_32.setImageResource(R.drawable.memoryback);
            iv_33.setImageResource(R.drawable.memoryback);
            iv_34.setImageResource(R.drawable.memoryback);
            iv_35.setImageResource(R.drawable.memoryback);
            iv_41.setImageResource(R.drawable.memoryback);
            iv_42.setImageResource(R.drawable.memoryback);
            iv_43.setImageResource(R.drawable.memoryback);
            iv_44.setImageResource(R.drawable.memoryback);
            iv_45.setImageResource(R.drawable.memoryback);

        }
        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);
        iv_15.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);
        iv_25.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        iv_34.setEnabled(true);
        iv_35.setEnabled(true);
        iv_41.setEnabled(true);
        iv_42.setEnabled(true);
        iv_43.setEnabled(true);
        iv_44.setEnabled(true);
        iv_45.setEnabled(true);
        //檢查遊戲結束
        checkEnd();
    }
    private void checkEnd() {
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_15.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_25.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE &&
                iv_35.getVisibility() == View.INVISIBLE &&
                iv_41.getVisibility() == View.INVISIBLE &&
                iv_42.getVisibility() == View.INVISIBLE &&
                iv_43.getVisibility() == View.INVISIBLE &&
                iv_44.getVisibility() == View.INVISIBLE &&
                iv_45.getVisibility() == View.INVISIBLE ) {
            //頁面跳轉
            Intent intent = new Intent();
            intent.setClass(MemoryGameMed.this, MemoryGamePro.class);
            intent.putExtra("time",startTime);
            startActivity(intent);
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


    //固定要執行的方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (Chronometer) findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
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