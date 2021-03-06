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
        //????????????
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
        Transition slide= TransitionInflater.from(this).inflateTransition(R.transition.slide);
        Transition fade = TransitionInflater.from(this).inflateTransition(R.transition.fade);
        //??????
        getWindow().setExitTransition(explode);
        //???????????????
        getWindow().setEnterTransition(fade);
        //????????????
        getWindow().setReenterTransition(slide);
        //??????title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_train_record);
        //header:????????????->?????????
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TrainRecord.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(TrainRecord.this).toBundle());
            }
        });

        //header:????????????->??????
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
        
        //??????????????????(????????????)
        text_pair.setTextColor(BlueDark);
        text_schulte.setTextColor(Yellow_light);
        text_memory.setTextColor(Yellow_light);
        count = 0;
        //?????????
        lineChart =(LineChart)findViewById(R.id.chart_line);
        ArrayList<Entry> values1=new ArrayList<>();
        ArrayList imagePairRecordList = new ArrayList<>();
        ArrayList maxList = new ArrayList<>();
        ArrayList maxALLUserList = new ArrayList<>();
        ArrayList<BarEntry> bar_others=new ArrayList<>();
        ArrayList<BarEntry> bar_own=new ArrayList<>();

        //firebase ?????? ?????????
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
                            //??????
                            text_all_line(values1);
                            initX_line();
                            initY_line();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        //firebase ??????
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
                            //?????????
                            barChart =(BarChart) findViewById(R.id.chart_bar);
                            count = Integer.parseInt(maxList.get(0).toString());
                            bar_others.add(new BarEntry(1,count));

                            //firebase ??????
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
                                                //?????????
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



        //??????????????????

        text_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(BlueDark);
                text_schulte.setTextColor(Yellow_light);
                text_memory.setTextColor(Yellow_light);
                count = 0;
                //?????????
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                ArrayList imagePairRecordList = new ArrayList<>();
                ArrayList maxList = new ArrayList<>();
                ArrayList maxALLUserList = new ArrayList<>();
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                ArrayList<BarEntry> bar_own=new ArrayList<>();

                //firebase ?????? ?????????
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
                                    //??????
                                    text_all_line(values1);
                                    initX_line();
                                    initY_line();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //firebase ??????
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
                                    //?????????
                                    barChart =(BarChart) findViewById(R.id.chart_bar);
                                    count = Integer.parseInt(maxList.get(0).toString());
                                    bar_others.add(new BarEntry(1,count));

                                    //firebase ??????
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
                                                        //?????????
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
        //?????????????????????
        text_schulte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Yellow_light);
                text_schulte.setTextColor(BlueDark);
                text_memory.setTextColor(Yellow_light);
                count = 0;
                countAll = 0;
                //?????????
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                ArrayList schulteRecordList = new ArrayList<>();
                ArrayList maxList = new ArrayList<>();
                ArrayList maxALLUserList = new ArrayList<>();
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                ArrayList<BarEntry> bar_own=new ArrayList<>();

                //firebase ?????? ?????????
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
                                    //??????
                                    text_all_line(values1);
                                    initX_line();
                                    initY_line();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //firebase ??????
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
                                    //?????????
                                    barChart =(BarChart) findViewById(R.id.chart_bar);
                                    count = Integer.parseInt(maxList.get(0).toString());
                                    bar_others.add(new BarEntry(1,count));

                                    //firebase ??????
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
                                                        //?????????
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
        //?????????????????????
        text_memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Yellow_light);
                text_schulte.setTextColor(Yellow_light);
                text_memory.setTextColor(BlueDark);
                count = 0;
                //?????????
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                ArrayList memoryRecordList = new ArrayList<>();
                ArrayList maxList = new ArrayList<>();
                ArrayList maxALLUserList = new ArrayList<>();
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                ArrayList<BarEntry> bar_own=new ArrayList<>();

                //firebase ?????? ?????????
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
                                    //??????
                                    text_all_line(values1);
                                    initX_line();
                                    initY_line();

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                //firebase ??????
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
                                    //?????????
                                    barChart =(BarChart) findViewById(R.id.chart_bar);
                                    count = Integer.parseInt(maxList.get(0).toString());
                                    bar_others.add(new BarEntry(1,count));

                                    //firebase ??????
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
                                                        //?????????
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
        LineDataSet set1;//???????????????????????????
        set1=new LineDataSet(values1,"????????????");

        set1.setMode(LineDataSet.Mode.LINEAR);//LINEAR???????????????
        set1.setColor(Orange);//????????????
        set1.setLineWidth(4);
        set1.setCircleRadius(4); //?????????????????????
        set1.setHighlightEnabled(false);//?????????????????????
        set1.setValueFormatter(new DefaultValueFormatter(0));//??????????????????????????????1???
        set1.setDrawCircles(false);//????????????????????????
        set1.setDrawValues(false);//????????????????????????Y????????????(????????????)

        //?????????description label?????????????????????
        Description description = lineChart.getDescription();
        description.setEnabled(false);//?????????Description Label (????????????)

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);//??????????????? (????????????)
        legend.setTextColor(Yellow);

        //??????LineData ?????????
        LineData data =new LineData(set1);

        lineChart.setBackgroundColor(Blue);//?????????????????????????????? (????????????)
        lineChart.setScaleEnabled(false);
        lineChart.setDrawBorders(false);

        lineChart.setData(data);//?????????????????????
        //????????????
        lineChart.invalidate();
    }
    private void text_all_bar(ArrayList<BarEntry> values1,ArrayList<BarEntry> values2) {
        BarDataSet bardataset1=new BarDataSet(values1,"???????????????");
        bardataset1.setColor(Green);//???????????????????????????
        bardataset1.setDrawValues(false);

        BarDataSet bardataset2=new BarDataSet(values2,"??????????????????");
        bardataset2.setColor(Red);//???????????????????????????
        bardataset2.setDrawValues(false);

        //?????????description label?????????????????????
        Description description = barChart.getDescription();
        description.setEnabled(false);//?????????Description Label (????????????)

        Legend legend = barChart.getLegend();
        legend.setEnabled(true);//??????????????? (????????????)
        legend.setTextColor(Yellow);

        ArrayList<IBarDataSet> totalBarData = new ArrayList<>();
        totalBarData.add(bardataset1);
        totalBarData.add(bardataset2);

        //??????LineData ?????????
        BarData data =new BarData(totalBarData);
        data.setBarWidth(0.2f);

        barChart.setBackgroundColor(Blue);//?????????????????????????????? (????????????)
        barChart.setScaleEnabled(false);

        barChart.setData(data);//?????????????????????
        barChart.invalidate();//????????????
    }
    private void initX_line() {
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setGridColor(Blue);//????????????
        xAxis.setAxisLineColor(Yellow);//X????????????

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X?????????????????????(???????????????????????????????????????/??????????????????/???????????????????????????)
        xAxis.setTextColor(Yellow);//X???????????????
        xAxis.setTextSize(12);//X???????????????

        xAxis.setLabelCount(list_size-1);//X???????????????
        xAxis.setSpaceMin(0.5f);//????????????????????????Y?????????
        xAxis.setSpaceMax(0.5f);//????????????????????????Y?????????
    }
    private void initY_line() {
        YAxis rightAxis = lineChart.getAxisRight();//?????????????????????
        rightAxis.setEnabled(false);//???????????????Y???
        YAxis leftAxis = lineChart.getAxisLeft();//?????????????????????

        leftAxis.setTextColor(Yellow);//Y???????????????
        leftAxis.setTextSize(12);//Y???????????????

        leftAxis.setAxisMinimum(0f);//Y??????????????????
    }
    private void initX_bar() {
        XAxis xAxis = barChart.getXAxis();

        xAxis.setGridColor(Blue);//????????????
        xAxis.setAxisLineColor(Yellow);//X????????????

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//X?????????????????????(???????????????????????????????????????/??????????????????/???????????????????????????)
        xAxis.setTextColor(Yellow);//X???????????????
        xAxis.setTextSize(12);//X???????????????

        xAxis.setLabelCount(2);//X???????????????
        xAxis.setSpaceMin(0.8f);//????????????????????????Y?????????
        xAxis.setSpaceMax(0.8f);//????????????????????????Y?????????
    }
    private void initY_bar() {
        YAxis rightAxis = barChart.getAxisRight();//?????????????????????
        rightAxis.setEnabled(false);//???????????????Y???

        YAxis leftAxis = barChart.getAxisLeft();//?????????????????????
        leftAxis.setTextColor(Yellow);//Y???????????????
        leftAxis.setTextSize(12);//Y???????????????
        leftAxis.setAxisMinimum(0f);//Y??????????????????
    }
}