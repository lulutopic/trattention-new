package com.github.pwittchen.neurosky.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class TrainRecord extends AppCompatActivity {
    LineChart lineChart;
    BarChart barChart;
    int Blue=Color.parseColor("#244F98");
    int Yellow=Color.parseColor("#FED900");
    int Yellow_light=Color.parseColor("#ffe445");
    int White =Color.parseColor("#ffffff");
    TextView text_pair;
    TextView text_schulte;
    TextView text_memory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TrainRecord.this , InstructionHome.class);
                startActivity(intent);
            }
        });





        //切換值
        text_pair=(TextView)findViewById(R.id.text_pair);
        text_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Blue);
                text_schulte.setTextColor(Yellow_light);
                text_memory.setTextColor(Yellow_light);
                //折線圖
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                values1.add(new Entry(1,10));
                values1.add(new Entry(2,20));
                values1.add(new Entry(3,40));
                values1.add(new Entry(4,50));
                values1.add(new Entry(5,70));
                values1.add(new Entry(6,60));
                values1.add(new Entry(7,40));
                values1.add(new Entry(8,90));
                values1.add(new Entry(9,70));
                values1.add(new Entry(10,60));
                values1.add(new Entry(11,40));
                values1.add(new Entry(12,90));


                //柱狀圖
                barChart =(BarChart) findViewById(R.id.chart_bar);
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                bar_others.add(new BarEntry(1,90.3f));

                ArrayList<BarEntry> bar_own=new ArrayList<>();
                bar_own.add(new BarEntry(2,60.8f));

                text_all_line(values1);
                text_all_bar(bar_others,bar_own);

            }
        });

        text_schulte=(TextView)findViewById(R.id.text_schulte);
        text_schulte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Yellow_light);
                text_schulte.setTextColor(Blue);
                text_memory.setTextColor(Yellow_light);

                //折線圖
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                values1.add(new Entry(1,10));
                values1.add(new Entry(2,20));
                values1.add(new Entry(3,40));
                values1.add(new Entry(4,20));
                values1.add(new Entry(5,70));
                values1.add(new Entry(6,60));
                values1.add(new Entry(7,20));
                values1.add(new Entry(8,90));
                values1.add(new Entry(9,70));
                values1.add(new Entry(10,60));
                values1.add(new Entry(11,40));
                values1.add(new Entry(12,100));


                //柱狀圖
                barChart =(BarChart) findViewById(R.id.chart_bar);
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                bar_others.add(new BarEntry(1,90.3f));

                ArrayList<BarEntry> bar_own=new ArrayList<>();
                bar_own.add(new BarEntry(2,20.8f));

                text_all_line(values1);
                text_all_bar(bar_others,bar_own);

            }
        });

        text_memory=(TextView)findViewById(R.id.text_memory);
        text_memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_pair.setTextColor(Yellow_light);
                text_schulte.setTextColor(Yellow_light);
                text_memory.setTextColor(Blue);

                //折線圖
                lineChart =(LineChart)findViewById(R.id.chart_line);
                ArrayList<Entry> values1=new ArrayList<>();
                values1.add(new Entry(1,10));
                values1.add(new Entry(2,20));
                values1.add(new Entry(3,40));
                values1.add(new Entry(4,50));
                values1.add(new Entry(5,10));
                values1.add(new Entry(6,60));
                values1.add(new Entry(7,40));
                values1.add(new Entry(8,0));
                values1.add(new Entry(9,70));
                values1.add(new Entry(10,60));
                values1.add(new Entry(11,20));
                values1.add(new Entry(12,0));



                //柱狀圖
                barChart =(BarChart) findViewById(R.id.chart_bar);
                ArrayList<BarEntry> bar_others=new ArrayList<>();
                bar_others.add(new BarEntry(1,40.3f));

                ArrayList<BarEntry> bar_own=new ArrayList<>();
                bar_own.add(new BarEntry(2,90.8f));

                text_all_line(values1);
                text_all_bar(bar_others,bar_own);

            }
        });


        //折線圖
        lineChart =(LineChart)findViewById(R.id.chart_line);
        ArrayList<Entry> values1=new ArrayList<>();
        values1.add(new Entry(1,10));
        values1.add(new Entry(2,20));
        values1.add(new Entry(3,40));
        values1.add(new Entry(4,50));
        values1.add(new Entry(5,70));
        values1.add(new Entry(6,60));
        values1.add(new Entry(7,40));
        values1.add(new Entry(8,90));
        values1.add(new Entry(9,70));
        values1.add(new Entry(10,60));
        values1.add(new Entry(11,40));
        values1.add(new Entry(12,90));

        //柱狀圖
        barChart =(BarChart) findViewById(R.id.chart_bar);
        ArrayList<BarEntry> bar_others=new ArrayList<>();
        bar_others.add(new BarEntry(1,90.3f));

        ArrayList<BarEntry> bar_own=new ArrayList<>();
        bar_own.add(new BarEntry(2,60.8f));



        //顯示
        text_all_line(values1);
        text_all_bar(bar_others,bar_own);

        initX_line();
        initY_line();
        initX_bar();
        initY_bar();



    }


    private void text_all_line(ArrayList<Entry> values1) {
        LineDataSet set1;//設定線數資料的方式
        set1=new LineDataSet(values1,"遊戲秒數");

        set1.setMode(LineDataSet.Mode.LINEAR);//LINEAR是立方曲線
        set1.setColor(Yellow);//線的顏色
        set1.setLineWidth(2);
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
        bardataset1.setColor(Yellow);//设置第一组数据颜色
        bardataset1.setDrawValues(false);

        BarDataSet bardataset2=new BarDataSet(values2,"我的最佳秒數");
        bardataset2.setColor(Yellow_light);//设置第一组数据颜色
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
        data.setBarWidth(0.3f);

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

        xAxis.setLabelCount(12);//X軸標籤個數
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
        xAxis.setSpaceMin(0.5f);//折線起點距離左側Y軸距離
        xAxis.setSpaceMax(0.5f);//折線終點距離右側Y軸距離
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