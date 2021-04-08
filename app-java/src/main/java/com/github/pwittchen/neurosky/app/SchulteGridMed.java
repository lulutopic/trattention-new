package com.github.pwittchen.neurosky.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;


public class SchulteGridMed extends AppCompatActivity {
    private Long startTime;
    private Long spentTime;
    private Long pauseTime=0L;
    private Long pauseTotal;
    private Chronometer timer;
    private Handler handler = new Handler();
    private int focus_count;
    private int focus_row=1;
    private int focus_column=1;



    //圖片的id設定的變數
    ImageView one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen,
            seventeen,eighteen,nineteen,twenty,twentyone,twentytwo,twentythree,twentyfour,twentyfive;
    ImageView btn_down,btn_right,btn_ok;
    View row1,row2,row3,row4,row5;

    int blue= Color.parseColor("#274C98");
    int focus_color=getColorWithAlpha(blue, 0.6f);
    int unfocus_color= getColorWithAlpha(blue, 0f);

    int[] ImageArray = {R.drawable.grid1,R.drawable.grid2,R.drawable.grid3,R.drawable.grid4,R.drawable.grid5,R.drawable.grid6,R.drawable.grid7
            ,R.drawable.grid8,R.drawable.grid9,R.drawable.grid10,R.drawable.grid11,R.drawable.grid12,R.drawable.grid13,R.drawable.grid14
            ,R.drawable.grid15,R.drawable.grid16,R.drawable.grid17,R.drawable.grid18,R.drawable.grid19,R.drawable.grid20,R.drawable.grid21,R.drawable.grid22
            ,R.drawable.grid23,R.drawable.grid24,R.drawable.grid25};

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schulte_grid1);
        //設定隱藏標題
        getSupportActionBar().hide();
        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);
        //接續前段時間
        pauseTotal= getIntent().getLongExtra("pause",0);
        Log.d("pauseTotal",pauseTotal+"");
        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);

        //暫停按鈕的觸發事件
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTime=System.currentTimeMillis();
                handler.removeCallbacks(updateTimer);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridMed.this);
                LayoutInflater inflater = SchulteGridMed.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_stop_button, null));
                alertDialogBuilder
                        .setNeutralButton("離開",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                Intent intent = new Intent();
                                intent.setClass(SchulteGridMed.this,GameHome.class);
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
                alertDialog.getWindow().setLayout(455, 400);

            }
        });

        //小提示按鈕的觸發事件
        ImageView button5 = findViewById(R.id.imagetips);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridMed.this)
                        .setTitle("小提示頁面")
                        .setMessage("請依照數字順序點選");
                LayoutInflater inflater = SchulteGridMed.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_memory_tips, null));

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


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
        seventeen=(ImageView)findViewById(R.id.seventeen);
        eighteen=(ImageView)findViewById(R.id.eighteen);
        nineteen=(ImageView)findViewById(R.id.nineteen);
        twenty=(ImageView)findViewById(R.id.twenty);
        twentyone=(ImageView)findViewById(R.id.twentyone);
        twentytwo=(ImageView)findViewById(R.id.twentytwo);
        twentythree=(ImageView)findViewById(R.id.twentythree);
        twentyfour=(ImageView)findViewById(R.id.twentyfour);
        twentyfive=(ImageView)findViewById(R.id.twentyfive);

        btn_down=(ImageView)findViewById(R.id.down_arrow);
        btn_right=(ImageView)findViewById(R.id.right_arrow);
        btn_ok=(ImageView)findViewById(R.id.ok);

        row1 =(View)findViewById(R.id.row1);
        row2 =(View)findViewById(R.id.row2);
        row3 =(View)findViewById(R.id.row3);
        row4 =(View)findViewById(R.id.row4);
        row5 =(View)findViewById(R.id.row5);



        ImageView[] NumArray = {one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen,
                seventeen,eighteen,nineteen,twenty,twentyone,twentytwo,twentythree,twentyfour,twentyfive};
        ImageView[] UnShuffle = {one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,
                fourteen,fifteen,sixteen,seventeen,eighteen,nineteen,twenty,twentyone,twentytwo,twentythree,twentyfour,twentyfive};

        //NumArray隨機排序
        Collections.shuffle(Arrays.asList(NumArray));

        for(int i = 0; i < ImageArray.length; i++){
            NumArray[i].setImageResource(ImageArray[i]);
            String s = String.valueOf(i);
            NumArray[i].setTag(s);
        }

        //初始設定：選取第一行、第一列
        row1.setBackgroundColor(focus_color);
        UnShuffle[0].setBackgroundColor(focus_color);
        UnShuffle[5].setBackgroundColor(focus_color);
        UnShuffle[10].setBackgroundColor(focus_color);
        UnShuffle[15].setBackgroundColor(focus_color);
        UnShuffle[20].setBackgroundColor(focus_color);

        //向下的按鈕
        btn_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clearRow(focus_row);
                focus_row+=1;
                switch(focus_row){
                    case(2):
                        row2.setBackgroundColor(focus_color);
                        break;
                    case(3):
                        row3.setBackgroundColor(focus_color);
                        break;
                    case(4):
                        row4.setBackgroundColor(focus_color);
                        break;
                    case(5):
                        row5.setBackgroundColor(focus_color);
                        break;
                    case(6):
                        focus_row=1;
                        row1.setBackgroundColor(focus_color);
                        break;
                }
            }
        });
        //向右的按鈕
        btn_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                clearColumn(focus_column);
                focus_column+=1;
                switch(focus_column){
                    case(2):
                        UnShuffle[1].setBackgroundColor(focus_color);
                        UnShuffle[6].setBackgroundColor(focus_color);
                        UnShuffle[11].setBackgroundColor(focus_color);
                        UnShuffle[16].setBackgroundColor(focus_color);
                        UnShuffle[21].setBackgroundColor(focus_color);
                        break;
                    case(3):
                        UnShuffle[2].setBackgroundColor(focus_color);
                        UnShuffle[7].setBackgroundColor(focus_color);
                        UnShuffle[12].setBackgroundColor(focus_color);
                        UnShuffle[17].setBackgroundColor(focus_color);
                        UnShuffle[22].setBackgroundColor(focus_color);
                        break;
                    case(4):
                        UnShuffle[3].setBackgroundColor(focus_color);
                        UnShuffle[8].setBackgroundColor(focus_color);
                        UnShuffle[13].setBackgroundColor(focus_color);
                        UnShuffle[18].setBackgroundColor(focus_color);
                        UnShuffle[23].setBackgroundColor(focus_color);
                        break;
                    case(5):
                        UnShuffle[4].setBackgroundColor(focus_color);
                        UnShuffle[9].setBackgroundColor(focus_color);
                        UnShuffle[14].setBackgroundColor(focus_color);
                        UnShuffle[19].setBackgroundColor(focus_color);
                        UnShuffle[24].setBackgroundColor(focus_color);
                        break;
                    case(6):
                        focus_column=1;
                        UnShuffle[0].setBackgroundColor(focus_color);
                        UnShuffle[5].setBackgroundColor(focus_color);
                        UnShuffle[10].setBackgroundColor(focus_color);
                        UnShuffle[15].setBackgroundColor(focus_color);
                        UnShuffle[20].setBackgroundColor(focus_color);
                        break;
                }

            }
        });

        //確認的按鈕
        btn_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                focus_count=(focus_row-1)*5+focus_column-1;
                int theCard = Integer.parseInt((String)UnShuffle[focus_count].getTag());
                doStuff(UnShuffle[focus_count],theCard);
            }
        });

    }

    private void clearRow(int focus_row){
        switch(focus_row){
            case(1):
                row1.setBackgroundColor(unfocus_color);
                break;
            case(2):
                row2.setBackgroundColor(unfocus_color);
                break;
            case(3):
                row3.setBackgroundColor(unfocus_color);
                break;
            case(4):
                row4.setBackgroundColor(unfocus_color);
                break;
            case(5):
                row5.setBackgroundColor(unfocus_color);
                break;
        }
    }

    private void clearColumn(int focus_column){
        ImageView[] UnShuffle = {one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen, fourteen,fifteen,sixteen,
                seventeen,eighteen,nineteen,twenty,twentyone,twentytwo,twentythree,twentyfour,twentyfive};
        switch(focus_column){
            case(1):
                UnShuffle[0].setBackgroundColor(unfocus_color);
                UnShuffle[5].setBackgroundColor(unfocus_color);
                UnShuffle[10].setBackgroundColor(unfocus_color);
                UnShuffle[15].setBackgroundColor(unfocus_color);
                UnShuffle[20].setBackgroundColor(unfocus_color);
                break;
            case(2):
                UnShuffle[1].setBackgroundColor(unfocus_color);
                UnShuffle[6].setBackgroundColor(unfocus_color);
                UnShuffle[11].setBackgroundColor(unfocus_color);
                UnShuffle[16].setBackgroundColor(unfocus_color);
                UnShuffle[21].setBackgroundColor(unfocus_color);
                break;
            case(3):
                UnShuffle[2].setBackgroundColor(unfocus_color);
                UnShuffle[7].setBackgroundColor(unfocus_color);
                UnShuffle[12].setBackgroundColor(unfocus_color);
                UnShuffle[17].setBackgroundColor(unfocus_color);
                UnShuffle[22].setBackgroundColor(unfocus_color);
                break;
            case(4):
                UnShuffle[3].setBackgroundColor(unfocus_color);
                UnShuffle[8].setBackgroundColor(unfocus_color);
                UnShuffle[13].setBackgroundColor(unfocus_color);
                UnShuffle[18].setBackgroundColor(unfocus_color);
                UnShuffle[23].setBackgroundColor(unfocus_color);
                break;
            case(5):
                UnShuffle[4].setBackgroundColor(unfocus_color);
                UnShuffle[9].setBackgroundColor(unfocus_color);
                UnShuffle[14].setBackgroundColor(unfocus_color);
                UnShuffle[19].setBackgroundColor(unfocus_color);
                UnShuffle[24].setBackgroundColor(unfocus_color);
                break;
        }
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
            iv.setImageResource(R.drawable.gridblank);
            count++;
        }

        checkEnd();
    }

    private void checkEnd() {
        if (count == 25) {
            //設定計時器的執行緒結束
            handler.removeCallbacks(updateTimer);
            //頁面跳轉
            Intent intent = new Intent();
            intent.setClass(SchulteGridMed.this, SchulteGridPro.class);
            intent.putExtra("time",startTime);
            intent.putExtra("pause",pauseTotal);
            startActivity(intent);
            finish();

        }
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
        timer.setFormat("0"+ String.valueOf(hour)+":%s");
    }

}