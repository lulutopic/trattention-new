package com.github.pwittchen.neurosky.app;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class TrainRecord extends AppCompatActivity {
    LineChart lineChart;
    BarChart barChart;
    int Blue=Color.parseColor("#5B7F9F"),BlueDark = Color.parseColor("#274C98"), Yellow=Color.parseColor("#F4E1A5"),Orange = Color.parseColor("#F4D3A5"), Yellow_light=Color.parseColor("#ffe445"), White =Color.parseColor("#ffffff")
            ,Red = Color.parseColor("#FCBABA"),Green = Color.parseColor("#A0D7D9");
    TextView text_pair, text_schulte, text_memory;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    float count, ownImagePairAverage, ownSchulteAverage, ownMemoryAverage, countAll;
    private int list_size;
    public static final String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();
        //轉場動畫
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide= TransitionInflater.from(this).inflateTransition(R.transition.slide);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        //退出
        getWindow().setExitTransition(explode);
        //第一次進入
        getWindow().setEnterTransition(fade);
        //再次進入
        getWindow().setReenterTransition(slide);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_train_record);
        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TrainRecord.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TrainRecord.this).toBundle());
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TrainRecord.this , SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TrainRecord.this).toBundle());
            }
        });

        text_pair=(TextView)findViewById(R.id.text_pair);
        text_schulte=(TextView)findViewById(R.id.text_schulte);
        text_memory=(TextView)findViewById(R.id.text_memory);
        
        //初始圖表設定(圖型配對)
        text_pair.setTextColor(BlueDark);
        text_schulte.setTextColor(Yellow_light);
        text_memory.setTextColor(Yellow_light);
        count = 0;
        //折線圖
        lineChart =(LineChart)findViewById(R.id.chart_line);
        ArrayList<Entry> values1=new ArrayList<>();
        ArrayList imagePairRecordList = new ArrayList<>();
        ArrayList maxList = new ArrayList<>();
        ArrayList maxALLUserList = new ArrayList<>();
        ArrayList<BarEntry> bar_others=new ArrayList<>();
        ArrayList<BarEntry> bar_own=new ArrayList<>();

        //firebase 資料 折線圖
        fStore.collection("game_record").document("game_record_imagepair").collection("data")
                .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                .orderBy("createdAt")
                .limitToLast(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("secondRecord")!=null){
                                    imagePairRecordList.add(document.get("secondRecord"));
                                }
                            }
                            list_size = imagePairRecordList.size();
                            for(int i = 1; i <= imagePairRecordList.size() ; i++){
                                float f1 =Float.parseFloat(valueOf(imagePairRecordList.get(i-1)));
                                values1.add(new Entry(i,f1));
                            }
                            //顯示
                            text_all_line(values1);
                            initX_line();
                            initY_line();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //firebase 資料
        fStore.collection("game_record").document("game_record_imagepair").collection("data")
                .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                .orderBy("secondRecord", Query.Direction.DESCENDING)
                .limitToLast(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("secondRecord")!=null){
                                    maxList.add(document.get("secondRecord"));
                                }
                            }
                            //柱狀圖
                            barChart =(BarChart) findViewById(R.id.chart_bar);
                            count = Integer.parseInt(maxList.get(0).toString());
                            bar_others.add(new BarEntry(1,count));

                            //firebase 資料
                            fStore.collection("game_record").document("game_record_imagepair").collection("data")
                                    .orderBy("secondRecord", Query.Direction.DESCENDING)
                                    .limitToLast(1)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if(document.get("secondRecord")!=null){
                                                        maxALLUserList.add(document.get("secondRecord"));
                                                    }
                                                }
                                                //柱狀圖
                                                countAll = Integer.parseInt(maxALLUserList.get(0).toString());
                                                bar_own.add(new BarEntry(2,countAll));
                                                text_all_bar(bar_others,bar_own);
                                                initX_bar();
                                                initY_bar();
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        //圖型配對圖表

        text_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(BlueDark);
                text_schulte.setTextColor(Yellow_light);
                text_memory.setTextColor(Yellow_light);
                count = 0;
                //折線圖
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                ArrayList imagePairRecordList = new ArrayList<>();
                ArrayList maxList = new ArrayList<>();
                ArrayList maxALLUserList = new ArrayList<>();
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                ArrayList<BarEntry> bar_own=new ArrayList<>();

                //firebase 資料 折線圖
                fStore.collection("game_record").document("game_record_imagepair").collection("data")
                        .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                        .orderBy("createdAt")
                        .limitToLast(10)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("secondRecord")!=null){
                                            imagePairRecordList.add(document.get("secondRecord"));
                                        }
                                    }
                                    list_size = imagePairRecordList.size();
                                    for(int i = 1; i <= imagePairRecordList.size() ; i++){
                                        float f1 =Float.parseFloat(valueOf(imagePairRecordList.get(i-1)));
                                        values1.add(new Entry(i,f1));
                                    }
                                    //顯示
                                    text_all_line(values1);
                                    initX_line();
                                    initY_line();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //firebase 資料
                fStore.collection("game_record").document("game_record_imagepair").collection("data")
                        .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                        .orderBy("secondRecord", Query.Direction.DESCENDING)
                        .limitToLast(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("secondRecord")!=null){
                                            maxList.add(document.get("secondRecord"));
                                        }
                                    }
                                    //柱狀圖
                                    barChart =(BarChart) findViewById(R.id.chart_bar);
                                    count = Integer.parseInt(maxList.get(0).toString());
                                    bar_others.add(new BarEntry(1,count));

                                    //firebase 資料
                                    fStore.collection("game_record").document("game_record_imagepair").collection("data")
                                            .orderBy("secondRecord", Query.Direction.DESCENDING)
                                            .limitToLast(1)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(document.get("secondRecord")!=null){
                                                                maxALLUserList.add(document.get("secondRecord"));
                                                            }
                                                        }
                                                        //柱狀圖
                                                        countAll = Integer.parseInt(maxALLUserList.get(0).toString());
                                                        bar_own.add(new BarEntry(2,countAll));
                                                        text_all_bar(bar_others,bar_own);
                                                        initX_bar();
                                                        initY_bar();
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        //舒爾特方格圖表
        text_schulte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Yellow_light);
                text_schulte.setTextColor(BlueDark);
                text_memory.setTextColor(Yellow_light);
                count = 0;
                countAll = 0;
                //折線圖
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                ArrayList schulteRecordList = new ArrayList<>();
                ArrayList maxList = new ArrayList<>();
                ArrayList maxALLUserList = new ArrayList<>();
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                ArrayList<BarEntry> bar_own=new ArrayList<>();

                //firebase 資料 折線圖
                fStore.collection("game_record").document("game_record_schulte").collection("data")
                        .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                        .orderBy("createdAt")
                        .limitToLast(10)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("secondRecord")!=null){
                                            schulteRecordList.add(document.get("secondRecord"));
                                        }
                                    }
                                    list_size = schulteRecordList.size();
                                    for(int i = 1; i <= schulteRecordList.size() ; i++){
                                        float f1 =Float.parseFloat(valueOf(schulteRecordList.get(i-1)));
                                        values1.add(new Entry(i,f1));
                                    }
                                    //顯示
                                    text_all_line(values1);
                                    initX_line();
                                    initY_line();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //firebase 資料
                fStore.collection("game_record").document("game_record_schulte").collection("data")
                        .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                        .orderBy("secondRecord", Query.Direction.DESCENDING)
                        .limitToLast(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("secondRecord")!=null){
                                            maxList.add(document.get("secondRecord"));
                                        }
                                    }
                                    //柱狀圖
                                    barChart =(BarChart) findViewById(R.id.chart_bar);
                                    count = Integer.parseInt(maxList.get(0).toString());
                                    bar_others.add(new BarEntry(1,count));

                                    //firebase 資料
                                    fStore.collection("game_record").document("game_record_schulte").collection("data")
                                            .orderBy("secondRecord", Query.Direction.DESCENDING)
                                            .limitToLast(1)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(document.get("secondRecord")!=null){
                                                                maxALLUserList.add(document.get("secondRecord"));
                                                            }
                                                        }
                                                        //柱狀圖
                                                        countAll = Integer.parseInt(maxALLUserList.get(0).toString());
                                                        bar_own.add(new BarEntry(2,countAll));
                                                        text_all_bar(bar_others,bar_own);
                                                        initX_bar();
                                                        initY_bar();
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        //記憶力遊戲圖表
        text_memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Yellow_light);
                text_schulte.setTextColor(Yellow_light);
                text_memory.setTextColor(BlueDark);
                count = 0;
                //折線圖
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                ArrayList memoryRecordList = new ArrayList<>();
                ArrayList maxList = new ArrayList<>();
                ArrayList maxALLUserList = new ArrayList<>();
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                ArrayList<BarEntry> bar_own=new ArrayList<>();

                //firebase 資料 折線圖
                fStore.collection("game_record").document("game_record_memory").collection("data")
                        .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                        .orderBy("createdAt")
                        .limitToLast(10)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("secondRecord")!=null){
                                            memoryRecordList.add(document.get("secondRecord"));
                                        }
                                    }
                                    list_size = memoryRecordList.size();
                                    for(int i = 1; i <= memoryRecordList.size() ; i++){
                                        float f1 =Float.parseFloat(valueOf(memoryRecordList.get(i-1)));
                                        values1.add(new Entry(i,f1));
                                    }
                                    //顯示
                                    text_all_line(values1);
                                    initX_line();
                                    initY_line();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //firebase 資料
                fStore.collection("game_record").document("game_record_memory").collection("data")
                        .whereEqualTo("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq")
                        .orderBy("secondRecord", Query.Direction.DESCENDING)
                        .limitToLast(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if(document.get("secondRecord")!=null){
                                            maxList.add(document.get("secondRecord"));
                                        }
                                    }
                                    //柱狀圖
                                    barChart =(BarChart) findViewById(R.id.chart_bar);
                                    count = Integer.parseInt(maxList.get(0).toString());
                                    bar_others.add(new BarEntry(1,count));

                                    //firebase 資料
                                    fStore.collection("game_record").document("game_record_memory").collection("data")
                                            .orderBy("secondRecord", Query.Direction.DESCENDING)
                                            .limitToLast(1)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(document.get("secondRecord")!=null){
                                                                maxALLUserList.add(document.get("secondRecord"));
                                                            }
                                                        }
                                                        //柱狀圖
                                                        countAll = Integer.parseInt(maxALLUserList.get(0).toString());
                                                        bar_own.add(new BarEntry(2,countAll));
                                                        text_all_bar(bar_others,bar_own);
                                                        initX_bar();
                                                        initY_bar();
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }
    private void text_all_line(ArrayList<Entry> values1) {
        LineDataSet set1;//設定線數資料的方式
        set1=new LineDataSet(values1,"遊戲秒數");

        set1.setMode(LineDataSet.Mode.LINEAR);//LINEAR是立方曲線
        set1.setColor(Orange);//線的顏色
        set1.setLineWidth(4);
        set1.setCircleRadius(4); //焦點圓心的大小
        set1.setHighlightEnabled(false);//禁用點擊高亮線
        set1.setValueFormatter(new DefaultValueFormatter(0));//座標點數字的小數位數1位
        set1.setDrawCircles(false);//設置範圍背景填充
        set1.setDrawValues(false);//不顯示座標點對應Y軸的數字(預設顯示)

        //右下方description label：設置圖表資訊
        Description description = lineChart.getDescription();
        description.setEnabled(false);//不顯示Description Label (預設顯示)

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);//不顯示圖例 (預設顯示)
        legend.setTextColor(Yellow);

        //創建LineData 對象，
        LineData data =new LineData(set1);

        lineChart.setBackgroundColor(Blue);//顯示整個圖表背景顏色 (預設灰底)
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(false);

        lineChart.setData(data);//一定要放在最後
        //繪製圖表
        lineChart.invalidate();
    }
    private void text_all_bar(ArrayList<BarEntry> values1,ArrayList<BarEntry> values2) {
        BarDataSet bardataset1=new BarDataSet(values1,"其他使用者");
        bardataset1.setColor(Green);//设置第一组数据颜色
        bardataset1.setDrawValues(false);

        BarDataSet bardataset2=new BarDataSet(values2,"我的最佳秒數");
        bardataset2.setColor(Red);//设置第一组数据颜色
        bardataset2.setDrawValues(false);

        //右下方description label：設置圖表資訊
        Description description = barChart.getDescription();
        description.setEnabled(false);//不顯示Description Label (預設顯示)

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);//不顯示圖例 (預設顯示)
        legend.setTextColor(Yellow);

        ArrayList<IBarDataSet> totalBarData = new ArrayList<>();
        totalBarData.add(bardataset1);
        totalBarData.add(bardataset2);

        //創建LineData 對象，
        BarData data =new BarData(totalBarData);
        data.setBarWidth(0.2f);

        barChart.setBackgroundColor(Blue);//顯示整個圖表背景顏色 (預設灰底)
        barChart.setScaleEnabled(false);

        barChart.setData(data);//一定要放在最後
        barChart.invalidate();//繪製圖表
    }
    private void initX_line() {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setGridColor(Blue);//格線顏色
        xAxis.setAxisLineColor(Yellow);//X軸線顏色

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Yellow);//X軸標籤顏色
        xAxis.setTextSize(12);//X軸標籤大小

        xAxis.setLabelCount(list_size-1);//X軸標籤個數
        xAxis.setSpaceMin(0.5f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.5f);//折線終點距離右側Y軸距離
    }
    private void initY_line() {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸
        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        leftAxis.setTextColor(Yellow);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小

        leftAxis.setAxisMinimum(0f);//Y軸標籤最小值
    }
    private void initX_bar() {
        XAxis xAxis = barChart.getXAxis();

        xAxis.setGridColor(Blue);//格線顏色
        xAxis.setAxisLineColor(Yellow);//X軸線顏色

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X軸標籤顯示位置(預設顯示在上方，分為上方內/外側、下方內/外側及上下同時顯示)
        xAxis.setTextColor(Yellow);//X軸標籤顏色
        xAxis.setTextSize(12);//X軸標籤大小

        xAxis.setLabelCount(2);//X軸標籤個數
        xAxis.setSpaceMin(0.8f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.8f);//折線終點距離右側Y軸距離
    }
    private void initY_bar() {
        YAxis rightAxis = barChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸

        YAxis leftAxis = barChart.getAxisLeft();//獲取左側的軸線
        leftAxis.setTextColor(Yellow);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小
        leftAxis.setAxisMinimum(0f);//Y軸標籤最小值
    }
}