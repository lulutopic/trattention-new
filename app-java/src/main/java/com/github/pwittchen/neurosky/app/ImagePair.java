package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageButton;
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
import java.util.Random;
import java.util.TimeZone;

public class ImagePair extends AppCompatActivity {

    private Long startTime; //初始時間
    private Chronometer timer; //已經過時間
    private Handler handler = new Handler(); //計時器的執行緒宣告
    private String formattedTime;
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String createdAt, a;


    private ArrayList<String> colorNames = new ArrayList<>(); //文字意思的顏色
    private ArrayList<Integer> colorValues = new ArrayList<>(); //文字真正的底色
    private ArrayList<TextView> button = new ArrayList<>(); // ABC選項

    private TextView colorTextView; // 題目的文字

    private int red;
    private int blue;
    private int green;


    private TextView ImageButtonA;
    private TextView ImageButtonB;
    private TextView ImageButtonC;

    int count = 0; //計算遊戲答對題數

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_image_pair);
        //設定隱藏標題
        getSupportActionBar().hide();

        //取得目前時間
        startTime = System.currentTimeMillis();
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
    }

    //監聽事件的函式
    private void setupViewsAndListeners(){

        ImageView right_arrow = findViewById(R.id.right_arrow);
        ImageView ok = findViewById(R.id.ok);


        //選項Ａ的監聽事件
        ImageButtonA.setOnClickListener(new View.OnClickListener(){
            @Override
            //設定點擊事件
            public void onClick(View v){
                //回傳題目的文字底色的文字標籤
                String TagA = (String) colorTextView.getTag();
                //如果選項Ａ的文字意思等於標籤Ａ
                if(ImageButtonA.getText() == TagA){
                    count++;
                    getRandomColor();
                    deter();
                    checkEnd();
                }
            }
        });

        ImageButtonB.setOnClickListener(new View.OnClickListener(){
            //選項Ｂ的監聽事件
            @Override
            //設定點擊事件
            public void onClick(View v){
                //如果選項Ｂ的文字意思等於標籤Ｂ
                String TagB = (String) colorTextView.getTag();
                if(ImageButtonB.getText() == TagB){
                    count++;
                    getRandomColor();
                    deter();
                    checkEnd();
                }
            }
        });

        ImageButtonC.setOnClickListener(new View.OnClickListener(){
            //選項Ｃ的監聽事件
            @Override
            //設定點擊事件
            public void onClick(View v){
                //如果選項Ｂ的文字意思等於標籤Ｃ
                String TagC = (String) colorTextView.getTag();
                if(ImageButtonC.getText() == TagC){
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
        if(count == 1){
            //停止計時器的執行緒
            handler.removeCallbacks(updateTimer);
            Log.d("MainActivity", "Current Timestamp: " + formattedTime);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImagePair.this);
            alertDialogBuilder
                    .setMessage("恭喜!遊戲結束~")
                    .setCancelable(false)
                    .setPositiveButton("查看結果",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(ImagePair.this, GameResult.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("離開",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(ImagePair.this, GameHome.class);
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
            gameresult.put("record", formattedTime);
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

        //Add color values to the arraylist [-571050, -5973084, -9328385]
        colorValues.add(ContextCompat.getColor(this,red));
        colorValues.add(ContextCompat.getColor(this,green));
        colorValues.add(ContextCompat.getColor(this,blue));

        //把ＡＢＣ選項加入到button ArrayLists
        button.add(ImageButtonA);
        button.add(ImageButtonB);
        button.add(ImageButtonC);
    }





    //計時器的計時方法
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
            formattedTime = String.format("%02d:%02d:%02d",hour, minius, seconds);
            time.setText(formattedTime);
            handler.postDelayed(this, 1000);
        }
    };
}
