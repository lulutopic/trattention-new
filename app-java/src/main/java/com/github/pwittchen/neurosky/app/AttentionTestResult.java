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
        setContentView(R.layout.activity_attention_test_result);

        ArrayList attentionList = new ArrayList<>();
        //圖表初始化
        lineChart =(LineChart)findViewById(R.id.chart_line);
        ArrayList<Entry> values1=new ArrayList<>();

        PreChart = findViewById(R.id.PreChart);
        CurChart = findViewById(R.id.CurChart);

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestResult.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTestResult.this).toBundle());
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTestResult.this , SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTestResult.this).toBundle());
            }
        });


        //抓取上一頁數值：測驗成績
        Intent intent = getIntent();
        String attention_value = intent.getStringExtra("attention");
        if(attention_value == null){
            attention_value = "87";
        }


        //計算當前時間
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.TAIWAN);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        createdAt = sdf.format(new Date()); //-prints-> 2015-01-22T03:23:26Z

//        先寫死，後期在統一改 UID
//        userID = fAuth.getCurrentUser().getUid();
//        DocumentReference documentReference = fStore.collection("mindwave_record").document(userID);
        //自動產生 document id
        DocumentReference documentReference = fStore.collection("mindwave_record").document("record").collection("MELJmK6vYxeoKCrWhvJyy4Xfriq2").document();
        Map<String,Object> mindwave = new HashMap<>();
//        user.put("user", userID);
        mindwave.put("user", "MELJmK6vYxeoKCrWhvJyy4Xfriq2");
        mindwave.put("attention_result", attention_value);//倒時候要把值改成 attention_result 先寫死寫圖表
        mindwave.put("createdAt", createdAt);
        documentReference.set(mindwave).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        //寫入個人資料資料 ps 先把 UID 寫死不然大家會不好測試
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
                            String pre_text = "無";
                            float cur = Float.parseFloat(cur_text);

                            show(PreChart,0,pre_text,"最佳測驗");
                            show(CurChart,cur,cur_text,"此次測驗");

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

    private void show(PieChart pieChart, float data,String text,String status) {
        //如果啟用此選項,則圖表中的值將以百分比形式繪製,而不是以原始值繪製
        pieChart.setUsePercentValues(true);
        //如果這個元件應該啟用(應該被繪製)FALSE如果沒有。如果禁用,此元件的任何內容將被繪製預設
        pieChart.getDescription().setEnabled(false);
        //將額外的偏移量(在圖表檢視周圍)附加到自動計算的偏移量
        pieChart.setExtraOffsets(5, 10, 5, 5);
        //較高的值表明速度會緩慢下降 例如如果它設定為0,它會立即停止。1是一個無效的值,並將自動轉換為0.999f。
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        //設定中間字型
        pieChart.setCenterText(text);
        pieChart.setCenterTextSize(25f);
        //設定為true將餅中心清空
        pieChart.setDrawHoleEnabled(true);
        //套孔,繪製在PieChart中心的顏色
        pieChart.setHoleColor(Color.WHITE);
        //設定透明圓應有的顏色。
        pieChart.setTransparentCircleColor(Color.WHITE);
        //設定透明度圓的透明度應該有0 =完全透明,255 =完全不透明,預設值為100。
        pieChart.setTransparentCircleAlpha(110);
        //設定在最大半徑的百分比餅圖中心孔半徑(最大=整個圖的半徑),預設為50%
        pieChart.setHoleRadius(70f);
        //設定繪製在孔旁邊的透明圓的半徑,在最大半徑的百分比在餅圖*(max =整個圖的半徑),預設55% -> 5%大於中心孔預設
        pieChart.setTransparentCircleRadius(50f);
        //將此設定為true,以繪製顯示在pie chart
        pieChart.setDrawCenterText(true);
        //集度的radarchart旋轉偏移。預設270f -->頂(北)
        pieChart.setRotationAngle(270);
        //設定為true,使旋轉/旋轉的圖表觸控。設定為false禁用它。預設值:true
        pieChart.setRotationEnabled(false);
        //將此設定為false,以防止由抽頭姿態突出值。值仍然可以通過拖動或程式設計高亮顯示。預設值:真
        pieChart.setHighlightPerTapEnabled(true);
        //建立Legend物件
        Legend l = pieChart.getLegend();
        //設定垂直對齊of the Legend
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        //設定水平of the Legend
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //設定方向
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //其中哪一個將畫在圖表或外
        l.setDrawInside(true);
        //設定水平軸上圖例項之間的間距
        l.setXEntrySpace(7f);
        //設定在垂直軸上的圖例項之間的間距
        l.setYEntrySpace(0f);
        //設定此軸上標籤的所使用的y軸偏移量 更高的偏移意味著作為一個整體的Legend將被放置遠離頂部。
        l.setYOffset(1f);

        //設定入口標籤的大小。預設值:13dp
        pieChart.setEntryLabelTextSize(15f);
        //模擬的資料來源
        PieEntry x1 = new PieEntry(data) ;
        PieEntry x2 = new PieEntry(100-data);

        //新增到List集合
        List<PieEntry> list = new ArrayList<>() ;
        list.add(x1) ;
        list.add(x2) ;

        //設定到PieDataSet物件
        PieDataSet set = new PieDataSet(list , " "+ text + " / " + "100") ;
        set.setDrawValues(false);//設定為true,在圖表繪製y
        set.setAxisDependency(YAxis.AxisDependency.RIGHT);//設定Y軸,這個資料集應該被繪製(左或右)。預設值:左
        set.setAutomaticallyDisableSliceSpacing(false);//當啟用時,片間距將是0時,最小值要小於片間距本身
        set.setSliceSpace(3f);//間隔
        set.setSelectionShift(10f);//點選伸出去的距離
        /**
         * 設定該資料集前應使用的顏色。顏色使用只要資料集所代表的條目數目高於顏色陣列的大小。
         * 如果您使用的顏色從資源, 確保顏色已準備好(通過呼叫getresources()。getColor(…))之前,將它們新增到資料集
         * */
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(green);
        colors.add(red);
        set.setColors(colors);
        //傳入PieData
        PieData data1 = new PieData(set);
        //為圖表設定新的資料物件
        pieChart.setData(data1);
        //重新整理
        pieChart.invalidate();
        //動畫圖上指定的動畫時間軸的繪製
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }


}