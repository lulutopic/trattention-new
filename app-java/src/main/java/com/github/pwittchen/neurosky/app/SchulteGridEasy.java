package com.github.pwittchen.neurosky.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;


public class SchulteGridEasy extends AppCompatActivity {
    private Long startTime; //初始時間
    private Chronometer timer; //已經過時間
    private Handler handler = new Handler();//執行緒

    private int focus_count;


    //圖片的變數
    ImageView one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen;
    ImageView btn_down,btn_right,btn_ok;
    View row1,row2,row3,row4;

    int focus_color= Color.parseColor("#274C98");
    int single_focus_color=Color.parseColor("#ade8f4");
    int unfocus_color= Color.parseColor("#FFFFFF");

    //圖片的檔案引入陣列
    int[] ImageArray = {R.drawable.grid1,R.drawable.grid2,R.drawable.grid3,R.drawable.grid4,R.drawable.grid5,R.drawable.grid6,R.drawable.grid7
    ,R.drawable.grid8,R.drawable.grid9,R.drawable.grid10,R.drawable.grid11,R.drawable.grid12,R.drawable.grid13,R.drawable.grid14
    ,R.drawable.grid15,R.drawable.grid16};

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schulte_grid);
        //設定隱藏標題
        getSupportActionBar().hide();

        //取得目前時間
        startTime = System.currentTimeMillis();

        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);

        //暫停按鈕的觸發事件
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridEasy.this);
                LayoutInflater inflater = SchulteGridEasy.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_stop_button, null));
                alertDialogBuilder
                        .setNeutralButton("離開",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                Intent intent = new Intent();
                                intent.setClass(SchulteGridEasy.this,GameHome.class);
                                startActivity(intent);
                                finish();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //小提示按鈕的觸發事件
        ImageView button5 = findViewById(R.id.imagetips);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridEasy.this)
                        .setTitle("小提示頁面")
                        .setMessage("請依照數字順序點選");
                LayoutInflater inflater = SchulteGridEasy.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_memory_tips, null));

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        //get ID
        one=(ImageView)findViewById(R.id.one);
        two=(ImageView)findViewById(R.id.two);
        three=(ImageView)findViewById(R.id.three);
        four=(ImageView)findViewById(R.id.four);
        five=(ImageView)findViewById(R.id.five);
        six=(ImageView)findViewById(R.id.six);
        seven=(ImageView)findViewById(R.id.seven);
        eight=(ImageView)findViewById(R.id.eight);
        nine=(ImageView)findViewById(R.id.nine);
        ten=(ImageView)findViewById(R.id.ten);
        eleven=(ImageView)findViewById(R.id.eleven);
        twelve=(ImageView)findViewById(R.id.twelve);
        thirteen=(ImageView)findViewById(R.id.thirteen);
        fourteen=(ImageView)findViewById(R.id.fourteen);
        fifteen=(ImageView)findViewById(R.id.fifteen);
        sixteen=(ImageView)findViewById(R.id.sixteen);

        btn_down=(ImageView)findViewById(R.id.down_arrow);
        btn_right=(ImageView)findViewById(R.id.up_arrow);
        btn_ok=(ImageView)findViewById(R.id.ok);

        row1 =(View)findViewById(R.id.row1);
        row2 =(View)findViewById(R.id.row2);
        row3 =(View)findViewById(R.id.row3);
        row4 =(View)findViewById(R.id.row4);

        row1.setBackgroundColor(focus_color);

        ImageView[] NumArray = {one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen};

        btn_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                focus_count+=1;
                if (focus_count==2){
                    row1.setBackgroundColor(unfocus_color);
                    row2.setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                    NumArray[0].setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                }
                else if(focus_count==3){
                    row2.setBackgroundColor(unfocus_color);
                    row3.setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                    NumArray[4].setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                }
                else if(focus_count==4){
                    row3.setBackgroundColor(unfocus_color);
                    row4.setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                    NumArray[7].setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                }
                else if(focus_count==5){
                    focus_count=1;
                    row4.setBackgroundColor(unfocus_color);
                    row1.setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                    NumArray[12].setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                }
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NumArray[focus_count].setBackgroundColor(getColorWithAlpha(focus_color, 0.4f));
                focus_count+=1;
                if(focus_count==3||focus_count==7||focus_count==11||focus_count==15){
                    focus_count-=3;
                }
            }
        });





        Collections.shuffle(Arrays.asList(NumArray));

        for(int i = 0; i < ImageArray.length; i++){
            NumArray[i].setImageResource(ImageArray[i]);
            String s = String.valueOf(i);
            NumArray[i].setTag(s);
        }



        //Listener 等待使用者點擊此事件
        //override 覆蓋掉原本android studio 上層物件


        one.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(one,theCard);

            }
        });
        two.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(two,theCard);
            }
        });
        three.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(three,theCard);
            }
        });
        four.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(four,theCard);
            }
        });
        five.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(five,theCard);
            }
        });
        six.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(six,theCard);
            }
        });
        seven.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(seven,theCard);
            }
        });
        eight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(eight,theCard);
            }
        });
        nine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(nine,theCard);
            }
        });
        ten.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(ten,theCard);
            }
        });
        eleven.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(eleven,theCard);
            }
        });
        twelve.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twelve,theCard);
            }
        });
        thirteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(thirteen,theCard);
            }
        });
        fourteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(fourteen,theCard);
            }
        });
        fifteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(fifteen,theCard);
            }
        });
        sixteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(sixteen,theCard);
            }
        });


    }

    private int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    private void doStuff(ImageView iv, int card){
        if(count == card){
            iv.setVisibility(View.INVISIBLE);
            count++;
        }
        else{
            iv.setVisibility(View.VISIBLE);
        }
        checkEnd();
    }

    private void checkEnd() {
        if (count == 16) {
            //設定計時器的執行緒結束
            handler.removeCallbacks(updateTimer);
            //頁面跳轉
            Intent intent = new Intent();
            intent.setClass(SchulteGridEasy.this, SchulteGridMed.class);
            intent.putExtra("time",startTime);
            startActivity(intent);
            finish();

        }
    }



    //固定要執行的方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (Chronometer) findViewById(R.id.timer);
            Long spentTime = System.currentTimeMillis() - startTime;
            //計算目前已過小時數
            Long hour = (spentTime/1000)/3600;
            //計算目前已過分鐘數
            Long minius = (spentTime/1000)/60;
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
        timer.setFormat("0"+ String.valueOf(hour)+":%s");
    }

}


