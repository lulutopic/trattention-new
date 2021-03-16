package com.github.pwittchen.neurosky.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.Locale;
import java.util.Set;

//MainActivity類別實作AppCompatActivity 介面
public class AttentionTesting extends AppCompatActivity {

    private final static String LOG_TAG = "NeuroSky";
    public static String test ="神奇專注力數值";
    private NeuroSky neuroSky;

    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.tv_attention) TextView tvAttention;
    @BindView(R.id.tv_meditation) TextView tvMeditation;
//    @BindView(R.id.tv_blink) TextView tvBlink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_attention_testing);

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTesting.this , Home.class);
                startActivity(intent);
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTesting.this , InstructionHome.class);
                startActivity(intent);
            }
        });

        ButterKnife.bind(this);

        //初始樣式設定：隱藏閱讀完畢按鈕
        Button connect_btn=(Button) findViewById(R.id.btn_stop_monitoring);
        connect_btn.setVisibility(View.GONE);
        neuroSky = createNeuroSky();
    }

    //開始測試
    @Override protected void onResume() {

        super.onResume();
        if (neuroSky != null && neuroSky.isConnected()) {
            neuroSky.start();
        }
    }

    //停止測試
    @Override protected void onPause() {
        super.onPause();
        if (neuroSky != null && neuroSky.isConnected()) {

            neuroSky.stop();
        }
    }

    @NonNull private NeuroSky createNeuroSky() {

        return new NeuroSky(new ExtendedDeviceMessageListener() {
            @Override public void onStateChange(State state) {
                handleStateChange(state);
            }

            @Override public void onSignalChange(Signal signal) {
                handleSignalChange(signal);
            }

            @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
                handleBrainWavesChange(brainWaves);
            }
        });
    }
    //STATE_CHANGE: 1(正在連線) 2(練線成功) 0(停止連線)
    private void handleStateChange(final State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
            neuroSky.start();
            //連線成功後樣式設定：顯示閱讀完畢、隱藏開始測驗按鈕
            Button stop_btn=(Button) findViewById(R.id.btn_stop_monitoring);
            stop_btn.setVisibility(View.VISIBLE);

            Button connect_btn=(Button) findViewById(R.id.btn_connect);
            connect_btn.setVisibility(View.GONE);

            //連線成功後樣式設定：顯示測驗文章、隱藏說明文章
            TextView connect_intro_textView=(TextView) findViewById(R.id.textView＿connect_intro);
            connect_intro_textView.setVisibility(View.GONE);

            TextView connect_article_textView=(TextView) findViewById(R.id.textView＿article);
            connect_article_textView.setVisibility(View.VISIBLE);

            //測驗開始提醒彈跳視窗
            AlertDialog.Builder builder = new AlertDialog.Builder(AttentionTesting.this);
            LayoutInflater inflater = AttentionTesting.this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_testing_startwarn, null));

            AlertDialog dialog = builder.create();
            dialog.show();

            // 測驗開始提醒彈跳視窗：顯示1.5秒
            final Handler handler  = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            };

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    handler.removeCallbacks(runnable);
                }
            });

            handler.postDelayed(runnable, 1500);

        }

        tvState.setText(state.toString());
    }


    private void handleSignalChange(final Signal signal) {
        //src:libary/src/main/java/message/enums/Signal

        switch (signal) {
            case ATTENTION:
                int temp=signal.getTotal_value()/signal.getCount_value();
                tvAttention.setText(getFormattedMessage("專注力數值: %d", signal));
                signal.setCount_value(signal.getCount_value()+1);
                signal.setTotal_value((signal.getTotal_value()+signal.getValue()));
                test=String.valueOf(signal.getTotal_value()/signal.getCount_value());

//                Log.d("總專注力數值",String.valueOf(signal.getTotal_value()));
//                Log.d("總讀取次數",String.valueOf(signal.getCount_value()));
//                Log.d("平均專注力",String.valueOf(signal.getTotal_value()/signal.getCount_value()));
                break;
            case MEDITATION:
                tvMeditation.setText(getFormattedMessage("冥想數值: %d", signal));
                break;
//     case BLINK:
//        tvBlink.setText(getFormattedMessage("blink: %d", signal));
//        break;
        }



    }

    private String getFormattedMessage(String messageFormat, Signal signal) {
        return String.format(Locale.getDefault(), messageFormat, signal.getValue());
    }







    //腦波儀：a b 波，獨力數值
    private void handleBrainWavesChange(final Set<BrainWave> brainWaves) {
        for (BrainWave brainWave : brainWaves) {
//        Log.d(LOG_TAG, String.format("%s: %d ", brainWave.toString(), brainWave.getValue()));

        }
    }




    @OnClick(R.id.btn_connect) void connect() {
        try {
            neuroSky.connect();


        } catch (BluetoothNotEnabledException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, e.getMessage());
        }
    }

//    @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
//        neuroSky.start();
//    }

    @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
        neuroSky.stop();
        AlertDialog.Builder builder = new AlertDialog.Builder(AttentionTesting.this);
        builder.setMessage("本次專注力測驗結果："+test +"/100");


        builder.setNegativeButton("完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {

                    Intent intent = new Intent();
                    intent.putExtra("attention", test);
                    intent.setClass(AttentionTesting.this , AttentionTestResult.class);
                    startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

//    @OnClick(R.id.btn_disconnect) void disconnect() {
//        neuroSky.disconnect();
//    }

}