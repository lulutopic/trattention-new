package com.github.pwittchen.neurosky.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

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


public class SchulteGridPro extends AppCompatActivity {
    private Long startTime;
    private Chronometer timer;
    private Handler handler = new Handler();
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String createdAt, a;

    //圖片的id設定的變數
    ImageView one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen,
            seventeen,eighteen,nineteen,twenty,twentyone,twentytwo,twentythree,twentyfour,twentyfive;

    int[] ImageArray = {R.drawable.grid1,R.drawable.grid2,R.drawable.grid3,R.drawable.grid4,R.drawable.grid5,R.drawable.grid6,R.drawable.grid7
            ,R.drawable.grid8,R.drawable.grid9,R.drawable.grid10,R.drawable.grid11,R.drawable.grid12,R.drawable.grid13,R.drawable.grid14
            ,R.drawable.grid15,R.drawable.grid16,R.drawable.grid17,R.drawable.grid18,R.drawable.grid19,R.drawable.grid20,R.drawable.grid21,R.drawable.grid22
            ,R.drawable.grid23,R.drawable.grid24,R.drawable.grid25};

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_schulte_grid2);
        //設定隱藏標題
        getSupportActionBar().hide();

        //接續前段時間
        startTime= getIntent().getLongExtra("time",0);

        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);
        //計算當前時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.TAIWAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        createdAt = sdf.format(new Date()); //-prints-> 2015-01-22T03:23:26Z
        Log.d("MainActivity", "Current Timestamp: " + sdf.format(new Date()));

        //        先寫死，後期在統一改 UID
//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("game_record").document(userID);
        //自動產生 document id
        DocumentReference documentReference = fStore.collection("game_record").document("game_record_schulte").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq").document();
        Map<String,Object> gameresult = new HashMap<>();
//        user.put("user", userID);
//        gameresult.put("record", updateTimer);
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

        //暫停按鈕的觸發事件
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridPro.this);
                LayoutInflater inflater = SchulteGridPro.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_stop_button, null));
                alertDialogBuilder
                        .setNeutralButton("離開",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface,int i){
                                Intent intent = new Intent();
                                intent.setClass(SchulteGridPro.this,GameHome.class);
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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridPro.this)
                        .setTitle("小提示頁面")
                        .setMessage("請依照數字順序點選");
                LayoutInflater inflater = SchulteGridPro.this.getLayoutInflater();
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

        ImageView[] NumArray = {one,two,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen,
                seventeen,eighteen,nineteen,twenty,twentyone,twentytwo,twentythree,twentyfour,twentyfive};

        //NumArray隨機排序
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
        seventeen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(seventeen,theCard);
            }
        });
        eighteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(eighteen,theCard);
            }
        });
        nineteen.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(nineteen,theCard);
            }
        });
        twenty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twenty,theCard);
            }
        });
        twentyone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twentyone,theCard);
            }
        });
        twentytwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twentytwo,theCard);
            }
        });
        twentythree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twentythree,theCard);
            }
        });
        twentyfour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twentyfour,theCard);
            }
        });
        twentyfive.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int theCard = Integer.parseInt((String)view.getTag());
                doStuff(twentyfive,theCard);
            }
        });

    }

    private void doStuff(ImageView iv, int card){
        if(count == card && count != 24){
            iv.setVisibility(View.INVISIBLE);
            count = count + 2;
        }
        else if(count == card && count == 24){
            iv.setVisibility(View.INVISIBLE);
            count = 1;
        }
        else{
            iv.setVisibility(View.VISIBLE);
        }
        checkEnd();
    }

    private void checkEnd() {
        if (count == 25) {
            //設定計時器的執行緒結束
            handler.removeCallbacks(updateTimer);
            //頁面跳轉
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridPro.this);
            alertDialogBuilder
                    .setMessage("恭喜!遊戲結束~")
                    .setCancelable(false)
                    .setPositiveButton("查看結果",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            Intent intent = new Intent();
                            intent.setClass(SchulteGridPro.this,GameResult.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("離開",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            Intent intent = new Intent();
                            intent.setClass(SchulteGridPro.this,GameHome.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    //固定要執行的方法
    public Runnable updateTimer = new Runnable() {
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
}