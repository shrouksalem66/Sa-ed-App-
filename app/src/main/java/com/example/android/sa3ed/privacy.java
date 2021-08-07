package com.example.android.sa3ed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class privacy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        Button cancel=(Button) findViewById(R.id.privacy_back);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(privacy.this,UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onBackPressed() {
        Intent intent=new Intent(privacy.this,UserActivity.class);
        startActivity(intent);
    }
}

