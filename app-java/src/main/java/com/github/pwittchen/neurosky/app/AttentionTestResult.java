package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.github.mikephil.charting.charts.PieChart;

public class AttentionTestResult extends AppCompatActivity {
    PieChart PreChart ;
    PieChart CurChart ;
    LineChart lineChart;
    public static final String TAG = "TAG";
    int Blue = Color.parseColor("#5B7F9F");
    int Yellow = Color.parseColor("#F4E1A5");
    int green = Color.parseColor("#A0D7D9");
    int red = Color.parseColor("#FCBABA");
    private int list_size;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView pre_attention, cur_attention;
    String createdAt,userID, data;
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
        setContentView(R.layout.activity_attention_test_result);

        ArrayList attentionList = new ArrayList<>();
        //???????????????
        lineChart =(LineChart)findViewById(R.id.chart_line);
        ArrayList<Entry> values1=new ArrayList<>();

        PreChart = findViewById(R.id.PreChart);
        CurChart = findViewById(R.id.CurChart);

        //header:????????????->?????????
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestResult.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTestResult.this).toBundle());
            }
        });

        //header:????????????->??????
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestResult.this , SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTestResult.this).toBundle());
            }
        });


        //????????????????????????????????????
        Intent intent = getIntent();
        String attention_value = intent.getStringExtra("attention");
        if(attention_value == null){
            attention_value = "87";
        }


        //??????????????????
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.TAIWAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        createdAt = sdf.format(new Date()); //-prints-> 2015-01-22T03:23:26Z

//        ?????????????????????????????? UID
//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("mindwave_record").document(userID);
        //???????????? document id
        DocumentReference documentReference = fStore.collection("mindwave_record").document("record").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq2").document();
        Map<String,Object> mindwave = new HashMap<>();
//        user.put("user", userID);
        mindwave.put("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq2");
        mindwave.put("attention_result", attention_value);//???????????????????????? attention_result ??????????????????
        mindwave.put("createdAt", createdAt);
        documentReference.set(mindwave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "??????");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "?????????" + e.toString());
            }
        });

        //???????????????????????? ps ?????? UID ?????????????????????????????????
        fStore.collection("mindwave_record").document("record").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq2")
                .orderBy("createdAt")
                .limitToLast(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                attentionList.add(document.getString("attention_result"));
                            }
                            list_size = attentionList.size();

                            for(int i = 1; i <= attentionList.size() ; i++){
                                float f1 =Float.parseFloat(attentionList.get(i-1).toString());
                                values1.add(new Entry(i,f1));
                            }

                            String cur_text = attentionList.get(attentionList.size()-1).toString();
                            String pre_text = "???";
                            float cur = Float.parseFloat(cur_text);

                            show(PreChart,0,pre_text,"????????????");
                            show(CurChart,cur,cur_text,"????????????");

                            text_all(values1);
                            initChartFormat();
                            initX();
                            initY();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void text_all(ArrayList<Entry> values1) {
        LineDataSet set1;//???????????????????????????
        set1=new LineDataSet(values1,"???????????????");
        set1.setMode(LineDataSet.Mode.LINEAR);//LINEAR???????????????
        set1.setColor(Yellow);//????????????
        set1.setLineWidth(3);
        set1.setCircleRadius(4); //?????????????????????
        set1.setHighlightEnabled(false);//?????????????????????
        set1.setValueFormatter(new DefaultValueFormatter(0));//??????????????????????????????1???
        set1.setDrawCircles(false);//????????????????????????
        set1.setDrawValues(false);//????????????????????????Y????????????(????????????)

        //??????LineData ?????????
        LineData data =new LineData(set1);

        lineChart.setData(data);//?????????????????????
        //????????????
        lineChart.invalidate();
    }

    private void initChartFormat() {
        //?????????description label?????????????????????
        Description description = lineChart.getDescription();
        description.setEnabled(false);//?????????Description Label (????????????)

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);//??????????????? (????????????)
        legend.setTextColor(Yellow);

        lineChart.setBackgroundColor(Blue);//?????????????????????????????? (????????????)
        lineChart.setScaleEnabled(false);
        lineChart.setBorderWidth(0f);
    }
    private void initX() {
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
    private void initY() {
        YAxis rightAxis = lineChart.getAxisRight();//?????????????????????
        rightAxis.setEnabled(false);//???????????????Y???

        YAxis leftAxis = lineChart.getAxisLeft();//?????????????????????

        leftAxis.setTextColor(Yellow);//Y???????????????
        leftAxis.setTextSize(12);//Y???????????????

        leftAxis.setAxisMinimum(0f);//Y??????????????????
        leftAxis.setAxisMaximum(100f);//Y??????????????????
    }

    private void show(PieChart pieChart, float data,String text,String status) {
        //?????????????????????,?????????????????????????????????????????????,???????????????????????????
        pieChart.setUsePercentValues(true);
        //??????????????????????????????(???????????????)FALSE???????????????????????????,??????????????????????????????????????????
        pieChart.getDescription().setEnabled(false);
        //?????????????????????(?????????????????????)?????????????????????????????????
        pieChart.setExtraOffsets(5, 10, 5, 5);
        //??????????????????????????????????????? ????????????????????????0,?????????????????????1?????????????????????,?????????????????????0.999f???
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //??????????????????
        pieChart.setCenterText(text);
        pieChart.setCenterTextSize(25f);
        //?????????true??????????????????
        pieChart.setDrawHoleEnabled(true);
        //??????,?????????PieChart???????????????
        pieChart.setHoleColor(Color.WHITE);
        //?????????????????????????????????
        pieChart.setTransparentCircleColor(Color.WHITE);
        //???????????????????????????????????????0 =????????????,255 =???????????????,????????????100???
        pieChart.setTransparentCircleAlpha(110);
        //??????????????????????????????????????????????????????(??????=??????????????????),?????????50%
        pieChart.setHoleRadius(70f);
        //?????????????????????????????????????????????,????????????????????????????????????*(max =??????????????????),??????55% -> 5%?????????????????????
        pieChart.setTransparentCircleRadius(50f);
        //???????????????true,??????????????????pie chart
        pieChart.setDrawCenterText(true);
        //?????????radarchart?????????????????????270f -->???(???)
        pieChart.setRotationAngle(270);
        //?????????true,?????????/?????????????????????????????????false?????????????????????:true
        pieChart.setRotationEnabled(false);
        //???????????????false,??????????????????????????????????????????????????????????????????????????????????????????????????????:???
        pieChart.setHighlightPerTapEnabled(true);
        //??????Legend??????
        Legend l = pieChart.getLegend();
        //??????????????????of the Legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //????????????of the Legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //????????????
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //????????????????????????????????????
        l.setDrawInside(true);
        //??????????????????????????????????????????
        l.setXEntrySpace(7f);
        //????????????????????????????????????????????????
        l.setYEntrySpace(0f);
        //????????????????????????????????????y???????????? ?????????????????????????????????????????????Legend???????????????????????????
        l.setYOffset(1f);

        //???????????????????????????????????????:13dp
        pieChart.setEntryLabelTextSize(15f);
        //?????????????????????
        PieEntry x1 = new PieEntry(data) ;
        PieEntry x2 = new PieEntry(100-data);

        //?????????List??????
        List<PieEntry> list = new ArrayList<>() ;
        list.add(x1) ;
        list.add(x2) ;

        //?????????PieDataSet??????
        PieDataSet set = new PieDataSet(list , " "+ text + " / " + "100") ;
        set.setDrawValues(false);//?????????true,???????????????y
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);//??????Y???,??????????????????????????????(?????????)????????????:???
        set.setAutomaticallyDisableSliceSpacing(false);//????????????,???????????????0???,?????????????????????????????????
        set.setSliceSpace(3f);//??????
        set.setSelectionShift(10f);//????????????????????????
        /**
         * ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
         * ?????????????????????????????????, ????????????????????????(????????????getresources()???getColor(???))??????,???????????????????????????
         * */
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(green);
        colors.add(red);
        set.setColors(colors);
        //??????PieData
        PieData data1 = new PieData(set);
        //?????????????????????????????????
        pieChart.setData(data1);
        //????????????
        pieChart.invalidate();
        //?????????????????????????????????????????????
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }


}