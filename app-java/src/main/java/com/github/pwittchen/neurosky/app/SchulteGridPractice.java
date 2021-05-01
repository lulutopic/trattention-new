package com.github.pwittchen.neurosky.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.MediaPlayer;

import androidx.appcompat.app.AlertDialog;

import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


import com.madgaze.watchsdk.MGWatch;
import com.madgaze.watchsdk.MobileActivity;
import com.madgaze.watchsdk.WatchException;
import com.madgaze.watchsdk.WatchGesture;


public class SchulteGridPractice extends MobileActivity {
    private final String MGTAG = MainActivity.class.getSimpleName();

    public final WatchGesture[] REQUIRED_WATCH_GESTURES = {
            //彈指
            WatchGesture.FINGER_SNAP,
            //手臂
            WatchGesture.FOREARM_LEFT,
            WatchGesture.FOREARM_RIGHT,
            //手背
            WatchGesture.HANDBACK_UP,
            WatchGesture.HANDBACK_DOWN,
            WatchGesture.HANDBACK_LEFT,
            WatchGesture.HANDBACK_RIGHT,
            WatchGesture.MOVE_FOREARM_DOWN,
            //拇指中指捏捏
            WatchGesture.THUMBTAP_MIDDLE,
            //三指捏捏
            WatchGesture.THUMBTAP_INDEX_MIDDLE,
            //拇指食指捏捏
            WatchGesture.THUMBTAP_INDEX,
            //指頭
            WatchGesture.JOINTTAP_LOWER_THUMB,
            WatchGesture.JOINTTAP_UPPER_THUMB,
            WatchGesture.JOINTTAP_MIDDLE_INDEX,
            WatchGesture.JOINTTAP_UPPER_INDEX,
            WatchGesture.JOINTTAP_MIDDLE_MIDDLE,
            WatchGesture.JOINTTAP_UPPER_MIDDLE,
            WatchGesture.JOINTTAP_MIDDLE_RING,
            WatchGesture.JOINTTAP_UPPER_RING,
            WatchGesture.JOINTTAP_MIDDLE_LITTLE,
            //手臂快速移動
            WatchGesture.MOVE_FOREARM_DOWN,
            WatchGesture.MOVE_FOREARM_LEFT,
            WatchGesture.MOVE_FOREARM_UP,
            WatchGesture.MOVE_FOREARM_RIGHT,
    };


    @Override
    public void onWatchGestureReceived(WatchGesture gesture) {
        Log.d(MGTAG, "onWatchGestureReceived: " + gesture.name());
        setResultText(gesture);
    }

    @Override
    public void onWatchGestureError(WatchException error) {
        Log.d(MGTAG, "onWatchGestureError: " + error.getMessage());
        setStatusText(error.getMessage());
    }

    @Override
    public void onWatchDetectionOn() {
        Log.d(MGTAG, "onWatchDetectionOn: ");
        setStatusText("Listening");
    }

    @Override
    public void onWatchDetectionOff() {
        Log.d(MGTAG, "onWatchDetectionOff: ");
        setStatusText("Idle");
    }

    @Override
    public void onMGWatchServiceReady() {
        setStatusText("Service Connected");
        tryStartDetection();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (MGWatch.isWatchGestureDetecting(this))
            MGWatch.stopGestureDetection(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MGWatch.isMGWatchServiceReady(this))
            tryStartDetection();
    }

    @Override
    public void onWatchConnected() {
        setStatusText("Watch Connected");
    }

    @Override
    public void onWatchDisconnected() {
        setStatusText("Watch Disconnected");
    }

    @Override
    protected WatchGesture[] getRequiredWatchGestures() {
        return REQUIRED_WATCH_GESTURES;
    }

    private void tryStartDetection() {

        if (!MGWatch.isWatchConnected(this)) {
            setStatusText("Connecting");
            return;
        }

        if (!MGWatch.isGesturesTrained(this)) {
            return;
        }

        if (!MGWatch.isWatchGestureDetecting(this)) {
            MGWatch.startGestureDetection(this);
        }
    }


    private void setStatusText(String text) {
        setText(R.id.status, "Status: " + text);
    }

    private void setResultText(final WatchGesture gesture) {
        setText(R.id.result, gesture.toString());
        ImageView[] UnShuffle = {one, two, three, four, five, six, seven, eight, nine};

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //手勢控制向上
                if (gesture == WatchGesture.HANDBACK_UP || gesture == WatchGesture.JOINTTAP_MIDDLE_LITTLE) {
                    setBtnStyle(btn_up);
                    clearRow(focus_row);
                    focus_row -= 1;
                    switch (focus_row) {
                        case (0):
                            focus_row = 3;
                            row3.setBackgroundColor(focus_color);
                            break;
                        case (1):
                            row1.setBackgroundColor(focus_color);
                            break;
                        case (2):
                            row2.setBackgroundColor(focus_color);
                            break;
                    }
                }
                //手勢控制向下
                else if (gesture == WatchGesture.HANDBACK_DOWN || gesture == WatchGesture.JOINTTAP_LOWER_THUMB || gesture == WatchGesture.JOINTTAP_MIDDLE_INDEX) {
                    setBtnStyle(btn_down);
                    clearRow(focus_row);
                    focus_row += 1;
                    switch (focus_row) {
                        case (2):
                            row2.setBackgroundColor(focus_color);
                            break;
                        case (3):
                            row3.setBackgroundColor(focus_color);
                            break;
                        case (4):
                            focus_row = 1;
                            row1.setBackgroundColor(focus_color);
                            break;
                    }
                }
                //手勢控制向左
                else if (gesture == WatchGesture.HANDBACK_LEFT || gesture == WatchGesture.FOREARM_RIGHT) {
                    setBtnStyle(btn_left);
                    clearColumn(focus_column);
                    focus_column -= 1;
                    switch (focus_column) {
                        case (0):
                            focus_column = 3;
                            UnShuffle[2].setBackgroundColor(focus_color);
                            UnShuffle[5].setBackgroundColor(focus_color);
                            UnShuffle[8].setBackgroundColor(focus_color);

                            break;
                        case (1):
                            UnShuffle[0].setBackgroundColor(focus_color);
                            UnShuffle[3].setBackgroundColor(focus_color);
                            UnShuffle[6].setBackgroundColor(focus_color);
                            break;
                        case (2):
                            UnShuffle[1].setBackgroundColor(focus_color);
                            UnShuffle[4].setBackgroundColor(focus_color);
                            UnShuffle[7].setBackgroundColor(focus_color);
                            break;
                    }
                }

                //手勢控制向右
                else if (gesture == WatchGesture.HANDBACK_RIGHT || gesture == WatchGesture.JOINTTAP_MIDDLE_RING
                        || gesture == WatchGesture.JOINTTAP_UPPER_RING || gesture == WatchGesture.JOINTTAP_MIDDLE_MIDDLE
                        || gesture == WatchGesture.JOINTTAP_UPPER_MIDDLE || gesture == WatchGesture.JOINTTAP_MIDDLE_INDEX
                        || gesture == WatchGesture.JOINTTAP_UPPER_INDEX) {
                    setBtnStyle(btn_right);
                    clearColumn(focus_column);
                    focus_column += 1;
                    switch (focus_column) {
                        case (2):
                            UnShuffle[1].setBackgroundColor(focus_color);
                            UnShuffle[4].setBackgroundColor(focus_color);
                            UnShuffle[7].setBackgroundColor(focus_color);
                            break;
                        case (3):
                            UnShuffle[2].setBackgroundColor(focus_color);
                            UnShuffle[5].setBackgroundColor(focus_color);
                            UnShuffle[8].setBackgroundColor(focus_color);
                            break;
                        case (4):
                            focus_column = 1;
                            UnShuffle[0].setBackgroundColor(focus_color);
                            UnShuffle[3].setBackgroundColor(focus_color);
                            UnShuffle[6].setBackgroundColor(focus_color);
                            break;
                    }
                }
                //手勢控制確認選取
                else if (gesture == WatchGesture.THUMBTAP_INDEX || gesture == WatchGesture.THUMBTAP_INDEX_2
                        || gesture == WatchGesture.THUMBTAP_MIDDLE || gesture == WatchGesture.THUMBTAP_MIDDLE_2
                        || gesture == WatchGesture.THUMBTAP_INDEX_MIDDLE || gesture == WatchGesture.THUMBTAP_INDEX_MIDDLE_2
                        || gesture == WatchGesture.FINGER_SNAP) {
                    setBtnStyle(btn_ok);
                    focus_count = (focus_row - 1) * 3 + focus_column - 1;
                    int theCard = Integer.parseInt((String) UnShuffle[focus_count].getTag());
                    doStuff(UnShuffle[focus_count], theCard);
                }
            }
        });

    }

    public void setText(final int resId, final String text) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            TextView textView = ((TextView) findViewById(resId));
            if (textView != null)
                textView.setText(text);
        } else runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textView = ((TextView) findViewById(resId));
                if (textView != null)
                    textView.setText(text);
            }
        });
    }


    private Long startTime; //初始時間
    private Long spentTime;
    private Long pauseTime = 0L;
    private Long pauseTotal;
    private Chronometer timer; //已經過時間
    private Handler handler = new Handler();//執行緒
    private int focus_count;
    private int focus_row = 1;
    private int focus_column = 1;
    private MediaPlayer music;


    //圖片的變數
    ImageView one, two, three, four, five, six, seven, eight, nine;
    ImageView btn_down, btn_up, btn_right, btn_left, btn_ok;
    RelativeLayout row1, row2, row3;

    int blue = Color.parseColor("#244f98");
    int focus_color = getColorWithAlpha(blue, 0.6f);
    int unfocus_color = getColorWithAlpha(blue, 0f);

    //圖片的檔案引入陣列
    int[] ImageArray = {R.drawable.grid1, R.drawable.grid2, R.drawable.grid3, R.drawable.grid4, R.drawable.grid5, R.drawable.grid6, R.drawable.grid7
            , R.drawable.grid8, R.drawable.grid9};

    int count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("imageArray", ImageArray.toString());

        //隱藏title
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_schulte_grid);

        //取得目前時間
        startTime = System.currentTimeMillis();
        //接續前段時間
        pauseTotal = getIntent().getLongExtra("pause", 0);
        //設定Delay的時間
        handler.postDelayed(updateTimer, 10);
        //音樂
        music = MediaPlayer.create(this, R.raw.bit2);
        music.setLooping(true);
        music.start();
        //暫停按鈕的觸發事件
        ImageView button4 = findViewById(R.id.imagepause);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //時間暫停
                pauseTime = System.currentTimeMillis();
                handler.removeCallbacks(updateTimer);
                //音樂暫停
                music.pause();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridPractice.this);
                LayoutInflater inflater = SchulteGridPractice.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_game_stop_button, null));
                alertDialogBuilder
                        .setNeutralButton("離開", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                intent.setClass(SchulteGridPractice.this, SchulteGridGameStart.class);
                                startActivity(intent);
                                //音樂釋放
                                music.release();
                                music = null;
                                finish();
                            }
                        })
                        .setNegativeButton("繼續", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pauseTotal += System.currentTimeMillis() - pauseTime;
                                handler.post(updateTimer);
                                pauseTime = 0L;
                                //音樂繼續
                                music.start();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(340, 400);
            }
        });

        //小提示按鈕的觸發事件
        ImageView button5 = findViewById(R.id.imagetips);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridPractice.this);
                LayoutInflater inflater = SchulteGridPractice.this.getLayoutInflater();
                alertDialogBuilder.setView(inflater.inflate(R.layout.activity_schulte_easy_tips, null));

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        ImageView button6 = findViewById(R.id.imagebgm);
        button6.setTag("0");
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button6.getTag().equals("0")) {
                    button6.setImageResource(R.drawable.bgm_off);
                    button6.setTag("1");
                    music.pause();
                } else {
                    button6.setImageResource(R.drawable.bgm_on);
                    button6.setTag("0");
                    music.start();
                }

            }
        });

        //get ID
        one = (ImageView) findViewById(R.id.one);
        two = (ImageView) findViewById(R.id.two);
        three = (ImageView) findViewById(R.id.three);
        four = (ImageView) findViewById(R.id.four);
        five = (ImageView) findViewById(R.id.five);
        six = (ImageView) findViewById(R.id.six);
        seven = (ImageView) findViewById(R.id.seven);
        eight = (ImageView) findViewById(R.id.eight);
        nine = (ImageView) findViewById(R.id.nine);

        btn_down = (ImageView) findViewById(R.id.down_arrow);
        btn_up = (ImageView) findViewById(R.id.up_arrow);
        btn_right = (ImageView) findViewById(R.id.right_arrow);
        btn_left = (ImageView) findViewById(R.id.left_arrow);
        btn_ok = (ImageView) findViewById(R.id.ok);

        row1 = (RelativeLayout) findViewById(R.id.row1);
        row2 = (RelativeLayout) findViewById(R.id.row2);
        row3 = (RelativeLayout) findViewById(R.id.row3);


        ImageView[] NumArray = {one, two, three, four, five, six, seven, eight, nine};
        ImageView[] UnShuffle = {one, two, three, four, five, six, seven, eight, nine};

        Collections.shuffle(Arrays.asList(NumArray));

        for (int i = 0; i < ImageArray.length; i++) {
            NumArray[i].setImageResource(ImageArray[i]);
            NumArray[i].setBackgroundColor(unfocus_color);
            String s = String.valueOf(i);
            NumArray[i].setTag(s);

        }
        //初始設定：選取第一行、第一列
        row1.setBackgroundColor(focus_color);
        UnShuffle[0].setBackgroundColor(focus_color);
        UnShuffle[3].setBackgroundColor(focus_color);
        UnShuffle[6].setBackgroundColor(focus_color);

        //向下的按鈕
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnStyle(view);
                clearRow(focus_row);
                focus_row += 1;
                switch (focus_row) {
                    case (2):
                        row2.setBackgroundColor(focus_color);
                        break;
                    case (3):
                        row3.setBackgroundColor(focus_color);
                        break;
                    case (4):
                        focus_row = 1;
                        row1.setBackgroundColor(focus_color);
                        break;
                }
            }
        });

        //向上的按鈕
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnStyle(view);
                clearRow(focus_row);
                focus_row -= 1;
                switch (focus_row) {
                    case (0):
                        focus_row = 3;
                        row3.setBackgroundColor(focus_color);
                        break;
                    case (1):
                        row1.setBackgroundColor(focus_color);
                        break;
                    case (2):
                        row2.setBackgroundColor(focus_color);
                        break;
                }
            }
        });


        //向右的按鈕
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnStyle(view);
                clearColumn(focus_column);
                focus_column += 1;
                switch (focus_column) {
                    case (2):
                        UnShuffle[1].setBackgroundColor(focus_color);
                        UnShuffle[4].setBackgroundColor(focus_color);
                        UnShuffle[7].setBackgroundColor(focus_color);
                        break;
                    case (3):
                        UnShuffle[2].setBackgroundColor(focus_color);
                        UnShuffle[5].setBackgroundColor(focus_color);
                        UnShuffle[8].setBackgroundColor(focus_color);
                        break;
                    case (4):
                        focus_column = 1;
                        UnShuffle[0].setBackgroundColor(focus_color);
                        UnShuffle[3].setBackgroundColor(focus_color);
                        UnShuffle[6].setBackgroundColor(focus_color);
                        break;
                }

            }
        });

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnStyle(view);
                clearColumn(focus_column);
                focus_column -= 1;
                switch (focus_column) {
                    case (0):
                        focus_column = 3;
                        UnShuffle[2].setBackgroundColor(focus_color);
                        UnShuffle[5].setBackgroundColor(focus_color);
                        UnShuffle[8].setBackgroundColor(focus_color);

                        break;
                    case (1):
                        UnShuffle[0].setBackgroundColor(focus_color);
                        UnShuffle[3].setBackgroundColor(focus_color);
                        UnShuffle[6].setBackgroundColor(focus_color);
                        break;
                    case (2):
                        UnShuffle[1].setBackgroundColor(focus_color);
                        UnShuffle[4].setBackgroundColor(focus_color);
                        UnShuffle[7].setBackgroundColor(focus_color);
                        break;
                }

            }
        });


        //確認的按鈕
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBtnStyle(view);
                focus_count = (focus_row - 1) * 3 + focus_column - 1;
                int theCard = Integer.parseInt((String) UnShuffle[focus_count].getTag());
                doStuff(UnShuffle[focus_count], theCard);
            }
        });


    }


    private int getColorWithAlpha(int color, float ratio) {
        int newColor = 0;
        int alpha = Math.round(Color.alpha(color) * ratio);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        newColor = Color.argb(alpha, r, g, b);
        return newColor;
    }

    private void setBtnStyle(View view) {
        view.setBackgroundResource(R.drawable.buttonshadow);
        Timer t = new Timer(false);
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        view.setBackgroundResource(0);
                    }
                });
            }
        }, 500);
    }

    private void clearRow(int focus_row) {
        switch (focus_row) {
            case (1):
                row1.setBackgroundColor(unfocus_color);
                break;
            case (2):
                row2.setBackgroundColor(unfocus_color);
                break;
            case (3):
                row3.setBackgroundColor(unfocus_color);
                break;
        }
    }

    private void clearColumn(int focus_column) {
        ImageView[] UnShuffle = {one, two, three, four, five, six, seven, eight, nine};
        switch (focus_column) {
            case (1):
                UnShuffle[0].setBackgroundColor(unfocus_color);
                UnShuffle[3].setBackgroundColor(unfocus_color);
                UnShuffle[6].setBackgroundColor(unfocus_color);
                break;
            case (2):
                UnShuffle[1].setBackgroundColor(unfocus_color);
                UnShuffle[4].setBackgroundColor(unfocus_color);
                UnShuffle[7].setBackgroundColor(unfocus_color);
                break;
            case (3):
                UnShuffle[2].setBackgroundColor(unfocus_color);
                UnShuffle[5].setBackgroundColor(unfocus_color);
                UnShuffle[8].setBackgroundColor(unfocus_color);
                break;
        }
    }

    private void doStuff(ImageView iv, int card) {
        if (count == card) {
            iv.setImageResource(R.drawable.gridblank);
            count++;
        }

        checkEnd();
    }

    private void checkEnd() {
        if (count == 9) {
            //設定計時器的執行緒結束
            handler.removeCallbacks(updateTimer);
            //頁面跳轉
            //頁面跳轉
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SchulteGridPractice.this);
            alertDialogBuilder
                    .setMessage("恭 喜 ! 練 習 完 成 ~")
                    .setCancelable(false)
                    .setNegativeButton("離開",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface,int i){
                            Intent intent = new Intent();
                            intent.setClass(SchulteGridPractice.this, SchulteGridGameStart.class);
                            startActivity(intent);
                            //音樂釋放
                            music.release();
                            music=null;
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    //固定要執行的方法
    private Runnable updateTimer = new Runnable() {
        public void run() {
            final TextView time = (Chronometer) findViewById(R.id.timer);
            spentTime = System.currentTimeMillis() - startTime - pauseTotal;
            //計算目前已過小時數
            Long hour = (spentTime / 1000) / 3600;
            //計算目前已過分鐘數
            Long minius = (spentTime / 1000) / 60;
            //計算目前已過秒數
            Long seconds = (spentTime / 1000) % 60;
            String formattedTime = String.format("%02d:%02d:%02d", hour, minius, seconds);
            time.setText(formattedTime);
            handler.postDelayed(this, 1000);
        }
    };

}