package com.example.android.sa3ed;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent registerintent=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(registerintent);
                finish();
            }
        },SPLASH_TIME_OUT);
     //Functional of facebook is only didn't implement ya shbab
    }
}
