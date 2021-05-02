package com.github.pwittchen.neurosky.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class Registration extends AppCompatActivity {

    private Spinner Spingender;
    private Spinner Spinage;
    public static final String TAG = "TAG";

    EditText mEmail, mPassword, mConfirm, mName;
    Button mLoginBtn, mSignUpBtn;
    TextView forgotTextLink;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        setContentView(R.layout.activity_registration);
        //設定隱藏標題
        getSupportActionBar().hide();

        //抓使用者輸入的值
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mConfirm = findViewById(R.id.pConfirm);
        mName = findViewById(R.id.name);
        //mLoginBtn = findViewById(R.id.login);
        mSignUpBtn = findViewById(R.id.registration);
        forgotTextLink = findViewById(R.id.ForgetPassword);
        Log.i("Document", mEmail.toString());
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        Spingender = (Spinner) findViewById(R.id.gender);

        ArrayAdapter<CharSequence> arrAdapSpn
                = ArrayAdapter.createFromResource(Registration.this, //對應的Context
                R.array.gender, //選項資料內容
                android.R.layout.simple_spinner_item); //自訂getView()介面格式(Spinner介面未展開時的View)

        arrAdapSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //自訂getDropDownView()介面格式(Spinner介面展開時，View所使用的每個item格式)
        Spingender.setAdapter(arrAdapSpn); //將宣告好的 Adapter 設定給 Spinner
        Spingender.setOnItemSelectedListener(spnRegionOnItemSelected);

        Spinage = (Spinner) findViewById(R.id.age);

        ArrayAdapter<CharSequence> arrAdapSpn1
                = ArrayAdapter.createFromResource(Registration.this, //對應的Context
                R.array.age, //選項資料內容
                android.R.layout.simple_spinner_item); //自訂getView()介面格式(Spinner介面未展開時的View)

        arrAdapSpn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //自訂getDropDownView()介面格式(Spinner介面展開時，View所使用的每個item格式)
        Spinage.setAdapter(arrAdapSpn1); //將宣告好的 Adapter 設定給 Spinner
        Spinage.setOnItemSelectedListener(spnRegionOnItemSelected);

        if(fAuth.getCurrentUser() != null){
//            startActivity(new Intent(getApplicationContext(),login.class));
//            finish();
        }


        //點選
        mSignUpBtn.setOnClickListener(view -> {
            final String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String passwordC = mConfirm.getText().toString().trim();
            final String Name = mName.getText().toString();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("請輸入 email");
                return;
            }

            if(TextUtils.isEmpty(password)){
                mPassword.setError("請輸入密碼");
                return;
            }

            if(TextUtils.isEmpty(passwordC)){
                mConfirm.setError("請輸入確認密碼");
                return;
            }

            if(password.length() < 6){
                mPassword.setError("密碼需大於六位數");
                return;
            }

            if(TextUtils.isEmpty(Name)){
                mName.setError("請輸入名字");
            }

            else{
                //判斷確認密碼以及密碼是否相同
                if(password.equals(passwordC)){
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                // send verification link

                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Registration.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                Toast.makeText(Registration.this, "User Created.", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fName",Name);
                                user.put("email",email);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),Login.class));

                            }
                            else {
                                Toast.makeText(Registration.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                //若不相同則顯示密碼不一致
                else{
                    Toast.makeText(this, "帳號密碼不一致", Toast.LENGTH_LONG).show();
                }

            }




        });
    }

    private AdapterView.OnItemSelectedListener spnRegionOnItemSelected
            = new AdapterView.OnItemSelectedListener()
    {
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0)
        {
            // TODO Auto-generated method stub
        }
    };
}


