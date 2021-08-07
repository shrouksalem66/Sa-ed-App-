package com.example.android.sa3ed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class offerHelp_btn extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance("https://sa3ed-194e7-default-rtdb.firebaseio.com/").getReference();
    Integer noti_count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_help_btn);
        Button cancel=(Button) findViewById(R.id.cancel_btn);
        Button offer_btn=(Button)findViewById(R.id.offer_btn);
        TextView username = (TextView) findViewById(R.id.username);
        TextView details = (TextView) findViewById(R.id.details);
        TextView max = (TextView) findViewById(R.id.max_amount);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        TextView amount = (TextView) findViewById(R.id.amount);
        EditText amount_box = (EditText) findViewById(R.id.amount_box);
        EditText comment=(EditText)findViewById(R.id.comment_box);
        ImageView picture=(ImageView)findViewById(R.id.profile_image);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String help_info=this.getIntent().getExtras().getString("helpinfo");
        String card_num=this.getIntent().getExtras().getString("cardnum");
        String name_owner=this.getIntent().getExtras().getString("nameowner");
        String userId=this.getIntent().getExtras().getString("userId");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(offerHelp_btn.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ValueEventListener namelistener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  String namee = dataSnapshot.child("Users").child(auth.getCurrentUser().getUid()).child("Name").getValue(String.class);
                //  username.setText(namee);
                username.setText(name_owner);
                String UserId = dataSnapshot.child("Cards").child("card"+card_num).child("UserId").getValue(String.class);

                String photo=dataSnapshot.child("Users").child(UserId).child("picture").getValue(String.class);
                picture.setImageURI(Uri.parse(photo));

                //  String statues = dataSnapshot.child("Cards").child(auth.getCurrentUser().getUid()).child("Status").getValue(String.class);
                //    String quantity = dataSnapshot.child("Cards").child("card"+card_num).child("Quantity").getValue(String.class);
                //   String item = dataSnapshot.child("Cards").child(auth.getCurrentUser().getUid()).child("Item").getValue(String.class);
                offer_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      /*  if((amount_box.getText().toString())equals(dataSnapshot.child("Cards").child("card"+card_num).child("Quantity").getValue(String.class))){

                        }*/
                        String UserId = dataSnapshot.child("Cards").child("card"+card_num).child("UserId").getValue(String.class);
                        noti_count=dataSnapshot.child("Counter").child("noti_acti_count").getValue(Integer.class);
                        noti_count++;
                        HashMap<String,Object> map =new HashMap<>();
                        map.put("From",auth.getCurrentUser().getUid());
                        map.put("To",UserId);
                        map.put("Comment",comment.getText().toString());
                        map.put("Offered quantity",amount_box.getText().toString());
                        map.put("Card id","card"+card_num);
                        reference.child("noti_acti").child("noti"+noti_count).setValue(map);
                        reference.child("Counter").child("noti_acti_count").setValue(noti_count);
                        Toast.makeText(offerHelp_btn.this,"DONE ",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(offerHelp_btn.this,HomeActivity.class);
                        startActivity(intent);

                    }
                });
                details.setText(help_info);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        String quantity = dataSnapshot.child("Cards").child("card"+card_num).child("Quantity").getValue(String.class);
                        if(i==R.id.custom_quantity_radioBtn){
                            amount.setVisibility(View.VISIBLE);
                            amount_box.setVisibility(View.VISIBLE);
                            max.setVisibility(View.VISIBLE);
                            amount_box.setText(quantity);
                        }
                        else{
                            amount.setVisibility(View.INVISIBLE);
                            amount_box.setVisibility(View.INVISIBLE);
                            max.setVisibility(View.INVISIBLE);
                            amount_box.setText(quantity);
                        }
                    }
                });
                //   max.setText(quantity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addValueEventListener(namelistener);


    }
    public void onBackPressed() {
        Intent intent=new Intent(offerHelp_btn.this,HomeActivity.class);
        startActivity(intent);
    }
    public void showDialog(View view) {

    }
}