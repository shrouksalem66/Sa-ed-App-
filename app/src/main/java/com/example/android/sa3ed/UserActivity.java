package com.example.android.sa3ed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ImageView homelogo=(ImageView) findViewById(R.id.homelogo);
        ImageView shieldlogo=(ImageView) findViewById(R.id.shieldlogo);
        ImageView notification=(ImageView) findViewById(R.id.notification);
        TextView username = (TextView) findViewById(R.id.username);
        Button log_out = (Button) findViewById(R.id.log_out);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button privacy = (Button) findViewById(R.id.privacy);
        Button manage = (Button) findViewById(R.id.manager_account);

        ValueEventListener namelistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String namee = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("Name").getValue(String.class);
                username.setText(namee);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addValueEventListener(namelistener);

        ImageView logo = (ImageView) findViewById(R.id.logoimage);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        shieldlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserActivity.this,ShieldActivity.class);
                startActivity(intent);
                finish();
            }
        });
        homelogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(UserActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, Notification_page.class);
                startActivity(intent);
                finish();
            }
        });



        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, privacy.class);
                startActivity(intent);
                finish();
            }
        });

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, manage_account.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void onBackPressed() {
        Intent intent=new Intent(UserActivity.this,HomeActivity.class);
        startActivity(intent);
    }

}
