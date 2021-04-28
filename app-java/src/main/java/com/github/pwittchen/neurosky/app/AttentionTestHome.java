package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AttentionTestHome extends AppCompatActivity {
    LineChart lineChart;
    public static final String TAG = "TAG";
    int Blue=Color.parseColor("#8BB2D4");
    int Yellow=Color.parseColor("#F4E1A5");
    private int list_size;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView pre_attention, cur_attention;
    String userID, data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fStore = FirebaseFirestore.getInstance();

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_attention_test_home);
        ArrayList attentionList = new ArrayList<>();
        //圖表初始化
        lineChart =(LineChart)findViewById(R.id.chart_line);
        ArrayList<Entry> values1=new ArrayList<>();

        pre_attention = findViewById(R.id.pre_attention);
        cur_attention = findViewById(R.id.cur_attention);

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
                intent.setClass(AttentionTestHome.this , SafariHome.class);
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
                overridePendingTransition(R.anim.ani_zoomin,R.anim.ani_zoomout);
            }
        });

        //寫入個人資料資料 ps 先把 UID 寫死不然大家會不好測試
        fStore.collection("mindwave_record").document("record").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq2")
                .orderBy("createdAt")
                .limitToLast(10)
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

                            if(attentionList.size() >= 2){
                                pre_attention.setText(attentionList.get(attentionList.size()-2).toString());
                                cur_attention.setText(attentionList.get(attentionList.size()-1).toString());
                            }
                            else{
                                pre_attention.setText("尚未有先前記錄");
                                cur_attention.setText(attentionList.get(attentionList.size()-1).toString());
                            }

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
        LineDataSet set1;//設定線數資料的方式
        set1=new LineDataSet(values1,"專注力數值");
        set1.setMode(LineDataSet.Mode.LINEAR);//LINEAR是立方曲線
        set1.setColor(Yellow);//線的顏色
        set1.setLineWidth(3);
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

        xAxis.setLabelCount(list_size-1);//X軸標籤個數
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