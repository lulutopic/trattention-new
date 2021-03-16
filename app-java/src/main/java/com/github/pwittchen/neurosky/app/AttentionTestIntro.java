
package com.github.pwittchen.neurosky.app;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.view.Window;
        import android.view.WindowManager;
        import android.widget.ImageView;

public class AttentionTestIntro extends AppCompatActivity {
    private Button button;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
      //隱藏title
      requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
      getSupportActionBar().hide(); // hide the title bar
      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
              WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

      setContentView(R.layout.activity_attention_test_intro);
      //header:頁面跳轉->回首頁
      ImageView btn_home=(ImageView)findViewById(R.id.imagehome);
      btn_home.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent();
              intent.setClass(AttentionTestIntro.this , Home.class);
              startActivity(intent);
          }
      });

      //header:頁面跳轉->指南
      ImageView btn_safari=(ImageView)findViewById(R.id.imagesafari);
      btn_safari.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent();
              intent.setClass(AttentionTestIntro.this , InstructionHome.class);
              startActivity(intent);
          }
      });
      //頁面跳轉->測驗開始
      Button nextPageBtn = (Button)findViewById(R.id.btn_start_test);
      nextPageBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent();
              intent.setClass(AttentionTestIntro.this , AttentionTesting.class);
              startActivity(intent);
          }
      });
  }
}



