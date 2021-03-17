package com.github.pwittchen.neurosky.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;

public class AttentionTestHome extends AppCompatActivity {
    LineChart lineChart;
    int Blue=Color.parseColor("#244F98");
    int Yellow=Color.parseColor("#fff5b8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_attention_test_home);

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestHome.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestHome.this , InstructionHome.class);
                startActivity(intent);
            }
        });


        //頁面跳轉->測驗說明
        Button nextPageBtn = (Button)findViewById(R.id.btn_home_start);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestHome.this , AttentionTestIntro.class);
                startActivity(intent);
            }
        });

        //圖表
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

        text_all(values1);
        initChartFormat();
        initX();
        initY();





    }


    private void text_all(ArrayList<Entry> values1) {
        LineDataSet set1;//設定線數資料的方式
        set1=new LineDataSet(values1,"專注力數值");
        set1.setMode(LineDataSet.Mode.LINEAR);//LINEAR是立方曲線
        set1.setColor(Yellow);//線的顏色
        set1.setLineWidth(2);
        set1.setCircleRadius(4); //焦點圓心的大小
        set1.setHighlightEnabled(false);//禁用點擊高亮線
        set1.setValueFormatter(new DefaultValueFormatter(0));//座標點數字的小數位數1位
        set1.setDrawCircles(false);//設置範圍背景填充
        set1.setDrawValues(false);//不顯示座標點對應Y軸的數字(預設顯示)



        //創建LineData 對象，
        LineData data =new LineData(set1);

        lineChart.setData(data);//一定要放在最後
        //繪製圖表
        lineChart.invalidate();
    }

    private void initChartFormat() {
        //右下方description label：設置圖表資訊
        Description description = lineChart.getDescription();
        description.setEnabled(false);//不顯示Description Label (預設顯示)

        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);//不顯示圖例 (預設顯示)
        legend.setTextColor(Yellow);

        lineChart.setBackgroundColor(Blue);//顯示整個圖表背景顏色 (預設灰底)
        lineChart.setScaleEnabled(false);
        lineChart.setBorderWidth(0f);


    }
    private void initX() {
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
    private void initY() {
        YAxis rightAxis = lineChart.getAxisRight();//獲取右側的軸線
        rightAxis.setEnabled(false);//不顯示右側Y軸

        YAxis leftAxis = lineChart.getAxisLeft();//獲取左側的軸線

        leftAxis.setTextColor(Yellow);//Y軸標籤顏色
        leftAxis.setTextSize(12);//Y軸標籤大小

        leftAxis.setAxisMinimum(0f);//Y軸標籤最小值
        leftAxis.setAxisMaximum(100f);//Y軸標籤最大值
    }


}