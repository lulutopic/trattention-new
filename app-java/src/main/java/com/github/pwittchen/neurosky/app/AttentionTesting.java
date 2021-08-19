package com.github.pwittchen.neurosky.app;


import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class AttentionTesting extends AppCompatActivity {

    FirebaseFirestore fStore;
    private final static String LOG_TAG = "NeuroSky";
    public static String test ="87";
    private NeuroSky neuroSky;
    private String answer;
    private int answerCount=3;

    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.tv_attention) TextView tvAttention;
    @BindView(R.id.tv_meditation) TextView tvMeditation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
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

        setContentView(R.layout.activity_attention_testing);

        getArticle();

        //header:頁面跳轉->回首頁
        ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTesting.this , Home.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTesting.this).toBundle());
            }
        });

        //header:頁面跳轉->指南
        ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
        btn_safari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AttentionTesting.this , SafariHome.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTesting.this).toBundle());
            }
        });


        ButterKnife.bind(this);
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
//        if (neuroSky != null && state.equals(State.CONNECTED)) {
//            neuroSky.start();
            //連線成功後樣式設定：顯示閱讀完畢、隱藏開始測驗按鈕
            Button stop_btn=(Button) findViewById(R.id.btn_stop_monitoring);
            stop_btn.setVisibility(View.VISIBLE);

            View question=(View) findViewById(R.id.testing);
            question.setVisibility(View.VISIBLE);

            Button connect_btn=(Button) findViewById(R.id.btn_connect);
            connect_btn.setVisibility(View.GONE);

            //連線成功後樣式設定：顯示測驗文章、隱藏說明文章
            TextView connect_intro_textView=(TextView) findViewById(R.id.textView＿connect_intro);
            connect_intro_textView.setVisibility(View.GONE);

            TextView connect_article_textView=(TextView) findViewById(R.id.textView＿article);
            connect_article_textView.setVisibility(View.VISIBLE);

            TextView connect_article_question=(TextView) findViewById(R.id.question);
            connect_article_question.setVisibility(View.VISIBLE);

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

//        }

        tvState.setText(state.toString());
    }


    private void handleSignalChange(final Signal signal) {
        switch (signal) {
            case ATTENTION:
                int temp=signal.getTotal_value()/signal.getCount_value();
                tvAttention.setText(getFormattedMessage("專注力數值: %d", signal));
                signal.setCount_value(signal.getCount_value()+1);
                signal.setTotal_value((signal.getTotal_value()+signal.getValue()));
                test=String.valueOf(signal.getTotal_value()/signal.getCount_value());


                break;
            case MEDITATION:
                tvMeditation.setText(getFormattedMessage("冥想數值: %d", signal));
                break;

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


        AlertDialog.Builder builder = new AlertDialog.Builder(AttentionTesting.this);
        EditText editText=(EditText)findViewById(R.id.editTextNumber);
        String text=editText.getText().toString().trim();

        answerCount--;
        if (text.equals(answer)||answerCount<=0){
            neuroSky.stop();
            builder.setMessage("本次專注力測驗結果："+test +"/100");
            builder.setPositiveButton("完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {

                    Intent intent = new Intent();
                    intent.putExtra("attention", test);
                    intent.setClass(AttentionTesting.this , AttentionTestResult.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(AttentionTesting.this).toBundle());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        else{

            String temp="答案錯誤，請重新輸入（還有 "+answerCount+" 次機會)";
            Toast.makeText(AttentionTesting.this, temp,Toast.LENGTH_SHORT).show();
//            builder.setMessage(temp);
//            builder.setPositiveButton("了解", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int id) {
//                    builder.create().dismiss();
//                }
//            });
//            AlertDialog dialog = builder.create();
//            dialog.show();


        }




    }

    private void getArticle() {
        fStore = FirebaseFirestore.getInstance();
        ArrayList<String> articleList = new ArrayList<>();
        fStore.collection("article")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document:task.getResult()) {
                                articleList.add(document.getId());
                            }
                            Collections.shuffle(articleList);
                            setArticle(articleList.get(0));

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());

                        }
                    }
                });
    }


    private void setArticle(String article) {

        fStore = FirebaseFirestore.getInstance();
        TextView connect_article_textView=(TextView) findViewById(R.id.textView＿article);
        TextView question=(TextView)findViewById(R.id.question);

        DocumentReference documentReference = fStore.collection("article").document(article);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                connect_article_textView.setText(documentSnapshot.getString("content").replaceAll("\\\\n","\n"));
                question.setText("請找出文章中有幾個「"+documentSnapshot.getString("question")+"」");
                answer=documentSnapshot.getString("answer");
            }
        });
    };

}