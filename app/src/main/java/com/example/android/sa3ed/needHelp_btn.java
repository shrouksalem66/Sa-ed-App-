package com.example.android.sa3ed;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.HashMap;

public class needHelp_btn extends AppCompatActivity {
    public void onBackPressed() {
        Intent intent=new Intent(needHelp_btn.this,HomeActivity.class);
        startActivity(intent);
    }
    DatabaseReference reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_help_btn);
        Button cancel=(Button) findViewById(R.id.cancel_btn);
        Button request_btn=(Button) findViewById(R.id.request_btn);
        TextView username = (TextView) findViewById(R.id.username);
        TextView details = (TextView) findViewById(R.id.details);
        Dialog dialog = new Dialog(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String help_info=this.getIntent().getExtras().getString("helpinfo");
        String card_num=this.getIntent().getExtras().getString("cardnum"); //name card
        String name_owner=this.getIntent().getExtras().getString("nameowner");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(needHelp_btn.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.success_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ImageView icon = dialog.findViewById(R.id.success_icon);
                TextView textView = (TextView) dialog.findViewById(R.id.text);
                dialog.show();

            }
        });

        ValueEventListener namelistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //   String namee = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("Name").getValue(String.class);
                //   username.setText(namee);
                username.setText(name_owner);
                String statues = dataSnapshot.child("Cards").child(auth.getCurrentUser().getUid()).child("Status").getValue(String.class);
                String quantity = dataSnapshot.child("Cards").child(auth.getCurrentUser().getUid()).child("Quantity").getValue(String.class);
                String item = dataSnapshot.child("Cards").child(auth.getCurrentUser().getUid()).child("Item").getValue(String.class);
                details.setText(help_info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addValueEventListener(namelistener);
    }
}