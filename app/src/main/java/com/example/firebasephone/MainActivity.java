package com.example.firebasephone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import com.google.firebase.FirebaseException;

public class MainActivity extends AppCompatActivity {

    EditText et_phone;
    Button send_otp_btn;
    ProgressBar pb_bar;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_phone = findViewById(R.id.et_phone);
        send_otp_btn = findViewById(R.id.send_otp_btn);
        pb_bar = findViewById(R.id.pb_bar);

        pb_bar.setVisibility(View.GONE);

        send_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = et_phone.getText().toString().trim();
                if(phoneNumber.isEmpty()){
                    et_phone.setError("Invalid Phone Number");
                }else {
                    pb_bar.setVisibility(View.VISIBLE);
                    sendVerificationCode(phoneNumber);
                }
            }
        });
    }

    private void sendVerificationCode(String phoneNumber){

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCall
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCall = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            pb_bar.setVisibility(View.GONE);
        }

        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

            pb_bar.setVisibility(View.GONE);
            String mVerificationId = verificationId;
            Log.e("MainActivity" , "Verification id : " + verificationId);
            Intent intent = new Intent(MainActivity.this , OtpActivity.class);
            intent.putExtra("verificationId" , mVerificationId);
            startActivity(intent);
            finish();
        }
    };
}